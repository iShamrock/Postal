package iShamrock.Postal.activity.fore;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import iShamrock.Postal.R;

/**
 * Created by Tong on 02.15.
 */
public class Signup extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.signup);
        super.onCreate(savedInstanceState);

        EditText txtName = (EditText) findViewById(R.id.signup_username);
        EditText txtPorE = (EditText) findViewById(R.id.signup_signature);
        EditText txtPswd = (EditText) findViewById(R.id.signup_psward);
        final ImageView signup = (ImageView) findViewById(R.id.signup_signup);
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
                // SIGNUP SERVICE
            }
        });

        // Auto acquire phone number if possible.
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        if (mPhoneNumber != null) txtPorE.setText(mPhoneNumber);
    }
}
