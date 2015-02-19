package iShamrock.Postal.activity.publishers;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Tong on 02.18.
 */
public class ButtonTouchAnimationListener implements View.OnTouchListener {
    private int alphaOnPress = 128;
    private ImageView respondingView;

    public ButtonTouchAnimationListener(ImageView view, int alphaOnPress) {
        this.alphaOnPress = alphaOnPress;
        this.respondingView = view;
        view.setLongClickable(true);
    }

    public ButtonTouchAnimationListener(ImageView view) {
        this.respondingView = view;
        view.setLongClickable(true);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                respondingView.setAlpha(alphaOnPress);
                break;
            case MotionEvent.ACTION_UP:
                respondingView.setAlpha(255);
                break;
        }
        return false;
    }
}
