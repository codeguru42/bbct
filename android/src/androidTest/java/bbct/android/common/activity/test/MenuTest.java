/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2016 codeguru <codeguru@users.sourceforge.net>
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

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bbct.android.common.R;
import bbct.android.common.activity.MainActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(AndroidJUnit4.class)
public abstract class MenuTest<T extends MainActivity> {
    @Rule
    public ActivityTestRule<T> activityTestRule;

    public MenuTest(Class<T> activityClass) {
        activityTestRule = new ActivityTestRule<>(activityClass);
    }

    @Test
    public void testAboutMenu() throws Exception {
        Context context = getTargetContext();
        String aboutMenu = context.getString(R.string.about_menu);
        String aboutTitle = context.getString(R.string.about_title);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(aboutMenu))
                .perform(click());
        onView(withText(containsString(aboutTitle)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSettingsMenu() throws Exception {
        Context context = getTargetContext();
        String settingsMenu = context.getString(R.string.settings_menu);
        String settingsTitle = context.getString(R.string.settings_title);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(settingsMenu))
                .perform(click());
        onView(withText(containsString(settingsTitle)))
                .check(matches(isDisplayed()));
    }
}
