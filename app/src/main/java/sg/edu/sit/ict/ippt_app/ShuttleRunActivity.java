package sg.edu.sit.ict.ippt_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import sg.edu.sit.ict.ippt_app.extras.Chronometer;

public class ShuttleRunActivity extends Activity {
    Chronometer myStopWatch;
    Button startBtn;
    Button doneBtn;
    Button cancelBtn;
    boolean isFirstRun = false;
    boolean isTimeTakenOnce = false;
    Context myContext = this;
    SharedPreferences napfaSP;
    static final String testStation = "shuttle run";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shutterun);

        myStopWatch = (Chronometer)findViewById(R.id.shuttleRunChronometer);

        startBtn = (Button)findViewById(R.id.button3);
        doneBtn = (Button)findViewById(R.id.btn_SignIn);
        cancelBtn = (Button)findViewById(R.id.btn_SignUp);

        napfaSP = getSharedPreferences("NAPFA_RESULT_DRAFT", 0);

        if(napfaSP.contains("adminNo"))
        {
            myStopWatch.setText(Float.toString(napfaSP.getFloat("shuttleRun", 0.0f)) );
        }
        else
        {
            doneBtn.setEnabled(isFirstRun);
        }

        startBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(isTimeTakenOnce)
                {
                    new AlertDialog.Builder(myContext)
                            .setTitle("Reset the timer ?")
                            .setMessage("Are you sure you want to retake the timing for " + testStation + "?\nThis will clear previous taken timing!")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener(){

                                public void onClick(DialogInterface dialog, int which)
                                {
                                    //startBtn.setEnabled(true);
                                    myStopWatch.setText("00.0");
                                    //status = false;
                                    dialog.dismiss();
                                    isFirstRun = false;
                                    isTimeTakenOnce = false;
                                    doneBtn.setEnabled(false);

                                    if(!isFirstRun){
                                        myStopWatch.start();
                                        startBtn.setText("Stop Timer");
                                        isFirstRun = true;
                                    }
                                }

                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                    //this.onClick(v);
                }

                applyStopWatchState();
            }

        });

        doneBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                float secs = Float.parseFloat(myStopWatch.getText().toString());
                secs =  Math.round(secs*100)/100.0f;

                setResult(RESULT_OK, new Intent()
                        .putExtra("SHUTTLE_NO", secs));

                finish();
            }

        });

        cancelBtn.setOnClickListener(new OnClickListener(){

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
    }//end onCreate

    void applyStopWatchState()
    {
        if(!isFirstRun){
            myStopWatch.start();
            startBtn.setText("Stop Timer");
            isFirstRun = true;
        }
        else {
            myStopWatch.stop();
            startBtn.setText("Start Timer");
            isTimeTakenOnce = true;
            doneBtn.setEnabled(true);

        }
    }
}
