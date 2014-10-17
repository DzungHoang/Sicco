package com.sicco.erp.widget;

import afzkl.development.colorpickerview.dialog.ColorPickerDialog;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.utils.SharedPrefUtils;

public class WidgetCVConfigure extends Activity implements OnClickListener {
	private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	LinearLayout itemBgColor;
	LinearLayout itemTextColor;
	int bgColor;
	int textColor;
	int mAppWidgetId = 1;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_widget);
		assignAppWidgetId();
		itemBgColor = (LinearLayout) findViewById(R.id.bgColor);
		itemTextColor = (LinearLayout) findViewById(R.id.textColor);
		itemBgColor.setOnClickListener(this);
		itemTextColor.setOnClickListener(this);

		intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {

			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);

		}

		bgColor = SharedPrefUtils.getPref(getApplicationContext(),
				SharedPrefUtils.KEY_BG_WIDGET_COLOR, 0x80E5E5E5);
		textColor = SharedPrefUtils.getPref(getApplicationContext(),
				SharedPrefUtils.KEY_TEXT_WIDGET_COLOR, 0xFFFFFFFF);
		Log.d("DungHV","onCreate: bgColor = " + bgColor);
		Log.d("DungHV","onCreate:textColor = " + textColor);

	}

	public void onClickColorPickerDialogAlpha() {
		Log.d("DungHV","onClickColorPickerDialogAlpha:bgColor = " + bgColor);
		Log.d("DungHV","onClickColorPickerDialogAlpha:textColor = " + textColor);
		final ColorPickerDialog colorDialog = new ColorPickerDialog(this,
				bgColor);

		colorDialog.setAlphaSliderVisible(true);
		colorDialog.setTitle("Chá»�n mÃ u ná»�n vÃ  Ä‘á»™ trong suá»‘t!");

		colorDialog.setButton(DialogInterface.BUTTON_POSITIVE,
				getString(android.R.string.ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						bgColor = Color.parseColor(colorToHexString(colorDialog
								.getColor()));
						SharedPrefUtils.savePref(getApplicationContext(),
								SharedPrefUtils.KEY_BG_WIDGET_COLOR, bgColor);
					}
				});

		colorDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				getString(android.R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Nothing to do here.
					}
				});

		colorDialog.show();
	}

	public void onClickColorPickerDialog() {
		Log.d("DungHV","onClickColorPickerDialog:bgColor = " + bgColor);
		Log.d("DungHV","onClickColorPickerDialog:textColor = " + textColor);
		final ColorPickerDialog colorDialog = new ColorPickerDialog(this,
				textColor);

		colorDialog.setAlphaSliderVisible(false);
		colorDialog.setTitle("Chá»�n mÃ u chá»¯!");

		colorDialog.setButton(DialogInterface.BUTTON_POSITIVE,
				getString(android.R.string.ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						textColor = Color.parseColor(colorToHexString(colorDialog
								.getColor()));
						Log.d("DungHV", "onClickColorPickerDialog: textColor = " + textColor);
						SharedPrefUtils.savePref(getApplicationContext(),
								SharedPrefUtils.KEY_TEXT_WIDGET_COLOR, textColor);
					}
				});

		colorDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				getString(android.R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Nothing to do here.
					}
				});

		colorDialog.show();
	}

	private String colorToHexString(int color) {
		return String.format("#%08X", 0xFFFFFFFF & color);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bgColor:
			onClickColorPickerDialogAlpha();
			break;
		case R.id.textColor:
			onClickColorPickerDialog();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_save_and_cancel, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_save) {
			AppWidgetManager manager = AppWidgetManager
					.getInstance(getApplicationContext());
			int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(
					getApplicationContext(), WidgetCVProvider.class));
			Intent updateIntent = new Intent(getApplicationContext(),
					WidgetCVProvider.class);
			updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
					appWidgetIds);

			sendBroadcast(updateIntent);
			startWidget();
			return true;
		} else if (id == R.id.action_cancel) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void assignAppWidgetId() {
		Bundle extras = getIntent().getExtras();
		if (extras != null)
			appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
	}
	private void startWidget() {

		// this intent is essential to show the widget
		// if this intent is not included,you can't show
		// widget on homescreen
		Intent intent = new Intent();
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		setResult(Activity.RESULT_OK, intent);

		// start your service
		// to fetch data from web
		Intent serviceIntent = new Intent(this, RemoteFetchService.class);
		serviceIntent
				.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		startService(serviceIntent);

		// finish this activity
		this.finish();

	}
}
