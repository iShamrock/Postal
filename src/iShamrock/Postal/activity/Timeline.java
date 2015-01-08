package iShamrock.Postal.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import iShamrock.Postal.R;
import iShamrock.Postal.commons.utils.Views;
import iShamrock.Postal.foldablelayout.UnfoldableView;
import iShamrock.Postal.foldablelayout.shading.GlanceFoldShading;
import iShamrock.Postal.items.Painting;
import iShamrock.Postal.items.PaintingsAdapter;

import java.io.IOException;


/**
 * Created by lifengshuang on 11/27/14.
 */
public class Timeline extends BaseActivity {

    private ListView mListView;
    private View mListTouchInterceptor;
    private View mDetailsLayout;
    private UnfoldableView mUnfoldableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setTitle("Postal Box");
        initUnfoldableDetailsActivity();
        initLeftDrawer();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem add = menu.add("");
        add
                .setIcon(R.drawable.plus)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent intent = new Intent();
                        intent.setClass(Timeline.this, PostalEditor.class);
                        startActivity(intent);
                        finish();
                        return false;
                    }
                })
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    private void initUnfoldableDetailsActivity(){
        mListView = Views.find(this, R.id.list_view);
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
        ImageView image = Views.find(mDetailsLayout, R.id.details_image);
        TextView title = Views.find(mDetailsLayout, R.id.details_title);
        TextView description = Views.find(mDetailsLayout, R.id.details_text);
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

/*        SpannableBuilder builder = new SpannableBuilder(this);
        builder
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append(R.string.year).append(": ")
                .clearStyle()
                .append(painting.getYear()).append("\n")
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append(R.string.location).append(": ")
                .clearStyle()
                .append(painting.getLocation());*/

        description.setText(painting.getContent() + "\n" + painting.getYear());

        mUnfoldableView.unfold(coverView, mDetailsLayout);
    }

/*    private void initFoldableListLayout(){
        FoldableListLayout foldableListLayout = (FoldableListLayout) findViewById(R.id.listView_timeline);
        foldableListLayout.setAdapter(new PaintingsAdapter(this));

    }*/

    private void initLeftDrawer() {
        String[] titles = new String[]{"Postal Box", "In the map", "Edit Postal", "My Posts"};
        ListView drawerList = (ListView) findViewById(R.id.left_drawer_timeline);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_timeline);
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
        } else if (i == 2) {
            Intent intent = new Intent();
            intent.setClass(this, PostalEditor.class);
            startActivity(intent);
            finish();
        } else if (i == 3) {
            Intent intent = new Intent();
            intent.setClass(this, FoldableListActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
