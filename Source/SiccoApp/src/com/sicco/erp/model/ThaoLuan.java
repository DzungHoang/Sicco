package com.sicco.erp.model;

public class ThaoLuan {
	int anhdaidien;
	String nguoithaoluan, thoigianthaoluan, noidungthaoluan;

	public ThaoLuan(int anhdaidien, String nguoithaoluan,
			String thoigianthaoluan, String noidungthaoluan) {
		this.anhdaidien = anhdaidien;
		this.nguoithaoluan = nguoithaoluan;
		this.thoigianthaoluan = thoigianthaoluan;
		this.noidungthaoluan = noidungthaoluan;
	}

	public void setAnhDaiDien(int image) {
		this.anhdaidien = image;
	}

	public int getAnhDaiDien() {
		return anhdaidien;
	}

	public void setNguoiThaoLuan(String nguoithaoluan) {
		this.nguoithaoluan = nguoithaoluan;
	}

	public String getNguoiThaoLuan() {
		return nguoithaoluan;
	}
	public void setThoiGianThaoLuan(String thoigianthaoluan) {
		this.thoigianthaoluan = thoigianthaoluan;
	}

	public String getThoiGianThaoLuan() {
		return thoigianthaoluan;
	}
	public void setNoiDungThaoLuan(String noidungthaoluan) {
		this.noidungthaoluan = noidungthaoluan;
	}

	public String getNoiDungThaoLuan() {
		return noidungthaoluan;
	}
}
