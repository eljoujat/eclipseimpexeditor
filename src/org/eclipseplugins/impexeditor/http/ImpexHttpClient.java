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
package org.eclipseplugins.impexeditor.http;

import impexeditor.Activator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;


public class ImpexHttpClient
{

	private static final String USER_AGENT = "Mozilla/5.0";
	private final static Pattern pattern = Pattern
			.compile("JSESSIONID=[a-zA-Z0-9_-]*");
	private String jsessionId;
	private final String hostName;
	private final ILog logger = Activator.getDefault().getLog();

	public ImpexHttpClient(final String hostName)
	{
		this.hostName = hostName;
	}

	public static void main(final String[] args) throws Exception
	{

		final ImpexHttpClient impexHttpClient = new ImpexHttpClient("http://localhost:9001/hac");
		final long startTime = System.currentTimeMillis();
		final Map<String, JsonArray> allatypes = new HashMap<String, JsonArray>();
		for (final JsonValue type : impexHttpClient.getAllTypes())
		{
			allatypes.put(type.asString(), impexHttpClient.getTypeandAttribute(type.asString()).get("attributes").asArray());
		}
		for (final String type : allatypes.keySet())
		{
			System.out.println("type :" + type);
			for (final JsonValue string : allatypes.get(type))
			{
				System.out.println("---- " + string.asString());
			}
		}
		final long stopTime = System.currentTimeMillis();
		final long elapsedTime = stopTime - startTime;
		System.out.println("Success elapsed time in seconde " + TimeUnit.MILLISECONDS.toSeconds(elapsedTime));
	}

	public JsonObject getTypeandAttribute(final String type) throws Exception
	{
		final List<BasicNameValuePair> params = Arrays
				.asList(new BasicNameValuePair[]
				{
						new BasicNameValuePair("type", type) });

		final HttpResponse response = makeHttpPostRequest(hostName + "/console/impex/typeAndAttributes", getJsessionId(), params);
		final BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		final JsonObject impexJsonType = JsonObject.readFrom(rd);
		return impexJsonType;
	}

	public JsonArray getAllTypes() throws Exception
	{

		final HttpResponse response = makeHttpPostRequest(hostName + "/console/impex/allTypes", getJsessionId(),
				Collections.<BasicNameValuePair> emptyList());
		final BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		final JsonObject impexJsonType = JsonObject.readFrom(rd);
		final boolean isExist = impexJsonType.get("exists").asBoolean();
		JsonArray types = null;
		if (isExist)
		{
			types = impexJsonType.get("types").asArray();
		}
		return types;
	}


	private String sendLoginPost()
	{

		final String jssessionId = null;
		final String validJSessionID = getValidJSessionID();
		String csrfToken = null;
		try
		{
			csrfToken = getCSrfToken(validJSessionID);
		}
		catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final List<BasicNameValuePair> params = Arrays
				.asList(new BasicNameValuePair[]
				{
						new BasicNameValuePair("j_username", "admin"),
						new BasicNameValuePair("j_password", "nimda"),
						new BasicNameValuePair("_csrf", csrfToken) });
		final String jsessionIdCookieToken = validJSessionID;
		HttpResponse response = null;
		String jsessionIDToken = null;
		try
		{
			response = makeHttpPostRequest(
					hostName + "/j_spring_security_check", validJSessionID,
					params);
			for (final Header header : response.getAllHeaders())
			{
				if ("Set-Cookie".equals(header.getName()))
				{
					final Matcher m = pattern.matcher(header.getValue());
					if (m.find())
					{
						jsessionIDToken = m.group(0);
					}
				}
			}

			if (jsessionIDToken != null && jsessionIDToken.split("=").length > 0)
			{
				jsessionIDToken = jsessionIDToken.split("=")[1];
			}
			return jsessionIDToken;
		}
		catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getJsessionId()
	{
		if (jsessionId != null)
		{
			return jsessionId;
		}
		else
		{
			jsessionId = sendLoginPost();
			return jsessionId;
		}
	}

	public HttpResponse makeHttpPostRequest(final String actionUrl,
			final String connectionToken, final List<BasicNameValuePair> params) throws IOException
	{
		final String csrfToken = getCSrfToken(connectionToken);
		final HttpClient client = new DefaultHttpClient();
		final HttpPost post = new HttpPost(actionUrl);
		// add header
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("X-CSRF-TOKEN", csrfToken);
		final List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		for (final BasicNameValuePair nameValuePair : params)
		{
			urlParameters.add(nameValuePair);
		}
		if (connectionToken != null)
		{
			post.setHeader("Cookie", "JSESSIONID=" + connectionToken);
		}
		HttpEntity entity;
		HttpResponse response = null;
		try
		{
			entity = new UrlEncodedFormEntity(urlParameters, "utf-8");
			post.setEntity(entity);

			response = client.execute(post);
		}
		catch (final IOException e)
		{
			logger.log(new Status(Status.ERROR, Activator.PLUGIN_ID, Status.ERROR, "Connection refused to " + hostName, e));
		}
		return response;

	}

	public String validateImpex(final String content)
	{
		final List<BasicNameValuePair> params = Arrays
				.asList(new BasicNameValuePair[]
				{
						new BasicNameValuePair("scriptContent", content), new BasicNameValuePair("validationEnum", "IMPORT_STRICT"),
						new BasicNameValuePair("encoding", "UTF-8"),
						new BasicNameValuePair("maxThreads", "4") });
		System.out.println("############################# ");
		System.out.println(content);
		System.out.println("############################# ");
		try
		{
			final HttpResponse response = makeHttpPostRequest(hostName + "/console/impex/import", getJsessionId(), params);
			final String html = EntityUtils.toString(response.getEntity());
			System.out.println(html);
		}
		catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	private String getCSrfToken(final String jSessionid) throws IOException
	{
		//<meta name="_csrf" content="c1dee1f7-8c79-43b1-8f3f-767662abc87a" />
		final Document doc = Jsoup.connect(hostName)
				.cookie("JSESSIONID", jSessionid)
				.get();
		final Elements csrfMetaElt = doc.select("meta[name=_csrf]");
		final String csrfToken = csrfMetaElt.attr("content");
		return csrfToken;

	}

	private String getValidJSessionID()
	{
		Connection.Response res = null;
		try
		{
			res = Jsoup.connect(hostName)
					.method(Method.GET)
					.execute();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		if (res == null)
		{
			return null;
		}
		final String sessionId = res.cookie("JSESSIONID"); // you will need to check what the right cookie name is
		return sessionId;
	}

}
