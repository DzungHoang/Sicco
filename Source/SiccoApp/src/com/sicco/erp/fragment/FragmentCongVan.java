package com.sicco.erp.fragment;

import com.sicco.erp.R;
import com.sicco.erp.R.layout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentCongVan extends Fragment{
	View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_cong_van, container, false);
		return rootView;
	}
}
