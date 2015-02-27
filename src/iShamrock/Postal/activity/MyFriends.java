package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.publishers.ButtonTouchAnimationListener;
import iShamrock.Postal.activity.publishers.PEditor;
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
public class MyFriends extends Activity {

    private ListView listView;
    private ImageView friends_add, friends_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        initListView();

        initCommonComponents();
    }

    private void initCommonComponents() {
        friends_add = (ImageView) findViewById(R.id.friends_add);
        friends_add.setOnTouchListener(new ButtonTouchAnimationListener(friends_add));
        friends_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent friendsIntent = new Intent(MyFriends.this, AddFriend.class);
                startActivity(friendsIntent);
            }
        });

        friends_cancel = (ImageView) findViewById(R.id.friends_cancel);
        friends_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        friends_cancel.setOnTouchListener(new ButtonTouchAnimationListener(friends_cancel));
    }

    private void initListView() {
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
                    intent.putExtra("name", friend.getNickname());
                    startActivity(intent);
                }
            });
            map.put("img", imageView);
            map.put("name", friend.getNickname());
            ImageView button = new ImageView(this);
//            button.setText("Send");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(MyFriends.this, PEditor.class);
                    intent.putExtra("name", friend.getNickname());
                    startActivity(intent);
                }
            });
            map.put("send", button);
            list.add(map);
        }
        return list;
    }
}
