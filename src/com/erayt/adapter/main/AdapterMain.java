package com.erayt.adapter.main;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.erayt.adapter.common.FileUtil;

public class AdapterMain {
	private static final Logger logger = Logger.getLogger(AdapterMain.class.getSimpleName());
	

	public static void main(String[] args) {
		 try
		    {
			 //系统启动加载配置项
		      String conPath = FileUtil.getBinPath(AdapterMain.class);
		      PropertyConfigurator.configure(conPath + "config/Log4j.properties");
		      logger.info("Adapter System Start");
		      logger.info("Adapter Spring loading Start");
		      
		      
		      
		      //new ClassPathXmlApplicationContext("classpath:/config/ServerContext.xml");
		      
		      AdapterContext context = new AdapterContext("config/Adapter_Config.xml");
		      Thread priceThread = new Thread(context);
			  priceThread.start();
			  
			  
//			  //接收处自定义字段
			/* Map<Object,Object> beans = new HashMap<Object, Object>();
			  Map<String,Object> params = new HashMap<String, Object>();
			  MvelUtil utils = new MvelUtil();
			  beans.put(SystemParams.MVEL_RECDATA_KEYMAP, AdapterContext.keyMap);
			  	
			params.put(SystemParams.MVEL_UTILS_NAME, utils);
			ELContext lc = new ELContext(beans, params);
			Object exeRelust = utils.executeExpreAndReturnObj("keyMap.get('CCSWO1 CMPN Curncy').CYNAME.toString()", lc);
			System.out.println(exeRelust);*/
//		      logger.info("Adapter Spring loading End");
		      
//		      logger.info("内部通信线程开始启动......");
//				Thread internalThread = new Thread(xnetInternalService);
//				internalThread.start();
		      
		      Runtime.getRuntime().addShutdownHook(new Thread()
		      {
		        public void run()
		        {
		          try
		          {
		        	  AdapterMain.logger.info("Adapter Runtime ShutdownHook");
		          }
		          catch (Exception e)
		          {
		        	  AdapterMain.logger.error(e.getMessage());
		          }
		        }
		      });
		      logger.info("Adapter System Start End");
		    }
		    catch (Exception e)
		    {
		      logger.info("Adapter System Start error:" + e.getMessage());
		      e.printStackTrace();
		    }
	}
}
