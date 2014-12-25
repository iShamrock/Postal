package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import iShamrock.Postal.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by Tong on 12.24.
 */
public class AddPostal extends Activity {

    private final int PHOTO_ZOOM = 0;
    private final int TAKE_PHOTO = 1;
    private final int PHOTO_RESULT = 2;
    private final String IMAGE_UNSPECIFIED = "image/*";
    private String imageDir = "";
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpostal);

        WebView postalContainer = (WebView) findViewById(R.id.postal_container);

        /* Add javascript support.*/
        WebSettings webSettings = postalContainer.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLightTouchEnabled(true);
        webSettings.setSupportZoom(false);

        postalContainer.setHapticFeedbackEnabled(true);
        postalContainer.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        postalContainer.loadUrl("file:///android_asset/web/content.html");

        Button btnImage = (Button) findViewById(R.id.btn_img);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(IMAGE_UNSPECIFIED);
                Intent wrapperIntent = Intent.createChooser(intent, null);
                startActivityForResult(wrapperIntent, PHOTO_ZOOM);
            }
        });

        imageView = (ImageView) findViewById(R.id.imageView2);

        Button btnTake = (Button) findViewById(R.id.btn_take_photo);
        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDir = "temp.jpg";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), imageDir)));
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });
    }

    private void photoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", "1");
        intent.putExtra("aspectY", "1");

        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_ZOOM) {
            photoZoom(data.getData());
        }
        if (requestCode == TAKE_PHOTO) {
            File picture = new File(Environment.getExternalStorageDirectory() + "/" + imageDir);
            photoZoom(Uri.fromFile(picture));
        }
        if (requestCode == PHOTO_RESULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                imageView.setImageBitmap(photo);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
