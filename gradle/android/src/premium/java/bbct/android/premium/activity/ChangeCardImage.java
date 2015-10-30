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

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import android.support.v7.app.ActionBarActivity;
import bbct.android.common.BbctPictureHelper;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import java.io.File;
import java.io.IOException;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.annotation.SuppressLint;
import android.os.Build;

import android.graphics.BitmapFactory;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import android.widget.ImageView;

import bbct.android.common.R;
import bbct.android.common.data.BaseballCard;

import android.os.Bundle;

import android.app.Activity;

public class ChangeCardImage extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        card = null;
        Bundle args = this.getArguments();
        if(args != null) {
            card = (BaseballCard)args.getSerializable("card");
        }


        return super.onCreateView(inflater, container, savedInstanceState);
    }
    /**
     * Respond to the user selecting a menu item.
     * If change picture is selected, a new camera intent is
     * initiated
     * If remove picture is selected, the image is reset to null
     * and the activity is closed.
     * If home button is selected, the activity is closed.
     * @param item
     *            The menu item selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            ((PremiumActivity)getActivity()).onChangeCardClosed(card);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    private BaseballCard card = null;
    private static final String TAG = ChangeCardImage.class.getName();
}
