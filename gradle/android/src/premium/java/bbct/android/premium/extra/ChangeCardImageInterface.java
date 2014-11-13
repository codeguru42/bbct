package bbct.android.premium.extra;

import bbct.android.common.data.BaseballCard;

public interface ChangeCardImageInterface {

    public void onChangeCardSelected(boolean frontImage, BaseballCard card);
    public void onChangeCardClosed(BaseballCard card);

}
