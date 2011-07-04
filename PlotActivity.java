package com.loopback.androidapps.saveapp;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.series.XYSeries;
import com.androidplot.ui.layout.AnchorPosition;
import com.androidplot.ui.layout.DynamicTableModel;
import com.androidplot.ui.layout.SizeLayoutType;
import com.androidplot.ui.layout.SizeMetrics;
import com.androidplot.ui.layout.XLayoutStyle;
import com.androidplot.ui.layout.YLayoutStyle;
import com.androidplot.xy.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PlotActivity extends Activity {

	private XYPlot mySimpleXYPlot;
	private DBManager dbManager;
	private TextView txtAccount;
	public SaveApp saveApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plot1);

		Log.i("PLOT", "Loading...");
		saveApp = ((SaveApp) getApplicationContext());
		dbManager = saveApp.getDbManager();

		txtAccount = (TextView) findViewById(R.id.txtAccount);
		txtAccount.setText(getString(R.string.strAccount) + ": "
				+ saveApp.getAccountDesc());

		// Initialize our XYPlot reference:
		mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);

		Cursor cursor = dbManager.selectPlotList();
		DBReader dbReader = new DBReader();
		dbReader.plotList(cursor);

		if (dbReader.dateList == null)
			Toast.makeText(this,
					"You don«t have enough data to plot. Please add some. ",
					Toast.LENGTH_SHORT).show();
		else {
			// DATA----------------------------------------------
			// Inflate acumulated List
			ArrayList<Integer> acumulatedList = new ArrayList<Integer>();
			acumulatedList.add(dbReader.chargeList.get(0));
			for (int i = 1; i < dbReader.chargeList.size(); i++) {
				acumulatedList.add(dbReader.chargeList.get(i)
						+ acumulatedList.get(i - 1));
			}

			// 0 to Null
			for (int i = 0; i < dbReader.chargeList.size(); i++) {
				if (dbReader.chargeList.get(i) == 0)
					dbReader.chargeList.set(i, null);
			}
			// Turn the above arrays into XYSeries:
			// ----------------------------------------------
		//	XYSeries series1 = new SimpleXYSeries(dbReader.dateList, // SimpleXYSeries
			//		dbReader.chargeList, "Movement"); // Set the display title
														// of the
														// series
			XYSeries series2 = new SimpleXYSeries(dbReader.dateList, // SimpleXYSeries
					acumulatedList, "Bag"); // Set the display title of the
											// series

			// FOR THE BARS ----------------------------------------------
			/*
			 * XYSeries series2 = new
			 * SimpleXYSeries(Arrays.asList(series2Numbers),
			 * SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");
			 */

			//FORMATTING LINE----------------------------------------------
			LineAndPointFormatter series1Format = new LineAndPointFormatter(
					Color.rgb(100, 25, 20), Color.rgb(4, 100, 88), Color.rgb(
							66, 100, 3));
			series1Format.setFillPaint(null);
			series1Format.setVertexPaint(null);
			series1Format.getLinePaint().setShadowLayer(0, 0, 0, 0);

			// Add series1 to the xyplot:
			mySimpleXYPlot.addSeries(series2, series1Format);
			// MOVEMENTS SERIE 
			//mySimpleXYPlot.addSeries(series1,  series1Format);
			
			
			// MARKER----------------------------------------------
			XYRegionFormatter regionFormatter = new XYRegionFormatter(Color.RED);
			series1Format.addRegion(new RectRegion(Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY, 0, 3, "Zero"), regionFormatter);

			mySimpleXYPlot.addMarker(new YValueMarker(0, "0"));

			// LEGEND----------------------------------------------
			mySimpleXYPlot.getLegendWidget().setTableModel(
					new DynamicTableModel(2, 2));
			mySimpleXYPlot.getLegendWidget().setSize(
					new SizeMetrics(25, SizeLayoutType.ABSOLUTE, 125,
							SizeLayoutType.ABSOLUTE));

			Paint bgPaint = new Paint();
			bgPaint.setColor(Color.BLACK);
			bgPaint.setStyle(Paint.Style.FILL);
			bgPaint.setAlpha(140);
			mySimpleXYPlot.getLegendWidget().setBackgroundPaint(bgPaint);

			mySimpleXYPlot.getLegendWidget().setPadding(10, 10, 10, 10);
			mySimpleXYPlot.position(mySimpleXYPlot.getLegendWidget(), 20,
					XLayoutStyle.ABSOLUTE_FROM_RIGHT, 35,
					YLayoutStyle.ABSOLUTE_FROM_BOTTOM,
					AnchorPosition.RIGHT_BOTTOM);
			mySimpleXYPlot.getGraphWidget().setRangeLabelMargin(-1);
			mySimpleXYPlot.getGraphWidget().setRangeLabelWidth(25);
			mySimpleXYPlot.getGraphWidget().setDomainLabelWidth(10);
			mySimpleXYPlot.getGraphWidget().setDomainLabelMargin(-6);
			mySimpleXYPlot.getGraphWidget().setBackgroundPaint(null);
			mySimpleXYPlot.setBorderPaint(null);
			mySimpleXYPlot.getGraphWidget().getGridBackgroundPaint()
					.setColor(Color.WHITE);
			mySimpleXYPlot.getGraphWidget().setMarginTop(10);
			mySimpleXYPlot.getGraphWidget().setMarginRight(10);

			// GENERAL ----------------------------------------------
			// RANGE
			mySimpleXYPlot.setRangeLabel(saveApp.getCurrencySymbol());
			mySimpleXYPlot.setRangeStepValue(25);
			mySimpleXYPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 10);
			mySimpleXYPlot.setRangeValueFormat(new DecimalFormat("#"));
			// RANGE BOUNDARIES
			mySimpleXYPlot.setRangeBoundaries(
					(saveApp.getCurrentBudget() > 0 ? -20 : saveApp
							.getCurrentBudget()),
					saveApp.getBudget() + saveApp.getBudget() / 20,
					BoundaryMode.FIXED);
			mySimpleXYPlot.setTicksPerRangeLabel(5);
			// DOMAIN
			mySimpleXYPlot.setDomainLabel("Day");
			mySimpleXYPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
			mySimpleXYPlot.setDomainValueFormat(new DecimalFormat("#"));
			mySimpleXYPlot.getDomainLabelWidget().setVisible(true);
			// GENERAL
			mySimpleXYPlot.setTitle(saveApp.getAccountDesc());
			mySimpleXYPlot.setPlotPadding(20, 20, 20, 20);
			mySimpleXYPlot.getRangeLabelWidget().setVisible(true);
			mySimpleXYPlot.getTitleWidget().pack();
			mySimpleXYPlot.disableAllMarkup();

		}
	}
}