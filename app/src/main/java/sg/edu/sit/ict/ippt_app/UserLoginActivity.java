package sg.edu.sit.ict.ippt_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import sg.edu.sit.ict.ippt_app.database.myDbAdapter;

public class UserLoginActivity extends Activity {
    /** Called when the activity is first created. */
    Button myBtn;
    Intent myIntent;
    myDbAdapter myDB;
    Context myContext;
    Button myAddRecordBtn;
    EditText addNameET;
    EditText addPassET;
    EditText addAgeET;
    EditText addAdminET;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        myContext = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaccountpage);

        myAddRecordBtn = (Button)findViewById(R.id.button2);
        addNameET = (EditText)findViewById(R.id.editText1);
        addPassET = (EditText)findViewById(R.id.editText2);
        addAgeET = (EditText)findViewById(R.id.enterAge);
        addAdminET= (EditText)findViewById(R.id.adminNo);
        myDB = new myDbAdapter(myContext);
        myDB.open();
        myAddRecordBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                String addNameStr = addNameET.getText().toString();
                String addPassStr = addPassET.getText().toString();

                String addAdminStr = addAdminET.getText().toString();
                String addAgeInt = addAgeET.getText().toString();
                myDB.insertEntry(addNameStr, addPassStr, addAdminStr, Integer.parseInt(addAgeInt));
            }
        });




        myBtn=(Button)findViewById(R.id.button1);
        myIntent= new Intent(this, LoginProfile.class);
        myBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(myIntent);
            }



        });
    }
}
