package com.example.bookdatabase;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class EditBooks extends AppCompatActivity {
    private static Button btnQuery;
    private static EditText title, author, publisher, datepublished;
    private static TextView tv_civ;
    private static String cItemcode = "";
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "https://4dad-49-148-116-21.ngrok.io/bookdatabase/UpdateQty.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    String[] StringStatus = new String[] {"Single", "Married", "Widow", "Divorced"};
    public static String String_isempty = "";
    public static final String TITLE = "TITLE";
    public static final String AUTHOR = "AUTHOR";
    public static final String PUBLISHER = "PUBLISHER";
    public static final String DATEPUB = "DATE";
    public static String ID = "ID";
    private String tle, auth, pub, datepub, aydi;
    public static String Title = "";
    public static String Author = "";
    public static String Publisher = "";
    public static String DatePub = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_books);
        title = (EditText) findViewById(R.id.title);
        author = (EditText) findViewById(R.id.author);
        publisher = (EditText) findViewById(R.id.publisher);
        datepublished = (EditText) findViewById(R.id.datepub);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        Intent i = getIntent();
        tle = i.getStringExtra(TITLE);
        auth = i.getStringExtra(AUTHOR);
        pub = i.getStringExtra(PUBLISHER);
        datepub = i.getStringExtra(DATEPUB);
        aydi = i.getStringExtra(ID);
        title.setText(tle);
        author.setText(auth);
        publisher.setText(pub);
        datepublished.setText(datepub);


        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Title = title.getText().toString();
                Author = author.getText().toString();
                Publisher = publisher.getText().toString();
                DatePub = datepublished.getText().toString();
                new uploadDataToURL().execute();
            }
        });
    }
    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        String gens, civil;
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(EditBooks.this);

        public uploadDataToURL() {}
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMessage(cMessage);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            int nSuccess;
            try {
                ContentValues cv = new ContentValues();
                cPostSQL = aydi;
                cv.put("id", cPostSQL);

                cPostSQL = " '" + Title + "' ";
                cv.put("title", cPostSQL);

                cPostSQL = " '" + Author + "' ";
                cv.put("auth", cPostSQL);

                cPostSQL =  " '" + Publisher + "' ";
                cv.put("pub", cPostSQL);

                cPostSQL =  " '" + DatePub + "' ";
                cv.put("dop", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHost, "POST", cv);
                if(json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if(nSuccess == 1) {
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                } else {
                    return "HTTPSERVER_ERROR";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            String isEmpty = "";
            AlertDialog.Builder alert = new AlertDialog.Builder(EditBooks.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) {}
                Toast.makeText(EditBooks.this, s ,Toast.LENGTH_SHORT).show();
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}
