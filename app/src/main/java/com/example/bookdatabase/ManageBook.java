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
    private static Button btnQuery;

    TextView textView, txtDefault, txtDefault_author, txtDefault_publisher, txtDefault_datepub, txtDefault_ID;
    private static EditText title;
    private static JSONParser jsonParser = new JSONParser();
    private static String urlHost = "https://e4e3-49-145-174-8.ngrok.io/bookdatabase/SelectItemDetails.php";
    private static String urlHostDelete = "https://e4e3-49-145-174-8.ngrok.io/bookdatabase/delete.php";
    private static String urlHostAuthor = "https://e4e3-49-145-174-8.ngrok.io/bookdatabase/selectAuthor.php";
    private static String urlHostPublisher = "https://e4e3-49-145-174-8.ngrok.io/bookdatabase/selectPublisher.php";
    private static String urlHostDatePub = "https://e4e3-49-145-174-8.ngrok.io/bookdatabase/selectDatePub.php";
    private static String urlHostID = "https://e4e3-49-145-174-8.ngrok.io/bookdatabase/selectid.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    private static String cItemcode = "";


    //how to globalize string
    public static String wew = "";
    public static String gender = "";
    public static String civilstats = "";

    private String tle, auth, pub, datepub, aydi;

    String cItemSelected, cItemSelected_author, cItemSelected_publisher, cItemSelected_datepub, cItemSelected_ID;
    ArrayAdapter<String> adapter_title;
    ArrayAdapter<String> adapter_author;
    ArrayAdapter<String> adapter_publisher;
    ArrayAdapter<String> adapter_datepub;
    ArrayAdapter<String> adapter_ID;
    ArrayList<String> list_title;
    ArrayList<String> list_author;
    ArrayList<String> list_publisher;
    ArrayList<String> list_datepub;
    ArrayList<String> list_ID;
    AdapterView.OnItemLongClickListener longItemListener_title;
    Context context = this;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_book);
        btnQuery = (Button) findViewById(R.id.btnSearch);
        title = (EditText) findViewById(R.id.edttitle);
        txtDefault = (TextView) findViewById(R.id.tv_default);
        listView = (ListView) findViewById(R.id.listview);
        textView = (TextView) findViewById(R.id.textView4);
        txtDefault_author = (TextView) findViewById(R.id.txt_author);
        txtDefault_publisher = (TextView) findViewById(R.id.txt_publisher);
        txtDefault_datepub = (TextView) findViewById(R.id.txt_datepub);
        txtDefault_ID = (TextView) findViewById(R.id.txt_ID);

        txtDefault.setVisibility(View.GONE);
        txtDefault_author.setVisibility(View.GONE);
        txtDefault_publisher.setVisibility(View.GONE);
        txtDefault_datepub.setVisibility(View.GONE);
        txtDefault_ID.setVisibility(View.GONE);

        Toast.makeText(ManageBook.this, "Nothing Selected", Toast.LENGTH_SHORT).show();

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cItemcode = title.getText().toString();
                new uploadDataToURL().execute();
                new Author().execute();
                new Publisher().execute();
                new DatePub().execute();
                new id().execute();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cItemSelected = adapter_title.getItem(position);
                cItemSelected_author = adapter_author.getItem(position);
                cItemSelected_publisher = adapter_publisher.getItem(position);
                cItemSelected_datepub = adapter_datepub.getItem(position);
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
                        txtDefault_datepub.setText(cItemSelected_datepub);
                        txtDefault_ID.setText(cItemSelected_ID);
                        tle = txtDefault.getText().toString().trim();
                        auth = txtDefault_author.getText().toString().trim();
                        pub = txtDefault_publisher.getText().toString().trim();
                        datepub = txtDefault_datepub.getText().toString().trim();
                        aydi = txtDefault_ID.getText().toString().trim();
                        Intent intent = new Intent(ManageBook.this, EditBooks.class);
                        intent.putExtra(EditBooks.TITLE, tle);
                        intent.putExtra(EditBooks.AUTHOR, auth);
                        intent.putExtra(EditBooks.PUBLISHER, pub);
                        intent.putExtra(EditBooks.DATEPUB, datepub);
                        intent.putExtra(EditBooks.ID, aydi);
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
                cItemSelected_author = adapter_author.getItem(position);
                cItemSelected_publisher = adapter_publisher.getItem(position);
                cItemSelected_datepub = adapter_datepub.getItem(position);
                cItemSelected_ID = adapter_ID.getItem(position);

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
                cPostSQL = cItemcode;
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
                final String fnames[] = str.split("-");
                list_title = new ArrayList<String>(Arrays.asList(fnames));
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
    private class Author extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBook.this);

        public Author() {
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
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHostAuthor, "POST", cv);
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
        protected void onPostExecute(String Gender) {
            super.onPostExecute(Gender);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBook.this);
            if (Gender != null) {
                if (isEmpty.equals("") && !Gender.equals("HTTPSERVER_ERROR")) { }
                String gender = Gender;
                String str = gender;
                final String Genders[] = str.split("-");
                list_author = new ArrayList<String>(Arrays.asList(Genders));
                adapter_author = new ArrayAdapter<String>(ManageBook.this,
                        android.R.layout.simple_list_item_1, list_author);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class Publisher extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBook.this);

        public Publisher() {
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
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHostPublisher, "POST", cv);
                if(json != null) {
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
        protected void onPostExecute(String CS) {
            super.onPostExecute(CS);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBook.this);
            if (CS != null) {
                if (isEmpty.equals("") && !CS.equals("HTTPSERVER_ERROR")) { }
                String CivitStat = CS;
                String str = CivitStat;
                final String Civs[] = str.split("-");
                list_publisher= new ArrayList<String>(Arrays.asList(Civs));
                adapter_publisher = new ArrayAdapter<String>(ManageBook.this,
                        android.R.layout.simple_list_item_1, list_publisher);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class DatePub extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBook.this);

        public DatePub() {
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
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHostDatePub, "POST", cv);
                if(json != null) {
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
        protected void onPostExecute(String CS) {
            super.onPostExecute(CS);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBook.this);
            if (CS != null) {
                if (isEmpty.equals("") && !CS.equals("HTTPSERVER_ERROR")) { }
                String CivitStat = CS;
                String str = CivitStat;
                final String Civs[] = str.split("-");
                list_datepub= new ArrayList<String>(Arrays.asList(Civs));
                adapter_datepub = new ArrayAdapter<String>(ManageBook.this,
                        android.R.layout.simple_list_item_1, list_datepub);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class id extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(ManageBook.this);

        public id() {
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
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHostID, "POST", cv);
                if(json != null) {
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
        protected void onPostExecute(String aydi) {
            super.onPostExecute(aydi);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(ManageBook.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !aydi.equals("HTTPSERVER_ERROR")) { }
                Toast.makeText(ManageBook.this, "Data selected", Toast.LENGTH_SHORT);
                String AYDI = aydi;
                String str = AYDI;
                final String ayds[] = str.split("-");
                list_ID = new ArrayList<String>(Arrays.asList(ayds));
                adapter_ID = new ArrayAdapter<String>(ManageBook.this,
                        android.R.layout.simple_list_item_1, list_ID);
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