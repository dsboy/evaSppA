package com.loopback.androidapps.saveapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AccountDetailActivity extends Activity implements
		View.OnClickListener {

	private TextView txtNameValue, txtPeriodValue, txtBudgetValue,
			txtStartDateValue, txtEndDateValue, txtCurrencyValue,txtAccountSelection;
	private Button btnEditAccount, btnNewAccount;
	private ImageButton btnAccountSelection;
	public boolean isEuropeanCalendar = true;
	public Account account; 
	public Currency currency;
	
	private SaveApp saveApp;

	public void onCreate(Bundle savedInstanceState) {
		Log.i("AD", "Init");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accountdetail);

		loadActivity();
	}

	public void loadActivity() {
		Log.i("AD", "Loading...");
		saveApp = ((SaveApp)getApplicationContext());

		txtNameValue = (TextView) findViewById(R.id.txtNameValue);
		txtBudgetValue = (TextView) findViewById(R.id.txtBudgetValue);
		txtPeriodValue = (TextView) findViewById(R.id.txtPeriodValue);
		txtStartDateValue = (TextView) findViewById(R.id.txtStartDateValue);
		txtEndDateValue = (TextView) findViewById(R.id.txtEndDateValue);
		txtCurrencyValue = (TextView) findViewById(R.id.txtCurrencyValue);
		txtAccountSelection= (TextView) findViewById(R.id.txtAccountSelection);
		btnNewAccount = (Button) findViewById(R.id.btnNewAccount);
		btnAccountSelection = (ImageButton) findViewById(R.id.btnAccountSelection);
		btnEditAccount = (Button) findViewById(R.id.btnEditAccount);

		btnNewAccount.setOnClickListener(this);
		btnAccountSelection.setOnClickListener(this);
		btnEditAccount.setOnClickListener(this);
	}
	
	public void onResume(){
		super.onResume();
		loadAccount();
	}
	public void loadAccount(){
		Log.i("AD", "Loading Account...");
		account =  new Account(saveApp.getAccountId());
		txtAccountSelection.setText(getString(R.string.strAccount)
				+ ": " + saveApp.getAccountDesc());
		txtNameValue.setText(saveApp.getAccountDesc());
		txtBudgetValue.setText(String.valueOf(account.getBudget()));
		txtPeriodValue.setText(account.getPeriod());
		txtStartDateValue.setText(account.getStartDate());
		txtEndDateValue.setText(account.getEndDate());
		
		currency= new Currency(saveApp.getCurrencyId());
		txtCurrencyValue.setText(currency.getDescription() + ": "+ saveApp.getCurrencySymbol());

		Log.i("AD", "Loaded");
	}
	public void onBackPressed() {
		Intent intent = new Intent(this.getApplicationContext(),
				HomeActivity.class);
		startActivity(intent);
	}
	public void onClick(View v) {
		if (v == btnEditAccount) {
			Log.i("AD", "Edit Account");
			Intent intent = new Intent(this.getApplicationContext(),
					AccountEditActivity.class);
			startActivity(intent);
		} else if (v == btnAccountSelection) {
			final CharSequence[] accounts;
			Account account = new Account();
			accounts = account.selectAccounts();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Chooese Account");
			builder.setItems(accounts, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int _item) {
					saveApp.setAccountId(_item + 1);
				}
			});
			AlertDialog dropdown = builder.create();
			dropdown.show();
			Toast.makeText(this, "More than one account is not allowed in this version",
					Toast.LENGTH_LONG).show();
		} else if (v == btnNewAccount) {
			Toast.makeText(this, "More than one account is not allowed in this version",
					Toast.LENGTH_LONG).show();
		}

	}

}

