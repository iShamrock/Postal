package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.publishers.ButtonTouchAnimationListener;
import iShamrock.Postal.activity.publishers.JEditor;
import iShamrock.Postal.activity.publishers.PEditor;
import iShamrock.Postal.commons.utils.Views;
import iShamrock.Postal.database.Database;
import iShamrock.Postal.entity.PostalDataItem;
import iShamrock.Postal.foldablelayout.UnfoldableView;
import iShamrock.Postal.foldablelayout.shading.GlanceFoldShading;
import iShamrock.Postal.items.Painting;
import iShamrock.Postal.items.PaintingsAdapter;
import iShamrock.Postal.util.SystemUtil;

/**
 * Created by lifengshuang on 2/14/15.
 */
public class Timeline extends Activity {

    private ListView mListView;
    private View mListTouchInterceptor;
    private View mDetailsLayout;
    private UnfoldableView mUnfoldableView;

    private ImageView postal_friend, postal_user_avatar, postal_add, postal_add_text, postal_add_image, postal_add_video, postal_add_audio, postal_add_web, postal_edit;
    private RelativeLayout postal_cover_container;
    private PaintingsAdapter adapter;
    private Runnable run;
    private Thread refreshThread;

    private boolean isAddButtonsFolded = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);

        initThread();
        initCommonComponents();
        initDatabase();
        initUnfoldableDetailsActivity();
    }

    private void initThread(){
        run = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                System.out.println("try to update view...");
            }
        };
        refreshThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++){
                    runOnUiThread(run);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initCommonComponents() {
        postal_edit = (ImageView) findViewById(R.id.postal_edit);
        postal_friend = (ImageView) findViewById(R.id.postal_friend);
        postal_user_avatar = (ImageView) findViewById(R.id.postal_user_avatar);
        postal_cover_container = (RelativeLayout) findViewById(R.id.postal_cover_container);

        postal_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent friendIntent = new Intent(Timeline.this, MyFriends.class);
                startActivity(friendIntent);
            }
        });
        postal_friend.setOnTouchListener(new ButtonTouchAnimationListener(postal_friend));

        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();

//        ViewGroup.LayoutParams params = postal_cover_container.getLayoutParams();
//        params.height = screenWidth / 16 * 9;
//        postal_cover_container.setLayoutParams(params);

        postal_user_avatar.setImageBitmap(SystemUtil.toRoundCorner(BitmapFactory.decodeResource(getResources(), R.drawable.p1)));

        postal_add = (ImageView) findViewById(R.id.postal_add);
        postal_add_text = (ImageView) findViewById(R.id.postal_add_text);
        postal_add_image = (ImageView) findViewById(R.id.postal_add_image);
        postal_add_video = (ImageView) findViewById(R.id.postal_add_video);
        postal_add_audio = (ImageView) findViewById(R.id.postal_add_audio);
        postal_add_web = (ImageView) findViewById(R.id.postal_add_web);

        postal_add.setOnTouchListener(new ButtonTouchAnimationListener(postal_add));
        postal_add_text.setOnTouchListener(new ButtonTouchAnimationListener(postal_add_text));
        postal_add_image.setOnTouchListener(new ButtonTouchAnimationListener(postal_add_image));
        postal_add_video.setOnTouchListener(new ButtonTouchAnimationListener(postal_add_video));
        postal_add_audio.setOnTouchListener(new ButtonTouchAnimationListener(postal_add_audio));
        postal_add_web.setOnTouchListener(new ButtonTouchAnimationListener(postal_add_web));

        postal_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foldAddButtons();
            }
        });
        postal_add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Timeline.this, JEditor.class);
                intent.putExtra("type", PostalDataItem.TYPE_TEXT);
                startActivity(intent);
                foldAddButtons();
            }
        });
        postal_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Timeline.this, JEditor.class);
                intent.putExtra("type", PostalDataItem.TYPE_IMAGE);
                startActivity(intent);
                foldAddButtons();
            }
        });
        postal_add_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Timeline.this, JEditor.class);
                intent.putExtra("type", PostalDataItem.TYPE_AUDIO);
                startActivity(intent);
                foldAddButtons();
            }
        });
        postal_add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Timeline.this, JEditor.class);
                intent.putExtra("type", PostalDataItem.TYPE_VIDEO);
                startActivity(intent);
                foldAddButtons();
            }
        });
        postal_add_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Timeline.this, JEditor.class);
                intent.putExtra("type", PostalDataItem.TYPE_WEB);
                startActivity(intent);
                foldAddButtons();
            }
        });
        postal_edit.setOnTouchListener(new ButtonTouchAnimationListener(postal_edit));
        postal_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Timeline.this, PEditor.class);
                startActivity(intent);
                foldAddButtons();
            }
        });
    }

    private void foldAddButtons() {
        int isVisible = (isAddButtonsFolded) ? View.VISIBLE : View.INVISIBLE;
        postal_add_text.setVisibility(isVisible);
        postal_add_audio.setVisibility(isVisible);
        postal_add_image.setVisibility(isVisible);
        postal_add_web.setVisibility(isVisible);
        postal_add_video.setVisibility(isVisible);
        postal_add.setImageDrawable(getResources().getDrawable((isAddButtonsFolded) ? R.drawable.icon_more_red : R.drawable.icon_add_red));
        isAddButtonsFolded = !isAddButtonsFolded;
    }


    private void initUnfoldableDetailsActivity() {
        mListView = Views.find(this, R.id.list_view_timeline);
        adapter = new PaintingsAdapter(this);
        mListView.setAdapter(adapter);
        refreshThread.start();
        mListTouchInterceptor = Views.find(this, R.id.touch_interceptor_view);
        mListTouchInterceptor.setClickable(false);

        mDetailsLayout = Views.find(this, R.id.details_layout);
        mDetailsLayout.setVisibility(View.INVISIBLE);

        mUnfoldableView = Views.find(this, R.id.unfoldable_view);

        Bitmap glance = BitmapFactory.decodeResource(getResources(), R.drawable.unfold_glance);
        mUnfoldableView.setFoldShading(new GlanceFoldShading(this, glance));

        mUnfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {
            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(true);
                mDetailsLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUnfolded(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(false);
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(true);
            }

            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(false);
                mDetailsLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mUnfoldableView != null && (mUnfoldableView.isUnfolded() || mUnfoldableView.isUnfolding())) {
            mUnfoldableView.foldBack();
        } else {
            super.onBackPressed();
        }
    }

    public void openDetails(View coverView, Painting painting) {

        PostalDataItem item = painting.getItem();
        LinearLayout timeline_media = Views.find(mDetailsLayout, R.id.timeline_media);
        ImageView timeline_action = Views.find(mDetailsLayout, R.id.timeline_action);
        TextView timeline_text = Views.find(mDetailsLayout, R.id.timeline_text);
        TextView timeline_loc = Views.find(mDetailsLayout, R.id.timeline_loc);
        TextView timeline_time = Views.find(mDetailsLayout, R.id.timeline_time);

        timeline_loc.setText(item.location_text);
        timeline_text.setText(item.text);
        timeline_time.setText(item.time);


        switch (item.type) {

            case PostalDataItem.TYPE_AUDIO: {
                final ImageView audioImageview = new ImageView(this);
                audioImageview.setImageDrawable(getResources().getDrawable(R.drawable.voice_message));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.gravity = Gravity.LEFT;
                audioImageview.setLayoutParams(lp);
                final MediaPlayer mMediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(item.uri));
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
                ViewGroup.LayoutParams params = timeline_media.getLayoutParams();
                params.height = getWindowManager().getDefaultDisplay().getWidth() / 6;
                timeline_media.setLayoutParams(params);
                timeline_media.removeView(timeline_action);
                timeline_media.addView(audioImageview);
                break;
            }

            case PostalDataItem.TYPE_VIDEO: {
                final VideoView videoView = new VideoView(this);
                videoView.setVideoURI(Uri.parse(item.uri));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lp.gravity = Gravity.CENTER;

                videoView.setLayoutParams(lp);
                MediaController mediaController = new MediaController(this);
                videoView.setMediaController(mediaController);
                videoView.requestFocus();
                timeline_media.setBackgroundColor(0xff000000);
                timeline_media.removeView(timeline_action);
                timeline_media.addView(videoView);
                break;
            }

            case PostalDataItem.TYPE_IMAGE: {
                int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
                ViewGroup.LayoutParams params = timeline_action.getLayoutParams();
                params.height = screenWidth / 16 * 9;
                timeline_action.setLayoutParams(params);
                try {
                    timeline_action.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(item.uri)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case PostalDataItem.TYPE_WEB: {
                //todo
                break;
            }

        }
        mUnfoldableView.unfold(coverView, mDetailsLayout);

/*        ImageView image = Views.find(mDetailsLayout, R.id.details_image);
        TextView title = Views.find(mDetailsLayout, R.id.details_title);
        TextView description = Views.find(mDetailsLayout, R.id.details_text);
        try {
            image.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(painting.getItem().uri)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        title.setText(painting.getItem().title);

        description.setText(painting.getItem().text + "\n");//todo: add other contents

*//*
        if (painting.isLocal()) {
            image.setImageBitmap(BitmapFactory.decodeResource(getResources(), painting.getImageId()));
        }
        else {
            try {
                image.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(painting.getUri())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        title.setText(painting.getTitle());

        description.setText(painting.getContent() + "\n" + painting.getYear());
*//*

        mUnfoldableView.unfold(coverView, mDetailsLayout);*/
    }

    private void initDatabase() {
        Database.database = openOrCreateDatabase("postal.db", Context.MODE_PRIVATE, null);
        Database.initDatabase();
    }


}
