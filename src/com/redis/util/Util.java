package com.redis.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 帮助类
 * 
 * @author Roger
 */
public class Util {

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) {
		System.out.println(getSenconds());
		
		System.out.println(Util.toByteArray("luojiek"));
		System.out.println(Util.toObject(Util.toByteArray("luojiek")));
		
	}

	/**
	 * Object ---> byte[]
	 * @param param
	 * 		Object
	 * @return
	 * 		byte[]
	 */
	public static byte[] toByteArray(Object param) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(param);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
	}

	/**
	 * byte[] ---> Object
	 * @param param
	 * 		byte[]
	 * @return
	 * 		Object
	 */
	public static Object toObject(byte[] bytes) {
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return obj;
	}

	/**
	 * 获取当前时间到明天凌晨零点零分零秒的相差秒数； 【2015-8-15 20:20:20】 到 【2015-8-16 00:00:00】的相差秒数
	 * 
	 * @return long
	 */
	public static int getSenconds() {
		// 当前当前时间
		Date currentDate = getCurrentDate();
		// 明天零点时间
		Date TomorrowMorning = initDate(addDateToOneDay(currentDate));
		return getSeconds(currentDate, TomorrowMorning);
	}

	/**
	 * 获取2个时间相差秒数
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return seconds(秒)
	 */
	public static int getSeconds(Date startDate, Date endDate) {
		return (int) ((endDate.getTime() - startDate.getTime()) / 1000);
	}

	/**
	 * 获取传入时间的 00:00:00
	 * 
	 * @param dateSFM
	 *            yyyy-MM-dd
	 * @return yyyy-MM-dd 00:00:00
	 */
	public static Date initDate(String dateSFM) {
		try {
			return sdf.parse(dateSFM + " 00:00:00");
		} catch (ParseException e) {
			System.err.println("时间转换出现异常");
			e.printStackTrace();
			return new Date();
		}
	}

	/**
	 * 当前时间加一天
	 * 
	 * @param date
	 *            传入时间
	 * @return yyyy-MM-dd
	 */
	public static String addDateToOneDay(Date date) {
		Calendar time = Calendar.getInstance();
		time.setTime(date);
		time.add(Calendar.DAY_OF_YEAR, 1);
		return sdf.format(time.getTime());
	}

	/**
	 * 获取当前时间
	 * 
	 * @return date
	 */
	public static Date getCurrentDate() {
		return new Date();
	}

}
