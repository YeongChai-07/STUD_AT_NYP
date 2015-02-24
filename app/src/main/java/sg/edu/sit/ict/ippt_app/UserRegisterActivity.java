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

    static final String ADMIN_NO = "ADMIN_NUMBER";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        myContext = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaccountpage);

        myAddRecordBtn = (Button)findViewById(R.id.btn_SignUp);
        addNameET = (EditText)findViewById(R.id.etRegister_UN);
        addPassET = (EditText)findViewById(R.id.etRegister_Pwd);
        addAgeET = (EditText)findViewById(R.id.etRegister_Age);

        myDB = new myDbAdapter(myContext);
        myDB.open();
        myAddRecordBtn.setOnClickListener(new OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated method stub
                String addNameStr = addNameET.getText().toString();
                String addPassStr = addPassET.getText().toString();

                String str_Age = addAgeET.getText().toString();
                int addAgeInt = 0;

                if(addNameStr.length() == 0)
                {
                    Toast.makeText(myContext,"Please provide a user name for registration.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(addPassStr.length() == 0)
                {
                    Toast.makeText(myContext,"Please provide your password for registration.",Toast.LENGTH_SHORT).show();
                    return;
                }

                try
                {
                    addAgeInt = Integer.parseInt(str_Age);

                    //Checks the age whether is within range
                    if(addAgeInt >=19 && addAgeInt <=24) {
                        myDB.insertEntry(addNameStr, addPassStr, ADMIN_NO, addAgeInt);
                        //Shows a Toast message indicating the registration was successful
                        Toast.makeText(myContext, "The registration was successful !! \nYou may proceed to login to the system.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //Shows a Toast message indicating that the age is out of valid range
                        Toast.makeText(myContext,"Please enter the age with the range of 19 to 24 years old.",Toast.LENGTH_SHORT).show();
                    }


                }
                catch(NumberFormatException nfe)
                {
                    addAgeET.setText("");
                    Toast.makeText(myContext, "You entered an invalid age.\nPlease enter your age again.", Toast.LENGTH_LONG).show();
                }



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
