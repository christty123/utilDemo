package com.util.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * for GET & POST http(s) request
 * @author christ
 *
 */
public class HttpClientUtil {
	final static int DEFAULT_TIMEOUT=12000;
	final static String FORM_CONTENT="application/x-www-form-urlencoded";
	final static String JSON_CONTENT="application/json";
	
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	public static String post(String url, Map<String, String> params,int socketTimeout, int connectTimeout) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpPost post = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(socketTimeout)
					.setConnectTimeout(connectTimeout).build();// 设置请求和传输超时时间
			post.setConfig(requestConfig);
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();

			if(params!=null && !params.isEmpty()){
				for (String key : params.keySet()) {
					qparams.add(new BasicNameValuePair(key, params.get(key)));
				}
			}

			post.setEntity(new UrlEncodedFormEntity(qparams, "UTF-8"));
			HttpResponse httpResponse = client.execute(post);
		
			HttpEntity entity = httpResponse.getEntity();
			
			if (entity != null) {
				return EntityUtils.toString(entity);
			} else {
				return null;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			client.close();
		}
	}
	/***
	 * 推送编码中文处理
	 * @param url
	 * @param params
	 * @param socketTimeout
	 * @param connectTimeout
	 * @param contentType default is :application/x-www-form-urlencoded
	 * @return
	 * @throws Exception
	 */
	public static String post(String url, Map<String, String> params,int socketTimeout, int connectTimeout,String contentType) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpPost post = new HttpPost(url);
			post.setHeader("content-type", (null==contentType?FORM_CONTENT:contentType)); 
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(socketTimeout)
					.setConnectTimeout(connectTimeout).build();// 设置请求和传输超时时间
			post.setConfig(requestConfig);
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();

			if(params!=null && !params.isEmpty()){
				for (String key : params.keySet()) {
					qparams.add(new BasicNameValuePair(key, params.get(key)));
				}
			}

			post.setEntity(new UrlEncodedFormEntity(qparams, "UTF-8"));
			HttpResponse httpResponse = client.execute(post);
		
			HttpEntity entity = httpResponse.getEntity();
			
			if (entity != null) {
				return EntityUtils.toString(entity);
			} else {
				return null;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			client.close();
		}
	}
	
	
	
	/**
	 * POST method with Form content
	 * @param url
	 * @param params uri parameters in string format. eg. p1=abc&p2=def...
	 * @return response body
	 * @throws Exception
	 * @author Sam Hui 20160314 created
	 */
	public static String postForm(String url, String params) throws Exception {		
		return post(url, params, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT, FORM_CONTENT);
	}	
	
	/**
	 * 
	 * @param url url without parameters
	 * @param content uri parameters in string format. eg. p1=abc&p2=def...
	 * @param socketTimeout
	 * @param connectTimeout
	 * @param contentType application/x-www-form-urlencoded, application/json...
	 * @return response body
	 * @throws Exception
	 * @author Unknown
	 */
	public static String post(String url, String content,int socketTimeout, int connectTimeout,String contentType) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpPost post = new HttpPost(url);
			post.setHeader("content-type", contentType); 
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(socketTimeout)
					.setConnectTimeout(connectTimeout).build();// 设置请求和传输超时时间
			post.setConfig(requestConfig);
			
			StringEntity requestEntity = new StringEntity(content, "UTF-8"); 
			post.setEntity(requestEntity);
			HttpResponse httpResponse = client.execute(post);
			HttpEntity entity = httpResponse.getEntity();
			Header[] responseHeaders = httpResponse.getAllHeaders();
//			for(Header header : responseHeaders){
//				System.out.println(header.getName()  + ":"  + header.getValue());
//			}
			if (entity != null) {
				return EntityUtils.toString(entity);
			} else {
				return null;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			client.close();
		}
	}


	/**
	 * 
	 * @param url
	 * @param params uri param format eg. p1=abc&p2=def&...
	 * @return response body
	 * @throws Exception
	 * @author Sam Hui 20160314 created 
	 */
	public static String get(String url, String params) throws Exception { 
		Map<String, String> paramMap=null;
				
		if (params!=null && params.trim().length() > 0 ) {
			paramMap=new HashMap();		
			//System.out.println("debug: params=" + params);
			for (String pp: params.split("&")) {
				//System.out.println("debug: pp=" + pp);
			    paramMap.put(pp.split("=")[0], pp.split("=")[1]);
			}
		}
		
		return get(url, paramMap, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT );
	}
	
	/**
	 * 
	 * @param url
	 * @param params uri param format. eg. map.put("p1", "value")....
	 * @return response body
	 * @throws Exception
	 * @author Sam Hui 20160314 created
	 */
	public static String get(String url, Map<String, String> params) throws Exception { 		
		return get(url, params, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT );
	}	
	
	
	/**
	 * 
	 * @param url url without parameters
	 * @param content uri parameters hasMap.  eg. map.put("p1", "value").... 
	 * @param socketTimeout
	 * @param connectTimeout
	 * @return response body
	 * @throws Exception
	 * @author Unknown
	 */
	public static String get(String url, Map<String, String> params,int socketTimeout, int connectTimeout) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		try {

			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			if(params!=null && !params.isEmpty()){
				for (String key : params.keySet()) {
					qparams.add(new BasicNameValuePair(key, params.get(key)));
				}
			}
			
			// 要传递的参数.　
			url = url +"?"+ URLEncodedUtils.format(qparams, HTTP.UTF_8);
			HttpGet get = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(socketTimeout)
					.setConnectTimeout(connectTimeout).build();// 设置请求和传输超时时间
			get.setConfig(requestConfig);

			HttpResponse httpResponse = client.execute(get);
			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {
				return EntityUtils.toString(entity);
			} else {
				return null;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			client.close();
		}
	}
	

	public static CloseableHttpClient createSSLInsecureClient() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                public boolean isTrusted(X509Certificate[] chain,
                                String authType) throws CertificateException {
                    return true;
                }
                    }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
       
        return  HttpClients.createDefault();
    }
	
	
	public static String sslPost(String url, Map<String, String> params,int socketTimeout, int connectTimeout) throws Exception {
		CloseableHttpClient client = createSSLInsecureClient();
		try {
			HttpPost post = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(socketTimeout)
					.setConnectTimeout(connectTimeout).build();// 设置请求和传输超时时间
			post.setConfig(requestConfig);
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();

			if(params!=null && !params.isEmpty()){
				for (String key : params.keySet()) {
					qparams.add(new BasicNameValuePair(key, params.get(key)));
				}
			}

			post.setEntity(new UrlEncodedFormEntity(qparams, "UTF-8"));
			HttpResponse httpResponse = client.execute(post);
			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {
				return EntityUtils.toString(entity);
			} else {
				return null;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			client.close();
		}
	}
	
	
	
	public static CloseableHttpClient getHttpClientWithLogin(String host,String loginUrl,String userName,String password,int socketTimeout, int connectTimeout) throws ClientProtocolException, IOException {
		CloseableHttpClient client = createSSLInsecureClient();
		
		HttpGet indexGet = new HttpGet(loginUrl);
		HttpResponse indexRes = client.execute(indexGet);
		String cookieName = indexRes.getHeaders("Set-Cookie")[0].getValue();
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("user", userName));
		nvps.add(new BasicNameValuePair("passwd", password));
		//CloseableHttpClient client = createSSLInsecureClient();
		HttpPost httpPost = new HttpPost(loginUrl);
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout).build();// 设置请求和传输超时时间
		httpPost.setConfig(requestConfig);
		
		httpPost.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36");
		httpPost.setHeader("Host", host);
		httpPost.setHeader("Cookie", cookieName);
		//httpPost.setHeader("Referer",referUrl);
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			logger.debug("login to " + host + "success.");
		} else {
			logger.debug("login to " + host + "failed.");
			throw new RuntimeException("Login failed.");
		}
		return client;
		
	}
	
	
//	public static void main(String[] args) throws Exception {
//		//System.out.println(HttpClientUtil.get("http://www.baidu.com", null, 1000, 1000));
//		
//		/*Map<String,String> params = new HashMap<String, String>();
//		
//		String content = "{\"username\":\"Janey\",\"password\":\"123456\"}"; 
//		String response = HttpClientUtil.post("http://localhost:8080/coreService/rest/home/login", content, 1000, 1000,"application/json;charset=UTF-8");
//		System.out.println(response);*/
//		
//		File file = new File(
//				"D:\\sino-hkgta-be\\gta-integration\\src\\main\\java\\com\\sinodynamic\\hkgta\\integration\\util\\SinoDynamic_OTA_HotelAvailRQ.xml");
//		FileReader fin = new FileReader(file);
//		BufferedReader bf = new BufferedReader(fin);
//		StringBuffer sbf = new StringBuffer();
//		String line = "";
//		while ((line = bf.readLine()) != null) {
//			sbf.append(line);
//		}
//		System.out.println(sbf);
//
//		Map params2 = new HashMap();
//		params2.put("payload", sbf.toString());
//		System.out.println("return:"+ HttpClientUtil.sslPost("https://hkgtauat.sinodynamic.com:8088/pmsws/htng/xmlhtng.php",params2,2000,2000));
//	}

}
