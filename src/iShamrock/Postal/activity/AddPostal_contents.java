package iShamrock.Postal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import iShamrock.Postal.entity.PostalData;
import iShamrock.Postal.R;

import java.util.Calendar;

/**
 * Created by lifengshuang on 11/29/14.
 */
public class AddPostal_contents extends Activity{

    private int postalKind;
    private String contents;
    private Calendar time;
    private String location;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpostal_contents);
        Intent getIntent = getIntent();
        this.postalKind = getIntent.getIntExtra("postal_kind", PostalData.TEST_TYPE);
        EditText editText = (EditText)findViewById(R.id.contents_addpostal_contents);
        this.contents = editText.getText().toString();
        this.time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        location = "location not finished";
        Button send = (Button) findViewById(R.id.send_addpostal_contents);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*
                Intent intent = new Intent();
                intent.setClass(activity, Timeline.class);
                intent.putExtra("postal_kind", postalKind);
                intent.putExtra("content", content);
                intent.putExtra("time", time);
                intent.putExtra("location", location);
                activity.startActivity(intent);*/
//                PostalData.addPostal(postalKind, contents, time, location);
                activity.finish();
            }
        });
    }
}
