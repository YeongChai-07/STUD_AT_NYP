package sg.edu.sit.ict.ippt_app;

import sg.edu.sit.ict.ippt_app.Entity.User;
import sg.edu.sit.ict.ippt_app.database.myDbAdapter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginProfile extends Activity {
    EditText myEt, myEt2;
    Intent myIntent;
    Button myBtn2;
    Intent myIntent2;
    Context myContext;
    myDbAdapter myDB;
    Button myBtn, mySuBtn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
        Button myBtn;
        myContext = this;

        BroadcastReceiver closeReceiver;

        myBtn = (Button) findViewById(R.id.button1);
        myEt = (EditText) findViewById(R.id.editText1);
        myEt2 = (EditText) findViewById(R.id.editText2);
        myIntent = new Intent(getBaseContext(), yc_napfaPerfMain.class);
        myDB = new myDbAdapter(myContext);

        myBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = myEt.getText().toString();
                myDB.open();
                String pwd = myEt2.getText().toString();
                System.out
                        .println("UserName and password is " + username + pwd);
                Cursor loginc = myDB.retrieveAllEntriesCursor(username, pwd);

                String user_name = null, Password = null;

                if (loginc.getCount() > 0) {

                    if (loginc != null) {
                        loginc.moveToFirst();
                        String myString = loginc
                                .getString(myDB.COLUMN_ADMIN_ID);
                        int age = loginc.getInt(myDB.COLUMN_AGE_ID);
                        User.setAdminNo(myString);
                        User.setAge(age);
                        System.out.println(myString);
                    }

                    startActivity(myIntent);
                } else {
                    Toast.makeText(myContext, "Invalid username or password.",
                            Toast.LENGTH_SHORT).show();
                }

                myDB.close();

                // Cursor nameExistc = myDB.retrieveName();
				/*
				 * if (loginc.getCount() >0){ if (nameExistc.getCount() == 0){
				 * myDB.insertName(username); } else{ myDB.updateName(username);
				 * } startActivity(myIntent); }
				 */
                // else{
                // Toast toast=Toast.makeText(myContext,
                // "Invalid username or password.", 5);
                // toast.show();
            }
            // }

        });
        myBtn2 = (Button) findViewById(R.id.button2);
        myIntent2 = new Intent(this, UserLoginActivity.class);
        myBtn2.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(myIntent2);
            }

        });

    }
}
