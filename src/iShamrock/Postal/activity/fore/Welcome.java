package iShamrock.Postal.activity.fore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import iShamrock.Postal.R;

/**
 * Created by Tong on 02.14.
 */
public class Welcome extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.welcome);
        super.onCreate(savedInstanceState);
        final ImageView login = (ImageView) findViewById(R.id.welcome_login_button);
        login.setLongClickable(true);
        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        login.setImageDrawable(getResources().getDrawable(R.drawable.login_button_pressed));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        login.setImageDrawable(getResources().getDrawable(R.drawable.login_button));
                        break;
                    }
                }
                return false;
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, Login.class);
                Welcome.this.startActivity(intent);
            }
        });

        final ImageView signup = (ImageView) findViewById(R.id.welcome_signup_button);
        signup.setLongClickable(true);
        signup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        signup.setImageDrawable(getResources().getDrawable(R.drawable.signup_button_pressed));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        signup.setImageDrawable(getResources().getDrawable(R.drawable.signup_button));
                        break;
                    }
                }
                return false;
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, Signup.class);
                Welcome.this.startActivity(intent);
            }
        });

    }


}
