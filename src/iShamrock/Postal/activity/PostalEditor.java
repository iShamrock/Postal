package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.gc.materialdesign.views.ButtonRectangle;
import iShamrock.Postal.R;
import iShamrock.Postal.entity.PostalData;
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
public class PostalEditor extends Activity {

    private final int PHOTO_ZOOM = 0, TAKE_PHOTO = 1, PHOTO_RESULT = 2;
    private final String IMAGE_UNSPECIFIED = "image/*";
    private String imageDir;

    private int width, height;
    private ImageView imageView;
    private EditText editText;
    private ButtonRectangle btnImage, btnTake;
    private ImageView btnBack, btnSend;
    private PostalDataItem dataItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpostal);


        initLeftDrawer();
        imageView = (ImageView) findViewById(R.id.add_postal_imageView);
        editText = (EditText) findViewById(R.id.editText);
        btnBack = (ImageView) findViewById(R.id.img_back);
        btnSend = (ImageView) findViewById(R.id.img_send);
        Intent intent = getIntent();
        final PostalDataItem data = (PostalDataItem) intent.getSerializableExtra("data");
        if (data == null) {
            /* add new postal*/
            dataItem = new PostalDataItem();
            initImageCover(null);
            initEditComponents();
        } else {
            /* show existed postal*/
            switch (data.type) {
                default:
                    initImageCover(Uri.parse(data.pictureUrl));
                    break;
                case 1:
                    /*Unimplemented*/
                    break;
                case 2:
                    initWebCover(Uri.parse(data.pictureUrl));
                    break;
            }
            editText.setFocusable(false);
            editText.setClickable(false);
            editText.setText(data.text);
        }
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataItem.pictureUrl == null)
                    finish();
                else {
                    BDLocation location = BaiduLocUtil.location;
                    dataItem.time(SysInfoUtil.getTimeString())
                            .latitude(location.getLatitude())
                            .longitude(location.getLongitude())
                            .content(editText.getText().toString())
                            .type(PostalDataItem.TYPE_TEXT)
                            .title("My Postal");
                    PostalData.dataItemList.add(dataItem);
                    Intent intent = new Intent();
                    intent.setClass(PostalEditor.this, Timeline_prev.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(PostalEditor.this, Timeline_prev.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initLeftDrawer() {
        String[] titles = new String[]{"Postal Box", "In the map", "Edit Postal", "My Posts"};
        ListView drawerList = (ListView) findViewById(R.id.left_drawer_addpostal);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_addpostal);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, titles));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerItemOnClickAction(i);
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(toggle);
    }

    private void drawerItemOnClickAction(int i) {
        if (i == 1) {
            Intent intent = new Intent();
            intent.setClass(this, PostalNearby.class);
            startActivity(intent);
            finish();
        } else if (i == 0) {
            Intent intent = new Intent();
            intent.setClass(this, Timeline_prev.class);
            startActivity(intent);
            finish();
        } else if (i == 3) {
            Intent intent = new Intent();
            intent.setClass(this, FoldableListActivity.class);
            startActivity(intent);
            finish();
        }
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PHOTO)
            photoZoom(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + imageDir)));

        if (data == null)
            return;
        if (requestCode == PHOTO_ZOOM) photoZoom(data.getData());
        if (requestCode == PHOTO_RESULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                photo.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
                String tempDir="temp" + System.currentTimeMillis();
                try {
                    SysInfoUtil.saveBitmapToFile(photo, Environment.getExternalStorageDirectory() + "/" + tempDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dataItem.type(PostalDataItem.TYPE_IMAGE)
                        .coverUrl(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), tempDir)).toString());
                imageView.setImageBitmap(photo);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
