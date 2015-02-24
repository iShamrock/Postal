package iShamrock.Postal.activity.publishers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.baidu.location.BDLocation;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.Timeline;
import iShamrock.Postal.database.Database;
import iShamrock.Postal.entity.PostalDataItem;
import iShamrock.Postal.util.BaiduLocUtil;
import iShamrock.Postal.util.SysInfoUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Tong on 12.24.
 * Add a new postal or show existed postal.
 */
public class PEditor extends Activity {

    private final int PHOTO_ZOOM = 0, TAKE_PHOTO = 1, PHOTO_RESULT = 2, REQUEST_LOCATION = 4;
    private final String IMAGE_UNSPECIFIED = "image/*";
    private String imageDir;

    private int width, height;
    private RelativeLayout peditor_media;
    private EditText peditor_text, peditor_title;
    private ImageView peditor_cover, peditor_stamp, btnBack, btnSend, btnFromFile, btnTakePhoto;
    private PostalDataItem dataItem;
    private Uri mediaUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peditor);
        initCommonComponents();
        initUploadComponents();
        initCover();

    }

    private void initCommonComponents() {
        peditor_media = (RelativeLayout) findViewById(R.id.peditor_media);
        peditor_cover = (ImageView) findViewById(R.id.peditor_cover);
        peditor_text = (EditText) findViewById(R.id.peditor_text);
        peditor_title = (EditText) findViewById(R.id.peditor_title);
        peditor_stamp = (ImageView) findViewById(R.id.peditor_stamp);
        btnBack = (ImageView) findViewById(R.id.peditor_cancel);
        btnSend = (ImageView) findViewById(R.id.peditor_send);

        dataItem = new PostalDataItem();
        peditor_stamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PEditor.this, GeoEncodingActivity.class);
                startActivityForResult(intent, REQUEST_LOCATION);
            }
        });
        peditor_stamp.setOnTouchListener(new ButtonTouchAnimationListener(peditor_stamp));
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().hasExtra("name")){
                    dataItem.to_user = getIntent().getStringExtra("name");
                }
                else {
                    Intent friendIntent = new Intent(PEditor.this, ChooseFriendToSendTo.class);
                    startActivityForResult(friendIntent, 12345);
                }
                BDLocation location = BaiduLocUtil.location;
                dataItem.time(SysInfoUtil.getTimeString())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .content(peditor_text.getText().toString())
                        .type(PostalDataItem.TYPE_TEXT)
                        .title(peditor_title.getText().toString())
                        .uri(mediaUri.toString())
                        .from_user(Database.me.getName());
                Intent intent = new Intent();
                intent.setClass(PEditor.this, Timeline.class);
                startActivity(intent);
                finish();
            }
        });
        btnSend.setOnTouchListener(new ButtonTouchAnimationListener(btnSend));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnBack.setOnTouchListener(new ButtonTouchAnimationListener(btnBack));
    }

    private void initUploadComponents() {
        btnFromFile = (ImageView) findViewById(R.id.peditor_img_from_file);
        btnFromFile.setVisibility(View.VISIBLE);
        btnFromFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(IMAGE_UNSPECIFIED);
                Intent wrapperIntent = Intent.createChooser(intent, null);
                startActivityForResult(wrapperIntent, PHOTO_ZOOM);
            }
        });
        btnFromFile.setOnTouchListener(new ButtonTouchAnimationListener(btnFromFile, 196));


        btnTakePhoto = (ImageView) findViewById(R.id.peditor_img_take);
        btnTakePhoto.setVisibility(View.VISIBLE);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDir = "temp.jpg";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), imageDir)));
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });
        btnTakePhoto.setOnTouchListener(new ButtonTouchAnimationListener(btnTakePhoto, 196));
    }

    private void initCover() {
        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        float scale = this.getResources().getDisplayMetrics().densityDpi;
        width = (int) (screenWidth / scale * 160);
        height = width / 16 * 9;

        ViewGroup.LayoutParams params = peditor_media.getLayoutParams();
        params.height = screenWidth / 16 * 9 + 10;
        peditor_media.setLayoutParams(params);
    }


    private void photoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        /* width:height ratio*/
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 16);
        intent.putExtra("aspectY", 9);
        /* image size*/
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case REQUEST_LOCATION:
                dataItem.location = new double[]{BaiduLocUtil.location.getLatitude(), BaiduLocUtil.location.getLongitude()};
                String geoEncoding = data.getStringExtra("GeoEncoding");
                dataItem.location_text = geoEncoding;
                if (geoEncoding != null) {
                    peditor_stamp.setImageDrawable(getResources().getDrawable(R.drawable.stamp1));
                }
        }
        switch (requestCode) {
            case TAKE_PHOTO:
                photoZoom(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/temp.jpg")));
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
                mediaUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), tempDir));
                peditor_cover.setImageBitmap(photo);
                break;
            case 12345:

                dataItem.to_user(data.getStringExtra("name"));
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
