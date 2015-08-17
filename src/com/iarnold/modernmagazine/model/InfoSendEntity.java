package com.iarnold.modernmagazine.model;

import java.io.Serializable;

public class InfoSendEntity implements Serializable {

	public String uid;
	public String type;// 0 微信 1 QQ 2 新浪
	public String nickname;
	public String face;

	public InfoSendEntity(String uid, String type, String nickname, String face) {
		super();
		this.uid = uid;
		this.type = type;
		this.nickname = nickname;
		this.face = face;
	}

}
