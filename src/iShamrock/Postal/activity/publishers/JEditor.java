package iShamrock.Postal.activity.publishers;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.Timeline;
import iShamrock.Postal.database.Database;
import iShamrock.Postal.entity.PostalDataItem;
import iShamrock.Postal.util.BaiduLocUtil;
import iShamrock.Postal.util.SysInfoUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tong on 02.15.
 */
public class JEditor extends Activity {
    static final int PHOTO_CROP = 0, RESULT_CAPTURE_IMAGE = 1,
            REQUEST_CODE_TAKE_VIDEO = 2, RESULT_CAPTURE_RECORDER_SOUND = 3, REQUEST_LOCATION = 4;

    private int width, height;
    private int type;
    private Uri mediaUri = Uri.parse("");
    private ViewGroup jeditor_media;
    private ImageView jeditor_delete, jeditor_send, jeditor_action, jeditor_loc;
    private TextView jeditor_title, jeditor_time;
    private EditText jeditor_text;
    private PostalDataItem dataItem = new PostalDataItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.jeditor);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", PostalDataItem.TYPE_IMAGE);
        initCommonComponents();
        initMediaComponents();
    }

    private void initCommonComponents() {
        jeditor_media = (ViewGroup) findViewById(R.id.jeditor_media);
        jeditor_delete = (ImageView) findViewById(R.id.jeditor_icon_delete);
        jeditor_send = (ImageView) findViewById(R.id.jeditor_icon_send);
        jeditor_title = (TextView) findViewById(R.id.jeditor_title);
        jeditor_action = (ImageView) findViewById(R.id.jeditor_action);
        jeditor_time = (TextView) findViewById(R.id.jeditor_time);
        jeditor_loc = (ImageView) findViewById(R.id.jeditor_loc);
        jeditor_text = (EditText) findViewById(R.id.jeditor_text);

        jeditor_delete.setOnTouchListener(new ButtonTouchAnimationListener(jeditor_delete));

        jeditor_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        jeditor_send.setOnTouchListener(new ButtonTouchAnimationListener(jeditor_send));

        jeditor_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database.addPostal(new PostalDataItem(
                        type,
                        mediaUri.toString(),
                        jeditor_text.getText().toString(),
                        jeditor_time.getText().toString(),
                        "no title",
                        dataItem.location,
                        Database.me.getPhone(),
                        Database.me.getPhone(),
                        dataItem.location_text));
                Intent intent = new Intent();
                intent.setClass(JEditor.this, Timeline.class);
                startActivity(intent);
                finish();
            }
        });

        jeditor_time.setText(new SimpleDateFormat("MM/dd E").format(new Date()));

        jeditor_loc.setOnTouchListener(new ButtonTouchAnimationListener(jeditor_loc));
        jeditor_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JEditor.this, GeoEncodingActivity.class);
                startActivityForResult(intent, REQUEST_LOCATION);
            }
        });
    }

    private void initMediaComponents() {
        /* Decide the function of action button*/
        switch (type) {
            case PostalDataItem.TYPE_TEXT:
                jeditor_title.setText("Take a note");
                ViewGroup.LayoutParams params = jeditor_media.getLayoutParams();
                params.height = 0;
                jeditor_media.setLayoutParams(params);
                return;
            case PostalDataItem.TYPE_IMAGE: {
                jeditor_title.setText("Take a photo");
                jeditor_action.setImageDrawable(getResources().getDrawable(R.drawable.icon_image_take));
                jeditor_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cameraMethod();
                    }
                });
                break;
            }
            case PostalDataItem.TYPE_VIDEO: {
                jeditor_title.setText("Take a video");
                jeditor_action.setImageDrawable(getResources().getDrawable(R.drawable.icon_video));
                jeditor_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        videoMethod();
                    }
                });
                break;
            }
            case PostalDataItem.TYPE_AUDIO:
                jeditor_title.setText("Take an audio");
                jeditor_action.setImageDrawable(getResources().getDrawable(R.drawable.icon_audio));
                jeditor_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        soundRecorderMethod();
                    }
                });
                break;
            case PostalDataItem.TYPE_WEB:
                jeditor_title.setText("Take a page");
                jeditor_action.setImageDrawable(getResources().getDrawable(R.drawable.icon_web));
                jeditor_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        webContentMethod();
                    }
                });
                break;
        }
        /* Add press animation for action button*/
        jeditor_action.setOnTouchListener(new ButtonTouchAnimationListener(jeditor_action, 196));

        /* Adjust the size of media area*/
        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        float scale = this.getResources().getDisplayMetrics().densityDpi;
        width = (int) (screenWidth / scale * 160);
        height = width / 16 * 9;
        ViewGroup.LayoutParams params = jeditor_media.getLayoutParams();
        params.height = screenWidth / 16 * 9;
        jeditor_media.setLayoutParams(params);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            switch (resultCode) {
                case REQUEST_LOCATION:
                    dataItem.location=new double[]{BaiduLocUtil.location.getLatitude(),BaiduLocUtil.location.getLongitude()};
                    dataItem.location_text= data.getStringExtra("GeoEncoding");
            }
            return;
        }
        switch (requestCode) {
            case RESULT_CAPTURE_IMAGE:
                photoZoom(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/temp.jpg")));
                break;
            case PHOTO_CROP:
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

                /* Put the image into media area*/
                ImageView imageView = new ImageView(this);
                imageView.setImageURI(mediaUri);
                imageView.setLayoutParams(new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                jeditor_media.removeView(jeditor_action);
                jeditor_media.addView(imageView);

                break;
            case REQUEST_CODE_TAKE_VIDEO:
                mediaUri = data.getData();

                    /* Put the video into media area*/
                final VideoView videoView = new VideoView(this);
                videoView.setVideoURI(mediaUri);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.gravity = Gravity.CENTER;

                videoView.setLayoutParams(lp);
                MediaController mediaController = new MediaController(this);
                videoView.setMediaController(mediaController);
                videoView.requestFocus();
                jeditor_media.setBackgroundColor(0xff000000);
                jeditor_media.removeView(jeditor_action);
                jeditor_media.addView(videoView);

                break;
            case RESULT_CAPTURE_RECORDER_SOUND:
                mediaUri = data.getData();
                    /* Put the audio into media area*/
                final ImageView audioImageview = new ImageView(this);
                audioImageview.setImageDrawable(getResources().getDrawable(R.drawable.voice_message));
                lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.gravity = Gravity.LEFT;
                audioImageview.setLayoutParams(lp);
                final MediaPlayer mMediaPlayer = MediaPlayer.create(getApplicationContext(), mediaUri);
                audioImageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mMediaPlayer.isPlaying()) {
                            mMediaPlayer.pause();
                            audioImageview.setImageDrawable(getResources().getDrawable(R.drawable.voice_message));
                        } else {
                            mMediaPlayer.start();
                            audioImageview.setImageDrawable(getResources().getDrawable(R.drawable.voice_message_playing));
                        }
                    }
                });
                audioImageview.setOnTouchListener(new ButtonTouchAnimationListener(audioImageview));
                ViewGroup.LayoutParams params = jeditor_media.getLayoutParams();
                params.height = getWindowManager().getDefaultDisplay().getWidth() / 6;
                jeditor_media.setLayoutParams(params);


                jeditor_media.removeView(jeditor_action);
                jeditor_media.addView(audioImageview);
                break;
        }

    }

    private void photoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        /* width:height ratio*/
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 16);
        intent.putExtra("aspectY", 9);
        /* image size*/
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CROP);
    }

    private void cameraMethod() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
        startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
    }


    private void videoMethod() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        startActivityForResult(intent, REQUEST_CODE_TAKE_VIDEO);
    }


    private void soundRecorderMethod() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/amr");
        startActivityForResult(intent, RESULT_CAPTURE_RECORDER_SOUND);
    }

    private void webContentMethod() {
        final EditText urlInput = new EditText(this);
        urlInput.setText("http://");
        AlertDialog dialog = new AlertDialog.Builder(JEditor.this)
                .setTitle("Input the page link")
                .setView(urlInput)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                String urlString = urlInput.getText().toString();
                                if (urlString == null) return;

                                final WebView webView = new WebView(JEditor.this);
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                webView.setLayoutParams(lp);


                                /* Forbid outer link*/
                                class MyWebViewClient extends WebViewClient {
                                    @Override
                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                        view.loadUrl(url);
                                        return true;
                                    }
                                }
                                webView.setWebViewClient(new MyWebViewClient());

                                /*  Add javascript support.*/
                                WebSettings webSettings = webView.getSettings();
                                webSettings.setJavaScriptEnabled(true);
                                webSettings.setBuiltInZoomControls(false);
                                webSettings.setLightTouchEnabled(true);
                                webSettings.setSupportZoom(false);

                                webView.setHapticFeedbackEnabled(true);
                                webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                                webView.loadUrl(urlString);

                                jeditor_media.removeView(jeditor_action);
                                jeditor_media.addView(webView);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }

}