package iShamrock.Postal.activity.publishers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.Timeline;
import iShamrock.Postal.database.Database;
import iShamrock.Postal.entity.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tong on 02.20.
 */
public class ChooseFriendToSendTo extends Activity {
    private ImageView cho_cancel;
    ArrayList<User> friends;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.choose_friend);
        super.onCreate(savedInstanceState);
        friends = Database.getFriend();
        initCommonComponents();

        /*
        * TODO : It's a big TODO.
        *
        * Add a list view in this activity to show users to send to.
        * On touch the list item, call back the user id using setResult.
        * */
    }

    private void initCommonComponents() {
        cho_cancel = (ImageView) findViewById(R.id.choosefriend_cancel);
        cho_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cho_cancel.setOnTouchListener(new ButtonTouchAnimationListener(cho_cancel));
        listView.setAdapter(new SimpleAdapter(this, getListItems(), R.layout.friends_item,
                new String[]{"img", "name"}, new int[]{R.id.friends_img, R.id.friends_name}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = friends.get(i);
                Intent intent = new Intent();
                intent.putExtra("name", user.getName());
                setResult(RESULT_OK, intent);
            }
        });
    }

    private List<Map<String, Object>> getListItems() {
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        for (User friend : friends) {
            Map<String, Object> map = new HashMap<String, Object>();
            ImageView imageView = new ImageView(this);
            try {
                imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(friend.getPhotoURI())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            map.put("img", imageView);
            map.put("name", friend.getName());
            items.add(map);
        }
        return items;
    }
}
