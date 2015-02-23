package sg.edu.sit.ict.ippt_app;

import java.util.Calendar;

import sg.edu.sit.ict.ippt_app.Entity.User;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sg.edu.sit.ict.ippt_app.database.myDbAdapter;
import sg.edu.sit.ict.ippt_app.extras.yc_BL;

public class yc_takeNapfaMain extends Activity {

    ImageView ivSitUp = null;
    ImageView ivSBJ = null;
    ImageView ivSnR = null;
    ImageView ivPullUp = null;
    ImageView ivShuttle = null;
    ImageView iv2pt4 = null;

    TextView tv1 = null;

    byte sitUp = -1;
    int sbj = -1;
    byte sNR = -1;
    byte pullUp = -1;
    float shuttle = -1.0f;
    int longDistRun = -1;

    myDbAdapter myDB = null;
    yc_BL ycBL;
    Context myContext = this;
    Button submitResultBtn;

    SharedPreferences napfaSP;
    SharedPreferences studSP;
    SharedPreferences.Editor napfaEditor;
    SharedPreferences.Editor studEditor;

    Calendar myCal;

    String [] intentExtrasStr = {"SITUP_NO","SBJ_NO","SNR_NO", "PULLUP_NO", "SHUTTLE_NO", "2pt4_NO"};
    String adminNo = "";
    byte age = 0;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.yc_mainmenu);

        napfaSP = getSharedPreferences("NAPFA_RESULT_DRAFT", 0);

        studSP = getSharedPreferences("ADMINNO_PREF", 0);

        adminNo = studSP.getString("ADMIN_NO", User.getAdminNo());
        age = (byte)(studSP.getInt("AGE", User.getAge()));

        napfaEditor = napfaSP.edit();

        ivSitUp = (ImageView)findViewById(R.id.ivSitUp);
        ivSBJ = (ImageView)findViewById(R.id.ivSbj);
        ivSnR = (ImageView)findViewById(R.id.ivSnR);
        ivPullUp = (ImageView)findViewById(R.id.ivPullUp);
        ivShuttle = (ImageView)findViewById(R.id.ivSR);
        iv2pt4 = (ImageView) findViewById(R.id.iv2pt4);
        submitResultBtn = (Button)findViewById(R.id.takeNapfaSubmitResult);

        myCal = Calendar.getInstance();

        ycBL = new yc_BL(myContext);
        myDB = new myDbAdapter(myContext);

        boolean existsTodayNapfaRecord = ycBL.processCheckIfTestDateExists(getTodayDate(), adminNo);

        if(existsTodayNapfaRecord)
        {
            new AlertDialog.Builder(myContext)
                    .setTitle("Napfa test already taken today")
                    .setMessage("Our system shown that you taken your napfa test today.\nPlease try again on another day.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener(){

                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            napfaEditor.clear();
                            finish();
                        }

                    }).show();
        }

        if(napfaSP.contains("adminNo"))
        {
            sitUp = (byte)napfaSP.getInt("sitUp", 0);
            sbj = napfaSP.getInt("bJump", 0);
            sNR = (byte)napfaSP.getInt("sitReach", 0);
            pullUp = (byte)napfaSP.getInt("pullUp", 0);
            shuttle = napfaSP.getFloat("shuttleRun", 0.0f);
            longDistRun = napfaSP.getInt("2pt4", 0);
        }
        else
        {
            new AlertDialog.Builder(myContext)
                    .setTitle("IMPORTANT NOTICE")
                    .setMessage("In the event that you had entered invalid input on any of the test station, the respective station will have" +
                            " a red background over it when you attempt to submit your result.")
                    .setPositiveButton("Ok, I understood...", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    }).show();
        }

        ivSitUp.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivityForResult(new Intent(myContext, sitUpActivity.class), 401);
                //myDB.open();
                //myDB.close();
            }

        });

        ivSBJ.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivityForResult(new Intent(myContext, toStandingBoardJump.class), 402);
            }

        });

        ivSnR.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivityForResult(new Intent(myContext, sitNReachActivity.class), 403);
            }

        });

        ivPullUp.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivityForResult(new Intent(myContext, pullUpActivity.class), 404);
            }

        });

        ivShuttle.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivityForResult(new Intent(myContext, ShuttleRunActivity.class), 405);
            }

        });

        iv2pt4.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivityForResult(new Intent(myContext, longDistanceRun.class), 406);
            }

        });

        submitResultBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                //int month = myCal.get(Calendar.MONTH)+1;
                //Toast.makeText(myContext,"" + myCal.get(Calendar.DATE) + "/"+ month + "/" +myCal.get(Calendar.YEAR), Toast.LENGTH_SHORT).show();
                checkDraftExists();

                if (sitUp > -1 && sbj > -1 && sNR > -1 && pullUp > -1 && shuttle > -1.0f && longDistRun > -1)
                {
                    new AlertDialog.Builder(myContext)
                            .setTitle("Confirm submit napfa test result?")
                            .setMessage("Are you that you wish to permanently submit the napfa test result?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener(){

                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                    //process insert to DB
                                    long insertCode = ycBL.processInsertNapfaResult(adminNo, age, getTodayDate(), sitUp, sbj, sNR, pullUp, shuttle, longDistRun);
                                    //long insertCode = ycBL.processInsertNapfaResult(adminNo, age, getTodayDate(), (byte)37, 217,(byte)37,(byte)5, 10.7f, 743);

                                    if(! (insertCode == -1) )
                                    {
                                        Toast.makeText(myContext, "Napfa Test result inserted successfully! ", Toast.LENGTH_SHORT).show();
                                    }

                                    napfaEditor.clear();
                                    napfaEditor.commit();

                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener(){

                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    //Save to sharedPreferences
                                    napfaEditor.clear();

                                    napfaEditor.putString("adminNo", adminNo);
                                    napfaEditor.putString("testDate", getTodayDate());
                                    napfaEditor.putInt("sitUp", sitUp);
                                    napfaEditor.putInt("bJump", sbj);
                                    napfaEditor.putInt("sitReach", sNR);
                                    napfaEditor.putInt("pullUp", pullUp);
                                    napfaEditor.putFloat("shuttleRun", shuttle);
                                    napfaEditor.putInt("2pt4", longDistRun);

                                    napfaEditor.commit();

                                    resetIVBGColor();

                                    Toast.makeText(myContext, "Result successfully saved as draft", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }

                            }).show();

                }
            } //end onClick

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 401)
        {
            if (resultCode == RESULT_OK)
            {
                sitUp = (byte)processActivityResult(requestCode, resultCode, data);

            }
        }

        else if (requestCode == 402)
        {
            if (resultCode == RESULT_OK)
            {
                sbj = (int)processActivityResult(requestCode, resultCode, data);
            }
        }

        else if (requestCode == 403)
        {
            if(resultCode == RESULT_OK)
            {
                sNR = (byte)processActivityResult(requestCode, resultCode, data);
            }
        }

        else if(requestCode == 404)
        {
            if(resultCode == RESULT_OK)
            {
                pullUp = (byte)processActivityResult(requestCode, resultCode, data);
            }
        }

        else if(requestCode == 405)
        {
            if(resultCode == RESULT_OK)
            {
                shuttle = processActivityResult(requestCode, resultCode, data);
            }
        }
        else if(requestCode == 406)
        {
            if(resultCode == RESULT_OK)
            {
                longDistRun = (int)processActivityResult(requestCode, resultCode, data);
            }
        }
    }

    float processActivityResult(int requestCode, int resultCode, Intent data)
    {
        byte remain = (byte)(requestCode % 400);

        float temp  = -1.0f;

        if(data.hasExtra(intentExtrasStr[remain -1]))
        {
            if(remain == 1 || ( (remain >=3) && (remain < 5) ) )
            {
                temp = (float) data.getByteExtra(intentExtrasStr[remain -1], (byte)0);
            }
            else if(remain == 5)
            {
                temp = data.getFloatExtra(intentExtrasStr[remain-1], 0.0f);
            }
            else
            {
                temp = (float)data.getIntExtra(intentExtrasStr[remain-1], 0);
            }
        }
        return temp;
    }

    String getTodayDate()
    {
        byte month = (byte) (myCal.get(Calendar.MONTH)+1);

        return myCal.get(Calendar.DATE) + "/"+ month + "/" +myCal.get(Calendar.YEAR);
    }

    void checkDraftExists()
    {
        napfaSP = getSharedPreferences("NAPFA_RESULT_DRAFT", 0);

        if(! (napfaSP.contains("adminNo")) )
        {
            if(sitUp == -1)
            {
                ivSitUp.setBackgroundColor(Color.RED);
            }
            else
            {
                ivSitUp.setBackgroundColor(Color.TRANSPARENT);
            }

            if(sbj == -1)
            {
                ivSBJ.setBackgroundColor(Color.RED);
            }
            else
            {
                ivSBJ.setBackgroundColor(Color.TRANSPARENT);
            }

            if(sNR == -1)
            {
                ivSnR.setBackgroundColor(Color.RED);
            }
            else
            {
                ivSnR.setBackgroundColor(Color.TRANSPARENT);
            }

            if(pullUp == -1)
            {
                ivPullUp.setBackgroundColor(Color.RED);
            }
            else
            {
                ivPullUp.setBackgroundColor(Color.TRANSPARENT);
            }

            if(shuttle == -1.0f)
            {
                ivShuttle.setBackgroundColor(Color.RED);
            }
            else
            {
                ivShuttle.setBackgroundColor(Color.TRANSPARENT);
            }

            if(longDistRun == -1)
            {
                iv2pt4.setBackgroundColor(Color.RED);
            }
            else
            {
                iv2pt4.setBackgroundColor(Color.TRANSPARENT);
            }
        }
        else
        {
            resetIVBGColor();
        }

    }

    void resetIVBGColor()
    {

        ivSitUp.setBackgroundColor(Color.TRANSPARENT);
        ivSBJ.setBackgroundColor(Color.TRANSPARENT);
        ivSnR.setBackgroundColor(Color.TRANSPARENT);
        ivPullUp.setBackgroundColor(Color.TRANSPARENT);
        ivShuttle.setBackgroundColor(Color.TRANSPARENT);
        iv2pt4.setBackgroundColor(Color.TRANSPARENT);
    }

}

