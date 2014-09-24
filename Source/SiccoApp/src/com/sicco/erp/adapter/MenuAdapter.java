package com.sicco.erp.adapter;

import java.util.List;

import com.sicco.erp.R;
import com.sicco.erp.R.layout;
import com.sicco.erp.model.CategoryMenu;
import com.sicco.erp.model.ItemMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter {
	
	private List<Object> mItems;
	Context mContext;
    public MenuAdapter(Context context, List<Object> items) {
        mItems = items;
        mContext = context;
    }
    
	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
    public int getItemViewType(int position) {
        return getItem(position) instanceof ItemMenu ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position) instanceof ItemMenu;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
        Object item = getItem(position);

        if (item instanceof CategoryMenu) {
            if (v == null) {
                v = LayoutInflater.from(mContext).inflate(R.layout.menu_row_category, parent, false);
            }

            ((TextView) v).setText(((CategoryMenu) item).title);

        } else {
            if (v == null) {
                v = LayoutInflater.from(mContext).inflate(R.layout.menu_row_item, parent, false);
            }

            TextView tv = (TextView) v;
            tv.setText(((ItemMenu) item).title);
            tv.setCompoundDrawablesWithIntrinsicBounds(((ItemMenu) item).icon, 0, 0, 0);
        }
        return v;
	}
	
}