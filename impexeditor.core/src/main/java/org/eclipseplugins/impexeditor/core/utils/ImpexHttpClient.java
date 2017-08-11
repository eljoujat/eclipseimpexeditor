package org.eclipseplugins.impexeditor.core.utils;
/*******************************************************************************
 * Copyright 2014 Youssef EL JAOUJAT.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class ImpexHttpClient {

	private static final String USER_AGENT = "Mozilla/5.0";
	private final static Pattern pattern = Pattern.compile("JSESSIONID=[a-zA-Z0-9_-]*");
	private String JSESSIONID;
	private final String hostName;
	private final HttpClient httpClient;
	private final HttpClientFactory ignoreSSLhttpClientFactory;

	public ImpexHttpClient(final String hostName) {
		this.hostName = hostName;
		this.ignoreSSLhttpClientFactory = new TrustAllHttpClientFactory();
		this.httpClient = ignoreSSLhttpClientFactory.buildHttpClient();
	}

	public JsonObject getTypeandAttribute(final String type) throws Exception {
		final List<BasicNameValuePair> params = Arrays
				.asList(new BasicNameValuePair[] { new BasicNameValuePair("type", type) });

		final HttpResponse response = makeHttpPostRequest(hostName + "/console/impex/typeAndAttributes",
				getJsessionId(), params);
		final BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		final JsonObject impexJsonType = JsonObject.readFrom(rd);
		return impexJsonType;
	}

	public JsonArray getAllTypes() throws Exception {

		final HttpResponse response = makeHttpPostRequest(hostName + "/console/impex/allTypes", getJsessionId(),
				Collections.<BasicNameValuePair>emptyList());
		JsonArray types = null;
		if (response.getStatusLine().getStatusCode() == 200) {
			final BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			final JsonObject impexJsonType = JsonObject.readFrom(rd);
			final boolean isExist = impexJsonType.get("exists").asBoolean();

			if (isExist) {
				types = impexJsonType.get("types").asArray();
			}
		}

		return types;
	}

	private String sendLoginPost() {

		final String validJSessionID = getValidJSessionID();
		String csrfToken = null;
		try {
			csrfToken = getCSrfToken(validJSessionID);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		final List<BasicNameValuePair> params = Arrays
				.asList(new BasicNameValuePair[] { new BasicNameValuePair("j_username", "admin"),
						new BasicNameValuePair("j_password", "nimda"), new BasicNameValuePair("_csrf", csrfToken) });
		HttpResponse response = null;
		String jsessionIDToken = null;
		try {
			response = makeHttpPostRequest(hostName + "/j_spring_security_check", validJSessionID, params);
			for (final Header header : response.getAllHeaders()) {
				if ("Set-Cookie".equals(header.getName())) {
					final String[] sessionIdcookies = header.getValue().split(";");
					if (sessionIdcookies.length>0) {
						jsessionIDToken =sessionIdcookies[1];
						break;
					}
				}
			}

			if (jsessionIDToken != null && jsessionIDToken.split("=").length > 0) {
				jsessionIDToken = jsessionIDToken.split("=")[1];
			}
			return jsessionIDToken;
		} catch (final IOException e) {
		}
		return null;
	}

	public String getJsessionId() {
		if (JSESSIONID != null) {
			return JSESSIONID;
		} else {

			JSESSIONID = sendLoginPost();
			return JSESSIONID;
		}
	}

	public HttpResponse makeHttpPostRequest(final String actionUrl, final String connectionToken,
			final List<BasicNameValuePair> params) throws IOException {
		final String csrfToken = getCSrfToken(connectionToken);
		HttpResponse response = null;
		try {

			final HttpPost post = new HttpPost(actionUrl);
			// add header
			post.setHeader("User-Agent", USER_AGENT);
			post.setHeader("X-CSRF-TOKEN", csrfToken);
			final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			for (final BasicNameValuePair nameValuePair : params) {
				urlParameters.add(nameValuePair);
			}
			if (connectionToken != null) {
				post.setHeader("Cookie", "JSESSIONID=" + connectionToken);
			}
			HttpEntity entity;

			entity = new UrlEncodedFormEntity(urlParameters, "utf-8");
			post.setEntity(entity);

			response = ignoreSSLhttpClientFactory.buildHttpClient().execute(post);
		} catch (final Exception e) {
			System.out.println("Exception " + e.getMessage());
		}
		return response;

	}

	public String validateImpex(final String content) {
		final List<BasicNameValuePair> params = Arrays
				.asList(new BasicNameValuePair[] { new BasicNameValuePair("scriptContent", content),
						new BasicNameValuePair("validationEnum", "IMPORT_STRICT"),
						new BasicNameValuePair("encoding", "UTF-8"), new BasicNameValuePair("maxThreads", "4") });
		try {
			final HttpResponse response = makeHttpPostRequest(hostName + "/console/impex/import", getJsessionId(),
					params);
			return response.toString();
		} catch (final IOException e) {
		}
		return "";

	}

	private String getCSrfToken(final String jSessionid) throws IOException {
		final Document doc = Jsoup.connect(hostName).cookie("JSESSIONID", jSessionid).get();
		final Elements csrfMetaElt = doc.select("meta[name=_csrf]");
		final String csrfToken = csrfMetaElt.attr("content");
		return csrfToken;

	}

	private String getValidJSessionID() {
		Connection.Response res = null;
		try {
			disableSSLCertificateChecking();
			res = Jsoup.connect(hostName).method(Method.GET).execute();
		} catch (final IOException e) {
			System.out.println("Exception " + e.getMessage());
		}
		if (res == null) {
			return null;
		}
		final String sessionId = res.cookie("JSESSIONID"); // you will need to
															// check what the
															// right cookie name
															// is
		return sessionId;
	}

	private static void disableSSLCertificateChecking() {
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});

		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				// Do Nothing
				return null;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				// Do Nothing

			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				// Do Nothing

			}

		} };

		try {
			SSLContext sc = SSLContext.getInstance("TLS");

			sc.init(null, trustAllCerts, new SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

}
