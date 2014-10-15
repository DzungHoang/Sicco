package com.sicco.erp.model;

public class TatCaCongViec {
	String title, han_cuoi, id;
	public TatCaCongViec(String id, String title, String han_cuoi) {
		this.id = id;
		this.title = title;
		this.han_cuoi = han_cuoi;
	}
	public void setID(String id) {
		this.id = id;
	}
	public String getID() {
		return id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public void setHancuoi(String han_cuoi) {
		this.han_cuoi = han_cuoi;
	}
	public String getHancuoi() {
		return han_cuoi;
	}
}
