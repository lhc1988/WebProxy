package lab.cgcl.lhc.webProxy.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.cgcl.lhc.webProxy.Proxy;
import lab.cgcl.lhc.webProxy.parser.Pharser;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ProxyServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8417288687092589975L;
	
	private static String localURL = "http://127.0.0.1:8080";

	@Override
	public void doGet(HttpServletRequest request , HttpServletResponse response){
		String url = request.getParameter("url");
		Proxy proxy = new Proxy();
		
		try {
			proxy.doProxy(url);
			int type = setHeader(proxy.getHeaders(), response);
			String result = proxy.getContent();
			switch (type) {
			case 1 : 	Document doc = Jsoup.parse(result);
						Pharser.redirectURI(doc , localURL ,url);
				      	String guideBar = "<div>your url is ------------\n\r" + url + "</div>";
				      	Pharser.addGuideBar(doc, guideBar);
				      	result = doc.toString();
				      	break;
			case 2 :	Document doc1 = Jsoup.parse(result);
						Pharser.redirectURI(doc1 , localURL ,url);
						result = doc1.toString();
						break;
			default : 
			}
//			if (result.contains("<html") || url.endsWith(".js") || url.endsWith(".css")) {
//				Document doc = Jsoup.parse(result);
//				Pharser.redirectURI(doc , localURL ,url);
//		      	String guideBar = "<div>your url is ------------\n\r" + url + "</div>";
//		      	Pharser.addGuideBar(doc, guideBar);
//		      	
//			} 
			
			response.getWriter().write(result);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void doPost(HttpServletRequest request , HttpServletResponse response){
		doGet(request , response);
	}
	
	@Override
	public void init(){
		localURL = this.getServletConfig().getInitParameter("localURL");
	}
	
	/**
	 * 添加头信息
	 * @param headers
	 * @param response
	 * @return 1:是html  2:是css 3:是js 0 其他
	 */
	public int setHeader (Header[] headers , HttpServletResponse response) {
		for (int i = 0; i<headers.length; i++) {
			response.setHeader(headers[i].getName(), headers[i].getValue());
			if (headers[i].getName().equals("Content-Type")) {
				if (headers[i].getValue().contains("html")) {
					return 1;
				} else if (headers[i].getValue().contains("text/css")) {
					return 2;
				} else if (headers[i].getValue().contains("javascript")) {
					return 3;
				} else
					return 0;
			}
		}
		return 0;
	}

}
