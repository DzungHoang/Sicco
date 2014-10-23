package com.sicco.erp.model;

public class CongViecHoanThanh {
	String tencongviec, ngayhoanthanh, id;

	public CongViecHoanThanh(String id, String tencongviec, String ngayhoanthanh) {
		this.id = id;
		this.tencongviec = tencongviec;
		this.ngayhoanthanh = ngayhoanthanh;
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

	public void setNgayHoanThanh(String ngayhoanthanh) {
		this.ngayhoanthanh = ngayhoanthanh;
	}

	public String getNgayHoanThanh() {
		return ngayhoanthanh;
	}
}
