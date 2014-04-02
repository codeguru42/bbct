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
package bbct.android.common.provider;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import bbct.android.common.R;
import bbct.android.common.data.BaseballCard;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides helper methods to access the underlying SQLite database.
 *
 * TODO: Write JUnit tests.
 */
public class BaseballCardSQLHelper extends SQLiteOpenHelper {

    /**
     * Name of the SQLite database.
     */
    public static final String DATABASE_NAME = "bbct.db";
    /**
     * Current schema version.
     */
    public static final int SCHEMA_VERSION = 4;
    /**
     * Original schema version.
     */
    public static final int ORIGINAL_SCHEMA = 1;
    /**
     * Schema version when attempting to add the team field. This version was
     * buggy.
     */
    public static final int BAD_TEAM_SCHEMA = 2;
    /**
     * Schema version which correctly added the team field.
     */
    public static final int TEAM_SCHEMA = 3;
    /**
     * Schema versions to add the path to the picture of the card.
     */
    public static final int PICTURE_PATH_SCHEMA = 4;

    /**
     * Create a new {@link BaseballCardSQLHelper} with the given Android
     * {@link Context}.
     *
     * @param context
     *            The Android {@link Context} for this {@link SQLiteOpenHelper}.
     */
    public BaseballCardSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);

        Log.d(TAG, "ctor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate()");

        String sqlCreate = "CREATE TABLE IF NOT EXISTS "
                + BaseballCardContract.TABLE_NAME + "("
                + BaseballCardContract.ID_COL_NAME
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BaseballCardContract.PATH_TO_PICTURE_FRONT + " VARCHAR(50), "
                + BaseballCardContract.PATH_TO_PICTURE_BACK + " VARCHAR(50), "
                + BaseballCardContract.BRAND_COL_NAME + " VARCHAR(10), "
                + BaseballCardContract.YEAR_COL_NAME + " INTEGER, "
                + BaseballCardContract.NUMBER_COL_NAME + " INTEGER, "
                + BaseballCardContract.VALUE_COL_NAME + " INTEGER, "
                + BaseballCardContract.COUNT_COL_NAME + " INTEGER, "
                + BaseballCardContract.PLAYER_NAME_COL_NAME + " VARCHAR(50), "
                + BaseballCardContract.TEAM_COL_NAME + " VARCHAR(50), "
                + BaseballCardContract.PLAYER_POSITION_COL_NAME
                + " VARCHAR(20)," + "UNIQUE ("
                + BaseballCardContract.BRAND_COL_NAME + ", "
                + BaseballCardContract.YEAR_COL_NAME + ", "
                + BaseballCardContract.NUMBER_COL_NAME + "))";

        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == ORIGINAL_SCHEMA
                || oldVersion == BAD_TEAM_SCHEMA
                || oldVersion == TEAM_SCHEMA) {
            if (newVersion == TEAM_SCHEMA) {
                String sqlUpgrade = "ALTER TABLE "
                        + BaseballCardContract.TABLE_NAME + " ADD COLUMN "
                        + BaseballCardContract.TEAM_COL_NAME + " VARCHAR(50)";
                db.execSQL(sqlUpgrade);
            }
            else if (newVersion == PICTURE_PATH_SCHEMA) {
                String sqlAlterString = "ALTER TABLE "
                        + BaseballCardContract.TABLE_NAME + " ADD COLUMN ";
                if (oldVersion < TEAM_SCHEMA) {
                    String sqlUpgrade = sqlAlterString + BaseballCardContract.TEAM_COL_NAME + " VARCHAR(50)";
                    db.execSQL(sqlUpgrade);
                } else {
                    String sqlUpgrade = sqlAlterString + BaseballCardContract.PATH_TO_PICTURE_FRONT + " VARCHAR(50)";                              
                    db.execSQL(sqlUpgrade);
                    sqlUpgrade = sqlAlterString + BaseballCardContract.PATH_TO_PICTURE_BACK + " VARCHAR(50)";
                    db.execSQL(sqlUpgrade);
                }
            }
        }
    }

    /**
     * Insert baseball card data into a SQLite database.
     *
     * @param card
     *            The baseball card data to insert into the database.
     * @return The row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long insertBaseballCard(BaseballCard card) {
        return this.getWritableDatabase().insert(
                BaseballCardContract.TABLE_NAME, null,
                BaseballCardContract.getContentValues(card));
    }

    /**
     * Insert data for multiple baseball cards into a SQLite database.
     *
     * @param cards
     *            The list of cards to insert into the database.
     */
    public void insertAllBaseballCards(List<BaseballCard> cards) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransactionNonExclusive();
        try {
            for (BaseballCard card : cards) {
                db.insert(BaseballCardContract.TABLE_NAME, null,
                        BaseballCardContract.getContentValues(card));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Update the database with edited card data.
     *
     * @param oldCard
     *            The old card data.
     * @param newCard
     *            The new card data.
     * @return The number of rows updated, either 0 or 1.
     */
    public int updateBaseballCard(BaseballCard oldCard, BaseballCard newCard) {
        String[] args = { oldCard.getBrand(),
                Integer.toString(oldCard.getYear()),
                Integer.toString(oldCard.getNumber()) };
        String where = BaseballCardContract.BRAND_COL_NAME + "=? AND "
                + BaseballCardContract.YEAR_COL_NAME + "=? AND "
                + BaseballCardContract.NUMBER_COL_NAME + "=?";

        return this.getWritableDatabase().update(
                BaseballCardContract.TABLE_NAME,
                BaseballCardContract.getContentValues(newCard), where, args);
    }

    /**
     * Removes data related with {@link BaseballCard} from the database.
     *
     * @param card
     *            - the card to remove from database
     * @return - see {@link SQLiteDatabase#delete}
     */
    public int removeBaseballCard(BaseballCard card) {
        String[] args = { Integer.toString(card.getYear()),
                Integer.toString(card.getNumber()), card.getPlayerName() };
        String where = BaseballCardContract.YEAR_COL_NAME + "=? AND "
                + BaseballCardContract.NUMBER_COL_NAME + "=? AND "
                + BaseballCardContract.PLAYER_NAME_COL_NAME + "=?";

        return this.getWritableDatabase().delete(
                BaseballCardContract.TABLE_NAME, where, args);
    }

    /**
     * Get the most recently opened {@link Cursor}.
     *
     * @return The most recently opened {@link Cursor}.
     */
    public Cursor getCursor() {
        Log.d(TAG, "getCursor()");

        if (this.currCursor == null) {
            this.clearFilter();
        }

        return this.currCursor;
    }

    /**
     * Open a {@link Cursor} with no filter.
     */
    public void clearFilter() {
        Log.d(TAG, "clearFilter()");

        this.currCursor = this.getWritableDatabase().query(
                BaseballCardContract.TABLE_NAME, null, null, null, null, null,
                null);
    }

    /**
     * Constructs a query from multiple filter parameters and executes it,
     * updating the {@link Cursor}.
     * @param context - the {@link Context} from which this method was called
     * @param params - parameters which should be added to the query
     */
    public void applyFilter(Context context, Bundle params) {
        Log.d(TAG, "applyFilter()");

        Resources res = context.getResources();
        StringBuilder sb = new StringBuilder();
        String[] args = new String[params.size()];

        int numQueries = 0;
        for (String key : params.keySet()) {
            String value = params.getString(key);

            if (key.equals(res.getString(R.string.year_extra))) {
                sb.append(BaseballCardContract.YEAR_COL_NAME);
            } else if (key.equals(res.getString(R.string.brand_extra))) {
                sb.append(BaseballCardContract.BRAND_COL_NAME);
            } else if (key.equals(res.getString(R.string.number_extra))) {
                sb.append(BaseballCardContract.NUMBER_COL_NAME);
            } else if (key.equals(res.getString(R.string.player_name_extra))) {
                sb.append(BaseballCardContract.PLAYER_NAME_COL_NAME);
            } else {
                sb.append(BaseballCardContract.TEAM_COL_NAME);
            }

            args[numQueries] = value;
            numQueries++;

            if (numQueries < args.length) {
                sb.append(" = ?  AND ");
            } else {
                sb.append(" = ?");
            }
        }

        this.currCursor = this.getWritableDatabase().query(
                BaseballCardContract.TABLE_NAME, null, sb.toString(), args,
                null, null, null);
    }

    /**
     * Populate a {@link BaseballCard} from the data in the current row of the
     * current {@link Cursor}.
     *
     * @return A {@link BaseballCard} from the data in the current row of the
     *         current {@link Cursor}.
     */
    public BaseballCard getBaseballCardFromCursor() {
        return this.getBaseballCardFromCursor(this.currCursor);
    }

    /**
     * Populate a {@link BaseballCard} from the data in the current row of the
     * given {@link Cursor}.
     *
     * @param cursor
     *            The {@link Cursor} to obtain data from.
     * @return A {@link BaseballCard} from the data in the current row of the
     *         given {@link Cursor}.
     */
    public BaseballCard getBaseballCardFromCursor(Cursor cursor) {
        String pathToPictureFront = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.PATH_TO_PICTURE_FRONT));
        String pathToPictureBack = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.PATH_TO_PICTURE_BACK));
        String brand = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.BRAND_COL_NAME));
        int year = cursor.getInt(cursor
                .getColumnIndex(BaseballCardContract.YEAR_COL_NAME));
        int number = cursor.getInt(cursor
                .getColumnIndex(BaseballCardContract.NUMBER_COL_NAME));
        int value = cursor.getInt(cursor
                .getColumnIndex(BaseballCardContract.VALUE_COL_NAME));
        int count = cursor.getInt(cursor
                .getColumnIndex(BaseballCardContract.COUNT_COL_NAME));
        String name = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.PLAYER_NAME_COL_NAME));
        String team = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.TEAM_COL_NAME));
        String position = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.PLAYER_POSITION_COL_NAME));

        return new BaseballCard(brand, year, number, value, count, name, team,
                position, pathToPictureFront, pathToPictureBack);
    }

    /**
     * Populate a {@link List} of {@link BaseballCard}s from the data in the
     * given {@link Cursor}.
     *
     * @param cursor
     *            The {@link Cursor} to obtain data from.
     * @return A {@link List} of {@link BaseballCard}s from the data in the
     *         given {@link Cursor}.
     */
    public List<BaseballCard> getAllBaseballCardsFromCursor(Cursor cursor) {
        Log.d(TAG, "getAllBaseballCardsFromCursor()");
        Log.d(TAG, "cursor=" + cursor);

        List<BaseballCard> cards = new ArrayList<BaseballCard>();

        while (cursor.moveToNext()) {
            BaseballCard card = this.getBaseballCardFromCursor(cursor);
            cards.add(card);
        }

        return cards;
    }

    /**
     * Get the distinct values from the given column matching the given
     * constraint.
     *
     * @param colName
     *            The name of the column to query.
     * @param constraint
     *            An optional {@link String} to match against. May be
     *            {@code null}.
     * @return A {@link Cursor} containing the queried values.
     */
    public Cursor getDistinctValues(String colName, String constraint) {
        String[] cols = { BaseballCardContract.ID_COL_NAME, colName };
        String filter = (constraint == null) ? null : colName + " LIKE ?";
        String[] args = { constraint.trim() + '%' };

        return this.getWritableDatabase().query(
                BaseballCardContract.TABLE_NAME, cols, filter, args, colName,
                null, null, null);
    }

    private static final String TAG = BaseballCardSQLHelper.class.getName();
    private Cursor currCursor = null;
}
