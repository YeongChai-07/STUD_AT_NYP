package sg.edu.sit.ict.ippt_app;


import java.text.DecimalFormat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginProfile2 extends Activity {


    TextView myTv;
    Intent inIntent;
    Button attnThreshholdBtn;
    Intent myIntent;
    Button button_calc;
    EditText field_height;
    EditText field_weight;
    TextView view_result;
    TextView view_suggest;
    Context context;
    ImageView img;
    int notificationRef;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginprofile);
        context=this;

        myTv = (TextView)findViewById(R.id.textView1);
        button_calc = (Button) findViewById(R.id.submit);
        field_height = (EditText) findViewById(R.id.height);
        field_weight = (EditText) findViewById(R.id.weight);
        view_result = (TextView) findViewById(R.id.result);
        view_suggest = (TextView) findViewById(R.id.suggest);
        img=(ImageView)findViewById(R.id.imageView1);

        //button_calc
        button_calc.setOnClickListener(new OnClickListener(){

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                DecimalFormat nf = new DecimalFormat("0.00");


                double height = Double
                        .parseDouble(field_height.getText().toString()) / 100;
                double weight = Double
                        .parseDouble(field_weight.getText().toString());
                double BMI = weight / (height * height);

                //Present result
                view_result.setText(nf.format(BMI));

                if (BMI > 25) {
                    view_suggest.setText("Your are overweight. You should exercise more");
                    NotificationManager barManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationRef = 1;
                    Notification barmsg = new Notification(android.R.drawable.ic_notification_overlay,
                            "OverWeight",
                            System.currentTimeMillis()
                    );
                    barmsg.tickerText = "OverWeight";

                    barmsg.defaults = Notification.DEFAULT_ALL;

                    Intent intent = new Intent(context, LoginProfile2.class);
                    PendingIntent launchIntent = PendingIntent.getActivity(context, 0, intent, 0);

                    barmsg.setLatestEventInfo(context, "BMI warning", "*BMI*",launchIntent);

                    barManager.notify( notificationRef, barmsg);

                    view_result.setTextColor(Color.RED);

                    img.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.fat);
                } else if (BMI < 20) {
                    view_suggest.setText("You are too light. You should eat more");
                    img.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.skeleton);

                } else {
                    view_suggest.setText("Keep it up! You are in the moderate range.");
                    img.setVisibility(View.VISIBLE);
                    img.setImageResource(R.drawable.fit);
                }
            }
        });



        inIntent = getIntent();
        myTv.setText(inIntent.getCharSequenceExtra("USER"));


    }
}

