package iShamrock.Postal.activity.publishers;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import iShamrock.Postal.R;

/**
 * Created by Tong on 02.20.
 */
public class ChooseFriendToSendTo extends Activity {
    private ImageView cho_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.choose_friend);
        super.onCreate(savedInstanceState);
        initCommonComponents();

        /*
        * TODO : It's a big TODO.
        *
        * Add a list view in this activity to show users to send to.
        * On touch the list item, call back the user id using setResult.
        * */
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
    }
}
