package com.tomxiong.netty.bean;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tomxiong.netty.server.GlobalConstant;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.GlobalConstants;

public class UserInfo implements Serializable{
	private static final long serialVersionUID = 3562768188264006800L;
	public static Map<String, UserInfo> map = new ConcurrentHashMap<>();

	private Long id;

	private String phone;

	private String password;
	
	private String code;
	
	private String headImg;
	
	public UserInfo() {
		
	}
	
	public UserInfo(String phone) {
		this.phone = phone;
		this.headImg = GlobalConstant.headImg();
		this.code = GlobalConstant.code();
		this.id = System.currentTimeMillis();
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
