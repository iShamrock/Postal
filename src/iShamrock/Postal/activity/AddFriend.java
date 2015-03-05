package iShamrock.Postal.activity;

import android.app.ListActivity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import iShamrock.Postal.R;
import iShamrock.Postal.database.Database;
import iShamrock.Postal.entity.User;

import java.util.ArrayList;

/**
 * Created by lifengshuang on 2/22/15.
 */
public class AddFriend extends ListActivity {

    //    ListView listView;
    EditText searchName;
    ImageView search;
    Runnable run;

    ArrayList<User> allUsers;
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
            }
        };
        allUsers = Database.getAllUsers();
        friends = Database.getFriend();
        initComponents();
    }

    private void initComponents() {
        searchName = (EditText) findViewById(R.id.search_name);
        search = (ImageView) findViewById(R.id.search_button_add_friend);

        adapter = new AddFriendAdapter(this);
        setListAdapter(adapter);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadListData();
            }
        });
    }

    private void loadListData(){
        String name = searchName.getText().toString();
        list = new ArrayList<User>();
        for (User i : allUsers) {

            friends = Database.getFriend();
            boolean isFriend = false;
            for (User j : friends){
                if (j.getPhone().equals(i.getPhone())){
                    isFriend = true;
                    break;
                }
            }

            if (i.getNickname() != null && i.getPhone() != null) {
                if (i.getNickname().startsWith(name)
                        && !i.getPhone().equals(Database.me.getPhone())
                        && !isFriend) {
                    list.add(i);
                }
            }
        }
        runOnUiThread(run);
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
            } catch (Exception e) {
                e.printStackTrace();
                holder.imageView.setImageResource(R.drawable.zhihu);
            }


            holder.name.setText(list.get(i).getNickname());
            holder.phone.setText(list.get(i).getPhone());
            final int i_copy = i;
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Database.addFriend(list.get(i_copy));
                    loadListData();
                    Toast.makeText(AddFriend.this, "Added!", Toast.LENGTH_SHORT).show();
                }
            });
            runOnUiThread(run);
            return view;
        }
    }
}
