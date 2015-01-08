package iShamrock.Postal.items;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexvasilkov.android.commons.adapters.ItemsAdapter;
import com.alexvasilkov.android.commons.utils.Views;
//import com.squareup.picasso.Picasso;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.Timeline;

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

    @Override
    protected View createView(Painting item, int pos, ViewGroup parent, LayoutInflater inflater) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder();
        vh.image = Views.find(view, R.id.list_item_image);
        vh.image.setOnClickListener(this);
        vh.title = Views.find(view, R.id.list_item_title);
        view.setTag(vh);
        return view;
    }

    @Override
    protected void bindView(Painting item, int pos, View convertView) {
        ViewHolder vh = (ViewHolder) convertView.getTag();
        vh.image.setTag(item);
        if (item.isLocal()) {
            vh.image.setImageBitmap(BitmapFactory.decodeResource(resources, item.getImageId()));
            //Picasso.with(convertView.getContext()).load(item.getImageId()).noFade().into(vh.image);
            vh.title.setText(item.getTitle());
        }
        else {
            try {
                vh.image.setImageBitmap(MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(item.getUri())));
                vh.title.setText(item.getTitle());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        ImageView image;
        TextView title;
    }

}
