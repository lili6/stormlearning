package com.ad.model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jline.internal.Log;

import org.apache.commons.lang.StringUtils;

//import com.sohu.game.iputil.Ip2Country;
import com.ad.util.DataUtil;

/**
 * 
 * 2013;12.2I34.33.33;UID
 */
public class Ad extends Entity implements java.io.Serializable {
	// 注册时间
	private Timestamp regDate;
	// 注册IP
	private String regIp = "";
	// 用户ID
	private String uid = "";
	// 域名
	private String lastDomain = "";
	// 广告ID
	private String adId = "";
	// subid
	private String subId = "";
	// 时区
	private String TimeZone = "";
	// 注册国家ID
	private int regCountryId;
	// 插入时间
	private final Timestamp putDate = new Timestamp(System.currentTimeMillis());

	private String platType = "0";

	// 分隔符
	public static final String separative = "\\;";
	
	public static final String TABLE_NAME = "wg_ad.gamebox_regok_new";

	public static final String TABLE_CLOUMN[] = { "reg_date", "reg_ip", "uid",
			"last_domain", "ad_id", "sub_id", "reg_country", "put_date",
			"plat_type" };

	
	// 注册IP
	
	

	public String getRegIp() {
		return regIp;
	}

	public void setRegIp(String regIp) {
		this.regIp = regIp;
		// IP取国家ID
		/*Ip2Country ip2c = Ip2Country.getInstance();
		int lid = ip2c.getLid(this.regIp);*/
		int lid = 1;
		this.setRegCountryId(lid);
	}

	public int getRegCountryId() {
		return regCountryId;
	}
  
	public void setRegCountryId(int regCountryId) {
		this.regCountryId = regCountryId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLastDomain() {
		return lastDomain;
	}

	public void setLastDomain(String lastDomain) {
		this.lastDomain = lastDomain;
	}

	public String getAdId() {
		return adId;
	}

	public void setAdId(String adId) {
		if(StringUtils.isNotBlank(adId)){
			adId = adId.trim();
		}
		this.adId = adId;
	}

	public Timestamp getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		// Date date = java.sql.Date.valueOf(regDate);
		Timestamp ts = null;
		try {
			ts = Timestamp.valueOf(regDate);
		} catch (Exception e) {
			Log.error(regDate + " is  error");
			// e.printStackTrace();
		}
		this.regDate = ts;
	}

	public Timestamp getPutDate() {
		return putDate;
	}

	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public String getPlatType() {
		return platType;
	}

	public void setPlatType(String platType) {
		this.platType = platType;
	}

	public String getSeparative() {
		return separative;
	}

	public String getTimeZone() {
		return TimeZone;
	}

	public void setTimeZone(String timeZone) {
		TimeZone = timeZone;
	}

	public Timestamp getHour() {
	      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
		  format.setLenient(false);
		  String str_test =this.getRegDate().toString(); 
		  Timestamp ts = null;
		  try {
		    ts = new Timestamp(format.parse(str_test).getTime());
		  } catch (ParseException e) {
			 System.out.println(e.toString());
		     //e.printStackTrace();
		  } 
		return ts;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "regDate:" + this.getRegDate().toString() + "\nregIp:"
				+ this.getRegIp() + "\nuid: " + this.getUid()
				+ "\nlastDomain: " + this.getLastDomain() + "\nadId:"
				+ this.getAdId() + "\nsubId:" + this.getSubId() + "\nputDate: "
				+ this.getPutDate().toString() + "\nregCountry: "
				+ this.getRegCountryId() + "\nplatType: " + this.getPlatType();
	}

	public String toString2() {
		// TODO Auto-generated method stub
		return "regDate:" + this.getRegDate().toString() + "\tregIp:"
				+ this.getRegIp() + "\tuid: " + this.getUid()
				+ "\tlastDomain: " + this.getLastDomain() + "\tadId:"
				+ this.getAdId() + "\tsubId:" + this.getSubId() + "\tputDate: "
				+ this.getPutDate().toString() + "\tregCountry: "
				+ this.getRegCountryId() + "\tplatType: " + this.getPlatType();
	}

	public static void main(String[] args) {
		String s = "https://apps.facebook.com/warflare/?rcc_id=d8a6180f74264b6c90344e5c609c9b22&code=AQACry-c_BCcg6Z-OmqJwIB75c_YQCLuyG1Zj0gwm8ug_5gwSNIuAf-xTo60ovmLzvDV4lV9OQFsUJhcBSK5UZwAjsLpWotRpC3JrzFylfqP-qZfFjdBRaPnWAgQ2uHYBeho5fAf58tHnrFQNK1W6X4JCTKiF6QxsbpDMTEBclgTJ-G78VogukRAUmTRLSMpn1BNFFfpSnO96MXfYDkeKf2fSi1Fgokj1-cnsVPpP-tv7Cikf7HsCZTKTLcqNwHdu7hCIoZQrfKdmoFnzyw49hvv5zAo8wSICogvMT8S2n7VErG8W1Ay0FCQudOZ3ZSKJdNCRElETuTauhKUdiwuix7-";
		String msg = "2014-11-10 21:33:22;11.10.2.0;4192989;https://apps.facebook.com/odinquest-gamebox/?rcc_id=4b2f1aa37d5947e386c2195e8b1eb70d&code=AQA7kivxN5DoO57le4QHU3vMiDCPstbwVplAd1xobdRtTu-9oQPWmS2eJdYyZnpX5vqGHn1TTWzyMSN6piGnFH2C-VZ4xFHUv2ZsO2OPj9XfRetmq2_ZYi6sFDSs7PzrXHovP6r52o_nUlfxPsyhg3N7DNhHOJ4STs_H8rA8QSML3SQPzaQgXGudbNECyHPv4QZiEfkdFBx_EXlwE1FSaA3F9pS4BcSl7jRTFFs76-wrf3FqxoIOJpt6WporI1H4sNQew8-BDqEVGooIakrJx7TJ4ADzBFB0iL6ZXvL_TYeVToM1mElcmAcpszCZ8DdVE2c;4b2f1aa37d5947e386c2195e8b1eb70d;111";
		Ad ad = (Ad) DataUtil.getObject(msg, new Ad());
		System.out.println(ad.getHour());
		/*System.out.println("insert into "
						+ " (reg_date,reg_ip,uid,last_domain,ad_id,sub_id,reg_country,put_date,plat_type) values ('"
						+ ad.getRegDate() + "','" + ad.getRegIp() + "','"
						+ ad.getUid() + "','" + ad.getLastDomain() + "','"
						+ ad.getAdId() + "','" + ad.getSubId() + "','"
						+ ad.getRegCountry() + "','" + ad.getPutDate() + "','"
						+ ad.getPlatType() + "')");*/
		// System.out.println(ad.toString());
		/*
		 * Ad ad = new Ad (); ad.setRegDate("2013-10-23 23:59:54");
		 * System.out.println(ad.getRegDate());
		 */
	}

}
