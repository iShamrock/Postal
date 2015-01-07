package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import iShamrock.Postal.R;
import iShamrock.Postal.entity.PostalData;
import iShamrock.Postal.entity.PostalDataItem;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by Tong on 12.24.
 * Add a new postal or show existed postal.
 */
public class PostalEditor extends Activity {

    private final int PHOTO_ZOOM = 0, TAKE_PHOTO = 1, PHOTO_RESULT = 2;
    private final String IMAGE_UNSPECIFIED = "image/*";
    private String imageDir;

    private int width, height;
    private ImageView imageView;
    private EditText editText;
    private ButtonRectangle btnImage, btnTake;
    private ButtonFloat btnBack, btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpostal);


        imageView = (ImageView) findViewById(R.id.add_postal_imageView);
        editText = (EditText) findViewById(R.id.editText);
        btnBack = (ButtonFloat) findViewById(R.id.btn_back);
        btnSend = (ButtonFloat) findViewById(R.id.btn_send);
        btnSend.setBackgroundColor(0xff1bd411);
        Intent intent = getIntent();
        PostalDataItem data = (PostalDataItem) intent.getSerializableExtra("data");
        if (data == null) {
            /* add new postal*/
            initImageCover(null);
            initEditComponents();
        } else {
            /* show existed postal*/
            switch (data.coverType) {
                default:
                    initImageCover(Uri.parse(data.coverUrl));
                    break;
                case 1:
                    /*Unimplemented*/
                    break;
                case 2:
                    initWebCover(Uri.parse(data.coverUrl));
                    break;
            }
            editText.setFocusable(false);
            editText.setClickable(false);
            editText.setText(data.content);
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*TODO implement intent here to callback PostalDataItem*/
                finish();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostalData.dataItemList.add(new PostalDataItem(PostalDataItem.TYPE_IMAGE, "content://media/external/images/media/115219",
                        4, editText.getText().toString(), "now", new float[]{31.14333f, 121.80528f}));
                finish();
            }
        });
    }

    private void initEditComponents() {
        btnImage = (ButtonRectangle) findViewById(R.id.btn_img);
        btnImage.setVisibility(View.VISIBLE);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(IMAGE_UNSPECIFIED);
                Intent wrapperIntent = Intent.createChooser(intent, null);
                startActivityForResult(wrapperIntent, PHOTO_ZOOM);
            }
        });


        btnTake = (ButtonRectangle) findViewById(R.id.btn_take_photo);
        btnTake.setVisibility(View.VISIBLE);
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

    private void initImageCover(Uri uri) {
        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        float scale = this.getResources().getDisplayMetrics().densityDpi;
        width = (int) (screenWidth / scale * 160);
        height = width / 4 * 3;

        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.height = screenWidth / 4 * 3;
        imageView.setLayoutParams(params);
        if (uri != null) {
            Bitmap photo = null;
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                photo.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
                imageView.setImageBitmap(photo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void initWebCover(Uri uri) {
        WebView postalContainer = null;

       /*  Add javascript support.*/
        WebSettings webSettings = postalContainer.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLightTouchEnabled(true);
        webSettings.setSupportZoom(false);

        postalContainer.setHapticFeedbackEnabled(true);
        postalContainer.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        if (uri != null)
            postalContainer.loadUrl(uri.toString());
    }

    private void photoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        /* width:height ratio*/
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        /* image size*/
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESULT);
        Log.d("uri", uri.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        if (requestCode == PHOTO_ZOOM) photoZoom(data.getData());
        if (requestCode == TAKE_PHOTO)
            photoZoom(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + imageDir)));
        if (requestCode == PHOTO_RESULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                photo.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
                imageView.setImageBitmap(photo);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
