package lab.cgcl.lhc.webProxy.parser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Pharser {
	public static Document addGuideBar(Document source , String code) {
		Element body = source.body();
		Element firstEle = body.child(0);
		firstEle.prepend(code);
		return source;
	}
	
	public static Document redirectURI (Document document, String localURL) {
		//deal with src=
		Elements srcs = document.getElementsByAttribute("src");
		for(Element ele : srcs) {
			String sourceURI = ele.attr("src");
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
	
	

}
