package com.iarnold.modernmagazine.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iarnold.modernmagazine.model.DetailEntity;
import com.iarnold.modernmagazine.model.MainEntity;
import com.iarnold.modernmagazine.model.UserInfoEntity;

public class ShareDataTool {

	/**
	 * 保存首页信息
	 * 
	 * @param context
	 * @param mainEntity
	 * @return
	 */
	public static boolean saveMainInfo(Context context, MainEntity mainEntity) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		if (mainEntity != null) {
			Gson gson = new Gson();
			e.putString("carousel", gson.toJson(mainEntity.carousel));
			e.putString("weather", gson.toJson(mainEntity.weather));
			e.putString("blue", mainEntity.blue);
			e.putString("hlife", mainEntity.hlife);
			e.putString("driving", mainEntity.driving);
			e.putString("film", mainEntity.film);
			e.putString("food", mainEntity.food);
			e.putString("ques", mainEntity.ques);
			e.putString("ownerlife", mainEntity.ownerlife);
			e.putString("magazine", mainEntity.magazine);
			e.putString("userlogin", mainEntity.userlogin);
			e.putString("userinfo", mainEntity.userinfo);
			e.putString("userregister", mainEntity.userregister);
			e.putString("userresetpasswd", mainEntity.userresetpasswd);
			e.putString("event", mainEntity.event);
			e.putString("eventreport", mainEntity.eventreport);
		}
		return e.commit();
	}

	/**
	 * 保存登录信息
	 * 
	 * @param context
	 * @param token
	 * @return
	 */
	public static boolean SaveInfo(Context context, UserInfoEntity infoEntity) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		if (infoEntity != null) {
			e.putInt("state", infoEntity.state);
			e.putString("key", infoEntity.key);
			e.putString("username", infoEntity.username);
			e.putString("nickname", infoEntity.nickname);
			e.putString("face", infoEntity.face);
			e.putInt("integral", infoEntity.integral);
		} else {
			e.putInt("state", 0);
			e.putString("key", null);
			e.putString("username", null);
			e.putString("nickname", null);
			e.putString("face", null);
			e.putInt("integral", 0);

		}
		return e.commit();
	}

	/**
	 * 获取首页banner数据
	 * 
	 * @param context
	 * @return
	 */
	public static List<List<String>> getCarousel(Context context) {
		String temp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("carousel", "");
		if (ToosUtils.isStringEmpty(temp)) {
			List<String> list = new ArrayList<String>();
			list.add("assets/main_image.png");
			list.add("");
			List<List<String>> list2 = new ArrayList<List<String>>();
			list2.add(list);
			return list2;
		} else {
			Gson gson = new Gson();
			java.lang.reflect.Type type = new TypeToken<List<List<String>>>() {
			}.getType();
			return gson.fromJson(temp, type);
		}

	}

	/**
	 * 获取天气
	 * 
	 * @param context
	 * @return
	 */
	public static List<String> getWeather(Context context) {
		String temp = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("weather", "");
		if (ToosUtils.isStringEmpty(temp)) {
			List<String> wList = new ArrayList<String>();
			wList.add("32");
			wList.add("http://www.bluemembershlife.com//public/uploads/w_0.png");
			wList.add("https://www.bluemembers.com.cn/Mall/Index?pageName=Mall");
			wList.add("北京");
			wList.add("BEIJING");
			return wList;
		} else {
			Gson gson = new Gson();
			java.lang.reflect.Type type = new TypeToken<List<String>>() {
			}.getType();
			return gson.fromJson(temp, type);
		}

	}

	/**
	 * blue
	 * 
	 * @param context
	 * @return
	 */
	public static String getBlue(Context context) {
		return context
				.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString(
						"blue",
						"https://www.bluemembers.com.cn/Account/LogonPage?returnUrl=%2FMyCenter%2FIndex");
	}

	/**
	 * hlife
	 * 
	 * @param context
	 * @return
	 */
	public static String getHlife(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("hlife", "https://www.hlife.com");
	}

	/**
	 * driving
	 * 
	 * @param context
	 * @return
	 */
	public static String getDriving(Context context) {
		return context
				.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("driving",
						"http://www.bluemembershlife.com/hlife_app/driving_list.php");
	}

	/**
	 * film
	 * 
	 * @param context
	 * @return
	 */
	public static String getFilm(Context context) {
		return context
				.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("film",
						"http://www.bluemembershlife.com/hlife_app/film_list.php");
	}

	/**
	 * food
	 * 
	 * @param context
	 * @return
	 */
	public static String getFood(Context context) {
		return context
				.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("food",
						"http://www.bluemembershlife.com/hlife_app/food_list.php");
	}

	/**
	 * ques
	 * 
	 * @param context
	 * @return
	 */
	public static String getQues(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("ques", "http://www.bluemembershlife.com/ques.php");
	}

	/**
	 * ownerlife
	 * 
	 * @param context
	 * @return
	 */
	public static String getOwnerlife(Context context) {
		return context
				.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("ownerlife",
						"http://www.bluemembershlife.com/hlife_app/ownerlife.php");
	}

	/**
	 * magazine
	 * 
	 * @param context
	 * @return
	 */
	public static String getMagazine(Context context) {
		return context
				.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("magazine",
						"http://www.bluemembershlife.com/hlife_app/magazine_list.php");
	}

	/**
	 * userlogin
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserlogin(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("userlogin",
						"http://www.bluemembershlife.com/login.php");
	}

	/**
	 * userinfo
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserinfo(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("userinfo",
						"http://www.bluemembershlife.com/getuserinfo.php");
	}

	/**
	 * userregister
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserregister(Context context) {
		return context
				.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("userregister",
						"https://www.bluemembers.com.cn/Account/AccountRegister");
	}

	/**
	 * userresetpasswd
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserresetpasswd(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("userresetpasswd",
						"https://www.bluemembers.com.cn/Account/ResetPasswd");
	}

	/**
	 * event
	 * 
	 * @param context
	 * @return
	 */
	public static String getEvent(Context context) {
		return context
				.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("event", "http://www.bluemembershlife.com/event.php");
	}

	/**
	 * eventreport
	 * 
	 * @param context
	 * @return
	 */
	public static String getEventreport(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("eventreport",
						"http://www.bluemembershlife.com/getevent.php");
	}

	/**
	 * 获取stste
	 * 
	 * @param context
	 * @return
	 */
	public static int getState(Context context) {

		// return "bfcb78974f776fcb49ab917db55462cf";
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE).getInt(
				"state", 0);
	}

	/**
	 * 获取key
	 * 
	 * @param context
	 * @return
	 */
	public static String getKey(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("key", "");
	}

	/**
	 * 获取username
	 * 
	 * @param context
	 * @return
	 */
	public static String getUsername(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("username", "");
	}

	/**
	 * 获取nickname
	 * 
	 * @param context
	 * @return
	 */
	public static String getNickname(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("nickname", "");
	}

	/**
	 * 获取face
	 * 
	 * @param context
	 * @return
	 */
	public static String getFace(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("face", "");
	}

	/**
	 * 获取integral
	 * 
	 * @param context
	 * @return
	 */
	public static int getIntegral(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE).getInt(
				"integral", 0);
	}

	/**
	 * 获取设置链接
	 * 
	 * @param context
	 * @return
	 */
	public static String getUsercenter(Context context) {
		return context
				.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString(
						"usercenter",
						"https://www.bluemembers.com.cn/Account/LogonPage?returnUrl=%2FMyCenter%2FIndex");
	}

	/**
	 * 获取face
	 * 
	 * @param context
	 * @return
	 */
	public static String getAbouthlife(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("abouthlife", "https://www.hlife.com");
	}

	/**
	 * 保存登录账号
	 * 
	 * @param context
	 * @param token
	 * @return
	 */
	public static boolean savePhone(Context context, String phone) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putString("phone", phone);
		return e.commit();
	}

	/**
	 * 获取登录账号
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhone(Context context) {
		// return "bfcb78974f776fcb49ab917db55462cf";
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("phone", "");
	}

}
