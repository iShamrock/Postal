package iShamrock.Postal.activity.publishers;

import android.app.Activity;
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
public class ChooseFriendToSendTo extends ListActivity {
    private ImageView cho_cancel;
    ArrayList<User> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.choose_friend);
        super.onCreate(savedInstanceState);
        friends = Database.getFriend();
        initCommonComponents();
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
        setListAdapter(new ChooseFriendAdapter(this));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        User user = friends.get(position);
        Intent intent = new Intent();
        intent.putExtra("name", user.getNickname());
        setResult(RESULT_OK, intent);
    }

    public final class ViewHolder{
        ImageView imageView;
        TextView name;
        TextView phone;
    }

    public class ChooseFriendAdapter extends BaseAdapter{

        private LayoutInflater mInflater;

        public ChooseFriendAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return friends.size();
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
            if (view == null){
                holder = new ViewHolder();
                view = mInflater.inflate(R.layout.add_friend_item, null);
                holder.imageView = (ImageView) view.findViewById(R.id.photo_add_friend);
                holder.name = (TextView) view.findViewById(R.id.username_add_friend);
                holder.phone = (TextView) view.findViewById(R.id.phone_add_friend);
                view.setTag(holder);
            }
            else {
                holder = (ViewHolder)view.getTag();
            }
            try {
                holder.imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(friends.get(i).getPhotoURI())));
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
            holder.name.setText(friends.get(i).getNickname());
            holder.phone.setText(friends.get(i).getPhone());
            return view;
        }
    }
}


