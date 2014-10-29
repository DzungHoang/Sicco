package com.sicco.erp.model;

public class NguoiDung {
	String id;
	String username;
	String phongban;
	int postion = -1;
	
	public NguoiDung(String id, String username, String phongban) {
		this.id = id;
		this.username = username;
		this.phongban = phongban;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	
	public void setPhongban(String phongban) {
		this.phongban = phongban;
	}
	public String getPhongban() {
		return phongban;
	}
	public void setPostion(int postion) {
		this.postion = postion;
	}
	public int getPostion() {
		return postion;
	}
}
