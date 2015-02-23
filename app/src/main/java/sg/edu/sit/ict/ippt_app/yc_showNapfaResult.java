package sg.edu.sit.ict.ippt_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import sg.edu.sit.ict.ippt_app.extras.yc_BL;

public class yc_showNapfaResult extends Activity {
    TableLayout tl = null;

    Context myContext = this;
    yc_BL ycBL;

    String [] headerText = {"Date", "Sit Ups", "SBJ", "Sit & Reach", "Pull Ups", "Shuttle Run", "2.4 KM Run", "Show Details"};
    String[][] selectedTestStnResult;


    byte[][] napfaResultPts;

    boolean isGetFloat;
    boolean isShowBestResult = false;

    int selectedTestStnIndex = 0;

    Button showBestResult = null;
    Button showChart=null;

    SharedPreferences myShare;
    SharedPreferences.Editor myEditSharedPref;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yc_review_napfa_history_result1_layout);

        ycBL = new yc_BL(myContext);

        showBestResult = (Button)findViewById(R.id.btnGoToHistory2);
        showChart=(Button)findViewById(R.id.showChartBtn);

        myShare = getSharedPreferences("ADMINNO_PREF", 0);
        showAllTestStationsResult();

		/*myEditSharedPref = myShare.edit();
		myEditSharedPref.putString("ADMIN_NO", "096228B");
		myEditSharedPref.commit();*/

        showBestResult.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                startActivityForResult(new Intent(myContext, yc_showBestResult_main.class),100);
            }

        });

        showChart.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(myContext,selectedTestStationResultChart.class));
            }

        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        tl = (TableLayout)findViewById(R.id.tlNapfaReview);

        if(requestCode == 100)
        {
            if(resultCode == RESULT_OK)
            {
                isShowBestResult = true;

                String selectedTestStn = data.getStringExtra("TEST_STN");
                String orderType = data.getStringExtra("ORDER_TYPE");
                int showNoBestResults = data.getIntExtra("showNoBestResults", 0);
                isGetFloat = data.getBooleanExtra("IS_GETFLOAT", false);
                String unitType = data.getStringExtra("UNIT_TYPE");
                selectedTestStnIndex = data.getIntExtra("selectedTestStn", 0);

                myShare = getSharedPreferences("ADMINNO_PREF", 0);

                ycBL = new yc_BL(myContext);
                selectedTestStnResult = ycBL.processArrangeSelectedTestStationOrder(selectedTestStn, myShare.getString("ADMIN_NO", "096228B"), orderType,
                        showNoBestResults, isGetFloat, unitType);
                tl.removeAllViews();

                TableRow headRow = new TableRow(myContext);

                TextView dateHead = new TextView(myContext);
                TextView testStnHead = new TextView(myContext);
                TextView showDetailsHead = new TextView(myContext);

                dateHead.setText("Date");
                dateHead.setWidth(80);
                dateHead.setTextColor(Color.BLACK);

                testStnHead.setText(getResources().getStringArray(R.array.napfa)[selectedTestStnIndex]);
                testStnHead.setWidth(80);
                testStnHead.setTextColor(Color.BLACK);

                showDetailsHead.setText("Show Details");
                showDetailsHead.setWidth(80);
                showDetailsHead.setTextColor(Color.BLACK);

                dateHead.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                testStnHead.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                showDetailsHead.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                headRow.addView(dateHead);
                headRow.addView(testStnHead);
                headRow.addView(showDetailsHead);

                tl.addView(headRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

                //Row
                for(int rowNo=0;rowNo<selectedTestStnResult.length;rowNo++)
                {
                    TableRow myNewRow = new TableRow(myContext);
                    RadioGroup myRG = new RadioGroup(myContext);
                    myRG.setOrientation(RadioGroup.HORIZONTAL);

                    //Column
                    for(byte colNo=0;colNo<selectedTestStnResult[rowNo].length;colNo++)
                    {
                        TextView myTV = new TextView(myContext);

                        myTV.setText(selectedTestStnResult[rowNo][colNo]);
                        myTV.setTextColor(Color.BLACK);

                        myTV.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT));

                        myNewRow.addView(myTV);

                    }//end inner for-loop

                    RadioButton rb = new RadioButton(myContext);
                    rb.setId(rowNo);
                    rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                        public void onCheckedChanged(CompoundButton arg0,
                                                     boolean arg1) {
                            // TODO Auto-generated method stub
                            //int currId = arg0.getId();

                            if(arg1)
                            {
                                //Toast.makeText(myContext, "RadioButton " + arg0.getId() + " Checked!!!", Toast.LENGTH_SHORT).show();

                                //Intent showNapfaTarget = new Intent(myContext, yc_promoteNapfaTarget.class);

                                String upGradeMsg = "";

                                upGradeMsg = ycBL.processResultAchievement_Status(myShare.getInt("AGE", 22), selectedTestStnIndex,
                                        selectedTestStnResult[arg0.getId()][1] );


                                TextView suggestMsgTV = (TextView)findViewById(R.id.napfaSuggest);
                                suggestMsgTV.setText(upGradeMsg);
                                suggestMsgTV.setTextColor(Color.DKGRAY);
                                suggestMsgTV.setVisibility(View.VISIBLE);
                            }

                            arg0.setChecked(false);
                        }

                    });

                    myRG.addView(rb);

                    myNewRow.addView(myRG);

                    //tl.addView(tempRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

					/*Button myBtn = new Button(myContext);
					myBtn.setId(rowNo);
					myBtn.setText("Show improvements");
					myBtn.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

					myNewRow.addView(myBtn);*/
                    tl.addView(myNewRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));



					/*TableRow tempRow = new TableRow(myContext);

					Button myBtn = new Button(myContext);
					myBtn.setId(rowNo);
					myBtn.setText("Show improvements");
					myBtn.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

					tempRow.addView(myBtn);*/

                    //tl.addView(tempRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));


                }//end row for-loop*/
            }
        } // end requestCode if
    } // end onActivityResult

    public void onBackPressed()
    {
        if(isShowBestResult)
        {
            showAllTestStationsResult();
            isShowBestResult = false;
        }
        else
        {
            finish();
        }
    }

    private void showAllTestStationsResult()
    {

        tl = (TableLayout)findViewById(R.id.tlNapfaReview);

        tl.removeAllViews();

        TextView temp = (TextView)findViewById(R.id.napfaSuggest);
        temp.setVisibility(View.INVISIBLE);

        TableRow headerRow = new TableRow(myContext);

        String[][] napfaResult = ycBL.processRetrieveNapfaResult(myShare.getString("ADMIN_NO", "096228B"));

        if(napfaResult.length > 0)
        {
            for(byte i=0;i<headerText.length; i++)
            {
                TextView headTV = new TextView(myContext);
                headTV.setText(headerText[i]);
                headTV.setTextColor(Color.BLACK);

                headTV.setWidth(80);
                headerRow.addView(headTV);
            }
            tl.addView(headerRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));


            //Row
            for(int rowNo=0;rowNo<napfaResult.length;rowNo++)
            {
                TableRow myNewRow = new TableRow(myContext);
                //Column
                for(byte colNo=0;colNo<napfaResult[rowNo].length;colNo++)
                {
                    TextView myTV = new TextView(myContext);

                    myTV.setText(napfaResult[rowNo][colNo]);
                    myTV.setTextColor(Color.BLACK);

                    myTV.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));

                    myNewRow.addView(myTV);

                }//end inner for-loop

                RadioButton showDetails = new RadioButton(myContext);
                showDetails.setId(rowNo);
                showDetails.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked)
                        {
                            napfaResultPts = ycBL.processRetrieveTestStnPts(myShare.getString("ADMIN_NO", "096228B"), (byte)myShare.getInt("AGE", 22));
                            String temp = "";
                            String[] testStnArr = getResources().getStringArray(R.array.napfa);
                            byte totalPts = 0;
                            int currRow = buttonView.getId();

                            for(int colNo = 0;colNo < napfaResultPts[currRow].length;colNo++)
                            {
                                if (colNo > 0)
                                {
                                    temp +="\n" + testStnArr[colNo] + ": " + napfaResultPts[currRow][colNo] + " points.";
                                }
                                else
                                {
                                    temp = testStnArr[colNo] + ": " +  napfaResultPts[currRow][colNo] + " points.";
                                }

                                totalPts += napfaResultPts[currRow][colNo];

                            }//end for loop
                            temp += "\n\n" + "Total: " + totalPts + " points.";

                            new AlertDialog.Builder(myContext)
                                    .setTitle("Set Test Result Details")
                                    .setMessage(temp).show();
                        }//end if
                        buttonView.setChecked(false);
                    }

                });

                myNewRow.addView(showDetails);
                tl.addView(myNewRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
            }

        } //end if
        else
        {
            new AlertDialog.Builder(myContext)
                    .setTitle("No Napfa Test Result found!")
                    .setMessage("It seems that our system doesn't have your napfa test result history.\nWould you like " +
                            "to take napfa test now ?" )
                    .setPositiveButton("Yes, please", new DialogInterface.OnClickListener(){

                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            startActivity(new Intent(myContext, yc_takeNapfaMain.class));
                            finish();
                        }

                    })
                    .setNegativeButton("No, return to napfa main menu", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            startActivity(new Intent(myContext, yc_napfaPerfMain.class));
                            finish();
                        }

                    })
                    .show();

        } //end else


    }

}//end of class

