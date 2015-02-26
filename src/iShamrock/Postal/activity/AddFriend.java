package iShamrock.Postal.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import iShamrock.Postal.R;
import iShamrock.Postal.database.Database;
import iShamrock.Postal.entity.User;

import java.io.IOException;
import java.util.*;

/**
 * Created by lifengshuang on 2/22/15.
 */
public class AddFriend extends Activity{

    ListView listView;
    EditText searchName;
    ImageView search;

    ArrayList<User> friends;
    List<Map<String, Object>> list;
    SimpleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);

        friends = Database.getFriend();
        initComponents();
    }

    private void initComponents() {
        listView = (ListView) findViewById(R.id.listView_add_friend);
        searchName = (EditText) findViewById(R.id.search_name);
        search = (ImageView) findViewById(R.id.search_button_add_friend);

        adapter = new SimpleAdapter(this, list, R.layout.add_friend_item, new String[]{"name", "photo", "button"},
                new int[]{R.id.username_add_friend, R.id.photo_add_friend, R.id.button_add_friend});
        listView.setAdapter(adapter);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = searchName.getText().toString();
                ArrayList<User> found = new ArrayList<User>();
                for (User i : friends){
                    if (i.getName().startsWith(name)){
                        found.add(i);
                    }
                }
                list = new ArrayList<Map<String, Object>>();
                Map<String, Object> map = new HashMap<String, Object>();
                if (found.size() == 0){
                    map.put("photo", "");
                    map.put("name", "No result");
                    map.put("button", null);
                    list.add(map);
                }
                else {
                    for (final User i : found) {
                        map = new HashMap<String, Object>();
                        map.put("name", i.getName());
                        ImageView imageView = (ImageView) findViewById(R.id.photo_add_friend);
                        try {
                            imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(i.getPhotoURI())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        map.put("photo", imageView);
                        Button button = (Button) findViewById(R.id.button_add_friend);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Database.addFriend(i);
                            }
                        });
                        map.put("button", button);
                        list.add(map);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


}
