package iShamrock.Postal.activity.publishers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import iShamrock.Postal.R;
import iShamrock.Postal.activity.Timeline;
import iShamrock.Postal.entity.PostalDataItem;

/**
 * Created by lifengshuang on 2/21/15.
 */
public class TypeChooser extends Activity{

    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_chooser);
        String intentString = getIntent().getStringExtra("journal_or_postal");
        if (intentString.equals("journal")){
            intent.setClass(this, JEditor.class);
        }
        else {
            intent.setClass(this, PEditor.class);
        }
        initComponents();
    }

    private void initComponents(){
        ImageView cancel = (ImageView) findViewById(R.id.type_chooser_icon_cancel);
        LinearLayout text = (LinearLayout) findViewById(R.id.type_text);
        LinearLayout image = (LinearLayout) findViewById(R.id.type_image);
        LinearLayout audio = (LinearLayout) findViewById(R.id.type_audio);
        LinearLayout video = (LinearLayout) findViewById(R.id.type_video);
        LinearLayout web = (LinearLayout) findViewById(R.id.type_web);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setClass(TypeChooser.this, Timeline.class);
                startActivity(intent);
                finish();
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", PostalDataItem.TYPE_TEXT);
                startActivity(intent);
                finish();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", PostalDataItem.TYPE_IMAGE);
                startActivity(intent);
                finish();
            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", PostalDataItem.TYPE_AUDIO);
                startActivity(intent);
                finish();
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", PostalDataItem.TYPE_VIDEO);
                startActivity(intent);
                finish();
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", PostalDataItem.TYPE_WEB);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            intent.setClass(TypeChooser.this, Timeline.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }
}
