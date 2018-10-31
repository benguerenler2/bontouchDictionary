package benguerenler.bontouchdictionary;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import static benguerenler.bontouchdictionary.DictionarySaveSql.DictDbHelper;
import static benguerenler.bontouchdictionary.DictionarySaveSql.DictEntry.COLUMN_NAME_WORD;
import static benguerenler.bontouchdictionary.DictionarySaveSql.DictEntry.TABLE_NAME;


//Content provider provide custom search suggestions for project.
public class DictionaryContentProv  extends ContentProvider {
    DictionarySaveSql.DictDbHelper Dichelper;
    public static final String AUTHORITY = " benguerenler.bontouchdictionary";
    //Content URI identifies data in provider it points to our path
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    @Override
    public boolean onCreate() {
        Dichelper = new DictDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String sel, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        SQLiteDatabase db = Dichelper.getReadableDatabase();
        //String query = uri.getLastPathSegment().toLowerCase();
        String query = selectionArgs[0];
        //It retrieve data from sqlite. To use the arguments to select the table to query. Returning a cursor object
        if(query.length() > 0)
        {
            String selection = COLUMN_NAME_WORD + " LIKE '" + query + "%'";
            String [] Columns = {"_id", COLUMN_NAME_WORD + " AS "+ SearchManager.SUGGEST_COLUMN_TEXT_1};
            cursor = db.query(TABLE_NAME, Columns, selection, null,null,null,null);
            int CursorCount = cursor.getCount();
            String dump = DatabaseUtils.dumpCursorToString(cursor);
            return cursor;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String type = "vnd.android.cursor.dir/vnd"+ AUTHORITY +"." + TABLE_NAME;
        return type;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
