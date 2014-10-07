package com.sicco.erp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.manager.AlertDialogManager;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.service.GetNotificationService;

public class LoginActivity extends Activity {

<<<<<<< HEAD
<<<<<<< HEAD
	//DungHV 312312312
	//Test github
=======
	//LuanDT xong
>>>>>>> origin/master
	
	//Test 07/10/2014.
=======
>>>>>>> parent of 5d7e3ad... Test github
	// Email, password edittext
	EditText txtUsername, txtPassword;

	// login button
	Button btnLogin;

	// remember
	CheckBox cbRemember;
	boolean valueCbRemember;
	boolean vlRemember;
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Session Manager Class
	SessionManager session;

	String mPassword = "";
	String mName;
	String mToken;

	ProgressDialog mLoginDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Session Manager
		session = SessionManager.getInstance(getApplicationContext());
		//init dialog
		mLoginDialog = new ProgressDialog(this);
		mLoginDialog.setTitle("");
		mLoginDialog.setMessage(getString(R.string.progress_msg));

		// Email, Password input text
		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtPassword = (EditText) findViewById(R.id.txtPassword);

		// Check book remember
		cbRemember = (CheckBox) findViewById(R.id.cbRemember);
//		cbRemember.setChecked(session.isRememberInfor());
//		cbRemember.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//				vlRemember = isChecked;
//			}
//		});
		// Login button
		btnLogin = (Button) findViewById(R.id.btnLogin);

		valueCbRemember = session.isRememberInfor();
		if (valueCbRemember) {
			HashMap<String, String> hashMap = session.getUserDetails();
			mName = hashMap.get(SessionManager.KEY_NAME);
			mPassword = hashMap.get(SessionManager.KEY_PASSWORD);
			MyAsync loginTask = new MyAsync();
			loginTask.execute(mName, mPassword);

		} else {
			// Login button click event
			HashMap<String, String> hashMap = session.getUserDetails();
			mName = hashMap.get(SessionManager.KEY_NAME);
			if (mName != null && mName.length() > 0) {
				txtUsername.setText(mName);
				txtPassword.requestFocus();
			}

			btnLogin.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// Check if username, password is filled

					// Get username, password from EditText
					mName = txtUsername.getText().toString();
					mPassword = txtPassword.getText().toString();

					if (mName.trim().length() > 0
							&& mPassword.trim().length() > 0) {
						MyAsync loginTask = new MyAsync();
						loginTask.execute(mName, mPassword);
					} else {
						// user didn't entered username or password
						// Show alert asking him to enter the details
						alert.showAlertDialog(LoginActivity.this,
								"Login failed..",
								"Please enter username and password", false);
					}
				}
			});
		}
		
	}

	public class MyAsync extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			mLoginDialog.show();
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... arg0) {
			HTTPHandler handler = new HTTPHandler();
			List<NameValuePair> nameValue = new ArrayList<NameValuePair>();
			nameValue.add(new BasicNameValuePair("username", mName));
			nameValue.add(new BasicNameValuePair("password", mPassword));

			String ret = handler.makeHTTPRequest("http://office.sicco.vn/api/",
					HTTPHandler.POST, nameValue);
			// String ret;
			// ret =
			// "{\"success\":1,\"error\":0,\"token\":\"ae8a963e9b817ec74c520f3086cd3020\",\"username\":\"khachhang\"}";
			Log.d("DungHV", "ret = " + ret);

			return ret;
		}

		@Override
		protected void onPostExecute(String result) {
			mLoginDialog.dismiss();
			int error_code = -1;
			try {
				JSONObject json = new JSONObject(result);
				error_code = Integer.valueOf(json.getString("error"));
				mName = json.getString("username");
				mToken = json.getString("token");
				Log.d("DungHV", "mName = " + mName);
				Log.d("DungHV", "mToken = " + mToken);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (error_code == 0) {
//				// Creating user login session
//				if (vlRemember) {
//					SharedPrefUtils.savePref(getApplicationContext(),
//							SharedPrefUtils.KEY_CB_REMEMBER, true);
//				}
				if(valueCbRemember) vlRemember = valueCbRemember;
				else vlRemember = cbRemember.isChecked();
				session.createLoginSession(mName, mPassword, mToken, vlRemember);

				// Staring MainActivity
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
				
				Intent intent = new Intent(getApplicationContext(), GetNotificationService.class);
				startService(intent);
				
				finish();
			} else {
				// username / password doesn't match
				alert.showAlertDialog(LoginActivity.this, "Login failed..",
						"Username/Password is incorrect", false);
			}

			super.onPostExecute(result);
		}
	}
}
