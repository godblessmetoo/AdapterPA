package com.erayt.adapter.main;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.erayt.adapter.common.FileUtil;
import com.erayt.adapter.common.SystemParams;
import com.erayt.adapter.common.XmlUtil;
import com.erayt.adapter.domain.DataConfig;
import com.erayt.adapter.domain.DataPropertie;
import com.erayt.adapter.xnet122.XnetService;
import com.erayt.utils.XStream;
import com.erayt.xnet4j.XNetData;

/**
 * 适配主面板
 * @author hhq 20160713
 *
 */
public class AdapterContext implements Runnable{

	private static final Logger logger = Logger.getLogger(AdapterContext.class.getSimpleName());
	public static ConcurrentHashMap<String, XnetService> listenMap = new ConcurrentHashMap<String, XnetService>(); 
	public static List<DataConfig> responseList = new ArrayList<DataConfig>();
	public static List<DataConfig> requestList = new ArrayList<DataConfig>();
	public static Map<String, Object> keyMap = new HashMap<String, Object>();
	public static ConcurrentHashMap<String, XnetService> listenIpMap = new ConcurrentHashMap<String, XnetService>(); 
	public volatile static String appName;
	private  ApapterHandler utils = new ApapterHandler();
	public static BlockingQueue<Map> sendQueue = new LinkedBlockingQueue<Map>();

	/**
	 * 系统初始化启动
	 * @author hhq 20160713
	 * @param url
	 */
	public AdapterContext(String url){
		try {
			logger.info("init xnetList begin!");
			String conPath = FileUtil.getBinPath(AdapterMain.class);
			XmlUtil x = new XmlUtil(conPath+url);
			List<Element>  xnetInfos = x.getElements("/Listen");
			for(Element element : xnetInfos){
				List<Element> eles=element.elements();
				//titles
				for(Element ele : eles){
					XnetService xnetService = new XnetService();
					xnetService.setVersion(ele.element("Version").getTextTrim());
					xnetService.setAddress(ele.element("Address").getTextTrim());
					xnetService.setPort(Integer.parseInt(ele.element("Port").getTextTrim()));
					xnetService.setAliveInterval(Integer.parseInt(ele.element("AliveInterval").getTextTrim()));
					xnetService.setAppName(ele.element("AppName").getTextTrim());
					xnetService.setTitles(ele.element("Titles").getTextTrim());
					
					//load Data
					List<Element> dataEles=((Element)ele.elements("Datas").get(0)).elements();
					for(Element dataEle : dataEles){
						DataConfig dataConfig = new DataConfig();
						dataConfig.setTitleSource(dataEle.element("Title").attributeValue("source"));
						dataConfig.setTitleTarget(dataEle.element("Title").attributeValue("target"));
						
						//title + IP
						
						dataConfig.setMsgType(Integer.parseInt(dataEle.element("MsgType").getTextTrim()));
						dataConfig.setHandLerType(Integer.parseInt(dataEle.element("HandLerType").getTextTrim()));
						
						dataConfig.setxNetCommand(dataEle.element("XNetCommand").getTextTrim());
						dataConfig.setFilter(dataEle.element("Filter").getTextTrim());
						dataConfig.setXnetAppName(xnetService.getAppName());
						
						
						List<Element> propertiesEle = ((Element)dataEle.elements("propertys").get(0)).elements();
						List<DataPropertie> properties = loadProperties(propertiesEle);
						dataConfig.setPropertys(properties);
						if(dataConfig.getMsgType()==SystemParams.DATA_MSGTYPE_REC){
							responseList.add(dataConfig);
						}else{
							requestList.add(dataConfig);
						}
						
					}
					xnetService.init();
					listenMap.put(xnetService.getAppName(), xnetService);
					//推送转发平台的链路
					if(xnetService.getVersion().equals("1.2.2")&&xnetService.getAppName().contains("XpriceRateFw")){
						listenIpMap.put(xnetService.getAddress()+xnetService.getPort(), xnetService);
					}
				}
				
			}
			
			loadKeyMapConfig();
			logger.info("wait 10s to initSend.....");
			Thread.sleep(10*1000);
			this.initSend();
			
			//启动发送线程
			SendHandle sendHandle = new SendHandle();
			Thread sendTh = new Thread(sendHandle);
			sendTh.start();
			logger.info("init xnetList end!");
		} catch (Exception e) {
			logger.info("init xnetList error="+e.getMessage());
			logger.info("init xnetList error={}!",e);
		}
	}
	
	/***
	 * 加载系统映射转换配置文件
	 * @author hhq 20160713
	 */
	public void loadKeyMapConfig(){
		logger.info("init xnetList begin!");
		String conPath = FileUtil.getBinPath(AdapterMain.class);
		XmlUtil x = new XmlUtil(conPath+"config/KeyMap_Config.xml");
		List<Element>  xnetInfos = x.getElements("/Maps");
		for(Element element : xnetInfos){
			List<Element> eles =element.elements();
			//titles
			for(Element ele : eles){
				Map<String,Object> map = new HashMap<String, Object>();
				List<Element> tempeless = ele.elements();
				for(Element temp : tempeless){
					map.put(temp.getName(), temp.getTextTrim());
				}
				
				if(ele.getName().equals("FICNAME")){
					keyMap.put(ele.attributeValue("VALUE"), map);
				}else{
					keyMap.put(ele.getName(), map);
				}
				
			}
		}
	}
	
	/**
	 * 读取报文字段配置
	 * @author hhq 20160713
	 * @param properties
	 * @return
	 */
	public List<DataPropertie> loadProperties(List<Element> properties){
		try {
			List<DataPropertie> bodys = new ArrayList<DataPropertie>();
			for(Element proEle : properties){
				DataPropertie pro = new DataPropertie();
				pro.setField(proEle.attributeValue("field"));
				pro.setFieldType(Integer.parseInt(proEle.attributeValue("fieldType")));
				String valueType = proEle.attributeValue("valueType");
				if(valueType==null||valueType.equals("")){
					pro.setValueType(0);
				}else{
					pro.setValueType(Integer.parseInt(valueType));
				}
				pro.setValue(proEle.attributeValue("value"));
				pro.setFormat(proEle.attributeValue("format"));

				bodys.add(pro);
			}
			return bodys;
		} catch (Exception e) {
			logger.info("loadProperties error="+e.getMessage());
			logger.info("loadProperties error={}!",e);
		}
		return null;
	}
	
	/**
	 * 系统启动时需要主动发送的报文
	 * @author hhq 20160713
	 */
	public void initSend(){
		logger.info("init Send begin!");
		try {
			for(DataConfig  config : requestList){
				if(config.getMsgType()==SystemParams.DATA_MSGTYPE_INITSEND){
					XNetData d = new XNetData();
					d.setCommand(config.getxNetCommand().toCharArray()[0]);
					d.setTitle(config.getTitleTarget());
					XStream data = new XStream();
					List<DataPropertie> allPro = config.getPropertys();
					for(int i =0;i<allPro.size();i++){
						DataPropertie properties = allPro.get(i);
						//遇到循环报文体
						if(properties.getFieldType()==SystemParams.PROPERTIES_FIELDTYPE_EACH){
							writeEachData(allPro, properties, data, null);
							int skip =Integer.parseInt(properties.getValue());
							//路过循环报文
							i= i+skip;
						}else if(properties.getFieldType()==SystemParams.PROPERTIES_FIELDTYPE_NOMOR){
							data.append( utils.convertOnePropertie(null, properties, null).toString());
						}
					}
					d.setData(data.toString());
					logger.debug("Send  data :"+d.getTitle()+" "+data);
					listenMap.get(config.getXnetAppName()).getXnetSocket().sendData(d);
					d=null;
				}
			}
			logger.debug("init Send end!");
		} catch (Exception e) {
			logger.info("init Send error={}!",e);
		}
	}
	
	/**
	 * 向XNET写入循环体报文
	 * @author hhq 20160713
	 * @param allPro
	 * @param properties
	 * @param xStream
	 */
	public void writeEachData(List<DataPropertie> allPro,DataPropertie properties,XStream xStream,Map<String, Object> allData){
		try {
			int indexOf =1;
			if(properties!=null){
				indexOf = allPro.indexOf(properties)+1;
			}
			
			//笔数
			int count = Integer.parseInt(properties.getFormat());
			xStream.append(count+"");	
			//一笔的段数
			int eachFieldNum = Integer.parseInt(properties.getValue());
			for (int i = 0; i < 1; i++) {
				for (int j = 0; j < eachFieldNum; j++) {
					if(indexOf+j < allPro.size()){
						DataPropertie  temp = allPro.get(indexOf+j);
						if(temp.getFieldType()==SystemParams.PROPERTIES_FIELDTYPE_EACH){
							//迭代加载循环报文
							writeEachData(allPro, temp, xStream,allData);
							indexOf = indexOf + Integer.parseInt(temp.getValue());
						}else if(temp.getFieldType()==SystemParams.PROPERTIES_FIELDTYPE_NOMOR){
							xStream.append(utils.convertOnePropertie(null, temp,allData).toString());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("writeEachData error="+e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 赋值appName
	 * @author lmm 20170310
	 * @param String
	 */
	public static void setAppName(String keyString){
		XnetService xnetService=listenIpMap.get(keyString);
		appName=xnetService.getAppName();
	}
	

	/**
	 * 线断重连
	 * @author hhq 20160713
	 */
	public void run(){
		try {
			logger.info("start xnet reconn listen...");
			//wait
			Thread.sleep(1000L);
			//reconnect 监控XNET链接对象列表所有XNET链接状态，断开重连
			String conPath = FileUtil.getBinPath(AdapterMain.class);
			InputStream inStream = new FileInputStream(conPath+"config/Config.properties");
			Properties oper = new Properties();
			oper.load(inStream);
			inStream.close();
			while(1==1){
				Thread.sleep(Long.parseLong(oper.getProperty(SystemParams.XNETLIST_LISTEN_TIME)));
				Set<String> keys = listenMap.keySet();
				for(String key : keys){
					if(!listenMap.get(key).getXnetSocket().isLoginOK()){
						//listenMap.get(key).getXnetSocket().open();
						//if(!listenMap.get(key).getXnetSocket().isLoginOK()){
						listenMap.get(key).getXnetSocket().clear();
						listenMap.get(key).getXnetSocket().close();
							listenMap.get(key).setXnetSocket(null);
							//listenMap.get(key).init();XNET有自动重连的机制
						//}
						if(listenMap.get(key).getXnetSocket().isLoginOK()){
							logger.info("xnet reconn appName="+key +"IP="+listenMap.get(key).getAddress()+"port="+listenMap.get(key).getPort());
						}else{
							logger.error("xnet reconn fail appName="+key +"IP="+listenMap.get(key).getAddress()+"port="+listenMap.get(key).getPort());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("xnet reconn listen error...");
			logger.error(e.getMessage(),e);
		}
	}
	
	
	
	
}
