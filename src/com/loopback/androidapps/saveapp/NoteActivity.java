package com.loopback.androidapps.saveapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoteActivity extends Activity implements View.OnClickListener {

	private EditText edtNote;
	private Button btnSaveNote, btnDiscardNote;
	private String note;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note);
		Bundle bundle = this.getIntent().getExtras();
		note = bundle.getString("Note");
		edtNote = (EditText) findViewById(R.id.edtNote);
		btnSaveNote = (Button) findViewById(R.id.btnSaveNote);
		btnDiscardNote = (Button) findViewById(R.id.btnDiscardNote);
		btnSaveNote.setOnClickListener(this);
		btnDiscardNote.setOnClickListener(this);
		edtNote.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
	}

	public void onResume() {
		super.onResume();
		if (note.equals(""))
			note = edtNote.getText().toString();
		else
			edtNote.setText(note);
	}

	public void onBackPressed() {
		finishActivity();
	}

	public void onClick(View v) {
		Log.i("NTA", "Click...");
		if (v == btnSaveNote) {
			note = edtNote.getText().toString();
			finishActivity();
		} else if (v == btnDiscardNote) {
			finishActivity();
		}
	}

	private void finishActivity() {
		Log.i("NTA", "Finishing...");
		int RESULT = RESULT_FIRST_USER;
		Bundle bundle = new Bundle();
		bundle.putString("Note", note);
		Intent intent = new Intent(this.getApplicationContext(),
				OutlayEditActivity.class);
		intent.putExtras(bundle);
		setResult(RESULT, intent);
		finish();
	}
}
