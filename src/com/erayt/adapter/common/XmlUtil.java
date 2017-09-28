package com.erayt.adapter.common;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;


public class XmlUtil {
	private static final Logger logger = Logger.getLogger(XmlUtil.class.getSimpleName());
	
	private Document doc;
	
	public XmlUtil(String url){
		try {
			SAXReader  saXReader =new SAXReader();
			long startTime = System.currentTimeMillis();
			InputStream in = new FileInputStream(url);
			doc = saXReader.read(in);
			long endTime = System.currentTimeMillis();

			logger.debug("读取配置文件耗时["+(endTime-startTime)+"]ms");
		} catch (Exception e) {
			logger.error("load xml error{}.." + url,e);
			System.exit(1);
		}
	}

	public List<Element> getElements(String xPath){
		try {
			XPath xpathSelector = DocumentHelper.createXPath(xPath);
			List<Element> elements= (List<Element>)xpathSelector.selectNodes(doc);

			return elements;
		} catch (Exception e) {
			logger.error("load xml error{}..node=" + xPath +"...",e);
			System.exit(1);
		}
		return null;
	}
	
	
	
	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}
}
