package sg.edu.sit.ict.ippt_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;


public class sitUpActivity extends Activity{
    Button doneBtn;
    Button plusBtn;
    Button minusBtn;
    Button cancelBtn;
    Button timerBtn;
    EditText myET;
    TextView myTV;
    Context myContext = this;
    boolean countDownStop = true;
    boolean isRunOnce = false;
    int myET1;
    SharedPreferences napfaSP;
    static final String testStation = "sit up";

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.situp_testing);

        myTV = (TextView)findViewById(R.id.tvSURemainTime);
        doneBtn = (Button)findViewById(R.id.suDone);
        cancelBtn = (Button)findViewById(R.id.suCancel);
        myET = (EditText)findViewById(R.id.suET);
        timerBtn = (Button)findViewById(R.id.suCountDownBtn);
        plusBtn = (Button)findViewById(R.id.sitUpPlus);
        minusBtn = (Button)findViewById(R.id.sitUpMinus);

        napfaSP = getSharedPreferences("NAPFA_RESULT_DRAFT", 0);

        if(napfaSP.contains("adminNo"))
        {
            myET.setText(Integer.toString(napfaSP.getInt("sitUp", 0)));
        }
        /*else
        {
            applyCountDownConfig();
        }

        timerBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                applyCountDownConfig();
                new CountDownTimer(60000,1000)
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

        });*/

        plusBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                try
                {
                    int noSitUp = Integer.parseInt(myET.getText().toString());
                    if(noSitUp < 60)
                    {
                        noSitUp+=1;
                    }
                    myET.setText("" + noSitUp);
                }
                catch(NumberFormatException nfe)
                {
                    myET.setText("0");
                    Toast.makeText(myContext,  "You entered an invalid input. Reverting back to the default value.", Toast.LENGTH_SHORT).show();
                }

            }

        });
        minusBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                try
                {
                    int noSitUp = Integer.parseInt(myET.getText().toString());
                    if(noSitUp >=1)
                    {
                        noSitUp-=1;
                    }
                    myET.setText("" + noSitUp);
                }
                catch(NumberFormatException nfe)
                {
                    myET.setText("0");
                    Toast.makeText(myContext,  "You entered an invalid input. Reverting back to the default value.", Toast.LENGTH_SHORT).show();
                }

            }

        });

        doneBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                try
                {
                    myET1 = Integer.parseInt(myET.getText().toString());
                    //Toast.makeText(myContext, "Success!", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK,new Intent()
                            .putExtra("SITUP_NO", (byte)myET1));
                    finish();
                }
                //Do your fanciful validation code here
                catch(NumberFormatException nfe)
                {
                    myET.setText("0");
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

    /*void applyCountDownConfig()
    {
        if(countDownStop)
        {
            myTV.setText("60 secs remaining");
            myET.setEnabled(false);
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
            myET.setEnabled(true);
            doneBtn.setEnabled(true);
            timerBtn.setEnabled(true);
            plusBtn.setEnabled(true);
            minusBtn.setEnabled(true);
            countDownStop = true;
        }
    }*/

}
