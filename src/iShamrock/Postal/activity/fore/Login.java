package iShamrock.Postal.activity.fore;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import iShamrock.Postal.R;

/**
 * Created by Tong on 01.07.
 */
public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.login);
        super.onCreate(savedInstanceState);

        EditText txtPorE = (EditText) findViewById(R.id.login_psward);
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
                // LOGIN SERVICE REQUIRED
            }
        });

        // Auto acquire phone number if possible.
        String mPhoneNumber = ((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).getLine1Number();
        if (mPhoneNumber != null) txtPorE.setText(mPhoneNumber);
    }
}
