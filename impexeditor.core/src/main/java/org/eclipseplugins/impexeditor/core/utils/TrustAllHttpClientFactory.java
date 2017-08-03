package org.eclipseplugins.impexeditor.core.utils;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

public class TrustAllHttpClientFactory implements HttpClientFactory {

	@Override
	public HttpClient buildHttpClient() {
		HttpClient client = null;
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			// set up a TrustManager that trusts everything
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} }, new SecureRandom());

			SSLSocketFactory sf = new SSLSocketFactory(sslContext);
			Scheme httpsScheme = new Scheme("https", 443, sf);
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(httpsScheme);
			// apache HttpClient version >4.2 should use BasicClientConnectionManager
			ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);
			client = new DefaultHttpClient(cm);
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
		}

		return client;
	}

}
