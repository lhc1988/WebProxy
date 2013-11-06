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
		String remoteDomain = getDomain(baseURI);
		//deal with src=
		Elements srcs = document.getElementsByAttribute("src");
		for(Element ele : srcs) {
			String sourceURI = ele.attr("src");
			//判断是否绝对路径
			if (!sourceURI.contains("//")) {
				//相对根路径
				if (sourceURI.startsWith("/")) {
					sourceURI = remoteDomain + sourceURI;
				} else { //相对当前路径
					sourceURI = baseURI + sourceURI;
				}
			}
			try {
				String proxyURI = localURL + "?url=" + URLEncoder.encode(sourceURI , "UTF-8");
				ele.attr("src", proxyURI);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		//deal with href=
		Elements hrefs = document.getElementsByAttribute("href");
		for(Element ele : hrefs) {
			String sourceURI = ele.attr("href");
			if (!sourceURI.contains("//")) {
				if (sourceURI.startsWith("/")) {
					sourceURI = remoteDomain + sourceURI;
				} else {
					sourceURI = baseURI + sourceURI;
				}
			}
			try {
				String proxyURI = localURL + "?url=" + URLEncoder.encode(sourceURI , "UTF-8");
				ele.attr("href", proxyURI);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		//deal with url() 
		Elements urls = document.getElementsByAttribute("url");
		for(Element ele : urls) {
			String sourceURI = ele.attr("url");
			if (!sourceURI.contains("//")) {
				if (sourceURI.startsWith("/")) {
					sourceURI = remoteDomain + sourceURI;
				} else {
					sourceURI = baseURI + sourceURI;
				}
			}
			try {
				String proxyURI = localURL + "?url=" + URLEncoder.encode(sourceURI , "UTF-8");
				ele.attr("href", proxyURI);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return document;
		
	}
	
	/**
	 * 获取域名
	 * @param url
	 * @return
	 */
//	public static String getDomain (String url) {
//		final char split = '/';
//		final String root = "//";
//		int rootindex = url.indexOf(root);
//		int lastindex = url.indexOf(split, rootindex + 2);
//		if (rootindex == -1) 
//			return url;
//		else
//			return url.substring(rootindex +2  , lastindex);
//	}
	
	public static String getDomain (String url) {
		int index = -1;
//		int start = 0;
		final char split = '/';
		if (url.startsWith("http") || url.startsWith("//")) {
			for (int i = 0 ; i <3 ; i++) {
//				if (i == 2) {
//					start = index;
//				}
				index = url.indexOf(split , index+1);
			}
		} else {
			index = url.indexOf(split , index+1);
		}
		if (index == -1)
			return url;
		else 
//			return url.substring(start +1 , index);
			return url.substring(0 , index);
	}
	
	public static String getBaseURI (String url) {
		return null;
	}
	
	

}
