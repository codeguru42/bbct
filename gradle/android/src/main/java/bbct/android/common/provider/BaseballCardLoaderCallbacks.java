package bbct.android.common.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.CursorAdapter;
import bbct.android.common.activity.FilterCards;
import java.util.Arrays;

public class BaseballCardLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = BaseballCardLoaderCallbacks.class.getName();
    private Context mContext;
    private final Uri mUri;
    private final CursorAdapter mAdapter;

    public BaseballCardLoaderCallbacks(Context context, CursorAdapter adapter) {
        mContext = context;
        mAdapter = adapter;
        mUri = BaseballCardContract.getUri(mContext.getPackageName());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader()");
        Log.d(TAG, "  id=" + id);

        StringBuilder sb = null;
        String[] selectionArgs = null;

        if (args != null) {
            sb = new StringBuilder();
            selectionArgs = new String[args.size()];

            int numQueries = 0;
            for (String key : args.keySet()) {
                String value = args.getString(key);

                switch (key) {
                    case FilterCards.YEAR_EXTRA:
                        sb.append(BaseballCardContract.YEAR_SELECTION);
                        break;
                    case FilterCards.BRAND_EXTRA:
                        sb.append(BaseballCardContract.BRAND_SELECTION);
                        break;
                    case FilterCards.NUMBER_EXTRA:
                        sb.append(BaseballCardContract.NUMBER_SELECTION);
                        break;
                    case FilterCards.PLAYER_NAME_EXTRA:
                        sb.append(BaseballCardContract.PLAYER_NAME_SELECTION);
                        break;
                    default:
                        sb.append(BaseballCardContract.TEAM_SELECTION);
                        break;
                }

                selectionArgs[numQueries] = value;
                numQueries++;

                if (numQueries < selectionArgs.length) {
                    sb.append(" AND ");
                }
            }
        }

        Log.d(TAG, "selection=" + sb);
        Log.d(TAG, "selectionArgs=" + Arrays.toString(selectionArgs));

        return new CursorLoader(mContext, mUri, BaseballCardContract.PROJECTION,
                sb == null ? null : sb.toString(), selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished()");
        mAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Do nothing
    }

}
