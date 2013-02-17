/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android.common;

import android.content.Context;
import android.util.Log;
import java.lang.reflect.Constructor;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
final public class SQLHelperFactory {

    public static BaseballCardSQLHelper getSQLHelper(Context context) throws SQLHelperCreationException {
        try {
            String sqlHelperClassName = context.getString(R.string.sql_helper);
            Class<BaseballCardSQLHelper> sqlHelperClass = (Class<BaseballCardSQLHelper>) Class.forName(sqlHelperClassName);

            Log.d(TAG, "sqlHelperClass=" + sqlHelperClass.toString());

            Constructor<BaseballCardSQLHelper> sqlHelperCtor = sqlHelperClass.getConstructor(Context.class);

            Log.d(TAG, "sqlHelperCtor=" + sqlHelperCtor.toString());

            return sqlHelperCtor.newInstance(context);
        } catch (Exception ex) {
            throw new SQLHelperCreationException(ex);
        }
    }
    private static final String TAG = SQLHelperFactory.class.getName();
}
