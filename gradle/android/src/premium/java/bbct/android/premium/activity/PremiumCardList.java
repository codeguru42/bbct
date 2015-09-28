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
package bbct.android.premium.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.FragmentTags;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardContract;
import bbct.android.premium.provider.BaseballCardAdapterPremium;

public class PremiumCardList extends bbct.android.common.activity.BaseballCardList {

    public static PremiumCardList getInstance(Bundle filterArgs) {
        PremiumCardList cardList = new PremiumCardList();

        Bundle args = new Bundle();
        args.putBundle(FILTER_PARAMS, filterArgs);
        cardList.setArguments(args);

        return cardList;
    }
    /**
     * Creates an adapter and fills the rows with values
     * for brand, year, player name, number and front image.
     */
    @Override
    public void fillBaseballCardsListOnCreate() {
        super.adapter = new BaseballCardAdapterPremium(this.getActivity(), R.layout.row, null,
                ROW_PROJECTION_PREMIUM, ROW_TEXT_VIEWS_PREMIUM);
    }

    /**
     * Respond to the user selecting a menu item.
     *
     * @param item
     *            The menu item selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.add_menu) {
            PremiumCardDetails details = new PremiumCardDetails();
            this.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_holder, details)
                    .addToBackStack(EDIT_CARD)
                    .commit();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Respond to the user clicking on an item in the list. If the user clicks
     * on a checkbox, then the corresponding row will be marked for the next
     * delete operation. Otherwise, the app will display the card data for
     * editing in a {@link BaseballCardDetails}.
     *
     * @param l        The ListView where the click happened.
     * @param v        The view that was clicked within the ListView.
     * @param position The position of the view in the list.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (position == 0) {
            return;
        }

        BaseballCard card = PremiumCardList.this.adapter.getItem(position - 1);

        Fragment details = PremiumCardDetails.getInstance(id, card);
        this.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, details, FragmentTags.EDIT_CARD)
                .addToBackStack(FragmentTags.EDIT_CARD)
                .commit();
    }

    private static final String[] ROW_PROJECTION_PREMIUM = {
        BaseballCardContract.BRAND_COL_NAME,
        BaseballCardContract.YEAR_COL_NAME,
        BaseballCardContract.PLAYER_NAME_COL_NAME,
        BaseballCardContract.NUMBER_COL_NAME,
        BaseballCardContract.PATH_TO_PICTURE_FRONT};

    private static final int[] ROW_TEXT_VIEWS_PREMIUM = { R.id.brand_text_view,
        R.id.year_text_view, R.id.player_name_text_view,
        R.id.number_text_view, R.id.card_image_view };

    private static final String EDIT_CARD = "Edit Card";
}
