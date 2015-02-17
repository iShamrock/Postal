package iShamrock.Postal.items;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected View createView(Painting item, int pos, ViewGroup parent, LayoutInflater inflater) {
        View view = null;
        if (item.getItem().contentType == PostalDataItem.TYPE_TEXT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_text, parent, false);
            ViewHolder vh = new ViewHolder();
            vh.text = Views.find(view, R.id.list_item_text);
            view.setTag(vh);
        }
        else if (item.getItem().contentType == PostalDataItem.TYPE_IMAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            ViewHolder vh = new ViewHolder();
            vh.image = Views.find(view, R.id.list_item_image);
            vh.image.setOnClickListener(this);
            vh.title = Views.find(view, R.id.list_item_title);
            view.setTag(vh);
        }
        return view;
    }

    @Override
    protected void bindView(Painting item, int pos, View convertView) {
        ViewHolder vh = (ViewHolder) convertView.getTag();
        if (item.getItem().contentType == PostalDataItem.TYPE_TEXT){
            vh.text.setText(item.getItem().text);
        }
        else if (item.getItem().contentType == PostalDataItem.TYPE_IMAGE) {
            vh.image.setTag(item);
            vh.text.setText(item.getItem().title);
            try {
                vh.image.setImageBitmap(MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(item.getItem().pictureUrl)));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("error in bindView");
            }
        }
/*        if (item.getItem().isLocal()) {
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
        }*/
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
        TextView text;
    }

}
