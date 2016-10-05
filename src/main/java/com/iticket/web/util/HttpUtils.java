package com.iticket.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

	private static final Log logger = LogFactory.getLog(HttpUtils.class);

	public final static int MAX_TOTAL_CONNECTIONS = 3000;
	public final static int MAX_ROUTE_CONNECTIONS = 800;
	public static final int DEFAULT_TIMEOUT = 30 * 1000;
	public static final int SHORT_TIMEOUT = 30 * 1000;
	public static final int LONG_TIMEOUT = 120 * 1000;
	public static final int CONNECTION_TIMEOUT = 20 * 1000;
	public static final int EXCEPTION_HTTP_STATUSCODE = 9999;

	private static PoolingClientConnectionManager cm;
	static {
		SSLUtilities.trustAllHostnames();
		SSLUtilities.trustAllHttpsCertificates();

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

		cm = new PoolingClientConnectionManager(schemeRegistry);
		cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
	}

	public static DefaultHttpClient getHttpClient(int connectTimeout, int readTimeout) {
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeout);
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, readTimeout);
		DefaultHttpClient httpClient = new DefaultHttpClient(cm, params);
		return httpClient;
	}

	public static DefaultHttpClient getHttpClient(int connectTimeout, int readTimeout, BasicClientCookie cookie) {
		DefaultHttpClient httpClient = getHttpClient(connectTimeout, readTimeout);
		if (cookie != null) {
			CookieStore cookieStore = new BasicCookieStore();
			cookieStore.addCookie(cookie);
			httpClient.setCookieStore(cookieStore);
		}
		return httpClient;
	}

	public static void setMaxConnectionsPerHost(String url, int maxHostConnections) {
		try {
			URI uri = new URI(url);
			HttpHost httpHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
			cm.setMaxPerRoute(new HttpRoute(httpHost), maxHostConnections);
		} catch (Exception e) {
			logger.error("setMaxConnectionsPerHost error!", e);
		}
	}
	private static HttpGet getHttpGet(String url, Map<String, String> params, String encode) {
		String fullUrl = getFullUrl(url, params, encode);
		HttpGet httpGet = new HttpGet(fullUrl);
		httpGet.setHeader("Accept-Encoding", "gzip,deflate");
		return httpGet;
	}
	
	public static String getFullUrl(String url, Map<String, String> params, String encode) {
		if (params != null) {
			if (url.indexOf('?') == -1)
				url += "?";
			else
				url += "&";
			for (String name : params.keySet()) {
				try {
					if (StringUtils.isNotBlank(params.get(name)))
						url += name + "=" + URLEncoder.encode(params.get(name), encode) + "&";
				} catch (UnsupportedEncodingException e) {
				}
			}
			url = url.substring(0, url.length() - 1);
		}
		return url;
	}
	
	public static HttpResult getUrlAsString(String url, Map<String, String> params) {
		HttpGet httpGet = getHttpGet(url, params, "utf-8");
		HttpResult result = executeHttpRequest(httpGet, null, DEFAULT_TIMEOUT, "utf-8");
		return result;
	}
	public static HttpResult getUrlAsString(String url) {
		HttpGet httpGet = getHttpGet(url, new HashMap<String, String>(), "utf-8");
		HttpResult result = executeHttpRequest(httpGet, null, DEFAULT_TIMEOUT, "utf-8");
		return result;
	}
	private static HttpResult executeHttpRequest(HttpUriRequest request, Map<String, String> reqHeader, int timeoutMills, String charset) {
		return executeHttpRequest(request, reqHeader, null, timeoutMills, charset);
	}
	
	public static HttpResult postUrlAsString(String url, Map<String, String> params, Map<String, String> reqHeader, String encode) {
		HttpPost httpPost = getHttpPost(url, params, encode);
		return executeHttpRequest(httpPost, reqHeader, null, DEFAULT_TIMEOUT, encode);
	}

	private static HttpPost getHttpPost(String url, Map<String, String> params, String encoding) {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept-Encoding", "gzip,deflate");
		if (params != null) {
			List<NameValuePair> form = new ArrayList<NameValuePair>();
			for (String name : params.keySet()) {
				form.add(new BasicNameValuePair(name, params.get(name)));
			}
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, encoding);
				httpPost.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				logger.error("", e);
			}
		}
		return httpPost;
	}

	private static HttpResult executeHttpRequest(HttpUriRequest request, Map<String, String> reqHeader, BasicClientCookie cookie, int timeoutMills,
			String charset) {
		DefaultHttpClient client = getHttpClient(CONNECTION_TIMEOUT, timeoutMills, cookie);

		if (reqHeader != null) {
			for (String name : reqHeader.keySet()) {
				request.addHeader(name, reqHeader.get(name));
			}
		}
		try {
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(getEntity(response), charset);
				return HttpResult.getSuccessReturn(result);
			} else {
				int statusCode = response.getStatusLine().getStatusCode();
				String msg = "httpStatus:" + statusCode + response.getStatusLine().getReasonPhrase() + ", Header: ";
				Header[] headers = response.getAllHeaders();
				for (Header header : headers) {
					msg += header.getName() + ":" + header.getValue();
				}
				String result = EntityUtils.toString(getEntity(response), charset);
				request.abort();
				logger.error("ERROR HttpUtils:" + msg + request.getURI());
				return HttpResult.getFailure("httpStatus:" + response.getStatusLine().getStatusCode(), statusCode, result);
			}
		} catch (Exception e) {
			request.abort();
			logger.error(request.getURI().toString(), e);
			return HttpResult.getFailure(request.getURI() + " exception:" + e.getClass().getCanonicalName(), EXCEPTION_HTTP_STATUSCODE);
		}
	}

	private static HttpEntity getEntity(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		Header header = entity.getContentEncoding();
		if (header != null) {
			for (HeaderElement element : header.getElements()) {
				if (element.getName().toLowerCase().indexOf("gzip") != -1) {
					entity = new GzipDecompressingEntity(entity);
					break;
				}
			}
		}
		return entity;
	}

}
