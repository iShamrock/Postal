package iShamrock.Postal.activity;

import android.os.Bundle;
import com.alexvasilkov.android.commons.utils.Views;
import com.alexvasilkov.foldablelayout.FoldableListLayout;
import iShamrock.Postal.R;
import iShamrock.Postal.items.PaintingsAdapter;

/**
 * Created by lifengshuang on 1/8/15.
 */
public class FoldableListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foldable_list);

        FoldableListLayout foldableListLayout = Views.find(this, R.id.foldable_list);
        foldableListLayout.setAdapter(new PaintingsAdapter(this));
    }

}
