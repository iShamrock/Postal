package iShamrock.Postal.luanqibazao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import iShamrock.Postal.entity.PostalData;
import iShamrock.Postal.R;

/**
 * Created by lifengshuang on 11/29/14.
 */
public class AddPostal_kind extends Activity {

    private int postalKind = PostalData.TEST_TYPE;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpostal_kind);
        initButtonListeners();
    }

    @Override
    protected void onRestart() {
        finish();
    }

    private void initButtonListeners(){
        Button postal = (Button)findViewById(R.id.postal_addpostal_kind);
        postal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(activity, AddPostal_contents.class);
                intent.putExtra("postal_kind", postalKind);
                activity.startActivity(intent);
            }
        });

    }
}
