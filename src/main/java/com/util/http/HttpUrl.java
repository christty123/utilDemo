package com.util.http;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpUrl {
	private static final String	URL_PARAM_CONNECT_FLAG	= "&";
	private static Log			logger					= LogFactory.getLog(HttpUrl.class);
	private final static String	USER_AGENT				= "Mozilla/5.0";

	// HTTP GET request
	public static String sendHttpGet(String url) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		return response.toString();

	}

	public static String sendGet(String url, String param) {
		String result = "";
		try {
			String urlName = "";
			if (param != null) {
				urlName = url + "?" + param;//
			} else {
				urlName = url;
			}
			URL U = new URL(urlName);
			URLConnection connection = U.openConnection();
			connection.connect();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			in.close();
		} catch (Exception e) {
			logger.error("error" + e);
		}
		return result;
	}

	public static String sendHttpsPost(String url, String param) {
		String result = "";
		try {
			String httpsURL = url;

			String query = param;

			URL myurl = new URL(httpsURL);
			HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
			con.setRequestMethod("POST");

			con.setRequestProperty("Content-length", String.valueOf(query.length()));
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
			con.setDoOutput(true);
			con.setDoInput(true);

			DataOutputStream output = new DataOutputStream(con.getOutputStream());

			output.writeBytes(query);

			output.close();

			DataInputStream input = new DataInputStream(con.getInputStream());

			for (int c = input.read(); c != -1; c = input.read())
				System.out.print((char) c);
			input.close();

			logger.debug("Resp Code:" + con.getResponseCode());
			logger.debug("Resp Message:" + con.getResponseMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static String sendPost(String url, String param) {
		logger.debug(url + "?" + param);
		String result = "";
		try {
			URL httpurl = new URL(url);
			// logger.warn("httpurl:"+httpurl+"param"+param);
			HttpURLConnection httpConn = (HttpURLConnection) httpurl.openConnection();
			// logger.warn("httpConn:"+httpConn);
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			PrintWriter out = new PrintWriter(httpConn.getOutputStream());
			logger.debug("out:" + out);
			out.print(param);
			out.flush();
			out.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			// logger.warn("in:"+in);
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			// logger.warn("result :"+result );

			in.close();
		} catch (Exception e) {
			logger.warn("error" + e);
		}
		// System.out.println("------------------------"+result);
		return result;
	}

	/**
	 * ���URL
	 * 
	 * @param map
	 *            Map
	 * @return String
	 */
	private static String getUrl(Map map) {
		if (null == map || map.keySet().size() == 0) {
			return ("");
		}
		StringBuffer url = new StringBuffer();
		Set keys = map.keySet();
		for (Iterator i = keys.iterator(); i.hasNext();) {
			String key = String.valueOf(i.next());
			if (map.containsKey(key)) {
				url.append(key).append("=").append(String.valueOf(map.get(key))).append(URL_PARAM_CONNECT_FLAG);
			}
		}
		String strURL = "";
		strURL = url.toString();
		if (URL_PARAM_CONNECT_FLAG.equals("" + strURL.charAt(strURL.length() - 1))) {
			strURL = strURL.substring(0, strURL.length() - 1);
		}
		return (strURL);
	}

	public static List URLGet(String strUrl, Map map) throws IOException {
		String strtTotalURL = "";
		List result = new ArrayList();
		if (map != null) {
			if (strtTotalURL.indexOf("?") == -1) {
				strtTotalURL = strUrl + "?" + getUrl(map);
			} else {
				strtTotalURL = strUrl + "&" + getUrl(map);
			}
		} else {
			strtTotalURL = strUrl;
		}
		URL url = new URL(strtTotalURL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setUseCaches(false);
		con.setFollowRedirects(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		while (true) {
			String line = in.readLine();
			if (line == null) {
				break;
			} else {
				result.add(line);
			}
		}
		in.close();
		return (result);
	}

}