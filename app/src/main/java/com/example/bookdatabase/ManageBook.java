package com.example.bookdatabase;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
public class ManageBook extends AppCompatActivity {
    TextView textView, txtDefault, txtDefault_author, txtDefault_publisher, txtDefault_ID, txtDefault_datePub;
    private static JSONParser jsonParser = new JSONParser();
    private static String urlHost = "https://192.168.1.6/bookdatabase/SelectItemDetails.php";
    private static String urlHostDelete = "https://192.168.1.6/bookdatabase/delete.php";
    private static String urlHostAuthor = "https://7d2b-49-145-174-8.ngrok.io/databasecon/selectGender.php";
    private static String urlHostPublisher = "https://7d2b-49-145-174-8.ngrok.io/databasecon/selectCivilStatus.php";
    private static String urlHostDatePub = "https://7d2b-49-145-174-8.ngrok.io/databasecon/selectid.php";
    private static String urlHostID = "https://7d2b-49-145-174-8.ngrok.io/databasecon/selectid.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    private static EditText edtTitle;
    private static Button btnSearch;
    ArrayList<String> list_title;
    ArrayList<String> list_author;
    ArrayList<String> list_publisher;
    ArrayList<String> list_datepub;
    ArrayList<String> list_ID;
    ArrayAdapter<String> adapter_title;
    ArrayAdapter<String> adapter_author;
    ArrayAdapter<String> adapter_publisher;
    ArrayAdapter<String> adapter_datepub;
    ArrayAdapter<String> adapter_ID;
    AdapterView.OnItemLongClickListener longItemListener_title;
    private static String title = "";
    String cItemSelected, cItemSelected_author, cItemSelected_publisher, cItemSelected_ID, cItemSelected_datePub;
    Context context = this;
    ListView listView;
    private String tle, auth, pub, dpub, aydi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        edtTitle = (EditText) findViewById(R.id.edttitle);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_book);
        Toast.makeText(ManageBook.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = edtTitle.getText().toString();
                new uploadDataToURL().execute();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cItemSelected = adapter_title.getItem(position);
                cItemSelected_author = adapter_author.getItem(position);
                cItemSelected_publisher = adapter_publisher.getItem(position);
                cItemSelected_datePub = adapter_datepub.getItem(position);
                cItemSelected_ID = adapter_ID.getItem(position);
                androidx.appcompat.app.AlertDialog.Builder alert_confirm =
                        new androidx.appcompat.app.AlertDialog.Builder(context);
                alert_confirm.setMessage("Edit the records of" + " " + cItemSelected);
                alert_confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        txtDefault.setText(cItemSelected);
                        txtDefault_author.setText(cItemSelected_author);
                        txtDefault_publisher.setText(cItemSelected_publisher);
                        txtDefault_ID.setText(cItemSelected_ID);
                        txtDefault_datePub.setText(cItemSelected_datePub);
                        tle = txtDefault.getText().toString().trim();
                        auth = txtDefault_author.getText().toString().trim();
                        pub = txtDefault_publisher.getText().toString().trim();
                        dpub = txtDefault_datePub.getText().toString().trim();
                        aydi = txtDefault_ID.getText().toString().trim();
                        Intent intent = new Intent(ManageBook.this, EditBooks.class);
                        startActivity(intent);
                    }
                });
                alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert_confirm.show();
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cItemSelected = adapter_title.getItem(position);

                androidx.appcompat.app.AlertDialog.Builder alert_confirm =
                        new androidx.appcompat.app.AlertDialog.Builder(context);
                alert_confirm.setMessage("Are you sure you want to delete" + " " + cItemSelected);
                alert_confirm.setPositiveButton(R.string.msg2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        txtDefault_ID.setText(cItemSelected_ID);
                        aydi = txtDefault_ID.getText().toString().trim();
                        new delete().execute();
                    }
                });
                alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert_confirm.show();
            }
        });
    }
    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = " ", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBook.this);

        public uploadDataToURL() {
        }

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
                cPostSQL = title;
                cv.put("code", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHost, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
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
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBook.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) { }
                String wew = s;
                String str = wew;
                final String arrtitle[] = str.split("-");
                list_title = new ArrayList<String>(Arrays.asList(arrtitle));
                adapter_title = new ArrayAdapter<String>(ManageBook.this, android.R.layout.simple_list_item_1, list_title);
                listView.setAdapter(adapter_title);
                textView.setText(listView.getAdapter().getCount() + " " + "record(s) found.");
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class delete extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBook.this);

        public delete() {
        }

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
                cPostSQL = cItemSelected_ID;
                cv.put("id", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHostDelete, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
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
        protected void onPostExecute(String del) {
            super.onPostExecute(del);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBook.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !del.equals("HTTPSERVER_ERROR")) { }
                Toast.makeText(ManageBook.this, "Data Deleted", Toast.LENGTH_SHORT);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}
