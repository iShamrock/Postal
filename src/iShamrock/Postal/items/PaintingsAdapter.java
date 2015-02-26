package iShamrock.Postal.items;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

//import com.squareup.picasso.Picasso;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.Timeline;
import iShamrock.Postal.commons.adapters.ItemsAdapter;
import iShamrock.Postal.commons.utils.Views;
import iShamrock.Postal.entity.PostalDataItem;

import java.io.IOException;
import java.util.Arrays;

public class PaintingsAdapter extends ItemsAdapter<Painting> implements View.OnClickListener {

    Resources resources;
    ContentResolver contentResolver;

    public PaintingsAdapter(Context context) {
        super(context);
        resources = context.getResources();
        contentResolver = context.getContentResolver();
        setItemsList(Arrays.asList(Painting.getAllPaintings(context.getResources())));
    }

    /**
     * The layout is "list_item"
     * if you add components, modify this method
     * and you just need to combine the components here, Views.find() is equal to findViewById()
     * PS. do not use "type" property here, or the strange bug may appear again = =
     * PSS. so I suggest you adding components using code in bindView method
     * @param item: don't use it
     */
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
        view.setTag(vh);
        return view;
    }

    /**
     * modify all the view's things about UI here
     * It's preferred to change thing only in switch/case, or you debug yourself when something unexpected happens
     * @param item:
     */
    @Override
    protected void bindView(Painting item, int pos, View convertView) {
        ViewHolder vh = (ViewHolder) convertView.getTag();
        vh.linearLayout.setTag(item);
        if (item.getItem().time.equals("cover")){
            vh.total.removeAllViews();
            vh.total.addView(vh.cover);
            return;
        }
        else {
            vh.total.removeAllViews();
            vh.total.addView(vh.contents);
        }
        String text = item.getItem().text;
        if (text.length() > 50){
            text = text.substring(0, 50) + "......";
        }
        vh.imageView2.setImageBitmap(null);
        vh.imageView.setImageBitmap(null);
        switch (item.getItem().type){
            case PostalDataItem.TYPE_IMAGE:
                try {
                    vh.imageView2.setImageBitmap(MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(item.getItem().uri)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vh.text.setText(text);
                vh.name.setText(item.getItem().from_user);
                break;
            case PostalDataItem.TYPE_VIDEO:
                //VideoView videoView = new VideoView(getContext());
                //videoView.setVideoURI(Uri.parse(item.getItem().uri));
                //vh.frameLayout.addView(videoView);
                Bitmap video = BitmapFactory.decodeResource(resources, R.drawable.icon_video_red);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(video, 300, 300, false);
                vh.imageView.setImageBitmap(resizedBitmap);
                vh.text.setText(text);
                vh.name.setText(item.getItem().from_user);
                break;
            case PostalDataItem.TYPE_AUDIO:
//                ImageView audioImageView = new ImageView(getContext());
//                audioImageView.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.voice_message_playing));
//                audioImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                vh.frameLayout.addView(audioImageView);
                vh.text.setText(text);
                vh.name.setText(item.getItem().from_user);
                Bitmap audio = BitmapFactory.decodeResource(resources, R.drawable.icon_audio_red);
                resizedBitmap = Bitmap.createScaledBitmap(audio, 300, 300, false);
                vh.imageView.setImageBitmap(resizedBitmap);
                break;
            case PostalDataItem.TYPE_WEB:
                //todo
                break;
            case PostalDataItem.TYPE_TEXT:
                vh.text.setText(text);
                vh.linearLayout.setOnClickListener(null);
                vh.name.setText(item.getItem().from_user);
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
    }

}
