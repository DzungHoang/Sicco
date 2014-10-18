package com.sicco.erp.model;

public class CongViecTheoDoi {
	String tencongviec, nguoixuly, id;

	public CongViecTheoDoi(String id, String tencongviec, String nguoixuly) {
		this.id = id;
		this.tencongviec = tencongviec;
		this.nguoixuly = nguoixuly;
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

	public void setNguoiXuLy(String nguoixuly) {
		this.nguoixuly = nguoixuly;
	}

	public String getNguoiXuLy() {
		return nguoixuly;
	}
}
