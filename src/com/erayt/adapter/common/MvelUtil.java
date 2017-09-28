package com.erayt.adapter.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mvel2.MVEL;

import com.erayt.solar2.utils.el.ELContext;
import com.erayt.solar2.utils.el.ELEngine;


public final class MvelUtil {
	
	public Object executeExpreAndReturnObj(String expressino,
			ELContext lc) {
		return ELEngine.eval(expressino, lc);
	}
	
	public List getList(){
		return new ArrayList();
	}
	
	public Map getMap(){
		return new HashMap();
	}
	
	/*public List toArrayList(String[] arr){
		List list = new ArrayList();
		Map<String, Object> map =new HashMap<String, Object>();
		for (int i = 0; i < arr.length; i++) {
			map=new HashMap<String, Object>();
			map.put("FieldName", arr[i]);
			list.add(map);
		}
		return list;
	}*/
	
	public List toArrayList(String[] arr,String name){
		List list = new ArrayList();
		Map<String, Object> map = null;
		String[] names=name.split(";");
		int a=arr.length/names.length;
		for (int i = 0; i < a; i++) {
			map=new HashMap<String, Object>();
			for (int j = 0; j < names.length; j++) {
				map.put(names[j], arr[j+i*names.length]);
			}
			list.add(map);
		}
		return list;
	}
	
	public String getQuoteUnit(String quoteUnit){
		if(quoteUnit==null || "".equals(quoteUnit)){
			return "1";
		}else{
			return quoteUnit;
		}
		
	}
	
	public static void main(String[] args) {
		MvelUtil mv=new MvelUtil();
		List list=mv.toArrayList("BID;ASK;LAST_PRICE;TIME;LAST_TRADE".split(";"), "FieldName");
		System.out.println(list);
	}
}

