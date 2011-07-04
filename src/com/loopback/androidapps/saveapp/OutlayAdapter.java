package com.loopback.androidapps.saveapp;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class OutlayAdapter extends BaseAdapter {
	public Context context;
	public ArrayList<Outlay> items;
	public boolean landscape;

	public OutlayAdapter(Context _context, ArrayList<Outlay> list, boolean _landscape) {
		this.context = _context;
		items = list;
		landscape = _landscape;
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Outlay outlay = items.get(position);
		return new OutlayListItem(context, outlay, position + 1, landscape);
	}

	// public void remove(int position) {
	// if ((position < items.size()) && (position >= 0))
	// items.remove(position);
	// }
}
