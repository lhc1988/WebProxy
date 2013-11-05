package lab.cgcl.lhc.webProxy.parser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Pharser {
	public static Document addGuideBar(Document source , String code) {
		Element body = source.body();
		if(body!= null) {
			Element firstEle = body.child(0);
			firstEle.prepend(code);
		}
		return source;
	}
	
	public static Document redirectURI (Document document, String localURL , String baseURI) {
		//deal with src=
		Elements srcs = document.getElementsByAttribute("src");
		for(Element ele : srcs) {
			String sourceURI = ele.attr("src");
			//TODO 把相对路径统一成绝对路径
			if (!sourceURI.startsWith("http")) {
				sourceURI = baseURI + sourceURI;
			}
			try {
				String proxyURI = localURL + "?url=" + URLEncoder.encode(sourceURI , "UTF-8");
				ele.attr("src", proxyURI);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//deal with href=
		Elements hrefs = document.getElementsByAttribute("href");
		for(Element ele : hrefs) {
			String sourceURI = ele.attr("href");
			if (!sourceURI.startsWith("http")) {
				sourceURI = baseURI + sourceURI;
			}
			try {
				String proxyURI = localURL + "?url=" + URLEncoder.encode(sourceURI , "UTF-8");
				ele.attr("href", proxyURI);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return document;
		
	}
	
	/**
	 * 取得域名
	 * @param url
	 * @return
	 */
	public static String getDomain (String url) {
		int index = -1;
		final char split = '/';
		if (url.startsWith("http")) {
			for (int i = 0 ; i <3 ; i++) {
				index = url.indexOf(split , index+1);
			}
		} else {
			index = url.indexOf(split , index+1);
		}
		if (index == -1)
			return url;
		else 
			return url.substring(0 , index);
	}
	
	public static String getBaseURI (String url) {
		return null;
	}
	
	

}
