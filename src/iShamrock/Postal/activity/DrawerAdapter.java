package iShamrock.Postal.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import iShamrock.Postal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tong on 03.08.
 */
public class DrawerAdapter extends BaseAdapter {
    private Context context;
    private List<String> drawerList;
    private List<Integer> iconList;

    public final class ViewHolder {
        LinearLayout layout;
        TextView text;
        ImageView icon;
    }

    private LayoutInflater mInflater;

    public DrawerAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);

        drawerList = new ArrayList<String>();
        drawerList.add("Postal");
        drawerList.add("TIMELINE");
        drawerList.add("FRIENDS");
        drawerList.add("SEND POSTAL");
        drawerList.add("TAKE A NOTE");
        drawerList.add("TAKE AN IMAGE");
        drawerList.add("TAKE A VIDEO");
        drawerList.add("TAKE AN AUDIO");
        drawerList.add("TAKE A WEB PART");
        drawerList.add("CONNECT WEAR");
        drawerList.add("LOG OUT");

        iconList = new ArrayList<Integer>();
        iconList.add(R.drawable.drawer_timeline);
        iconList.add(R.drawable.drawer_timeline);
        iconList.add(R.drawable.drawer_friends);
        iconList.add(R.drawable.drawer_postal);
        iconList.add(R.drawable.drawer_text);
        iconList.add(R.drawable.drawer_image);
        iconList.add(R.drawable.drawer_video);
        iconList.add(R.drawable.drawer_audio);
        iconList.add(R.drawable.drawer_web);
        iconList.add(R.drawable.drawer_wear);
        iconList.add(R.drawable.drawer_exit);
    }

    @Override
    public int getCount() {
        return drawerList.size();
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
            if (i == 0) {
                /*postal*/
                view = mInflater.inflate(R.layout.drawer_list_title, null);
                holder.layout = (LinearLayout) view.findViewById(R.id.drawer_title_layout);
                holder.text = (TextView) view.findViewById(R.id.drawer_title_text);
            } else {
                view = mInflater.inflate(R.layout.drawer_list_item, null);
                holder.icon = (ImageView) view.findViewById(R.id.drawer_icon);
                holder.layout = (LinearLayout) view.findViewById(R.id.drawer_layout);
                holder.text = (TextView) view.findViewById(R.id.drawer_text);
                holder.icon.setImageDrawable(context.getResources().getDrawable(iconList.get(i)));
            }
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.text.setText(drawerList.get(i));
        return view;
    }
}
