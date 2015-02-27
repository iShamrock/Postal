package iShamrock.Postal.activity;

import android.app.ListActivity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import iShamrock.Postal.R;
import iShamrock.Postal.database.Database;
import iShamrock.Postal.entity.User;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lifengshuang on 2/22/15.
 */
public class AddFriend extends ListActivity {

    //    ListView listView;
    EditText searchName;
    ImageView search;
    Runnable run;

    ArrayList<User> friends;
    ArrayList<User> list = new ArrayList<User>();
    AddFriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);
        run = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                //onContentChanged();
            }
        };
        friends = Database.getAllUsers();
        initComponents();
    }

    private void initComponents() {
//        listView = (ListView) findViewById(R.id.listView_add_friend);
        searchName = (EditText) findViewById(R.id.search_name);
        search = (ImageView) findViewById(R.id.search_button_add_friend);

/*        adapter = new SimpleAdapter(this, list, R.layout.add_friend_item, new String[]{"name", "photo", "button"},
                new int[]{R.id.username_add_friend, R.id.photo_add_friend, R.id.button_add_friend});*/
//        listView.setAdapter(adapter);
        adapter = new AddFriendAdapter(this);
        setListAdapter(adapter);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = searchName.getText().toString();
                list = new ArrayList<User>();
                for (User i : friends) {
                    if (i.getNickname().startsWith(name)) {
                        list.add(i);
                    }
                }
/*                list.clear();
                Map<String, Object> map = new HashMap<String, Object>();
                if (found.size() == 0){
                    map.put("photo", "");
                    map.put("name", "No result");
                    map.put("button", null);
                    list.add(map);
                    System.out.println("not found");
                }
                else {
                    for (final User i : found) {
                        map = new HashMap<String, Object>();
                        map.put("name", i.getNickname());
                        ImageView imageView = (ImageView) findViewById(R.id.photo_add_friend);
                        try {
                            imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(i.getPhotoURI())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        map.put("photo", imageView);
                        Button button = (Button) findViewById(R.id.button_add_friend);
                        button.setText("Add");
*//*
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Database.addFriend(i);
                            }
                        });
*//*
                        map.put("button", button);
                        list.add(map);
                    }
                }
                System.out.println("try to run");*/
                runOnUiThread(run);
            }
        });
    }

    public final class ViewHolder {
        ImageView imageView;
        TextView name;
        TextView phone;
        ImageView add;
    }

    public class AddFriendAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public AddFriendAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = mInflater.inflate(R.layout.add_friend_item, null);
                holder.imageView = (ImageView) view.findViewById(R.id.photo_add_friend);
                holder.name = (TextView) view.findViewById(R.id.username_add_friend);
                holder.phone = (TextView) view.findViewById(R.id.phone_add_friend);
                holder.add = (ImageView) view.findViewById(R.id.button_add_friend);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            try {
                holder.imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(list.get(i).getPhotoURI())));
            } catch (IOException e) {
                e.printStackTrace();
            }/*
            if (holder.phone == null){
                System.out.println("phone");
            }
            if (list.get(i) == null){
                System.out.println("i null");
            }
            if (list.get(i).getPhone() == null){
                System.out.println("user phone null");
            }*/


            holder.name.setText(list.get(i).getNickname());
            holder.phone.setText(list.get(i).getPhone());
            final int i_copy = i;
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Database.addFriend(list.get(i_copy));
                    //list.remove(i_copy);
                }
            });
            runOnUiThread(run);
            return view;
        }
    }
}
