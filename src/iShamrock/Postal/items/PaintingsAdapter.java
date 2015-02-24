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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

//import com.squareup.picasso.Picasso;
import android.widget.VideoView;
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
        vh.frameLayout = Views.find(view, R.id.timeline_animation);
        vh.frameLayout.setOnClickListener(this);
        vh.title = Views.find(view, R.id.list_item_title);
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
        vh.frameLayout.setTag(item);
        switch (item.getItem().type){
            case PostalDataItem.TYPE_IMAGE:
                ImageView imageView = new ImageView(getContext());
                try {
                    imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(item.getItem().uri)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                vh.frameLayout.addView(imageView);
                break;
            case PostalDataItem.TYPE_VIDEO:
                VideoView videoView = new VideoView(getContext());
                videoView.setVideoURI(Uri.parse(item.getItem().uri));
                vh.frameLayout.addView(videoView);
                break;
            case PostalDataItem.TYPE_AUDIO:
                ImageView audioImageView = new ImageView(getContext());
                audioImageView.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.voice_message_playing));
                audioImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                vh.frameLayout.addView(audioImageView);
                break;
            case PostalDataItem.TYPE_WEB:
                //todo
                break;
            case PostalDataItem.TYPE_TEXT:
                TextView textView = new TextView(getContext());
                textView.setText(item.getItem().text);
                textView.setPadding(30, 20, 8, 8);
                vh.frameLayout.addView(textView);
                vh.frameLayout.setOnClickListener(null);
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
        FrameLayout frameLayout;
        TextView title;
    }

}
