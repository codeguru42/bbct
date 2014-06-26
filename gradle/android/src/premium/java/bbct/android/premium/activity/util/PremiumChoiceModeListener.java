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

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardList;
import bbct.android.common.activity.util.BaseballCardActionModeCallback;
import bbct.android.premium.social.ResponseListener;
import org.brickred.socialauth.android.SocialAuthAdapter;

public class PremiumChoiceModeListener extends BaseballCardActionModeCallback {

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
        mSocialAuthAdapter.addProvider(SocialAuthAdapter.Provider.FLICKR, R.drawable.flickr);
        mSocialAuthAdapter.addProvider(SocialAuthAdapter.Provider.INSTAGRAM, R.drawable.instagram);

        boolean result = super.onCreateActionMode(mode, menu);
        final MenuItem item = menu.findItem(R.id.share_menu);
        mSocialAuthAdapter.enable(item.getActionView());

        return result;
    }
}
