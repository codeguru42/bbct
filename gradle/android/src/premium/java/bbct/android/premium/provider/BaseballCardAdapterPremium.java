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
package bbct.android.premium.provider;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.widget.ImageView;
import bbct.android.common.BbctPictureHelper;
import bbct.android.common.R;
import bbct.android.common.provider.BaseballCardAdapter;

public class BaseballCardAdapterPremium extends BaseballCardAdapter {

    /*private ImageView currentView;
    private String currentValue;*/

    public BaseballCardAdapterPremium(Context context, int layout, Cursor c,
            String[] from, int[] to) {
        super(context, layout, c, from, to);
    }
    
    /**
     * Called by bindView() to set the image for an ImageView but only
     * if there is no existing ViewBinder or if the existing ViewBinder
     * cannot handle binding to an ImageView. By default, the value will
     * be treated as an image resource. If the value cannot be used as an
     * image resource, the value is used as an image Uri. So this method is overridden
     * to treat value as the path to the image.
     *
     * @param view
     *          ImageView to receive an image
     * @param value
     *          The value retrieved from the data set
     */
    @Override
    public void setViewImage(ImageView view, String value) {
        //this.currentView = view;
        //this.currentValue = value;
        int width = 48;
        int height = 48;
        if (value.equals("")) {
            view.setImageResource(R.drawable.no_card_image_on_list);
        }
        else {
            BbctPictureHelper pictureHelper = new BbctPictureHelper();
            Bitmap frontImage = pictureHelper.GetScaledImageFromPath(value, width, height);
            view.setImageBitmap(frontImage);
        }
    }
}
