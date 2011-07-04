package com.loopback.androidapps.saveapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AccountEditActivity extends Activity implements
		AdapterView.OnItemSelectedListener, View.OnClickListener {

	private String date_selected, dateInput;

	private TextView txtStartDateValue, txtEndDateValue;
	private int periodId;
	private EditText edtNameValue, edtBudgetValue;
	private Button btnChangeStartDate, btnChangeEndDate, btnAcceppt,
			btnDiscard;
	private Spinner spnPeriod, spnCurrency;
	public boolean isEuropeanCalendar = true;
	public Account account;
	public Currency currency;
	private List<String> currencies = new ArrayList<String>();
	private List<String> periods = new ArrayList<String>();
	static final int DATE_DIALOG_ID = 0, DATE_DIALOG_ID2 = 1;
	private boolean startDateSelected = false;

	public SaveApp saveApp;

	/*------------------------------------------------------------------------------------------------
	 *-------------------------------------- ON CREATE --------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */

	public void onCreate(Bundle savedInstanceState) {
		Log.i("OEA", "Init");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accountedit);
		loadActivity();
	}

	/*------------------------------------------------------------------------------------------------
	 *-------------------------------------- LOAD ACTIVITY --------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */

	public void loadActivity() {

		Log.i("AE", "Loading...");
		saveApp = ((SaveApp) getApplicationContext());

		edtNameValue = (EditText) findViewById(R.id.edtNameValue);
		edtBudgetValue = (EditText) findViewById(R.id.edtBudgetValue);
		spnPeriod = (Spinner) findViewById(R.id.spnPeriod);
		txtStartDateValue = (TextView) findViewById(R.id.txtStartDateValue);
		txtEndDateValue = (TextView) findViewById(R.id.txtEndDateValue);
		btnChangeStartDate = (Button) findViewById(R.id.btnChangeStartDate);
		btnChangeEndDate = (Button) findViewById(R.id.btnChangeEndDate);
		spnCurrency = (Spinner) findViewById(R.id.spnCurrency);
		btnAcceppt = (Button) findViewById(R.id.btnAccept);
		btnDiscard = (Button) findViewById(R.id.btnDiscard);

		btnChangeStartDate.setOnClickListener(this);
		btnChangeEndDate.setOnClickListener(this);
		btnAcceppt.setOnClickListener(this);
		btnDiscard.setOnClickListener(this);

		account = new Account(saveApp.getAccountId());

		Log.i("AD", "Loading...");
		edtNameValue.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		edtNameValue.setText(account.getDescription());
		edtBudgetValue.setText(String.valueOf(account.getBudget()));
		txtStartDateValue.setText(account.getStartDate().subSequence(0, 10));
		txtEndDateValue.setText(account.getEndDate());

		// SPINNER PERIOD
		periods.add("None");
		periods.add("Week");
		periods.add("Month");
		periods.add("Year");
		getPeriod(account.getPeriod());
		spnPeriod.setOnItemSelectedListener((OnItemSelectedListener) this);
		ArrayAdapter<String> adapterPeriod = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, periods);
		adapterPeriod
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnPeriod.setAdapter(adapterPeriod);
		spnPeriod.setSelection(periodId);

		// SPINNER CURRENCY
		currency = new Currency();
		currencies = currency.selectCurrencies();
		spnCurrency.setOnItemSelectedListener((OnItemSelectedListener) this);
		ArrayAdapter<String> adapterCurrency = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, currencies);
		adapterCurrency
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCurrency.setAdapter(adapterCurrency);
		spnCurrency.setSelection(saveApp.getCurrencyId() - 1);

		Log.i("AE", "Loaded");

	}

	/*------------------------------------------------------------------------------------------------
	 *-------------------------------------- BUTTONS --------------------------------------------------
	 *------------------------------------------------------------------------------------------------ */

	public void onBackPressed() {
		Intent intent = new Intent(this.getApplicationContext(),
				AccountDetailActivity.class);
		startActivity(intent);
	}

	public void onClick(View v) {
		// ACCEPT
		if (v == btnAcceppt) {
			Log.i("AE", "Edit Account");
			Log.i("AE", "Updating...");

			account.setDescription(String.valueOf(edtNameValue.getText()));
			account.setCurrencyId(spnCurrency.getSelectedItemPosition() + 1);
			account.setPeriod((String) spnPeriod.getSelectedItem());
			account.setStartDate((String) txtStartDateValue.getText());
			account.setEndDate((String) txtEndDateValue.getText());
			account.setBudget(Integer.valueOf(String.valueOf(edtBudgetValue
					.getText())));
			account.update();
			saveApp.setAccountDesc(account.getDescription().toString());
			saveApp.setCurrencyId(account.getCurrencyId());
			currency.inflate(saveApp.getCurrencyId());
			saveApp.setCurrencySymbol(currency.getSymbol().toString());

			Log.i("AE", "Updated.");
			Intent intent = new Intent(this.getApplicationContext(),
					AccountDetailActivity.class);
			startActivity(intent);
			// DISCARD
		} else if (v == btnDiscard) {
			Intent intent = new Intent(this.getApplicationContext(),
					AccountDetailActivity.class);
			startActivity(intent);
			// CHANGE DATE START
		} else if (v == btnChangeStartDate) {
			dateInput = account.getStartDate();
			showDialog(DATE_DIALOG_ID);
			startDateSelected = true;

		} else if (v == btnChangeEndDate) {
			dateInput = account.getEndDate();
			showDialog(DATE_DIALOG_ID2);
			startDateSelected = false;

		}
	}

	// UNIMPLEMETED
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	// PERIOD
	public void getPeriod(String period) {
		if (period.equals("Week"))
			periodId = 1;
		else if (period.equals("Month"))
			periodId = 2;
		else if (period.equals("Year"))
			periodId = 3;
		else
			periodId = 0;
	}

	// DATE PICKER
	public DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		// onDateSet method
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			date_selected = Utilities.formatDate(year, monthOfYear, dayOfMonth,
					0, 0);
			if (startDateSelected) {
				account.setStartDate(date_selected);
				txtStartDateValue.setText(Utilities.printDate(
						account.getStartDate()).subSequence(0, 10));
			} else {
				account.setEndDate(date_selected);
				txtEndDateValue.setText(Utilities.printDate(
						account.getEndDate()).subSequence(0, 10));
			}
		}
	};

	protected Dialog onCreateDialog(int id) {
		int year, month, day;
		if (dateInput.substring(0, 1).equals("N")) {
			year = 2012;
			month = 0;
			day = 1;
		} else {
			year = Integer.valueOf(Utilities.getDateUnit(dateInput, 'y'));
			month = Integer.valueOf(Utilities.getDateUnit(dateInput, 'M')) - 1;
			day = Integer.valueOf(Utilities.getDateUnit(dateInput, 'd'));
		}
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, year, month,
					day);

		case DATE_DIALOG_ID2:
			return new DatePickerDialog(this, mDateSetListener, year, month,
					day);
		}
		return null;

	}
}
