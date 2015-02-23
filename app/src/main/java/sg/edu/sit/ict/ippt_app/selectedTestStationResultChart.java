package sg.edu.sit.ict.ippt_app;

import java.util.Scanner;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import sg.edu.sit.ict.ippt_app.extras.yc_BL;
import sg.edu.sit.ict.ippt_app.database.myDbAdapter;

public class selectedTestStationResultChart extends Activity {

    LinearLayout layout;
    Context myContext = this;
    yc_BL ycBL;
    Spinner mySpin;
    SharedPreferences myPref;
    GraphicalView mChartView;
    int selectedIndex = 0;
    String[][] selectedTestStnResult;

    static final String msgStart = "You achieved ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stationresultchart);
        mySpin = (Spinner)findViewById(R.id.chartSpin);
        myPref = getSharedPreferences("ADMINNO_PREF",0);
        ycBL = new yc_BL(myContext);

        layout = (LinearLayout) findViewById(R.id.chart);

        mySpin.setOnItemSelectedListener(new OnItemSelectedListener(){

            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                // TODO Auto-generated method stub
                //yc codes
                String selectedStn = "";
                selectedIndex = mySpin.getSelectedItemPosition();
                boolean isGetFloat = false;
                String unitType ="";

                switch(selectedIndex)
                {
                    case 0:
                        selectedStn = myDbAdapter.SIT_UP;
                        break;
                    case 1:
                        selectedStn = myDbAdapter.SBJ;
                        unitType=" cm";
                        break;
                    case 2:
                        selectedStn = myDbAdapter.SNR;
                        unitType=" cm";
                        break;
                    case 3:
                        selectedStn = myDbAdapter.PULL_UP;
                        break;
                    case 4:
                        selectedStn = myDbAdapter.SHUTTLE_RUN;
                        isGetFloat = true;
                        unitType=" secs";
                        break;
                    default:
                        selectedStn = myDbAdapter.KM_Run;
                        break;
                }

                int rowsCount = ycBL.processRetrieveNoOfRows_SelectedTestStation(myPref.getString("ADMIN_NO", "096228B"));

                selectedTestStnResult= ycBL.processRetrieveSelectedTestStationNapfaResult(selectedStn, myPref.getString("ADMIN_NO", "096228B"),
                        rowsCount, isGetFloat, unitType);

                for(int rowNo=0;rowNo<selectedTestStnResult.length;rowNo++)
                {
                    for(byte colNo=0;colNo<selectedTestStnResult[rowNo].length;colNo++)
                    {
                        if(colNo == ((selectedTestStnResult[rowNo].length)-1))
                        {
                            if(selectedIndex == 5)
                            {
                                Scanner sc = new Scanner(selectedTestStnResult[rowNo][colNo]);
                                sc.useDelimiter(":");
                                //mins + secs
                                int secs = ( (sc.nextInt()) * 60) + sc.nextInt();
                                selectedTestStnResult[rowNo][colNo] = "" + secs;
                                sc.close();
                            }
                            else if (selectedIndex == 4)
                            {
                                selectedTestStnResult[rowNo][colNo] = selectedTestStnResult[rowNo][colNo].replace(" secs", "");
                            }
                            else if ((selectedIndex > 0) && (selectedIndex <=2))
                            {
                                selectedTestStnResult[rowNo][colNo] = selectedTestStnResult[rowNo][colNo].replace(" cm", "");
                            }
                        }
                    }
                }

                XYSeries series = new XYSeries(mySpin.getSelectedItem().toString());

                //Row
                for(int rowNo=0;rowNo<selectedTestStnResult.length;rowNo++)
                {
                    //Add values(numbers) into the series
                    series.add(rowNo, Double.parseDouble(selectedTestStnResult[rowNo][1]));
                }

                XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

                mDataset.addSeries(series);
                XYSeriesRenderer renderer = new XYSeriesRenderer();
                renderer.setPointStyle(PointStyle.CIRCLE);
                renderer.setColor(Color.YELLOW);
                renderer.setFillPoints(true);

                XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

                mRenderer.setApplyBackgroundColor(true);
                mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
                mRenderer.setAxisTitleTextSize(16);
                mRenderer.setChartTitleTextSize(20);
                mRenderer.setLabelsTextSize(15);
                mRenderer.setLegendTextSize(15);
                mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
                mRenderer.setZoomButtonsVisible(true);
                mRenderer.setPointSize(10);

                mRenderer.addSeriesRenderer(renderer);

                for(int rowNo=0;rowNo<selectedTestStnResult.length;rowNo++)
                {
                    mRenderer.addXTextLabel(rowNo,selectedTestStnResult[rowNo][0]);
                    mRenderer.setXLabels(0);
                    mRenderer.setShowLegend(true);
                }
                mRenderer.setChartTitle(mySpin.getSelectedItem().toString() + " result chart ");

                layout.removeAllViews();

                mChartView = ChartFactory.getLineChartView(myContext, mDataset, mRenderer);
                mRenderer.setClickEnabled(true);
                mRenderer.setSelectableBuffer(100);
                mChartView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                        double[] xy = mChartView.toRealPoint(0);
                        if (seriesSelection == null) {
                            Toast.makeText(myContext, "No chart element was clicked", Toast.LENGTH_SHORT)
                                    .show();
                        } else {

                            String temp = "";
                            if(selectedIndex == 5)
                            {
                                temp = ycBL.convertToMinsAndSecs((int)seriesSelection.getValue());
                            }
                            else if (selectedIndex == 4)
                            {
                                temp = seriesSelection.getValue() +  " secs";
                            }
                            else if ((selectedIndex > 0) && (selectedIndex <=2))
                            {
                                temp = seriesSelection.getValue() + " cm";
                            }
                            else
                            {
                                temp = Integer.toString((int)seriesSelection.getValue());
                            }

                            Toast.makeText(myContext,msgStart + temp + " on " + selectedTestStnResult[(int)seriesSelection.getXValue()][0], Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT));
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
    }
}
