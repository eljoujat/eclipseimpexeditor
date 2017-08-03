package org.eclipseplugins.impexeditor.core.utils;
import org.apache.http.client.HttpClient;

public interface HttpClientFactory {

	HttpClient buildHttpClient();
}
