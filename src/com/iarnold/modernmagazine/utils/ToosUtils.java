package com.iarnold.modernmagazine.utils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Environment;
import android.widget.TextView;

/**
 * @author Mu
 * @date 2014-11-4
 * @description 常用工具类
 */
public class ToosUtils {

	/**
	 * 判断字符串是否为空
	 * 
	 * @param msg
	 * @return 为空 true,不为空 false
	 */
	public static boolean isStringEmpty(String msg) {
		if (null == msg || "".equals(msg)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取TextView的内容
	 * 
	 * @param textView
	 * @return textView 的内容
	 */
	public static String getTextContent(TextView textView) {
		return textView.getText().toString().trim();
	}

	/**
	 * 判断TextView是否为空
	 * 
	 * @param textView
	 * @return 为空 true,不为空 false
	 */
	public static boolean isTextEmpty(TextView textView) {
		return isStringEmpty(getTextContent(textView));
	}

	// 将SD卡文件删除
	public static void deleteFile(File file) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (file.exists()) {
				if (file.isFile()) {
					file.delete();
				}

				file.delete();
			}
		}
	}

	/**
	 * 检查密码是否小于6位
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkPwd(String str) {
		if (str.length() < 6) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断字符串是否为手机号
	 * 
	 * @param msg
	 * @return true为手机号 ,false不为手机号
	 */
	public static boolean MatchPhone(String name) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(name);
		return m.matches();
	}

	/**
	 * 判断是否为有效的链接
	 * 
	 * @param strLink
	 * @return
	 */
	public static boolean isURL(String str) {
		// 转换为小写
		str = str.toLowerCase();
		String regex = "^((https|http|ftp|rtsp|mms)?://)"
				+ "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" // ftp的user@
				+ "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
				+ "|" // 允许IP和DOMAIN（域名）
				+ "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
				+ "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
				+ "[a-z]{2,6})" // first level domain- .com or .museum
				+ "(:[0-9]{1,4})?" // 端口- :80
				+ "((/?)|" // a slash isn't required if there is no file name
				+ "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		return m.matches();

	}

	public static String[] ListFile() {

		File file = new File("/sdcard/xUtils/");
		File[] f = file.listFiles();
		if (f != null) {
			String Path[] = new String[f.length];
			for (int i = 0; i < f.length; i++)

			{

				Path[i] = f[i].getPath();
			}
			return Path;
		} else {
			return null;
		}

	}

}
