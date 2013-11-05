package lab.cgcl.lhc.webProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import lab.cgcl.lhc.webProxy.parser.Pharser;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Proxy {
	
	private BasicCookieStore cookieStore;
	public static void main(String[] args) {
//		Proxy proxy = new Proxy() ;
//		try {
//			proxy.doProxy("www.google.com");
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		String url = "http://www.google.com.cn/ass/101";
		System.out.println(getDomain(url));
	}
	
	public String doProxy(String url) throws ClientProtocolException, IOException {
		String result = "";
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		
        try {
//            HttpHost target = new HttpHost("issues.apache.org", 443, "https");
        	//这里是主机名，
        	String decoded_url = URLDecoder.decode(url , "UTF-8");
        	String URI = Pharser.getDomain(decoded_url);
        	HttpHost target = null ;
        	if (URI.startsWith("https")) {
        		target = new HttpHost(URI.substring(8 , URI.length()) , 443 , "https" );
        	} else if (URI.startsWith("http")) {
        		target = new HttpHost(URI.substring(7 , URI.length()));
        	} else {
        		target = new HttpHost(URI);
        	}
            HttpHost proxy = new HttpHost(proxyAddr, proxyPort, proxyScheme);

            RequestConfig config = RequestConfig.custom().setProxy(proxy)
            		.setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).build();
            
            String path = decoded_url.substring(URI.length(), decoded_url.length());
            HttpGet request = new HttpGet(path);
            request.setConfig(config);

            System.out.println("executing request to " + decoded_url + " via " + proxy);
            CloseableHttpResponse response = httpclient.execute(target, request);
            try {
                HttpEntity entity = response.getEntity();

//                System.out.println("----------------------------------------");
//                System.out.println(response.getStatusLine());
//                Header[] headers = response.getAllHeaders();
//                for (int i = 0; i<headers.length; i++) {
//                    System.out.println(headers[i]);
//                }
//                System.out.println("----------------------------------------");

                if (entity != null) {
                	result = getHttpResponse(entity.getContent());
                	
                }
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return result;
	}
	
	private static String url = "";
	private static int port ;
	private static String scheme = "http";
	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		Proxy.url = url;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		Proxy.port = port;
	}

	public static String getScheme() {
		return scheme;
	}

	public static void setScheme(String scheme) {
		Proxy.scheme = scheme;
	}
	
	private static String proxyAddr = "127.0.0.1";
	private static int proxyPort = 8087;
	private static String proxyScheme = "http";
	public static String getProxyAddr() {
		return proxyAddr;
	}

	public static void setProxyAddr(String proxyAddr) {
		Proxy.proxyAddr = proxyAddr;
	}

	public static int getProxyPort() {
		return proxyPort;
	}

	public static void setProxyPort(int proxyPort) {
		Proxy.proxyPort = proxyPort;
	}

	public static String getProxyScheme() {
		return proxyScheme;
	}

	public static void setProxyScheme(String proxyScheme) {
		Proxy.proxyScheme = proxyScheme;
	}
	
	protected static String getDomain (String url) {
		char split = '/';
		int index = -1;
		for (int i = 0 ; i <3 ; i++) {
			index = url.indexOf(split , index+1);
		}
		
		return url.substring(0 , index);
		
	}
	
	public static String getHttpResponse(InputStream in)
	{
	String result;
	StringBuffer temp = new StringBuffer();
	BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
	try {
		for(String tempstr = ""; (tempstr = buffer.readLine()) != null;)
		temp = temp.append(tempstr);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		try {
			buffer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	result = temp.toString().trim();
	return result;
	}
		
}
