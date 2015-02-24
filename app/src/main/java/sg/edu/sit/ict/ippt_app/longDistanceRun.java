package sg.edu.sit.ict.ippt_app;

import java.util.Scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import sg.edu.sit.ict.ippt_app.extras.yc_BL;


public class longDistanceRun extends Activity {
    Button doneBtn;
    Button cancelBtn;
    Button timerBtn;
    //Chronometer myStopWatch;
    EditText runStop_Mins;
    EditText runStop_Secs;
    Context myContext = this;
    boolean isFirstRun = false;
    boolean isTimeTakenOnce = false;
    SharedPreferences napfaSP;
    yc_BL ycBL;
    static final String testStation = "2.4 KM";
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.longdistancerun_testing);

        //myStopWatch = (Chronometer)findViewById(R.id.longDistanceChronometer);
        runStop_Mins = (EditText)findViewById(R.id.et2pt4_Mins);
        runStop_Secs = (EditText)findViewById(R.id.et2pt4_Secs);
        doneBtn = (Button)findViewById(R.id.longDistanceDone);
        cancelBtn = (Button)findViewById(R.id.longDistanceCancel);
        timerBtn = (Button)findViewById(R.id.longDistanceStartTimer);

        ycBL = new yc_BL(myContext);

        napfaSP = getSharedPreferences("NAPFA_RESULT_DRAFT", 0);

        if(napfaSP.contains("adminNo"))
        {
            String minsAndSecs = ycBL.convertToMinsAndSecs(napfaSP.getInt("2pt4", 0));

            Scanner sc = new Scanner(minsAndSecs);
            sc.useDelimiter(":");
            //mins + secs
            runStop_Mins.setText( (sc.nextInt() / 60 ) );
            runStop_Secs.setText((sc.nextInt()) % 60) ;
            sc.close();
            //myStopWatch.setText(ycBL.convertToMinsAndSecs(napfaSP.getInt("2pt4", 0)));
        }
        /*else
        {
            doneBtn.setEnabled(isFirstRun);
        }*/

        timerBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(isTimeTakenOnce)
                {
                    new AlertDialog.Builder(myContext)
                            .setTitle("Retake the timing ?")
                            .setMessage("Are you sure you want to retake the timing for " + testStation + "?\nThis will clear previous taken timing!")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    /*
                                    //startBtn.setEnabled(true);
                                    myStopWatch.setText("00.0");
                                    //status = false;
                                    dialog.dismiss();
                                    isFirstRun = false;
                                    isTimeTakenOnce = false;
                                    doneBtn.setEnabled(false);

                                    if(!isFirstRun){
                                        myStopWatch.setBase(SystemClock.elapsedRealtime());
                                        myStopWatch.start();
                                        timerBtn.setText("Stop Timer");
                                        isFirstRun = true;
                                    }*/
                                    setResult(RESULT_CANCELED);
                                    finish();
                                }

                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                    //this.onClick(v);
                }

                //applyStopWatchState();



            }

        });

        doneBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                int secs = 0;

                try {
                    secs = Integer.parseInt(runStop_Mins.getText().toString()) * 60;
                    //Toast.makeText(myContext, "Success!", Toast.LENGTH_LONG).show();
                }
                catch(NumberFormatException nfe)
                {
                    runStop_Mins.setText("0");
                    Toast.makeText(myContext, "You entered an invalid input for Mins. Reverting back to the default value.", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    secs += Integer.parseInt(runStop_Secs.getText().toString());
                    //Toast.makeText(myContext, "Success!", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK, new Intent()
                            .putExtra("2pt4_NO", secs));
                    finish();
                }
                catch(NumberFormatException nfe)
                {
                    runStop_Secs.setText("0");
                    Toast.makeText(myContext, "You entered an invalid input for Secs. Reverting back to the default value.", Toast.LENGTH_LONG).show();
                }


                /*Scanner sc = new Scanner(myStopWatch.getText().toString());
                sc.useDelimiter(":");
                //mins + secs
                secs = ( (sc.nextInt()) * 60) + sc.nextInt();
                sc.close();
                setResult(RESULT_OK, new Intent()
                        .putExtra("2pt4_NO", secs));

                finish();*/
            }

        });

        cancelBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
				/*if(!isTimeTakenOnce)
				{*/
                new AlertDialog.Builder(myContext)
                        .setTitle("Discard changes?")
                        .setMessage("Are you sure that you wish to discard the current changes for " + testStation + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener(){

                            public void onClick(DialogInterface dialog, int which)
                            {
                                setResult(RESULT_CANCELED);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener(){

                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        })
                        .show();
                //}



            }

        });

    }

    /*void applyStopWatchState()
    {
        if(!isFirstRun){
            myStopWatch.setBase(SystemClock.elapsedRealtime());
            myStopWatch.start();
            timerBtn.setText("Stop Timer");
            isFirstRun = true;
        }
        else {
            myStopWatch.stop();
            timerBtn.setText("Start Timer");
            isTimeTakenOnce = true;
            doneBtn.setEnabled(true);

        }
    }*/

}

