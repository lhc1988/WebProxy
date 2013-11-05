package lab.cgcl.lhc.webProxy.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import lab.cgcl.lhc.webProxy.Proxy;
import lab.cgcl.lhc.webProxy.parser.Pharser;

public class ProxyServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8417288687092589975L;

	@Override
	public void doGet(HttpServletRequest request , HttpServletResponse response){
		String url = request.getParameter("url");
		Proxy proxy = new Proxy();
		try {
			String result = proxy.doProxy(url);
			Document doc = Jsoup.parse(result);
			Pharser.redirectURI(doc , "http://115.156.232.46:8089/WebProxy/browser");
	      	String guideBar = "<div>your url is ------------\n\r" + url + "</div>";
	      	Pharser.addGuideBar(doc, guideBar);
	      	result  = doc.toString();
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

}
