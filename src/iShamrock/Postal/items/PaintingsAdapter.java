package iShamrock.Postal.items;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.Timeline;
import iShamrock.Postal.commons.adapters.ItemsAdapter;
import iShamrock.Postal.commons.utils.Views;
import iShamrock.Postal.database.Database;
import iShamrock.Postal.entity.PostalDataItem;
import iShamrock.Postal.util.SystemUtil;

import java.util.Arrays;

//import com.squareup.picasso.Picasso;

public class PaintingsAdapter extends ItemsAdapter<Painting> implements View.OnClickListener {

    Context context;
    Resources resources;
    ContentResolver contentResolver;

    public PaintingsAdapter(Context context) {
        super(context);
        resources = context.getResources();
        contentResolver = context.getContentResolver();
        setItemsList(Arrays.asList(Painting.getAllPaintings(context.getResources())));
    }

    @Override
    public void notifyDataSetChanged() {
        setItemsList(Arrays.asList(Painting.getAllPaintings(resources)));
        super.notifyDataSetChanged();
    }

    @Override
    protected View createView(Painting item, int pos, ViewGroup parent, LayoutInflater inflater) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.total = Views.find(view, R.id.total_list_item);
        vh.linearLayout = Views.find(view, R.id.timeline_animation);
        vh.linearLayout.setOnClickListener(this);
        vh.contents = Views.find(view, R.id.list_contents);
        vh.name = Views.find(view, R.id.list_item_name);
        vh.text = Views.find(view, R.id.list_item_title);
        vh.imageView = Views.find(view, R.id.list_item_image);
        vh.imageView2 = Views.find(view, R.id.list_item_image_2);
        vh.cover = Views.find(view, R.id.list_cover);
        vh.profile = Views.find(view, R.id.profile_photo);
        view.setTag(vh);
        return view;
    }

    /**
     * modify all the view's things about UI here
     * It's preferred to change thing only in switch/case, or you debug yourself when something unexpected happens
     *
     * @param item:
     */
    @Override
    protected void bindView(Painting item, int pos, View convertView) {
        ViewHolder vh = (ViewHolder) convertView.getTag();
        vh.linearLayout.setTag(item);
        if (item.getItem().time.equals("cover")) {
            vh.total.removeAllViews();
            vh.total.addView(vh.cover);
            vh.total.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyDataSetChanged();
                }
            });
            return;
        } else {
            vh.total.removeAllViews();
            vh.total.addView(vh.contents);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver,
                        Uri.parse(Database.getPhotoURIWithName(item.getItem().from_user)));
                vh.profile.setImageBitmap(SystemUtil.toRoundCorner(bitmap));
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
        String text = item.getItem().text;
        if (text.length() > 50) {
            text = text.substring(0, 50) + "......";
        }
        vh.imageView2.setImageBitmap(null);
        vh.imageView.setImageBitmap(null);
        switch (item.getItem().type) {
            case PostalDataItem.TYPE_IMAGE:
                try {
                    vh.imageView2.setImageBitmap(MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(item.getItem().uri)));
                } catch (Exception e) {
//                    e.printStackTrace();
                }
                vh.text.setText(text);
                vh.name.setText(Database.getNameWithPhone(item.getItem().from_user));
                break;
            case PostalDataItem.TYPE_VIDEO:
                //VideoView videoView = new VideoView(getContext());
                //videoView.setVideoURI(Uri.parse(item.getItem().uri));
                //vh.frameLayout.addView(videoView);
                Bitmap video = BitmapFactory.decodeResource(resources, R.drawable.video_cover);
//                Bitmap resizedBitmap = Bitmap.createScaledBitmap(video, 300, 300, false);
                vh.imageView.setImageBitmap(video);
                vh.text.setText(text);
                vh.name.setText(Database.getNameWithPhone(item.getItem().from_user));
                break;
            case PostalDataItem.TYPE_AUDIO:
//                ImageView audioImageView = new ImageView(getContext());
//                audioImageView.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.voice_message_playing));
//                audioImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                vh.frameLayout.addView(audioImageView);
                vh.text.setText(text);
                vh.name.setText(Database.getNameWithPhone(item.getItem().from_user));
                Bitmap audio = BitmapFactory.decodeResource(resources, R.drawable.audio_cover);
//                audio = Bitmap.createScaledBitmap(audio, 300, 300, false);
                vh.imageView.setImageBitmap(audio);
                break;
            case PostalDataItem.TYPE_WEB:
                //todo
                break;
            case PostalDataItem.TYPE_TEXT:
                vh.text.setLines(8);
                vh.text.setText(text);
                vh.linearLayout.setOnClickListener(null);
                vh.name.setText(Database.getNameWithPhone(item.getItem().from_user));
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getContext() instanceof Timeline) {
            Timeline activity = (Timeline) view.getContext();
            activity.openDetails(view, (Painting) view.getTag());
        }
    }

    private static class ViewHolder {
        LinearLayout total;
        LinearLayout linearLayout;
        LinearLayout contents;
        FrameLayout cover;
        TextView name;
        ImageView imageView;
        ImageView imageView2;
        TextView text;
        ImageView profile;
    }

}
