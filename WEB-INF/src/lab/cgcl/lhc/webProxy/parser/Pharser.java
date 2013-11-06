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
		changeURL( "src" , document , localURL , baseURI , remoteDomain);
		changeURL( "href" , document , localURL , baseURI , remoteDomain);
		changeURL( "action" , document , localURL , baseURI , remoteDomain);
		
		//CSS
		
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
	
	
	protected static void changeURL (String attribute , Document document, String localURL , String baseURI ,String remoteDomain) {
		//deal with action
		Elements actions = document.getElementsByAttribute(attribute);
		for(Element ele : actions) {
			String sourceURI = ele.attr(attribute);
			if (!sourceURI.contains("//")) {
				if (sourceURI.startsWith("/")) {
					sourceURI = remoteDomain + sourceURI;
				} else {
					sourceURI = baseURI + sourceURI;
				}
			}
			try {
				String proxyURI = localURL + "?url=" + URLEncoder.encode(sourceURI , "UTF-8");
				ele.attr(attribute, proxyURI);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 解析css中的 url()
	 * @param attribute
	 * @param document
	 * @param localURL
	 * @param baseURI
	 * @param remoteDomain
	 */
	protected static void changeURLinCSS (String attribute , Document document, String localURL , String baseURI ,String remoteDomain){
		
	}
	
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
