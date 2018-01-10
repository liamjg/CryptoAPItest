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

    TextView cryptoChange0;
    TextView cryptoEUR0;
    TextView cryptoUSD0;
    TextView cryptoChange1;
    TextView cryptoEUR1;
    TextView cryptoUSD1;
    TextView cryptoChange2;
    TextView cryptoEUR2;
    TextView cryptoUSD2;

    boolean updated = false;
    long unixtime = System.currentTimeMillis() / 1000L;

    int update_stage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runnable.run();

        cryptoChange0 = (TextView) findViewById(R.id.cryptoChange0);
        cryptoChange1 = (TextView) findViewById(R.id.cryptoChange1);
        cryptoChange2 = (TextView) findViewById(R.id.cryptoChange2);
        cryptoEUR0    = (TextView) findViewById(R.id.cryptoEUR0);
        cryptoEUR1    = (TextView) findViewById(R.id.cryptoEUR1);
        cryptoEUR2    = (TextView) findViewById(R.id.cryptoEUR2);
        cryptoUSD0    = (TextView) findViewById(R.id.cryptoUSD0);
        cryptoUSD1    = (TextView) findViewById(R.id.cryptoUSD1);
        cryptoUSD2    = (TextView) findViewById(R.id.cryptoUSD2);



    }


    public void parseCryptoCompAPI(String precomp){
        Gson gson = new Gson();

        Coin BTC = null;
        Coin ETH = null;
        Coin LTC = null;
        Coin BTC_D24 = null;
        Coin ETH_D24 = null;
        Coin LTC_D24 = null;

        if (update_stage == 0){
            BTC = gson.fromJson(precomp, Coin.class);
        }else if (update_stage == 1){
            ETH = gson.fromJson(precomp, Coin.class);
        }else if (update_stage == 2){
            LTC = gson.fromJson(precomp, Coin.class);
        }else if (update_stage == 3){
            BTC_D24 = gson.fromJson(precomp, Coin.class);
        }else if (update_stage == 4){
            ETH_D24 = gson.fromJson(precomp, Coin.class);
        }else if (update_stage == 5){
            LTC_D24 = gson.fromJson(precomp, Coin.class);
        }

        cryptoUSD0.setText(String.valueOf(BTC.getName().get("BTC").getUSD()));
        cryptoUSD1.setText(String.valueOf(ETH.getName().get("BTC").getUSD()));
        cryptoUSD2.setText(String.valueOf(LTC.getName().get("BTC").getUSD()));



    }

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run(){
                for (int z = 0; z < 5; z++){
                    update_stage = z;
                    new RetrieveFeedTask().execute();
                    handler.postDelayed(runnable,1000);
                }

        }
    };

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... urls) {
            try {
                int d24 = (int) unixtime - 86400;
                int uxtime = (int) unixtime;

                URL url = null;

                if (update_stage == 0){
                    url = new URL("https://min-api.cryptocompare.com/data/pricehistorical?fsym=BTC&tsyms=USD,EUR&ts=" + uxtime + "&extraParams=your_app_name");

                }else if (update_stage == 1){
                    url = new URL("https://min-api.cryptocompare.com/data/pricehistorical?fsym=ETH&tsyms=USD,EUR&ts=" + uxtime + "&extraParams=your_app_name");
                }else if (update_stage == 2){
                    url = new URL("https://min-api.cryptocompare.com/data/pricehistorical?fsym=LTC&tsyms=USD,EUR&ts=" + uxtime + "&extraParams=your_app_name");
                }else if (update_stage == 3){
                    url = new URL("https://min-api.cryptocompare.com/data/pricehistorical?fsym=BTC&tsyms=USD,EUR&ts=" + d24 + "&extraParams=your_app_name");
                }else if (update_stage == 4){
                    url = new URL("https://min-api.cryptocompare.com/data/pricehistorical?fsym=ETH&tsyms=USD,EUR&ts=" + d24 + "&extraParams=your_app_name");
                }else if (update_stage == 5){
                    url = new URL("https://min-api.cryptocompare.com/data/pricehistorical?fsym=LTC&tsyms=USD,EUR&ts=" + d24 + "&extraParams=your_app_name");
                }


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
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);

            parseCryptoCompAPI(response);
        }
    }
}
