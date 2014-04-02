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
package bbct.android.premium;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import bbct.android.common.provider.BaseballCardContract;

public class BaseballCardList extends bbct.android.common.activity.BaseballCardList {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates an adapter and fills the rows with values
     * for brand, year, player name, number and front image.
     */
    @Override
    public void FillBaseballCardsListOnCreate() {
        super.adapter = new BbctCursorAdapterPremium(this, R.layout.row, null,
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
            Intent intent = new Intent(Intent.ACTION_EDIT,
                    BaseballCardDetails.DETAILS_URI);
            intent.setType(BaseballCardContract.BASEBALL_CARD_ITEM_MIME_TYPE);
            this.startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);  
        }
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
}
