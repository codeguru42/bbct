/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2017 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android.common.activity.test;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import bbct.android.common.BuildConfig;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DetailsTest {
    private BaseballCardDetails details;
    private View rootView;

    @Before
    public void setUp() throws Exception {
        details = new BaseballCardDetails();
        startFragment(details);
        rootView = details.getView();
    }

    @Test
    public void createFragment() {
        assertNotNull(rootView);
        assertNotNull(details);
    }

    @Test
    public void brandHasFocus() {
        View brandEdit = rootView.findViewById(R.id.brand_text);
        assertTrue(brandEdit.hasFocus());
    }
}
