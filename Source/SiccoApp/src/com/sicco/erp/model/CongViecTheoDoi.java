package com.sicco.erp.model;

public class CongViecTheoDoi {
	String tencongviec, tiendo, id;

	public CongViecTheoDoi(String id, String tencongviec, String tiendo) {
		this.id = id;
		this.tencongviec = tencongviec;
		this.tiendo = tiendo;
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

	public void setTienDo(String tiendo) {
		this.tiendo = tiendo;
	}

	public String getTienDo() {
		return tiendo;
	}
}
