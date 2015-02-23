package sg.edu.sit.ict.ippt_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class toStandingBoardJump extends Activity{
    Button sbjbtn1, sbjbtn2, cancelBtn;
    Button doneBtn;
    EditText sbjet1;
    int yc1=0, yc2;
    Context myContext = this;
    SharedPreferences napfaSP;
    static final String testStation = "standing board jump";

    public void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.standingbroadjump);

        sbjbtn1 = (Button) findViewById(R.id.sbjbtn1);
        sbjbtn2 = (Button) findViewById(R.id.sbjbtn2);
        sbjet1 = (EditText) findViewById(R.id.sbjet1);
        doneBtn = (Button)findViewById(R.id.sbjDone);
        cancelBtn = (Button)findViewById(R.id.sbjCancel);
        napfaSP = getSharedPreferences("NAPFA_RESULT_DRAFT", 0);

        if(napfaSP.contains("adminNo"))
        {
            sbjet1.setText(Integer.toString(napfaSP.getInt("bJump", 0)) );
        }

        sbjbtn1.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                try
                {
                    yc1 = Integer.parseInt(sbjet1.getText().toString());
                    if(yc1<300){
                        yc2 = yc1+1;
                        sbjet1.setText(yc2+"");
                    }else{
                        Toast.makeText(myContext, "It's impossible to jump over 300", Toast.LENGTH_LONG).show();
                    }
                }
                catch(NumberFormatException nfe)
                {
                    sbjet1.setText("0");
                    Toast.makeText(myContext, "You entered an invalid input. Reverting back to the default value.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        sbjbtn2.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                try
                {
                    yc1 = Integer.parseInt(sbjet1.getText().toString());
                    if(yc1>1){
                        yc2 = yc1-1;
                        sbjet1.setText(yc2+"");
                    }else{
                        Toast.makeText(myContext, "Hey, seriously. Try harder!", Toast.LENGTH_LONG).show();
                    }
                }
                catch(NumberFormatException nfe)
                {
                    sbjet1.setText("0");
                    Toast.makeText(myContext,  "You entered an invalid input. Reverting back to the default value.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        doneBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                //Needs Validation code
                try
                {
                    int sbj = Integer.parseInt(sbjet1.getText().toString());
                    setResult(RESULT_OK,new Intent()
                            .putExtra("SBJ_NO", sbj));

                    finish();
                }
                catch(NumberFormatException nfe)
                {
                    sbjet1.setText("0");
                    Toast.makeText(myContext,  "You entered an invalid input. Reverting back to the default value.",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

        cancelBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {
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

