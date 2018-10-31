package benguerenler.bontouchdictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class DictionarySaveSql {
    public DictionarySaveSql() {}

//To create a database
    public static abstract class DictEntry implements BaseColumns {
        public static final String TABLE_NAME = "dictionary";
        public static final String COLUMN_NAME_ENTRY_ID = "_id";
        public static final String COLUMN_NAME_WORD = "words";

    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + DictEntry.TABLE_NAME + " (" +
                    DictEntry.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DictEntry.COLUMN_NAME_WORD + " TEXT NOT NULL" +
                    " )";


    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS" + DictEntry.TABLE_NAME;

    public static class DictDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "dictionary.db";

        public DictDbHelper (Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }


    }





}
