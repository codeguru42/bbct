package bbct.android.premium.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardList;
import bbct.android.common.activity.FragmentTags;
import bbct.android.common.activity.MainActivity;
import bbct.android.common.provider.BaseballCardContract;

public class PremiumActivity extends MainActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void setHolderActivity() {
        Uri uri = BaseballCardContract.getUri(this.getPackageName());
        Cursor cursor = this.getContentResolver().query(uri,
                BaseballCardContract.PROJECTION, null, null, null);

        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        if (cursor == null || cursor.getCount() == 0) {
            ft.add(R.id.fragment_holder, new PremiumCardDetails(), FragmentTags.EDIT_CARD);
        } else {
            ft.add(R.id.fragment_holder, new BaseballCardList(), FragmentTags.CARD_LIST);
        }
        ft.commit();
    }
}
