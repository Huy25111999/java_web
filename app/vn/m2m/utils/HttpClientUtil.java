package vn.m2m.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HttpClientUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	public static String sendGetMethod(String url) {
		return sendGetMethod(null,url);
	}

	public static String sendGetMethod(List<NameValuePair> params, String url) {
		String response ="";
		try {
			URIBuilder builder = new URIBuilder(url);
			//url query string
			if(params!=null){
				builder.setParameters(params);
			}

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(builder.build());
			// set http header
			setDefaultHttpHeader(httpGet);

			CloseableHttpResponse httpResponse = httpclient.execute(httpGet);


			try {
				StatusLine statusLine = httpResponse.getStatusLine();
				int resultCode = statusLine.getStatusCode();
				if (resultCode != HttpStatus.SC_OK) {
					logger.debug("Call method failed: " + statusLine);
				} else {
					logger.debug("Call method sucess");
				}

				HttpEntity entity = httpResponse.getEntity();

				response = EntityUtils.toString(entity);
//				System.out.println("response:"+response);
				entity.consumeContent();
			} finally {
				httpResponse.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return response;
	}

	public static String sendPostMethod(String url) {
		return sendPostMethod(null,url);
	}

	public static String sendPostMethod(List<NameValuePair> params, String url) {
		String response ="";
		try {
			URIBuilder builder = new URIBuilder(url);
			//url query string
			if(params!=null){
				builder.setParameters(params);
			}

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(builder.build());
			// set http header
			setDefaultHttpHeader(httpPost);

			CloseableHttpResponse httpResponse = httpclient.execute(httpPost);

			try {
				StatusLine statusLine = httpResponse.getStatusLine();
				int resultCode = statusLine.getStatusCode();
				if (resultCode != HttpStatus.SC_OK) {
					logger.debug("Call method failed: " + statusLine);
				} else {
					logger.debug("Call method sucess");
				}

				HttpEntity entity = httpResponse.getEntity();

				response = EntityUtils.toString(entity);
				entity.consumeContent();
			} finally {
				httpResponse.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return response;
	}

	public static String sendPostMethod(List<NameValuePair> params, String url, String body) {
		String response ="";
		try {
			URIBuilder builder = new URIBuilder(url);
			//url query string
			if(params!=null){
				builder.setParameters(params);
			}


			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(builder.build());
			// set http header
			setDefaultHttpHeader(httpPost);

			// form data
			StringEntity input = new StringEntity(body);
			input.setContentType("application/x-www-form-urlencoded; charset=utf-8");
			httpPost.setEntity(input);

			CloseableHttpResponse httpResponse = httpclient.execute(httpPost);

			try {
				StatusLine statusLine = httpResponse.getStatusLine();
				int resultCode = statusLine.getStatusCode();
				if (resultCode != HttpStatus.SC_OK) {
					logger.debug("Call method failed: " + statusLine);
				} else {
					logger.debug("Call method sucess");
				}

				HttpEntity entity = httpResponse.getEntity();

				response = EntityUtils.toString(entity);
				entity.consumeContent();
			} finally {
				httpResponse.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return response;
	}

	private static HttpGet setDefaultHttpHeader(HttpGet httpGet){
		// http header
		httpGet.addHeader("Content-Type", "text/html; charset=utf-8");
		httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
//		httpGet.addHeader("Content-Type", "application/json; charset=utf-8");
//		httpGet.addHeader("Accept", "application/json");
		httpGet.addHeader("Accept-Charset", "utf-8");

		return httpGet;
	}

	private static HttpPost setDefaultHttpHeader(HttpPost httpPost){
		// http header
		httpPost.addHeader("Content-Type", "text/html; charset=utf-8");
		httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
		httpPost.addHeader("Accept-Charset", "utf-8");

		return httpPost;
	}

	private static URIBuilder createURIBuilder(String host, int port, String restPath, List<NameValuePair> params, boolean isHttps){
		URIBuilder builder = new URIBuilder();
		if(isHttps){
			builder.setScheme("https");
		}
		else {
			builder.setScheme("http");
		}
		builder.setHost(host).setPort(port).setPath(restPath)
				.setParameters(params);
		return builder;
	}
}
