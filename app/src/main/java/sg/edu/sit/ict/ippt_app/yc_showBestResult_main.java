package sg.edu.sit.ict.ippt_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import sg.edu.sit.ict.ippt_app.database.myDbAdapter;
import sg.edu.sit.ict.ippt_app.extras.yc_BL;

public class yc_showBestResult_main extends Activity {
    Context myContext = this;
    yc_BL ycBL;
    SharedPreferences myPref;
    Spinner rowsSpin;
    Spinner testSpin;
    ArrayAdapter<CharSequence> totalNoResultsAdapter;
    Button okBtn = null;
    Button cancelBtn = null;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yc_showbestresults_main);

        ycBL = new yc_BL(myContext);

        myPref = getSharedPreferences("ADMINNO_PREF",0);

        rowsSpin = (Spinner)findViewById(R.id.spinLimitNo);
        testSpin = (Spinner)findViewById(R.id.spinBestTestStations);

        int rowsCount = ycBL.processRetrieveNoOfRows_SelectedTestStation(myPref.getString("ADMIN_NO", "096228B"));

        String [] temp = new String[rowsCount];

        for(int i=0;i<rowsCount;i++)
        {
            temp[i] = "" + i;
        }

        totalNoResultsAdapter = new ArrayAdapter<CharSequence>(myContext,android.R.layout.simple_spinner_dropdown_item,temp);
        totalNoResultsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        rowsSpin.setAdapter(totalNoResultsAdapter);

        okBtn = (Button)findViewById(R.id.limitResultOK);
        okBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                String selectedStn = "";
                int selectedIndex = testSpin.getSelectedItemPosition();
                String orderType = "";
                boolean isGetFloat = false;
                String unitType ="";

                switch(selectedIndex)
                {
                    case 0:
                        selectedStn = myDbAdapter.SIT_UP;
                        orderType="DESC";
                        break;
                    case 1:
                        selectedStn = myDbAdapter.SBJ;
                        orderType="DESC";
                        unitType=" cm";
                        break;
                    case 2:
                        selectedStn = myDbAdapter.SNR;
                        orderType="DESC";
                        unitType=" cm";
                        break;
                    case 3:
                        selectedStn = myDbAdapter.PULL_UP;
                        orderType="DESC";
                        break;
                    case 4:
                        selectedStn = myDbAdapter.SHUTTLE_RUN;
                        orderType="ASC";
                        isGetFloat = true;
                        unitType=" secs";
                        break;
                    default:
                        selectedStn = myDbAdapter.KM_Run;
                        orderType="ASC";
                        break;
                }

                Intent data = new Intent();
                data.putExtra("TEST_STN", selectedStn);
                data.putExtra("showNoBestResults", Integer.parseInt(rowsSpin.getSelectedItem().toString()) );
                data.putExtra("ORDER_TYPE", orderType);
                data.putExtra("IS_GETFLOAT", isGetFloat);
                data.putExtra("UNIT_TYPE", unitType);
                data.putExtra("selectedTestStn", selectedIndex);

                setResult(RESULT_OK, data);
                finish();
            }

        });

        cancelBtn = (Button)findViewById(R.id.limitResultCancel);
        cancelBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                setResult(RESULT_CANCELED);
                finish();
            }

        });

    }

}
