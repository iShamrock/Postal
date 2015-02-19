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
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.fore.Splash;
import iShamrock.Postal.activity.publishers.ButtonTouchAnimationListener;
import iShamrock.Postal.commons.utils.Views;
import iShamrock.Postal.database.Database;
import iShamrock.Postal.entity.PostalDataItem;
import iShamrock.Postal.foldablelayout.UnfoldableView;
import iShamrock.Postal.foldablelayout.shading.GlanceFoldShading;
import iShamrock.Postal.items.Painting;
import iShamrock.Postal.items.PaintingsAdapter;

import java.io.IOException;

/**
 * Created by lifengshuang on 2/14/15.
 */
public class Timeline extends Activity{

    private ListView mListView;
    private View mListTouchInterceptor;
    private View mDetailsLayout;
    private UnfoldableView mUnfoldableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);/*
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setTitle("Timeline");*/
        initLeftDrawer();
        initDatabase();
        initUnfoldableDetailsActivity();
    }

    private void initUnfoldableDetailsActivity(){
        mListView = Views.find(this, R.id.list_view_timeline);
        mListView.setAdapter(new PaintingsAdapter(this));

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

        switch (item.type){

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
                try {
                    timeline_action.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(item.uri)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }

            case PostalDataItem.TYPE_WEBVIEW: {
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

    private void initLeftDrawer() {
        String[] titles = new String[]{"Timeline", "Friends", "Old version"};
        ListView drawerList = (ListView) findViewById(R.id.left_drawer_timeline_new);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_timeline_new);
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, titles));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    //do nothing
                }
                else if (i == 1){
                    Intent intent = new Intent();
                    intent.setClass(Timeline.this, MyFriends.class);
                    startActivity(intent);
                    finish();
                }
                else if (i == 2){
                    Intent intent = new Intent();
                    intent.setClass(Timeline.this, Splash.class);
                    startActivity(intent);
                    finish();
                }
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
}
