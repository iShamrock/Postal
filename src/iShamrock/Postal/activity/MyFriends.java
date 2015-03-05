package iShamrock.Postal.activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.publishers.ButtonTouchAnimationListener;
import iShamrock.Postal.activity.publishers.PEditor;
import iShamrock.Postal.database.Database;
import iShamrock.Postal.entity.User;

import java.util.ArrayList;

/**
 * Created by lifengshuang on 2/15/15.
 */
public class MyFriends extends ListActivity {

    private FriendAdapter adapter;
    private ImageView friends_add, friends_cancel;
    private ArrayList<User> friend = Database.getFriend();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);

        initCommonComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null){
            friend = Database.getFriend();
            adapter.notifyDataSetChanged();
        }
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
        adapter = new FriendAdapter(this);
        setListAdapter(adapter);
        friends_cancel = (ImageView) findViewById(R.id.friends_cancel);
        friends_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        friends_cancel.setOnTouchListener(new ButtonTouchAnimationListener(friends_cancel));
    }


    public final class ViewHolder {
        ImageView imageView;
        TextView name;
        ImageView send;
    }

    public class FriendAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public FriendAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return friend.size();
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = mInflater.inflate(R.layout.friends_item, null);
                holder.imageView = (ImageView) view.findViewById(R.id.friends_img);
                holder.name = (TextView) view.findViewById(R.id.friends_name);
                holder.send = (ImageView) view.findViewById(R.id.friends_send);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            try {
                holder.imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(friend.get(i).getPhotoURI())));
            } catch (Exception e) {
                e.printStackTrace();
                holder.imageView.setImageResource(R.drawable.zhihu);
            }
            holder.name.setText(friend.get(i).getNickname());
            final int i_copy = i;
            holder.send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MyFriends.this, PEditor.class);
                    intent.putExtra("to_user", friend.get(i_copy).getPhone());
                    startActivity(intent);
                    finish();
                }
            });
            return view;
        }
    }
}

