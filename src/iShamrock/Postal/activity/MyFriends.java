package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.*;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.fore.Splash;
import iShamrock.Postal.database.Database;
import iShamrock.Postal.entity.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lifengshuang on 2/15/15.
 */
public class MyFriends extends Activity{

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        initLeftDrawer();
        initListView();
    }

    private void initListView(){
        listView = (ListView) findViewById(R.id.friends_listView);
        listView.setAdapter(new SimpleAdapter(this, getListItemData(), R.layout.friends_item,
                new String[]{"img", "name", "send"}, new int[]{R.id.friends_img, R.id.friends_name, R.id.friends_send}));
    }

    private List<Map<String, Object>> getListItemData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        ArrayList<User> friends = Database.getFriend();
        for (final User friend : friends) {
            map = new HashMap<String, Object>();
            ImageView imageView = new ImageView(this);
            try {
                imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(friend.getPhotoURI())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(MyFriends.this, Timeline.class);
                    intent.putExtra("name", friend.getName());
                    startActivity(intent);
                }
            });
            map.put("img", imageView);
            map.put("name", friend.getName());
            Button button = new Button(this);
            button.setText("Send");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(MyFriends.this, PostalEditor.class);
                    intent.putExtra("name", friend.getName());
                    startActivity(intent);
                }
            });
            map.put("send", button);
            list.add(map);
        }
        return list;
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
                    Intent intent = new Intent();
                    intent.setClass(MyFriends.this, Timeline.class);
                    startActivity(intent);
                    finish();
                }
                else if (i == 1){
                    //do nothing
                }
                else if (i == 2){
                    Intent intent = new Intent();
                    intent.setClass(MyFriends.this, Splash.class);
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
