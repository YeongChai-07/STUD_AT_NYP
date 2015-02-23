package sg.edu.sit.ict.ippt_app;

import sg.edu.sit.ict.ippt_app.Entity.User;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import sg.edu.sit.ict.ippt_app.extras.yc_BL;

public class yc_napfaPerfMain extends Activity {
    Button insertDB;
    Button reviewNapfaResult;
    Button takeNapfa;
    yc_BL ycBL;
    Context myContext = this;
    SharedPreferences studSP;
    SharedPreferences.Editor studEditor;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.napfaperf_main);

        ycBL = new yc_BL(myContext);


        studSP = getSharedPreferences("ADMINNO_PREF", 0);
        studEditor = studSP.edit();
        studEditor.putString("ADMIN_NO", User.getAdminNo());
        studEditor.putInt("AGE", User.getAge());
        studEditor.commit();

        insertDB = (Button)findViewById(R.id.insertTestData);

        insertDB.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                ycBL.processInsertTestDataNapfaResult();

            }

        });

        takeNapfa = (Button)findViewById(R.id.btnTakeNapfa);
        takeNapfa.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startActivity(new Intent(myContext, yc_takeNapfaMain.class));
            }

        });

        reviewNapfaResult = (Button)findViewById(R.id.btnReviewNapfa);
        reviewNapfaResult.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                startActivity(new Intent(myContext, yc_showNapfaResult.class));
            }

        });
    }

}

