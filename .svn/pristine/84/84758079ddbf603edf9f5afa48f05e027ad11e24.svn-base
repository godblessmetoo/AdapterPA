package com.erayt.adapter.common;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <p>
 * Title: 通用日历处理类
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: http://www.erayt.com
 * </p>
 * 
 * @author yonee
 * @version 1.0
 */

public class GeneralCalendar
{
	private static final int[]	days				= { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	private Calendar			cal					= Calendar.getInstance();

	// private final static GeneralCalendar instance = new GeneralCalendar();

	public final static long	MILLISECONDS_OF_DAY	= 1000 * 60 * 60 * 24;

	public final static int		DATETIME_LENGTH		= 14;

	public final static int		DATE_LENGTH			= 8;

	public final static int		TIME_LENGTH			= 6;

	public final static int		TIME_LENGTH_2		= 5;													// 091213 中在数据库中存放的是91213

	final static int			closeTime			= 6;

	public GeneralCalendar()
	{
	}


	/**
	 * 返回日期 格式:yyyymmdd
	 * 
	 * @return int
	 */
	public int getDate()
	{
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return year * 10000 + (month + 1) * 100 + day;
	}

	/**
	 * 返回时间 格式:时/分/秒 hhnnss
	 * 
	 * @return int
	 */
	public int getTime()
	{
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		return hour * 10000 + minute * 100 + second;
	}

	/**
	 * 返回时间 格式:时/分/秒 hh:nn:ss
	 * 
	 * @return String
	 */
	public String getTime2()
	{
		return formatTime(getTime());
	}

	/**
	 * 返回日期时间
	 * 
	 * @return long
	 */
	public long getDateTime()
	{
		return (long) getDate() * 1000000L + (long) getTime();
	}

	// static public long getNow(){
	// cal = Calendar.getInstance();
	// return instance.getDateTime();
	// }
	/**
	 * 设置日期
	 * 
	 * @param date long 格式yyyymmdd 或 yyyymmddhhnnss
	 */
	public void set(long dateTime)
	{
		int date = 0;
		int time = 0;
		int hour = 0;
		int minute = 0;
		int second = 0;
		String str = String.valueOf(dateTime);
		if (str.length() != 8 && str.length() != 14)
		{
			return;
		}
		date = Integer.parseInt(str.substring(0, 8));
		int year = date / 10000;
		int month = (date - year * 10000) / 100;
		int day = date - year * 10000 - month * 100;
		if (str.length() == 14)
		{
			time = Integer.parseInt(str.substring(8, 14));
			hour = time / 10000;
			minute = (time - hour * 10000) / 100;
			second = time - hour * 10000 - minute * 100;
		}
		cal.set(year, month - 1, day, hour, minute, second);
	}

	/**
	 * @param monthCount int
	 * @return String
	 */
	public void addMonth(int monthCount)
	{
		cal.set(Calendar.MONTH, cal.get(Calendar.MONDAY) + monthCount);
	}

	/**
	 * @param field
	 * @param amount
	 */
	public void add(int field, int amount)
	{
		cal.set(field, cal.get(field) + amount);
	}

	/**
	 * @param startdate
	 * @param days
	 * @return
	 */
	public int addDays(int startdate, int days)
	{
		cal.set(startdate / 10000 - 1900, startdate / 100 % 100 - 1, startdate % 100);
		cal.add(Calendar.DATE, days);
		int year = cal.get(Calendar.YEAR) + 1900;
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return year * 10000 + month * 100 + day;
	}

	public static String addTime(String time, int seconds)
	{
		int len = time.length();
		if (len < 5 || len > 6){
			return "";
		}
			
		int hh = Integer.parseInt(time.substring(0, len - 4));
		int mm = Integer.parseInt(time.substring(len - 4, len - 2));
		int ss = Integer.parseInt(time.substring(len - 2, len));

		int s = seconds % 60;
		int m = seconds / 60;

		mm += m;
		ss += s;

		// 处理秒
		if (ss < 0)
		{
			mm--;
			ss += 60;
		}
		if (ss >= 60)
		{
			mm++;
			ss -= 60;
		}

		// 处理分
		hh += mm / 60;
		mm = mm % 60;
		if (mm >= 60)
		{
			hh++;
			mm -= 60;
		}
		if (mm < 0)
		{
			hh--;
			mm += 60;
		}

		// 处理小时
		hh = hh % 24;
		if (hh < 0)
		{
			hh += 24;
		}

		// 组返回字符串
		StringBuffer newTime = new StringBuffer();
		newTime.append(hh);
		if (mm < 10){
			newTime.append('0').append(mm);
		}
		else{
			newTime.append(mm);
		}
		if (ss < 10){
			newTime.append('0').append(ss);
		}
		else{
			newTime.append(ss);
		}
			
		return newTime.toString();
	}

	/**
	 * 格式化输出
	 * 
	 * @param sformat String
	 * @return String
	 */
	public String format(String sformat)
	{
		// yyyymmddhhnnss
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumIntegerDigits(2);
		nf.setMinimumIntegerDigits(2);
//		String strbuf = new String(sformat);
		String strbuf = sformat;
		strbuf = strbuf.replaceAll("yyyy", Integer.toString(cal.get(Calendar.YEAR)));
		strbuf = strbuf.replaceAll("mm", nf.format(cal.get(Calendar.MONTH) + 1));
		strbuf = strbuf.replaceAll("dd", nf.format(cal.get(Calendar.DAY_OF_MONTH)));
		strbuf = strbuf.replaceAll("hh", nf.format(cal.get(Calendar.HOUR_OF_DAY)));
		strbuf = strbuf.replaceAll("nn", nf.format(cal.get(Calendar.MINUTE)));
		strbuf = strbuf.replaceAll("ss", nf.format(cal.get(Calendar.SECOND)));
		return strbuf;
	}

	// 检验日期格式 年-月- 日
	public static boolean isRightDate(int date)
	{

		int year = date / 10000;
		int month = (date - year * 10000) / 100;
		int day = date - year * 10000 - month * 100;
		if (year < 1900 || year > 3000)
		{
			return false;
		}
		if (month > 12 || month < 1)
		{
			return false;
		}
		if ((year % 4 == 0 || (year % 100 == 0 && year % 400 == 0)) && month == 2)
		{
			days[1] = 29;
		}
		if (day < 1 || day > days[month - 1])
		{
			return false;
		}
		return true;
	}

	// 检验时间 时-份-秒
	public static boolean isRightTime(int time)
	{
		int hour = time / 10000;
		int minute = (time - hour * 10000) / 100;
		int second = time - hour * 10000 - minute * 100;
		if (hour > 23 || hour < 0)
		{
			return false;
		}
		if (minute > 59 || minute < 0)
		{
			return false;
		}
		if (second > 59 || second < 0)
		{
			return false;
		}
		return true;
	}

	// 检验时间 时-份-秒
	public static int getLeftTime(int begintime, int endtime)
	{
		int beginhour = begintime / 10000;
		int beginminute = (begintime - beginhour * 10000) / 100;
		int beginsecond = begintime - beginhour * 10000 - beginminute * 100;

		int endhour = endtime / 10000;
		int endminute = (endtime - endhour * 10000) / 100;
		int endsecond = endtime - endhour * 10000 - endminute * 100;

		return ((endhour - beginhour) * 3600 + (endminute - beginminute) * 60 + (endsecond - beginsecond));

	}

	/*del by zxh at 20130228 所有文件都没有使用到该函数，封闭
	// 检验日期 时间 年-月-日-时-分-秒
	public static boolean isRightDateTime(long dateTime)
	{
		int date = (int) (dateTime / 1000000);
		int time = (int) (dateTime - date * 1000000);
		return isRightDate(date) && isRightTime(time);
	}
	*/

	public int getWeekOfYear()
	{
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	public int getMonthOfYear()
	{
		return cal.get(Calendar.MONTH);
	}

	public int getYear()
	{
		return cal.get(Calendar.YEAR);
	}

	public int getDayOfWeek()
	{
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	// 取得月的天数
	public int getDaysOfMonth()
	{ // 0为1月
		int month = getMonthOfYear();
		int year = getYear();
		if (month == 1)
		{
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
			{
				return 29;
			} else
			{
				return 28;
			}
		} else
		{
			return days[month];
		}

	}

	public int getDaysOfMonthParam(int months)
	{ // 0为1月
		int month = months - 1;
		int year = getYear();
		if (month == 1)
		{
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
			{
				return 29;
			} else
			{
				return 28;
			}
		} else
		{
			return days[month];
		}

	}


	public static int getDays(int startday, int endday)
	{
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();

		int date = Integer.parseInt(String.valueOf(startday).substring(0, 8));
		int year = date / 10000;
		int month = date / 100 % 100;
		int day = date % 100;
		startCal.set(year, month - 1, day, 0, 0, 0);

		date = Integer.parseInt(String.valueOf(endday).substring(0, 8));
		year = date / 10000;
		month = date / 100 % 100;
		day = date % 100;
		endCal.set(year, month - 1, day, 0, 0, 0);
		return (int) ((endCal.getTime().getTime() - startCal.getTime().getTime()) / (1000 * 60 * 60 * 24));
	}

	public static String formatTime(int time)
	{
		int hour = time / 10000;
		int min = (time - hour * 10000) / 100;
		int sec = time - hour * 10000 - min * 100;
		StringBuffer timeStr = new StringBuffer();
		if (0 <= hour && hour <= 9){
			timeStr.append('0').append(hour).append(':');
		}
		else{
			timeStr.append(hour).append(':');
		}
			
		if (0 <= min && min <= 9){
			timeStr.append('0').append(min).append(':');
		}
		else{
			timeStr.append(min).append(':');
		}
			
		if (0 <= sec && sec <= 9){
			timeStr.append('0').append(sec);
		}
		else{
			timeStr.append(sec);
		}
			
		return timeStr.toString();
	}

	public static String formatTimeT6(int time)
	{
		int hour = time / 10000;
		int min = (time - hour * 10000) / 100;
		int sec = time - hour * 10000 - min * 100;
		StringBuffer timeStr = new StringBuffer();
		if (0 <= hour && hour <= 9){
			timeStr.append('0').append(hour);
		}
		else{
			timeStr.append(hour);
		}
			
		if (0 <= min && min <= 9){
			timeStr.append('0').append(min);
		}
		else{
			timeStr.append(min);
		}
			
		if (0 <= sec && sec <= 9){
			timeStr.append('0').append(sec);
		}
		else{
			timeStr.append(sec);
		}
			
		return timeStr.toString();
	}

	public static String formatDate(int date)
	{
		int year = date / 10000;
		int month = (date - year * 10000) / 100;
		int day = date - year * 10000 - month * 100;
		StringBuffer dateStr = new StringBuffer();
		dateStr.append(year).append('-');
		if (0 <= month && month <= 9){
			dateStr.append('0').append(month).append('-');
		}
		else{
			dateStr.append(month).append('-');
		}
			
		if (0 <= day && day <= 9){
			dateStr.append('0').append(day);
		}
		else{
			dateStr.append(day);
		}
			
		return dateStr.toString();
	}

	/**
	 * 格式化中文年月日时间输出
	 * 
	 * @param date
	 * @return
	 * @since 2007-8-30上午11:48:56
	 * @auther chen
	 */
	public static String formatzhCNDate(int date)
	{
		int year = date / 10000;
		int month = (date - year * 10000) / 100;
		int day = date - year * 10000 - month * 100;
		StringBuffer dateStr = new StringBuffer();
		dateStr.append(year).append('年');
		if (0 <= month && month <= 9){
			dateStr.append('0').append(month).append('月');
		}
		else{
			dateStr.append(month).append('月');
		}
			
		if (0 <= day && day <= 9){
			dateStr.append('0').append(day).append('日');
		}
		else{
			dateStr.append(day).append('日');
		}
			
		return dateStr.toString();
	}

	public static int formatzhCNDate2(int date)
	{
		return date;
	}

	/**
	 * 根据条件生成有效日期
	 * 
	 * @param item String[]
	 */

	public int createValidDate(int years, int months, int days)
	{
		Calendar now = new GregorianCalendar();
		Calendar validDate = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
		int year = validDate.get(Calendar.YEAR) + years;
		int month = validDate.get(Calendar.MONTH) + months;
		int day = validDate.get(Calendar.DAY_OF_MONTH) + days;
		int nowDate = year * 10000 + month * 100 + day;
		return nowDate;
	}

	/**
	 * 生成指定天数后的日期
	 */
	public static int createByDays(int days)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, days);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return Integer.parseInt(dateFormat.format(calendar.getTime()));
	}

	public int getYearBegDate(int date)
	{
		int year = date / 10000;
		int month = 0;
		int day = 1;
		return year * 10000 + (month + 1) * 100 + day;
	}

	public int getYearBegMon(int date)
	{// YYYYMM6位长度年初 200801
		int year = date / 10000;
		int month = 0;
	//	int day = 1;
		return year * 100 + (month + 1);
	}

	public int getNowMon(int date)
	{// YYYYMM6位长度年初 返回当前月份
		int month = date / 100;
		return month;
	}

	public int getMonthBegDate(int date)
	{
		int month = date / 100;
		int day = 1;
		return month * 100 + day;
	}

	/**
	 * 增加年
	 * 
	 * @param date 20100421
	 * @param years 年数
	 * @return int 20100421
	 * @throws ParseException
	 * @throws Exception
	 */
	public static int addNumericYears(int date, int years) throws Exception
	{
		Calendar cl = Calendar.getInstance();
		cl.setTime(parse(date));
		cl.add(Calendar.YEAR, years);
		return getNumericDate(cl.getTime());
	}

	/**
	 * 增加月
	 * 
	 * @param date 20100421
	 * @param months 月数
	 * @return int 20100421
	 * @throws ParseParseException
	 * @throws ParseException
	 */
	public static int addNumericMonths(int date, int months) throws Exception
	{
		Calendar cl = Calendar.getInstance();
		cl.setTime(parse(date));
		cl.add(Calendar.MONTH, months);
		return getNumericDate(cl.getTime());
	}

	/**
	 * 增加日
	 * 
	 * @param date 20100421
	 * @param days 日数
	 * @return int 20100421
	 * @throws ParseParseException
	 * @throws ParseException
	 */
	public static int addNumericDays(int date, int days) throws Exception
	{
		return getNumericDate(new Date(parse(date).getTime() + days * MILLISECONDS_OF_DAY));
	}

	/**
	 * 日期转换
	 * 
	 * @param date 三种格式 20100421153055|20100421|153055
	 * @return
	 * @throws ParseException
	 * @throws ApplicationException
	 */
	public static Date parse(long date) throws Exception
	{
		String dateStr = String.valueOf(date);

		boolean isDate = false, isTime = false, isDateTime = false;
		int length = dateStr.length();
		if (length == DATETIME_LENGTH)
		{
			isDateTime = true;
		} else if (length == DATE_LENGTH)
		{
			isDate = true;
		} else if (length == TIME_LENGTH || length == TIME_LENGTH_2)
		{
			isTime = true;
		} else
		{
			throw new Exception("Date format is not correct[" + dateStr + "]");
		}
		SimpleDateFormat sf = (SimpleDateFormat) DateFormat.getDateInstance();
		try
		{
			if (isDateTime)
			{ // 日期时间
				sf.applyPattern("yyyyMMddHHmmss");
				return sf.parse(dateStr);
			} else if (isDate)
			{
				sf.applyPattern("yyyyMMdd");
				return sf.parse(dateStr);
			} else if (isTime)
			{
				sf.applyPattern("HHmmss");
				return sf.parse(dateStr);
			}
		} catch (ParseException pe)
		{
			throw new Exception(pe.getMessage());
		}

		return new Date(0);
	}

	/**
	 * 读取当前日期
	 * 
	 * @return int 20100421
	 */
	public static int getNumericDate()
	{
		SimpleDateFormat sf = (SimpleDateFormat) DateFormat.getInstance();
		sf.applyPattern("yyyyMMdd");
		String date = sf.format(Calendar.getInstance().getTime());
		return Integer.parseInt(date);
	}

	/**
	 * 读取当前的日期+时间 return long 20100421132630
	 */
	public static long getNumericDateTime()
	{
		SimpleDateFormat sf = (SimpleDateFormat) DateFormat.getInstance();
		sf.applyPattern("yyyyMMddHHmmss");
		String dateTime = sf.format(Calendar.getInstance().getTime());
		return Long.parseLong(dateTime);
	}

	/**
	 * 读取当前的日期+时间 return long 20100421132630
	 */
	public static int getNumericTime()
	{
		SimpleDateFormat sf = (SimpleDateFormat) DateFormat.getInstance();
		sf.applyPattern("HHmmss");
		String dateTime = sf.format(Calendar.getInstance().getTime());
		return Integer.parseInt(dateTime);
	}

	/**
	 * Date类型 转换为 int 类型
	 * 
	 * @param date
	 * @return int 20100421
	 */
	public static int getNumericDate(Date dateTime)
	{
		SimpleDateFormat sf = (SimpleDateFormat) DateFormat.getInstance();
		sf.applyPattern("yyyyMMdd");
		return Integer.parseInt(sf.format(dateTime));
	}

	/**
	 * Date类型(日期+时间) 转换为 long 类型
	 * 
	 * @param dateTime
	 * @return long 20100421132630
	 */
	public static long getNumericDateTime(Date dateTime)
	{
		SimpleDateFormat sf = (SimpleDateFormat) DateFormat.getInstance();
		sf.applyPattern("yyyyMMddHHmmss");
		return Long.parseLong(sf.format(dateTime));
	}

	/**
	 * 转换日期 Date -> 20080101
	 * 
	 * @param date 日期
	 * @param format 日期的字符串格式如yyyyMMdd
	 * @return 日期的字符串形式如yyyyMMdd
	 */
	public static String getDateStr(Date date, String format)
	{
		String formatTmp = format;
		if (date == null){
			return null;
		}
		if(formatTmp==null)	{
			formatTmp = "yyyy-MM-dd HH:mm:ss";
		}
//		formatTmp = formatTmp == null ? "yyyy-MM-dd HH:mm:ss" : formatTmp;
		if ("CNS".equals(formatTmp))
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String[] str = sdf.format(date).split("-");
			return str[0] + "年" + str[1] + "月" + str[2] + "日";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatTmp);
		return sdf.format(date);
	}

	/**
	 * 转换日期 20080101 -> Date
	 * 
	 * @param dateStr 日期字符串
	 * @param format 日期的字符串格式如yyyyMMdd
	 * @return Date 日期
	 * @throws ParseException 日期解析异常
	 */
	public static Date getUtilDate(String dateStr, String format) throws ParseException
	{
		String formatTmp = format;
		if (dateStr == null || dateStr.length() == 0){
			return null;
		}
			
//		formatTmp = (formatTmp == null ? "yyyy-MM-dd HH:mm:ss" : formatTmp);
		if(formatTmp == null){
			formatTmp = "yyyy-MM-dd HH:mm:ss";
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(formatTmp);
		return sdf.parse(dateStr);
	}
	
	/**
	 * 日期转换成星期
	 * @desc 用途描述: 
	 * @param iDate
	 * @return
	 * @throws Exception 返回说明:
	 * @exception 内部异常说明:
	 * @throws 抛出异常说明:
	 * @author lp
	 * @version 1.0      
	 * @created 2012-10-31 上午11:41:17 
	 * @mod 修改描述:
	 * @modAuthor 修改人:
	 */
	public static Integer getDayForWeek(String iDate) throws Exception {
		String iDateTmp = iDate;
		iDateTmp = iDateTmp.replaceAll("\\-", "");
		if(iDateTmp.length()<8){
			return 0;
		}
			
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");  
		Calendar c = Calendar.getInstance();  
		c.setTime(format.parse(iDateTmp));  
		int dayForWeek = 0;  
		if(c.get(Calendar.DAY_OF_WEEK) == 1){  
			dayForWeek = 7;  
		}else{  
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;  
		}  
		return dayForWeek;
	}
}
