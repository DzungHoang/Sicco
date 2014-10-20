package com.sicco.erp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class SuaCongViecActivity extends Activity implements OnClickListener {
	LinearLayout mLayoutNgayHoanThanh;
	LinearLayout mLayoutDuAn;
	LinearLayout mLayoutNguoiXuLy;
	LinearLayout mLayoutNguoiXem;
	LinearLayout mLayoutTepDinhKem;
	Dialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_them_cong_viec);

		mLayoutNgayHoanThanh = (LinearLayout) findViewById(R.id.layout_ngay_hoan_thanh);
		mLayoutDuAn = (LinearLayout) findViewById(R.id.layout_du_an);
		mLayoutNguoiXuLy = (LinearLayout) findViewById(R.id.layout_nguoi_xu_ly);
		mLayoutNguoiXem = (LinearLayout) findViewById(R.id.layout_nguoi_xem);
		mLayoutTepDinhKem = (LinearLayout) findViewById(R.id.layout_tep_dinh_kem);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_ngay_hoan_thanh:
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SuaCongViecActivity.this);
			builder.setTitle("Ngay Hoan Thanh")
					.setMessage("Ngay Hoan Thanh")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							})
					.setNegativeButton("Hủy",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();

								}
							});
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
			break;
		case R.id.layout_du_an:
			break;

		default:
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_them_cong_viec, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

}
