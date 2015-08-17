package com.iarnold.modernmagazine.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mu
 * @date 2015-6-21
 * @description 首页返回数据
 */
public class MainEntity implements Serializable {

	public List<List<String>> carousel;// 首页banner数据

	public List<String> weather;// 天气

	public String blue;

	public String hlife;

	public String driving;

	public String film;

	public String food;

	public String ques;

	public String ownerlife;

	public String magazine;

	public String userlogin;

	public String userinfo;

	public String userregister;

	public String userresetpasswd;

	public String event;

	public String eventreport;
}
