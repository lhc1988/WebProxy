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
import org.apache.http.util.EntityUtils;

public class Proxy {
	
	private BasicCookieStore cookieStore;
	private Header[] headers;
	private String content;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Header[] getHeaders() {
		return headers;
	}

	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}

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
		System.out.println(Pharser.getDomain(url));
	}
	
	public void doProxy(String url) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        try {
        	String decoded_url = URLDecoder.decode(url , "UTF-8");
        	String domain = Pharser.getDomain(decoded_url);
        	HttpHost target = null ;
        	if (domain.startsWith("https")) {
        		target = new HttpHost(domain.substring(8 , domain.length()) , 443 , "https" );
        	} else if (domain.startsWith("http")) {
        		target = new HttpHost(domain.substring(7 , domain.length()));
        	} else {
        		target = new HttpHost(domain);
        	}
        	
//        	if (decoded_url.startsWith("http")) {
//        		target = new HttpHost(decoded_url.substring(7 , decoded_url.length()));
//        	} 
        	//--------------
            HttpHost proxy = new HttpHost(proxyAddr, proxyPort, proxyScheme);

            RequestConfig config = RequestConfig.custom().setProxy(proxy)
            		.setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).build();
            
            String path = decoded_url.substring(domain.length(), decoded_url.length());
            HttpGet request = new HttpGet(path);
//            HttpGet request = new HttpGet("/");
            request.setConfig(config);

            System.out.println("executing request to " + decoded_url + " via " + proxy);
            CloseableHttpResponse response = httpclient.execute(target, request);
            if ( response.getStatusLine().getStatusCode() != 200) {
            	System.out.println("error:" + response.getStatusLine().getStatusCode() );
            	return;
            }
            try {
            	headers = response.getAllHeaders();
            	HttpEntity entity = response.getEntity();
//            	String charset = entity.getContentEncoding().getValue();
            	content = EntityUtils.toString(entity );
//            	content = getHttpResponse(entity.getContent());
            } catch (Exception e) {
            	e.printStackTrace();
            }
            finally {
            	response.close();
            }

//                System.out.println("----------------------------------------");
//                System.out.println(response.getStatusLine());
//                Header[] headers = response.getAllHeaders();
//                for (int i = 0; i<headers.length; i++) {
//                    System.out.println(headers[i]);
//                }
//                System.out.println("----------------------------------------");

        } finally {
            httpclient.close();
        }
	}
	
	private String url = "";
	private int port ;
	private String scheme = "http";
	
	private String proxyAddr = "127.0.0.1";
	private int proxyPort = 8087;
	private String proxyScheme = "http";
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getProxyAddr() {
		return proxyAddr;
	}

	public void setProxyAddr(String proxyAddr) {
		this.proxyAddr = proxyAddr;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyScheme() {
		return proxyScheme;
	}

	public void setProxyScheme(String proxyScheme) {
		this.proxyScheme = proxyScheme;
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
