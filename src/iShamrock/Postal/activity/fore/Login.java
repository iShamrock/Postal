package iShamrock.Postal.activity.fore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.Timeline;
import iShamrock.Postal.database.Connect;
import iShamrock.Postal.database.Database;
import iShamrock.Postal.entity.User;

import java.io.IOException;

/**
 * Created by Tong on 01.07.
 */
public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.login);
        super.onCreate(savedInstanceState);

        final EditText txtPorE = (EditText) findViewById(R.id.login_psward);
        final EditText phone = (EditText) findViewById(R.id.login_signature);
        final ImageView login = (ImageView) findViewById(R.id.login_login);
        login.setLongClickable(true);
        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        login.setImageDrawable(getResources().getDrawable(R.drawable.login_button_pressed_notext));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        login.setImageDrawable(getResources().getDrawable(R.drawable.login_button_notext));
                        break;
                    }
                }
                return false;
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (Database.login(user.getText().toString(), txtPorE.getText().toString())) {
                    Intent intent = new Intent(Login.this, Timeline.class);
                    startActivity(intent);
                }else {
                    //login failed
                }*/
                Toast.makeText(Login.this, "Logging in... ", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = null;
                        try {
                            user = Connect.login(phone.getText().toString(), txtPorE.getText().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("Login Exception");
                        }
                        if (user == null){
                            Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Database.me = user;
                            Intent intent = new Intent(Login.this, Timeline.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).start();
            }
        });

        // Auto acquire phone number if possible.
        String mPhoneNumber = ((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).getLine1Number();
        if (mPhoneNumber != null) txtPorE.setText(mPhoneNumber);
    }
}
