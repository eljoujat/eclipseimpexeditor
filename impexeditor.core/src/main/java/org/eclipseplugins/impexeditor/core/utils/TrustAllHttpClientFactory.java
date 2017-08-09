package org.eclipseplugins.impexeditor.core.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

public class TrustAllHttpClientFactory implements HttpClientFactory {

	@Override
	public HttpClient buildHttpClient() {
		HttpClient client = null;
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("SSL");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// set up a TrustManager that trusts everything
		try {
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
			            public X509Certificate[] getAcceptedIssuers() {
			                    return null;
			            }

			            public void checkClientTrusted(X509Certificate[] certs,
			                            String authType) {
			            }

			            public void checkServerTrusted(X509Certificate[] certs,
			                            String authType) {
			            }
			} }, new SecureRandom());
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SSLSocketFactory sf = new SSLSocketFactory(sslContext);
		sf.setHostnameVerifier(new X509HostnameVerifier() {
			
			@Override
			public boolean verify(String hostname, SSLSession session) {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void verify(String arg0, X509Certificate arg1) throws SSLException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void verify(String arg0, SSLSocket arg1) throws IOException {
				// TODO Auto-generated method stub
				
			}
		});
		Scheme httpsScheme = new Scheme("https", 443, sf);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(httpsScheme);

		// apache HttpClient version >4.2 should use BasicClientConnectionManager
		ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);
		HttpClient httpClient = new DefaultHttpClient(cm);

		return httpClient;
	}

}
