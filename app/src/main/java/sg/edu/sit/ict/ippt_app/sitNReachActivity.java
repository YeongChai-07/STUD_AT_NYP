package sg.edu.sit.ict.ippt_app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.AlertDialog;


public class sitNReachActivity extends Activity {
    Button doneBtn;
    Button cancelBtn;
    Button plusBtn;
    Button minusBtn;
    EditText sRET;
    Context myContext = this;
    int sRET1;
    SharedPreferences napfaSP;
    static final String testStation = "sit and reach";

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitandreach);

        doneBtn = (Button)findViewById(R.id.snrDone);
        cancelBtn = (Button)findViewById(R.id.snrCancel);
        sRET = (EditText)findViewById(R.id.snrET);
        plusBtn = (Button)findViewById(R.id.srPlus);
        minusBtn = (Button)findViewById(R.id.srMinus);
        napfaSP = getSharedPreferences("NAPFA_RESULT_DRAFT", 0);

        if(napfaSP.contains("adminNo"))
        {
            sRET.setText(Integer.toString(napfaSP.getInt("sitReach", 0)) );
        }

        plusBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                try
                {
                    int noSR = Integer.parseInt(sRET.getText().toString());
                    if(noSR < 80)
                    {
                        noSR+=1;
                    }
                    sRET.setText("" + noSR);
                }
                catch(NumberFormatException nfe)
                {
                    sRET.setText("0");
                    Toast.makeText(myContext,  "You entered an invalid input. Reverting back to the default value.", Toast.LENGTH_SHORT).show();
                }

            }

        });
        minusBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                try
                {
                    int noSR = Integer.parseInt(sRET.getText().toString());
                    if(noSR >= 1)
                    {
                        noSR-=1;
                    }
                    sRET.setText("" + noSR);
                }
                catch(NumberFormatException nfe)
                {
                    sRET.setText("0");
                    Toast.makeText(myContext,  "You entered an invalid input. Reverting back to the default value.", Toast.LENGTH_SHORT).show();
                }

            }

        });

        doneBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                //Do your fanciful validation code here

                try {
                    sRET1 = Integer.parseInt(sRET.getText().toString());
                    //Toast.makeText(myContext, "Success!", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK, new Intent()
                            .putExtra("SNR_NO", (byte)sRET1));
                    finish();
                }
                catch(NumberFormatException nfe)
                {
                    sRET.setText("0");
                    Toast.makeText(myContext, "You entered an invalid input. Reverting back to the default value.", Toast.LENGTH_LONG).show();
                }

            }

        });

        cancelBtn.setOnClickListener(new OnClickListener() {

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
}

