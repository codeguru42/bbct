/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT for Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT for Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.android.premium.activity.util;

import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardList;
import bbct.android.common.activity.util.BaseballCardActionModeCallback;
import bbct.android.common.data.BaseballCard;
import java.util.List;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

public class PremiumChoiceModeListener extends BaseballCardActionModeCallback {

    private static final String TAG = PremiumChoiceModeListener.class.getName();
    private static final String TWITTER_CALLBACK = "bbct-premium-oauth-twitter://callback";
    private SocialAuthAdapter mSocialAuthAdapter;

    public PremiumChoiceModeListener(BaseballCardList listFragment) {
        super(listFragment);
    }

    /**
     * Called when action mode is first created. The menu supplied will be used to
     * generate action buttons for the action mode.
     *
     * @param mode ActionMode being created
     * @param menu Menu used to populate action buttons
     * @return true if the action mode should be created, false if entering this
     * mode should be aborted.
     */
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mSocialAuthAdapter = new SocialAuthAdapter(new ResponseListener());
        mSocialAuthAdapter.addProvider(SocialAuthAdapter.Provider.FACEBOOK, R.drawable.facebook);
        mSocialAuthAdapter.addProvider(SocialAuthAdapter.Provider.TWITTER, R.drawable.twitter);
        mSocialAuthAdapter.addCallBack(SocialAuthAdapter.Provider.TWITTER, TWITTER_CALLBACK);

        boolean result = super.onCreateActionMode(mode, menu);
        final MenuItem item = menu.findItem(R.id.share_menu);
        mSocialAuthAdapter.enable(item.getActionView());

        return result;
    }

    private class ResponseListener implements DialogListener {

        @Override
        public void onComplete(Bundle bundle) {
            List<BaseballCard> cards = mListFragment.getSelectedCards();

            for (BaseballCard card : cards) {
                String message = String.valueOf(card.getYear()) + ' ' + card.getBrand() + ' '
                        + card.getPlayerName() + '\n' + mListFragment.getString(R.string.sent_with);
                mSocialAuthAdapter.updateStatus(message, new MessageListener(), false);
            }
        }

        @Override
        public void onError(SocialAuthError socialAuthError) {
            socialAuthError.printStackTrace();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onBack() {

        }

    }

    private class MessageListener implements SocialAuthListener<Integer> {

        @Override
        public void onExecute(String provider, Integer status) {
            if (status == 200 || status == 201 || status == 204) {
                Toast.makeText(mListFragment.getActivity(), "Card posted on " + provider,
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mListFragment.getActivity(), "Card not posted on" + provider,
                        Toast.LENGTH_LONG).show();
            }

            finish();
        }

        @Override
        public void onError(SocialAuthError e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            Toast.makeText(mListFragment.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

}
