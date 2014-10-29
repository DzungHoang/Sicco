package com.sicco.erp;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.adapter.DuAnAdapter;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.model.DuAn;
import com.sicco.erp.utils.FileUtils;

public class ThemCongViecActivity extends Activity implements OnClickListener {
	LinearLayout mLayoutNgayHoanThanh;
	LinearLayout mLayoutDuAn;
	LinearLayout mLayoutNguoiXuLy;
	LinearLayout mLayoutNguoiXem;
	LinearLayout mLayoutTepDinhKem;
	TextView tvTepDinhKem;
	TextView tvNgayhoanThanh;
	Dialog mDialog;
	String path;

	ProgressDialog pDialog;
	String url_duan = "http://apis.mobile.vareco.vn/sicco/duan.php";
	JSONArray duan = null;
	ArrayList<HashMap<String, String>> duAnList;
	ArrayList<DuAn> mDuAn;
	ListView mLvDuAn;
	DuAnAdapter mDuAnAdapter;

	private int date;
	private int months;
	private int years_now;

	private static final String TAG = "ThemCongViecActivity";
	private static final int FILE_SELECT_CODE = 0;

	static final int DATE_DIALOG_ID = 111;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_them_cong_viec);

		mLayoutNgayHoanThanh = (LinearLayout) findViewById(R.id.layout_ngay_hoan_thanh);
		mLayoutDuAn = (LinearLayout) findViewById(R.id.layout_du_an);
		mLayoutNguoiXuLy = (LinearLayout) findViewById(R.id.layout_nguoi_xu_ly);
		mLayoutNguoiXem = (LinearLayout) findViewById(R.id.layout_nguoi_xem);
		mLayoutTepDinhKem = (LinearLayout) findViewById(R.id.layout_tep_dinh_kem);

		tvTepDinhKem = (TextView) findViewById(R.id.tv_tep_dinh_kem);
		tvTepDinhKem.setText(R.string.choose_file);
		tvNgayhoanThanh = (TextView) findViewById(R.id.tv_ngay_hoan_thanh);
		final Calendar c = Calendar.getInstance();
		date = c.get(Calendar.DATE);
		months = c.get(Calendar.MONTH);
		years_now = c.get(Calendar.YEAR);
		tvNgayhoanThanh.setText(new StringBuilder().append(" ")
				.append(padding_str(date)).append("-")
				.append(padding_str(months + 1)).append("-")
				.append(padding_str(years_now)));

	}

	@Override
	protected void onResume() {
		if (path != null) {
			String fileName = path.substring(path.lastIndexOf("/") + 1);
			tvTepDinhKem.setText(fileName);
		}
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_ngay_hoan_thanh:
			showDialog(DATE_DIALOG_ID);

			break;
		case R.id.layout_du_an:
			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			View dialogDuAn = inflater.inflate(R.layout.duan_dialog, null);

			duAnList = new ArrayList<HashMap<String, String>>();
			new GetCongViec().execute();
			mDuAn = new ArrayList<DuAn>();
			mLvDuAn = (ListView) dialogDuAn.findViewById(R.id.lv_duan);
			mDuAnAdapter = new DuAnAdapter(ThemCongViecActivity.this,
					android.R.layout.simple_list_item_single_choice, mDuAn);
			mLvDuAn.setAdapter(mDuAnAdapter);
			mLvDuAn.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
				}
			});
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getResources().getString(R.string.choose_duan));
			builder.setView(dialogDuAn);
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialogInterface,
								int arg1) {
							dialogInterface.dismiss();

						}
					});
			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			AlertDialog alertDialog = builder.create();
			alertDialog.show();

			break;
		case R.id.layout_nguoi_xu_ly:
			break;
		case R.id.layout_nguoi_xem:

			break;
		case R.id.layout_tep_dinh_kem:
			showFileChooser();
			break;

		default:
			break;
		}

	}

	// ------------choose date------------------//

	private DatePickerDialog.OnDateSetListener datePickerListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			date = dayOfMonth;
			months = monthOfYear;
			years_now = year;

			tvNgayhoanThanh.setText(new StringBuilder()
					.append(padding_str(date)).append("-")
					.append(padding_str(months + 1)).append("-")
					.append(padding_str(years_now)));
		}

	};

	@Deprecated
	protected Dialog onCreateDialog(int id) {
		DatePickerDialog datePickerDialog = new DatePickerDialog(this,
				datePickerListener, years_now, months, date);
		datePickerDialog.getDatePicker().setSpinnersShown(false);
		datePickerDialog.getDatePicker().setCalendarViewShown(true);
		return datePickerDialog;
	}

	private static String padding_str(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	// ------------choose du an------------------------//

	private class GetCongViec extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(ThemCongViecActivity.this);
			pDialog.setMessage(getResources().getString(R.string.vui_long_doi));
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... arg0) {
			HTTPHandler handler = new HTTPHandler();
			String ret = handler.makeHTTPRequest(url_duan, HTTPHandler.GET);
			return ret;
		}

		@Override
		protected void onPostExecute(String result) {
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			if (result != null) {
				try {
					JSONObject jsonObj = new JSONObject(result);

					// Getting JSON Array node
					duan = jsonObj.getJSONArray("row");
					// looping through All Contacts
					for (int i = 0; i < duan.length(); i++) {
						JSONObject c = duan.getJSONObject(i);

						String id = c.getString("id_du_an");
						String tenduan = c.getString("ten_du_an");

						Log.d("LuanDT", "tenduan = " + tenduan + " - " + id);

						mDuAn.add(new DuAn(id, tenduan));
						mDuAnAdapter.notifyDataSetChanged();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("LuanDT", "Khong the lay data tu url nay");
			}

			super.onPostExecute(result);
		}

	}

	// ------------choose nguoi xu ly------------------//

	// ------------choose nguoi xem--------------------//

	// ------------choose file-------------------------//

	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try {
			startActivityForResult(Intent.createChooser(intent,
					getString(R.string.choose_file)), FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(this, "Please install a File Manager.",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case FILE_SELECT_CODE:
			if (resultCode == RESULT_OK) {
				// Get the Uri of the selected file
				Uri uri = data.getData();
				Log.d("LuanDT", "File Uri: " + uri.toString());
				// Get the path
			path = FileUtils.getPath(this, uri);
				Log.d("LuanDT", "File Path: " + path);
				Toast.makeText(this, "File Selected: " + path,
						Toast.LENGTH_SHORT).show();
				// Get the file instance
				// File file = new File(path);
				// Initiate the upload
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static String getPath(Context context, Uri uri)
			throws URISyntaxException {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				// Eat it
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	// ------------option menu-------------------------//

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
