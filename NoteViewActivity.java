package com.loopback.androidapps.saveapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NoteViewActivity extends Activity implements View.OnClickListener {

	private TextView txtNote;
	private Button btnOkNote;
	private String note;
	private int outlayId;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noteview);
		Bundle bundle = this.getIntent().getExtras();
		outlayId = bundle.getInt("Id");
		note= bundle.getString("Note");
		txtNote = (TextView) findViewById(R.id.txtNote);
		btnOkNote = (Button) findViewById(R.id.btnOkNote);
		btnOkNote.setOnClickListener(this);
		txtNote.setText(note);
	}
	public void onResume(){
		super.onResume();
		txtNote.setText(note);
	}
	
	public void onClick(View v) {
		Log.i("NTA", "Click...");
		if (v == btnOkNote) {
			Bundle bundle = new Bundle();
			bundle.putInt("Id", outlayId);
			Intent intent = new Intent(this.getApplicationContext(),
					OutlayDetailActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}


}
