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
package bbct.android.common.activity;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bbct.android.common.R;
import bbct.android.common.activity.util.BaseballCardMultiChoiceModeListener;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardAdapter;
import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.BaseballCardLoaderCallbacks;
import bbct.android.common.view.HeaderView;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Displays a list of all baseball cards stored in the database.
 * <p/>
 * TODO: Make list fancier
 */
public class BaseballCardList extends ListFragment {

    private static final String[] ROW_PROJECTION = {
            BaseballCardContract.BRAND_COL_NAME,
            BaseballCardContract.YEAR_COL_NAME,
            BaseballCardContract.NUMBER_COL_NAME,
            BaseballCardContract.PLAYER_NAME_COL_NAME};
    private static final int[] ROW_TEXT_VIEWS = {R.id.brand_text_view,
            R.id.year_text_view, R.id.number_text_view,
            R.id.player_name_text_view};
    private static final int LOADER_ID = 42;
    private static final String FILTER_PARAMS = "filterParams";
    private static final String TAG = BaseballCardList.class.getName();

    @InjectView(android.R.id.empty) TextView emptyList = null;
    @InjectView(android.R.id.list) ListView listView;

    private BaseballCardAdapter adapter = null;
    private Bundle filterParams = null;
    private BaseballCardMultiChoiceModeListener mCallbacks;
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    public static BaseballCardList getInstance(Bundle filterArgs) {
        BaseballCardList cardList = new BaseballCardList();

        Bundle args = new Bundle();
        args.putBundle(FILTER_PARAMS, filterArgs);
        cardList.setArguments(args);

        return cardList;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        Log.d(TAG, "  savedInstanceState=" + savedInstanceState);

        super.onCreate(savedInstanceState);

        this.adapter = new BaseballCardAdapter(this.getActivity(),
                R.layout.baseball_card, null, ROW_PROJECTION, ROW_TEXT_VIEWS);
        this.loaderCallbacks = new BaseballCardLoaderCallbacks(this.getActivity(), this.adapter);

        Log.d(TAG, "  adapter=" + this.adapter);

        Bundle args = this.getArguments();

        if (savedInstanceState != null) {
            this.filterParams = savedInstanceState.getBundle(FILTER_PARAMS);
        } else if (args != null) {
            this.filterParams = args.getBundle(FILTER_PARAMS);
        }

        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");

        View view = inflater.inflate(R.layout.card_list, container, false);
        ButterKnife.inject(this, view);

        View headerView = new HeaderView(this.getActivity());
        CheckBox selectAll = ButterKnife.findById(headerView, R.id.select_all);
        selectAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !mCallbacks.isStarted()) {
                    BaseballCardList.this.getActivity().startActionMode(mCallbacks);
                } else if (mCallbacks.isStarted()) {
                    mCallbacks.finish();
                }

                BaseballCardList.this.setAllChecked(isChecked);
            }
        });
        listView.addHeaderView(headerView);
        this.setListAdapter(this.adapter);
        this.adapter.setListFragment(this);

        mCallbacks = new BaseballCardMultiChoiceModeListener(this);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(mCallbacks);
        this.adapter.setActionModeCallback(mCallbacks);
        this.startLoader();

        return view;
    }

    private void startLoader() {
        LoaderManager loaderManager = this.getActivity().getSupportLoaderManager();
        if (loaderManager.getLoader(LOADER_ID) == null) {
            loaderManager.initLoader(LOADER_ID, this.filterParams, this.loaderCallbacks);
        } else {
            loaderManager.restartLoader(LOADER_ID, this.filterParams, this.loaderCallbacks);
        }
    }

    /**
     * Create the options menu. This is simply inflated from the
     * {@code list.xml} resource file.
     *
     * @param menu The options menu in which new menu items are placed.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list, menu);
    }

    /**
     * Prepare the options menu to be displayed. If a filter is active and
     * displays the "Clear Filter" menu item, otherwise displays the
     * "Filter Cards" menu item.
     *
     * @param menu The options menu which will be displayed.
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu()");

        MenuItem filter = menu.findItem(R.id.filter_menu);
        MenuItem clearFilter = menu.findItem(R.id.clear_filter_menu);
        if (this.filterParams == null) {
            filter.setVisible(true);
            filter.setEnabled(true);

            clearFilter.setVisible(false);
            clearFilter.setEnabled(false);
        } else {
            filter.setVisible(false);
            filter.setEnabled(false);

            clearFilter.setVisible(true);
            clearFilter.setEnabled(true);
        }
    }

    /**
     * Respond to the user selecting a menu item.
     *
     * @param item The menu item selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.add_menu) {
            BaseballCardDetails details = new BaseballCardDetails();
            this.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_holder, details, FragmentTags.EDIT_CARD)
                    .addToBackStack(FragmentTags.EDIT_CARD)
                    .commit();
            return true;
        } else if (itemId == R.id.filter_menu) {
            FilterCards filterCards = new FilterCards();
            this.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_holder, filterCards, FragmentTags.FILTER_CARDS)
                    .addToBackStack(FragmentTags.FILTER_CARDS)
                    .commit();
            return true;
        } else if (itemId == R.id.clear_filter_menu) {
            this.emptyList.setText(R.string.start);
            this.filterParams = null;
            this.startLoader();

            this.getActivity().supportInvalidateOptionsMenu();

            return true;
        } else {
            Log.e(TAG, "onOptionsItemSelected(): Invalid menu code: " + itemId);
            // TODO Throw exception?
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Save the currently active filter when the system asks for it. Also save
     * the current state of marked list items.
     *
     * @param outState The saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBundle(FILTER_PARAMS, this.filterParams);
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

        BaseballCard card = BaseballCardList.this.adapter.getItem(position - 1);

        Fragment details = BaseballCardDetails.getInstance(id, card);
        this.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, details, FragmentTags.EDIT_CARD)
                .addToBackStack(FragmentTags.EDIT_CARD)
                .commit();
    }

    public void deleteSelectedCards() {
        for (int i = 0; i < getListAdapter().getCount() + 1; ++i) {
            if (getListView().isItemChecked(i)) {
                // Subtract one to compensate for the header view
                long id = this.adapter.getItemId(i - 1);
                Uri deleteUri = ContentUris.withAppendedId(
                        BaseballCardContract.getUri(this.getActivity().getPackageName()), id);
                this.getActivity().getContentResolver().delete(deleteUri, null, null);
            }
        }

        Toast.makeText(this.getActivity(), R.string.card_deleted_message, Toast.LENGTH_LONG)
                .show();
    }

    private void setAllChecked(boolean checked) {
        ListView listView = this.getListView();
        // Add 1 for the header view
        for (int i = 0; i < this.adapter.getCount() + 1; ++i) {
            listView.setItemChecked(i, checked);
        }
    }

}
