package bbct.android.premium.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import bbct.android.common.R;
import bbct.android.common.activity.FragmentTags;
import bbct.android.common.activity.MainActivity;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardContract;
import bbct.android.premium.extra.ChangeCardImageInterface;

public class PremiumActivity extends MainActivity
                             implements ChangeCardImageInterface {

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
            ft.add(R.id.fragment_holder, new PremiumCardList(), FragmentTags.CARD_LIST);
        }
        ft.commit();
    }
    public void onChangeCardSelected(boolean frontImage, BaseballCard card) {
        String CARD = "card";
        String FRONTIMAGE = "frontimage";
        Bundle args = new Bundle();
        //args.putString(getString(R.string.image_path_extra), imagePath);
        args.putBoolean(FRONTIMAGE, frontImage);
        args.putSerializable(CARD, card);
        ChangeCardImage changeImage = new ChangeCardImage();
        changeImage.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, changeImage)
                .addToBackStack(CHANGE_CARD)
                .commit();
    }

    public void onChangeCardClosed(BaseballCard card) {
        PremiumCardDetails details = PremiumCardDetails.getInstance(-1, card);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, details)
                .addToBackStack(CARD_DETAILS)
                .commit();

    }

    private PremiumCardDetails currentDetailsFrag = null;
    private static final String CHANGE_CARD = "ChangeCards";
    private static final String CARD_DETAILS = "cardDetails";
}
