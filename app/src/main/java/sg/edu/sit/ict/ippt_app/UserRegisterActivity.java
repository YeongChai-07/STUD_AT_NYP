package sg.edu.sit.ict.ippt_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import sg.edu.sit.ict.ippt_app.database.myDbAdapter;

public class UserRegisterActivity extends Activity {
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

        myAddRecordBtn = (Button)findViewById(R.id.btn_SignUp);
        addNameET = (EditText)findViewById(R.id.etRegister_UN);
        addPassET = (EditText)findViewById(R.id.etRegister_Pwd);
        addAgeET = (EditText)findViewById(R.id.etRegister_Age);
        addAdminET= (EditText)findViewById(R.id.etRegister_AdminNo);
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

                //Shows a Toast message indicating the registration was successful
                Toast.makeText(myContext,"THe registration was successful !! \nYou may proceed to login to the system.",Toast.LENGTH_SHORT).show();
            }
        });




        myBtn=(Button)findViewById(R.id.btn_SignIn);
        myIntent= new Intent(this, LoginProfile.class);
        myBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(myIntent);
            }



        });
    }
}
