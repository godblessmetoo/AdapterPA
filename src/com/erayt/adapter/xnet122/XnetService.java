package com.erayt.adapter.xnet122;


import org.apache.log4j.Logger;

import com.erayt.adapter.common.SystemParams;
import com.erayt.adapter.main.ApapterHandler;
import com.erayt.xnet4j.XNetCommand;
import com.erayt.xnet4j.XNetData;
import com.erayt.xnet4j.XNetHandler;
import com.erayt.xnet4j.XNetSocket;

/**
 * xnet服务类
 * @author hhq 20161229
 *
 */
/**
 * xnet控制类
 * @author hhq 20160713
 *
 */
public class XnetService implements XNetHandler{

	private static final Logger logger = Logger.getLogger(XnetService.class.getSimpleName());
	private String version;
	private String address;
	private int port;
	private int aliveInterval;
	private String appName;
	private String titles;
	private XNetSocket xnetSocket =null;
	private ApapterHandler handle = new ApapterHandler();
	
	public void dataRead(XNetSocket xnetSocket, XNetData xnetData) {
		try {
			handle.dataRead(xnetSocket, xnetData);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public void init(){
		try {
			if(xnetSocket==null){
				xnetSocket =  new XNetSocket("", appName, address, 	port, this, aliveInterval);
			}
			xnetSocket.open();
			logger.info("XNet[" + address + ":" + port + "---" + appName +"] connect success...");
		} catch (Exception e) {
			logger.error("XNet[" + address + ":" + port + "---" + appName +"] connect error...{}", e);
		}
	}
	
	public void exceptionCaught(XNetSocket socket, Throwable t) {
	    logger.error("XNet[" + socket.getIp() + ":" + socket.getPort() + "---" + appName +"]error:{}", t);
	}
	
	public void loginOK(XNetSocket socket) {
		logger.info("XNet[" + socket.getIp() + ":" + socket.getPort() + "---" + appName +"] success");
		//socket.commandBuffer(XNetCommand.REGTITLE, null, titles.getBytes());
		if(this.version.equals(SystemParams.XNET_VERSION_2)){
			socket.commandBuffer(XNetCommand.REGTITLE, null, titles.getBytes());
		}else if(this.version.equals(SystemParams.XNET_VERSION_3)){
			/*for(DataConfig temp :AdapterContext.responseList){
				if(temp.getXnetAppName().equals(appName)&&temp.getMsgType()==1&&(temp.getHandLerType()==1 || temp.getHandLerType()==2)){
					String title=temp.getTitleSource();
					if(temp.getxNetCommand().equals("U")){
						socket.addCasts(title);
					}else if(temp.getxNetCommand().equals("R")){
						socket.addGroups(title);
					}else{
						socket.commandBuffer(XNetCommand.REGGROUP, null, title.getBytes());
					}
				}
			}*/
			socket.commandBuffer(XNetCommand.REGGROUP, null, titles.getBytes());
			socket.addCasts("XFeed.Bloomberg.ReLoginMsg");
		}
		System.out.println(this.getAppName()+this.getVersion());
	}
	public void connectionClosed(XNetSocket xnetSocket) {
		logger.info("XNet[" + xnetSocket.getIp() + ":" + xnetSocket.getPort() + "---" + appName +"] close");
	}
	
	public void close(){
		logger.info("XNet[" + address + ":" + port + "---" + appName +"] close...");
		if(xnetSocket != null){
			try{
				xnetSocket.close();
			}catch (Exception e) {
				logger.info(e.getMessage(),e);
			}
			
		}
	}
	
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getAliveInterval() {
		return aliveInterval;
	}

	public void setAliveInterval(int aliveInterval) {
		this.aliveInterval = aliveInterval;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public XNetSocket getXnetSocket() {
		return xnetSocket;
	}

	public void setXnetSocket(XNetSocket xnetSocket) {
		this.xnetSocket = xnetSocket;
	}

	public String getTitles() {
		return titles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}
	
}