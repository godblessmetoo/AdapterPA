package com.erayt.adapter.main;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

import com.erayt.adapter.common.GeneralCalendar;
import com.erayt.adapter.common.SystemParams;
import com.erayt.adapter.domain.DataConfig;
import com.erayt.adapter.xnet122.XnetService;
import com.erayt.rate.domain.MarketSpotRate;
import com.erayt.rate.domain.MarketSpotRateList;
import com.erayt.rate.domain.MarketSwapRate;
import com.erayt.rate.domain.MarketSwapRateList;
import com.erayt.utils.XStream;
import com.erayt.xnet4j.XNetCommand;
import com.erayt.xnet4j.XNetData;

public class AdapterRepository {

	private static final Logger logger = Logger.getLogger(AdapterRepository.class.getSimpleName());
	/*
	 * RMDS掉期价格存缓存
	 */
	public static void cmdsSpotRateRepository(String spotString){
		//XStream spotXStream = new XStream();
		logger.debug("即期价格数据即将存入缓存中："+spotString);
		//spotXStream.append(data.toString());
		String[] bondsCodsCyName=spotString.split(",");
		MarketSpotRate marketSpotRate = new MarketSpotRate();
		marketSpotRate.setPriceType(bondsCodsCyName[0]);
		marketSpotRate.setFeedCode(bondsCodsCyName[1]);
		marketSpotRate.setCypairName(bondsCodsCyName[2]);
		marketSpotRate.setFeedRic("");
		marketSpotRate.setQuoteUnit(Integer.parseInt(bondsCodsCyName[3]));
		marketSpotRate.setBid(BigDecimal.valueOf(Double.parseDouble(bondsCodsCyName[4])));
		marketSpotRate.setAsk(BigDecimal.valueOf(Double.parseDouble(bondsCodsCyName[5])));
		marketSpotRate.setMid(BigDecimal.valueOf(Double.parseDouble(bondsCodsCyName[6])));
		marketSpotRate.setUpdateDate(Integer.parseInt(bondsCodsCyName[7]));
		marketSpotRate.setUpdateTime(Integer.parseInt(bondsCodsCyName[8]));

		MarketSpotRateList.SPOTRATE_LISTS.update(marketSpotRate);

	}

	/*
	 * RMDS掉期价格存缓存
	 */
	public static void cmdsSwapRateRepository(String swapString){
		//XStream swapXStream = new XStream();
		logger.debug("掉期价格数据即将存入缓存中："+swapString);
		String[] bondsCodsCyName=swapString.split(",");
		MarketSwapRate marketSwapRate = new MarketSwapRate();
		marketSwapRate.setPriceType(bondsCodsCyName[0]);
		marketSwapRate.setFeedCode(bondsCodsCyName[1]);
		marketSwapRate.setCypairName(bondsCodsCyName[2]);
		marketSwapRate.setTermName(bondsCodsCyName[3]);
		marketSwapRate.setFeedRic("");
		marketSwapRate.setBid(BigDecimal.valueOf(Double.parseDouble(bondsCodsCyName[4])));
		marketSwapRate.setAsk(BigDecimal.valueOf(Double.parseDouble(bondsCodsCyName[5])));
		marketSwapRate.setMid(BigDecimal.valueOf(Double.parseDouble(bondsCodsCyName[6])));
		marketSwapRate.setUpdateDate(Integer.parseInt(bondsCodsCyName[7]));
		marketSwapRate.setUpdateTime(Integer.parseInt(bondsCodsCyName[8]));

		MarketSwapRateList.SWAPRATE_LISTS.update(marketSwapRate);
	}
	/*
	 * 发送即期掉期缓存中全量牌价
	 */
	public static void sendRepositoryPrice(DataConfig config){
		//发送即期缓存中全量牌价
		sendSpotRepositoryPrice(config);
		//发送掉期缓存中全量牌价
		sendSwapRepositoryPrice(config);
	}
	/*
	 * 发送即期缓存中全量牌价
	 */
	public static void sendSpotRepositoryPrice(DataConfig config){
		// 获取内存中即期牌价
		List<MarketSpotRate> data = MarketSpotRateList.SPOTRATE_LISTS.getData();
		XStream stream = new XStream();
		GeneralCalendar gc = new GeneralCalendar();
		stream.append(gc.getDate());
		stream.append(gc.getTime());
		stream.append(gc.getDateTime());
		stream.append(data.size());
		for (MarketSpotRate marketSpotRate : data) {
			stream.append(marketSpotRate.getPriceType());
			stream.append(marketSpotRate.getFeedCode());
			stream.append(marketSpotRate.getCypairName());
			stream.append(marketSpotRate.getFeedRic());
			stream.append(marketSpotRate.getQuoteUnit());
			stream.append(marketSpotRate.getBid().toString());
			stream.append(marketSpotRate.getAsk().toString());
			stream.append(marketSpotRate.getMid().toString());
			stream.append(marketSpotRate.getUpdateDate());
			stream.append(marketSpotRate.getUpdateTime());
			stream.append(marketSpotRate.getSave1());
			stream.append(marketSpotRate.getSave2());
			stream.append(marketSpotRate.getSave3());
			stream.append(marketSpotRate.getSave4());
		}
		for (DataConfig temp : AdapterContext.requestList) {
			if(temp.getMsgType()==SystemParams.DATA_MSGTYPE_SEND&&temp.getTitleSource().equals(config.getTitleSource())&&"XFWDING.ReqCltParam".equals(temp.getTitleTarget())){
				if(AdapterContext.appName!=null){
					if(temp.getXnetAppName().equals(AdapterContext.appName)){
						XNetData xNetData = new XNetData();
						xNetData.setCommand(XNetCommand.MUTITITLE);
						xNetData.setTitle("XFWDING.FeedPrice.SPOT");
						xNetData.setData(stream.toString());

						XnetService xnet = AdapterContext.listenMap.get(temp.getXnetAppName());
						xnet.getXnetSocket().sendData(xNetData);
						logger.info("AppName为"+temp.getXnetAppName()+",从缓存向转发平台推送即期价格："+stream.toString());
					}
				}
				else{
					XNetData xNetData = new XNetData();
					xNetData.setCommand(XNetCommand.MUTITITLE);
					xNetData.setTitle("XFWDING.FeedPrice.SPOT");
					xNetData.setData(stream.toString());

					XnetService xnet = AdapterContext.listenMap.get(temp.getXnetAppName());
					xnet.getXnetSocket().sendData(xNetData);
					logger.info("AppName为"+temp.getXnetAppName()+",从缓存向转发平台推送即期价格："+stream.toString());
				}

			}
		}
	}

	/*
	 * 发送掉期缓存中全量牌价
	 */
	public static void sendSwapRepositoryPrice(DataConfig config){
		// 获取内存中掉期牌价
		List<MarketSwapRate> data = MarketSwapRateList.SWAPRATE_LISTS.getData();
		XStream stream = new XStream();
		GeneralCalendar gc = new GeneralCalendar();
		stream.append(gc.getDate());
		stream.append(gc.getTime());
		stream.append(gc.getDateTime());
		stream.append(data.size());
		for (MarketSwapRate marketSwapRate : data) {
			stream.append(marketSwapRate.getPriceType());
			stream.append(marketSwapRate.getFeedCode());
			stream.append(marketSwapRate.getCypairName());
			stream.append(marketSwapRate.getTermName());
			stream.append(marketSwapRate.getFeedRic());
			stream.append(marketSwapRate.getBid().toString());
			stream.append(marketSwapRate.getAsk().toString());
			stream.append(marketSwapRate.getMid().toString());
			stream.append(marketSwapRate.getUpdateDate());
			stream.append(marketSwapRate.getUpdateTime());
			stream.append(marketSwapRate.getSave1());
			stream.append(marketSwapRate.getSave2());
			stream.append(marketSwapRate.getSave3());
			stream.append(marketSwapRate.getSave4());
		}
		for (DataConfig temp : AdapterContext.requestList) {
			if(temp.getMsgType()==SystemParams.DATA_MSGTYPE_SEND&&temp.getTitleSource().equals(config.getTitleSource())&&"XFWDING.ReqCltParam".equals(temp.getTitleTarget())){
				if(AdapterContext.appName!=null){
					if(temp.getXnetAppName().equals(AdapterContext.appName)){
						XNetData xNetData = new XNetData();
						xNetData.setCommand(XNetCommand.MUTITITLE);
						xNetData.setTitle("XFWDING.FeedPrice.SWAP");
						xNetData.setData(stream.toString());
						XnetService xnet = AdapterContext.listenMap.get(temp.getXnetAppName());
						xnet.getXnetSocket().sendData(xNetData);
						logger.info("AppName为"+temp.getXnetAppName()+",从缓存向转发平台推送掉期价格："+stream.toString());
					}
				}else{
					XNetData xNetData = new XNetData();
					xNetData.setCommand(XNetCommand.MUTITITLE);
					xNetData.setTitle("XFWDING.FeedPrice.SWAP");
					xNetData.setData(stream.toString());
					XnetService xnet = AdapterContext.listenMap.get(temp.getXnetAppName());
					xnet.getXnetSocket().sendData(xNetData);
					logger.info("AppName为"+temp.getXnetAppName()+",从缓存向转发平台推送掉期价格："+stream.toString());
				}
			}
		}
	}
}
