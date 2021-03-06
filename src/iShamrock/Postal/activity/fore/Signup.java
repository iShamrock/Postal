package iShamrock.Postal.activity.fore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.Timeline;
import iShamrock.Postal.activity.publishers.ButtonTouchAnimationListener;
import iShamrock.Postal.database.Connect;
import iShamrock.Postal.database.Database;
import iShamrock.Postal.entity.User;
import iShamrock.Postal.util.SysInfoUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Tong on 02.15.
 */
public class Signup extends Activity {
    private final String IMAGE_UNSPECIFIED = "image/*";
    private final int PHOTO_ZOOM = 1, PHOTO_RESULT = 2;
    private ImageView signup_avatar;
    private Uri avatarUri = Uri.EMPTY;
    private EditText txtName, txtPorE, txtPswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.signup);
        super.onCreate(savedInstanceState);

        txtName = (EditText) findViewById(R.id.signup_username);
        txtPorE = (EditText) findViewById(R.id.signup_signature);
        txtPswd = (EditText) findViewById(R.id.signup_psward);
        signup_avatar = (ImageView) findViewById(R.id.signup_avatar);
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
                String name = txtName.getText().toString();
                final String password = txtPswd.getText().toString();
                String phone = txtPorE.getText().toString();
                String photoUri = avatarUri.toString();
                final User user = new User(name, phone, photoUri, "");
                boolean legal = !name.isEmpty() && !password.isEmpty() && !phone.isEmpty();
                if (legal) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Connect.signUp(user, password);
                                User user1 = Connect.login(user.getPhone(), password);
                                if (user1 == null || !txtPorE.getText().toString().equals(user.getPhone())) {
                                    Looper.prepare();
                                    Toast.makeText(Signup.this, "Sign up failed!", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                } else {
                                    Database.me = user1;
                                    Intent intent = new Intent(Signup.this, Timeline.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("Sign up failed with IOException");
                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(Signup.this, "Please fill your basic information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signup_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(IMAGE_UNSPECIFIED);
                Intent wrapperIntent = Intent.createChooser(intent, null);
                startActivityForResult(wrapperIntent, PHOTO_ZOOM);
            }
        });
        signup_avatar.setOnTouchListener(new ButtonTouchAnimationListener(signup_avatar));

        // Auto acquire phone number if possible.
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        if (mPhoneNumber != null) txtPorE.setText(mPhoneNumber);
    }

    private void photoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        /* width:height ratio*/
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        /* image size*/
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_ZOOM:
                if (RESULT_OK == resultCode)
                    photoZoom(data.getData());
                break;
            case PHOTO_RESULT:
                if (data == null) return;
                Bundle extras = data.getExtras();
                if (extras == null) return;
                Bitmap photo = extras.getParcelable("data");
                if (photo == null) return;
                photo.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
                String tempDir = "cache" + System.currentTimeMillis();
                try {
                    SysInfoUtil.saveBitmapToFile(photo, Environment.getExternalStorageDirectory() + "/" + tempDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                avatarUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), tempDir));
                signup_avatar.setImageBitmap(photo);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
