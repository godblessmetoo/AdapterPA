package com.erayt.adapter.main;

import java.util.Map;

import org.apache.log4j.Logger;

import com.erayt.adapter.common.SystemParams;
import com.erayt.adapter.domain.DataConfig;
import com.erayt.adapter.xnet122.XnetService;
import com.erayt.utils.XStream;
import com.erayt.xnet4j.XNetData;

public class SendHandle implements Runnable{

	private  final Logger logger = Logger.getLogger(SendHandle.class.getSimpleName());
	
	public void run(){
		try {
			while(true){
				Map<String,Object> map = AdapterContext.sendQueue.take();
				DataConfig config = (DataConfig)map.get("config");
				XStream send = (XStream)map.get("send");
				this.send(config, send);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 通过报文规则发送XNET报文
	 * @author hhq 20160713
	 * @param dataBody
	 * @param sendData
	 */
	public void send(DataConfig config,XStream data){
		logger.debug("Send  data begin!");
		try {
			
			for(DataConfig temp : AdapterContext.requestList){
				if(temp.getMsgType()!=SystemParams.DATA_MSGTYPE_INITSEND 
						&& temp.getTitleTarget().equals(config.getTitleTarget())
						&& temp.getTitleSource().equals(config.getTitleSource())){
					XNetData d = new XNetData();
					d.setCommand(temp.getxNetCommand().toCharArray()[0]);
					d.setTitle(temp.getTitleTarget());
					d.setData(data.toString());
					if(flag(temp.getTitleTarget())&&AdapterContext.appName!=null){
						if(temp.getXnetAppName().equals(AdapterContext.appName)){
							XnetService xnet = AdapterContext.listenMap.get(temp.getXnetAppName());
							String[] titles = xnet.getTitles().split(";");
							
							if(temp.getTitleSource().equals("XFeed.Bloomberg.ReLoginMsg") && temp.getTitleTarget().equals("XFeed.Bloomberg.ReqLogin")){
								//延迟两秒重签
								logger.info("wait 2s to Relogin.....");
								Thread.sleep(2*1000);
							}
							xnet.getXnetSocket().sendData(d);
							logger.debug("Send  data to :"+xnet.getAppName()+" "+temp.getTitleTarget()+" "+data.toString());
							d = null;
						}
					}else{
						XnetService xnet = AdapterContext.listenMap.get(temp.getXnetAppName());
						String[] titles = xnet.getTitles().split(";");
						
						if(temp.getTitleSource().equals("XFeed.Bloomberg.ReLoginMsg") && temp.getTitleTarget().equals("XFeed.Bloomberg.ReqLogin")){
							//延迟两秒重签
							logger.info("wait 2s to Relogin.....");
							Thread.sleep(2*1000);
						}
						xnet.getXnetSocket().sendData(d);
						logger.debug("Send  data to :"+xnet.getAppName()+" "+temp.getTitleTarget()+" "+data.toString());
						d = null;
					}
					
				}
			}
			logger.debug("Send  data end!");
		} catch (Exception e) {
			logger.info("Send data error={}!",e);
		}
	}
	
	/**
	 * 判断title是否是推送转发平台的title
	 * @author lmm 20170313
	 * @param String
	 */
	public static boolean flag(String title){
		boolean flag=false;
		if(SystemParams.XFWDING_BONDS.equals(title)||SystemParams.XFWDING_IR.equals(title)||SystemParams.XFWDING_SWAP.equals(title)||SystemParams.XFWDING_SPOT.equals(title)){
			flag=true;
		}
		return flag;
	}
}
