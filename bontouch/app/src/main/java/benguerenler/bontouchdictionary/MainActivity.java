package benguerenler.bontouchdictionary;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static benguerenler.bontouchdictionary.DictionarySaveSql.DictEntry.COLUMN_NAME_WORD;
import static benguerenler.bontouchdictionary.DictionarySaveSql.DictEntry.TABLE_NAME;
import static benguerenler.bontouchdictionary.DictionarySaveSql.DictDbHelper;
import static benguerenler.bontouchdictionary.DictionarySaveSql.SQL_CREATE_TABLE;
public class MainActivity extends AppCompatActivity {

    //private Context context;
    SearchView search_view;
    DictionarySaveSql.DictDbHelper Dichelper;
    ProgressDialog progressDialog;
    boolean found ;


    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dichelper = new DictionarySaveSql.DictDbHelper(MainActivity.this);
        //saveFile();
        String filepath = "http://runeberg.org/words/ss100.txt";
        //Get URL and execute it.
        found = false;
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // Handle a suggestions click (because the suggestions all use ACTION_VIEW)
            Uri data = intent.getData();
            found = true;
        }

        //Found is to avoid the programme start over.
        if(found == false){
            try {
                URL url = new URL(filepath);
                new GetU().execute(url);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }


        // Get the intent, verify the action and get the query


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search_view = (SearchView) findViewById(R.id.search_view);
        search_view.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search_view.setIconifiedByDefault(false);
        //search_view.setClickable(false);


    }

    class GetU extends AsyncTask<URL, Void, Boolean> {
        @Override
        protected void onPreExecute (){
            //Showing a progress bar while writing to the sqlite so the user can understand and wait
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Dictionary installing");
            progressDialog.setMessage("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(URL... urls) {
            Log.i("BACKGROUND", "STARTIN THE ASYNC TASK");
            try {
                URLConnection ucon = urls[0].openConnection();
                ucon.setReadTimeout(5000);
                ucon.setConnectTimeout(10000);

                BufferedReader inStream = new BufferedReader(new InputStreamReader(urls[0].openStream(),"ISO-8859-1"));

                Log.i("BACKGROUND", "READING THE FILE");


                //Save file to internal
                File file = new File(getApplicationContext().getFilesDir(), "test.txt");
                //Making writable
                SQLiteDatabase db = Dichelper.getWritableDatabase();
                if (file.exists()) {
                    file.delete();
                    db.execSQL("drop table if exists " + TABLE_NAME);
                    db.execSQL(SQL_CREATE_TABLE);
                }
                file.createNewFile();

                String Rline= inStream.readLine();
                FileOutputStream fos = new FileOutputStream(file);
                BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(fos,"ISO-8859-1"));
                while (Rline!= null) {
                    bw.write(Rline);
                    bw.newLine();
                    Rline = inStream.readLine();
                }
                bw.close();
                inStream.close();

                Log.i("BACKGROUND", "FINISHED SAVING FILE");

                Log.i("BACKGROUND", "WILL READ A LINE");
                FileInputStream inputstream = openFileInput("test.txt");
                BufferedReader read = new BufferedReader(new InputStreamReader(inputstream,"ISO-8859-1"));


                //Reading line by line
                String line = read.readLine();
                String dic_insert = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME_WORD +") VALUES ";

                db.beginTransaction();
                //To make it faster to insert into database, inserted with batches
                while(line != null){
                    String [] values = new String[500]; //number of lines to be inserterd in a batch = 500
                    StringBuilder valuesBuilder = new StringBuilder();
                    int i = 0;
                    for ( ; (i<500) && (line != null); i++)
                    {
                        if(i !=0){
                            valuesBuilder.append(", ");
                        }
                        values[i] = line;
                        valuesBuilder.append("(?)");
                        line = read.readLine();

                    }
                    if (i<500)
                    {
                        String [] end_values = Arrays.copyOfRange(values, 0, i);
                        db.execSQL(dic_insert + valuesBuilder.toString() ,end_values);

                    }
                    else{
                        db.execSQL(dic_insert + valuesBuilder.toString() ,values);

                    }
                }

                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();


            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean bool) {
            if (bool) {
                SQLiteDatabase db = Dichelper.getReadableDatabase();
                long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
                String text = "Added count rows: " + count;
                Log.i("BACKGROUND" , text);
                Toast.makeText(getApplicationContext(), text,Toast.LENGTH_LONG).show();
                db.close();
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }
    }



}
