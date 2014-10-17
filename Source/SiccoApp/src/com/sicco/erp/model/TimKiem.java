package com.sicco.erp.model;

public class TimKiem {
	String tencongviec, hancuoi, id;
	public TimKiem(String id, String tencongviec, String hancuoi) {
		this.id = id;
		this.tencongviec = tencongviec;
		this.hancuoi = hancuoi;
	}
	public void setID(String id) {
		this.id = id;
	}
	public String getID() {
		return id;
	}
	public void setTenCongViec(String tencongviec) {
		this.tencongviec = tencongviec;
	}
	public String getTenCongViec() {
		return tencongviec;
	}
	public void setHanCuoi(String han_cuoi) {
		this.hancuoi = hancuoi;
	}
	public String getHanCuoi() {
		return hancuoi;
	}
}
