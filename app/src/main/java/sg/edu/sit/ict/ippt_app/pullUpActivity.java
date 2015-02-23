package sg.edu.sit.ict.ippt_app;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.AlertDialog;


public class pullUpActivity extends Activity {
    Button doneBtn;
    Button cancelBtn;
    Button timerBtn;
    Button plusBtn;
    Button minusBtn;
    EditText pullUpET;
    TextView myTV;
    Context myContext = this;
    byte noOfPullUp = 0;
    boolean countDownStop = true;
    boolean isRunOnce = false;
    byte oldPullUpReps = 0;

    SharedPreferences napfaSP;
    static final String testStation = "pull up";
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pullup);

        myTV = (TextView)findViewById(R.id.tvPURemainTime);
        doneBtn = (Button)findViewById(R.id.chinUpDone);
        cancelBtn = (Button)findViewById(R.id.chinUpCancel);
        pullUpET = (EditText)findViewById(R.id.chinUpET);
        timerBtn = (Button)findViewById(R.id.puCountDownBtn);
        plusBtn = (Button)findViewById(R.id.pullUpPlus);
        minusBtn = (Button)findViewById(R.id.pullUpMinus);

        napfaSP = getSharedPreferences("NAPFA_RESULT_DRAFT", 0);

        if(napfaSP.contains("adminNo"))
        {
            pullUpET.setText(Integer.toString(napfaSP.getInt("pullUp", 0)) );
        }
        else
        {
            applyCountDownConfig();
        }

        timerBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                applyCountDownConfig();
                new CountDownTimer(90000,1000)
                {

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        applyCountDownConfig();
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {
                        // TODO Auto-generated method stub
                        myTV.setText(millisUntilFinished/1000 + " secs remaining");
                    }

                }
                        .start();



            }

        });

        plusBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try
                {
                    int noPullUp = Integer.parseInt(pullUpET.getText().toString());
                    if(noPullUp < 50)
                    {
                        noPullUp+=1;
                    }
                    pullUpET.setText("" + noPullUp);
                }
                catch(NumberFormatException nfe)
                {
                    pullUpET.setText("0");
                    Toast.makeText(myContext,  "You entered an invalid input. Reverting back to the default value.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        minusBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try
                {
                    int noPullUp = Integer.parseInt(pullUpET.getText().toString());
                    if(noPullUp >=1)
                    {
                        noPullUp-=1;
                    }
                    pullUpET.setText("" + noPullUp);
                }
                catch(NumberFormatException nfe)
                {
                    pullUpET.setText("0");
                    Toast.makeText(myContext,  "You entered an invalid input. Reverting back to the default value.", Toast.LENGTH_SHORT).show();
                }

            }

        });

        doneBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                try{
                    noOfPullUp = (byte) Integer.parseInt(pullUpET.getText().toString());
                    setResult(RESULT_OK, new Intent()
                            .putExtra("PULLUP_NO", noOfPullUp));

                    finish();
                }
                catch(NumberFormatException nfe)
                {
                    pullUpET.setText("0");
                    Toast.makeText(myContext,  "You entered an invalid input. Reverting back to the default value.", Toast.LENGTH_SHORT).show();
                }

            }

        });

        cancelBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
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



            }

        });
    }

    void applyCountDownConfig()
    {
        if(countDownStop)
        {
            myTV.setText("90 secs remaining");
            pullUpET.setEnabled(false);
            doneBtn.setEnabled(false);
            plusBtn.setEnabled(false);
            minusBtn.setEnabled(false);
            if(! isRunOnce)
            {
                timerBtn.setEnabled(true);
                isRunOnce = true;
            }
            else
            {
                timerBtn.setEnabled(false);
                countDownStop = false;
            }
        }
        else
        {
            myTV.setText("0 secs remaining");
            pullUpET.setEnabled(true);
            doneBtn.setEnabled(true);
            timerBtn.setEnabled(true);
            plusBtn.setEnabled(true);
            minusBtn.setEnabled(true);
            countDownStop = true;
        }
    }

}
