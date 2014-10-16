package com.sicco.erp.model;

public class CongViecDuocGiao {
	String tencongviec, nguoigiao, id;

	public CongViecDuocGiao(String id, String tencongviec, String nguoigiao) {
		this.id = id;
		this.tencongviec = tencongviec;
		this.nguoigiao = nguoigiao;
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

	public void setNguoiGiao(String nguoigiao) {
		this.nguoigiao = nguoigiao;
	}

	public String getNguoiGiao() {
		return nguoigiao;
	}
}
