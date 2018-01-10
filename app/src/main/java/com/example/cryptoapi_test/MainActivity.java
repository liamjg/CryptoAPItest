package com.example.cryptoapi_test;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Handler;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText searchText;
    TextView responseView;
    ProgressBar progressBar;
    boolean update = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runnable.run();


//        responseView = (TextView) findViewById(R.id.responseView);
//        searchText = (EditText) findViewById(R.id.searchText);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

//    public void parseCryptoCompAPI(String precomp){
//
//    }

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run(){
            new RetrieveFeedTask().execute();
            handler.postDelayed(runnable,1000);

        }
    };

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
//            progressBar.setVisibility(View.VISIBLE);
//            responseView.setText("");
        }

        @Override
        protected String doInBackground(Void... urls) {
            //String coinType = searchText.getText().toString();

            try {
                URL url = new URL("https://min-api.cryptocompare.com/data/price?fsym=" + "LTC" + "&tsyms=BTC,USD,EUR");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "ERROR";
            }
           // progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
           // responseView.setText(response);

         //   parseCryptoCompAPI(response);
        }
    }
}
