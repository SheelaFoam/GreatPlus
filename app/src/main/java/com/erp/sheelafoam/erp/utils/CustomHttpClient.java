//
//  
//  Copyright (c) 2013  Shabdkosh.com. All rights reserved.
//
package com.erp.sheelafoam.erp.utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

public class CustomHttpClient {
	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds

	/** Single instance of our HttpClient */
	private static HttpClient mHttpClient;

	private static HttpClient getHttpClient() {
		if (mHttpClient == null) {
			mHttpClient = new DefaultHttpClient();
			final HttpParams params = mHttpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
		}
		return mHttpClient;

	}

	public static String executeHttpPost(String url,
                                         ArrayList<NameValuePair> postParameters) throws Exception {
		BufferedReader in = null;
		HttpResponse response = null;
		HttpPost request = null;
		try {
			HttpClient client = getHttpClient();
		//	String encodedUrl = URLEncoder.encode(url, "UTF-8");
			if (url.contains(" ") || url.contains("\n")/*|| url.contains("<") || url.contains(">") ||
				url.contains("^") || url.contains("%") || url.contains("{") ||  url.contains("}")
				|| url.contains("[") || url.contains("]") || url.contains("`") || url.contains("&")
				|| url.contains("\"") || url.contains("\\") || url.contains("|") || url.contains("%")*/){
			
				url = url.replace(" ", "%20");
				url = url.replace("\n", "%0A");
				
			/*url= (url.trim().replace(" ", "%20").replace("&", "%26")
			            .replace(",", "%2c").replace("(", "%28").replace(")", "%29")
			            .replace("!", "%21").replace("=", "%3D").replace("<", "%3C")
			            .replace(">", "%3E").replace("#", "%23").replace("$", "%24")
			            .replace("'", "%27").replace("*", "%2A").replace("-", "%2D")
			            .replace(".", "%2E").replace("/", "%2F").replace(":", "%3A")
			            .replace(";", "%3B").replace("?", "%3F").replace("@", "%40")
			            .replace("[", "%5B").replace("\\", "%5C").replace("]", "%5D")
			            .replace("_", "%5F").replace("`", "%60").replace("{", "%7B")
			            .replace("|", "%7C").replace("}", "%7D"));*/
				
			//	url = url.replace("]" , "%5d");
			//	url = url.replace("&", "&amp;");
			//	url = url.replace("%", "&amp;");
				
				
				request = new HttpPost(url);
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						postParameters);
				request.setEntity(formEntity);
				response = client.execute(request);

				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));

				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();

				String result = sb.toString();
				return result;
			} else if (url.contains("\n")) {
				url = url.replace("\n", "%0A");
				
				request = new HttpPost(url);
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						postParameters);
				request.setEntity(formEntity);
				response = client.execute(request);

				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));

				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();

				String result = sb.toString();
				return result;
			}else {
				request = new HttpPost(url);
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						postParameters);
				request.setEntity(formEntity);
				response = client.execute(request);

				in = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));

				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();

				String result = sb.toString();
				return result;
			}

		} finally {
			if (in != null) {
				try {
					in.close();

				} catch (IOException e) {
					request.abort();
					e.printStackTrace();
				}
			}
		}
	}

	public static String executeHttpGet(String url) throws Exception {
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			if (url.contains(" ") || url.contains("\n")/*|| url.contains("<") || url.contains(">") ||
					url.contains("^") || url.contains("%") || url.contains("{") ||  url.contains("}")
					|| url.contains("[") || url.contains("]") || url.contains("`") || url.contains("&")
					|| url.contains("\"") || url.contains("\\") || url.contains("|") || url.contains("%")*/){
				
					url = url.replace(" ", "%20");
					url = url.replace("\n", "%0A");
					/*url = url.replace("<" , "%3c");
					url = url.replace(">", "%3e");
					url = url.replace("^", "%5e");
					url = url.replace("{" , "%7b");
					url = url.replace("}" , "%7d");
					url = url.replace("[" , "%5b");
					url = url.replace("]" , "%5d");
					url = url.replace("`" , "%60");
					url = url.replace("\"","");
					url = url.replace("\\\"", "\"");
					url = url.replace("\\", "%5c");
					url = url.replace("|", "%7c");
					
				//	url = url.replace("]" , "%5d");
				//	url = url.replace("&", "&amp;");
					url = url.replace("%", "&amp;");*/
			}
			
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			String result = sb.toString();
			return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String executeHttpDelete(String url,
                                           ArrayList<NameValuePair> postParameters) throws Exception,
            IOException {
		// TODO Auto-generated method stub
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			HttpPost request = new HttpPost(url);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters);
			request.setEntity(formEntity);
			HttpResponse response = client.execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();

			String result = sb.toString();
			return result;
		} finally {
			if (in != null) {
				try {
					in.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
