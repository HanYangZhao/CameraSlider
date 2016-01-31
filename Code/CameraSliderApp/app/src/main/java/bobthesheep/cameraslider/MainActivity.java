package bobthesheep.cameraslider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.Externalizable;
import java.io.IOException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements View.OnClickListener {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    final MediaType text = MediaType.parse("text/x-markdown; charset=utf-8");

    String address = "";


    public final static String PREF_IP = "PREF_IP_ADDRESS";
    public final static String PREF_PORT = "PREF_PORT_NUMBER";
    // declare buttons and text inputs
    private Button buttonPin11,buttonPin12,buttonPin13;
    private EditText editTextIPAddress, editTextPortNumber;
    // shared preferences objects used to save the IP address and port so that the user doesn't have to
    // type them next time he/she opens the app.
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("HTTP_HELPER_PREFS",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // assign buttons
        buttonPin11 = (Button)findViewById(R.id.buttonPin11);
        buttonPin12 = (Button)findViewById(R.id.buttonPin12);
        buttonPin13 = (Button)findViewById(R.id.buttonPin13);


        // set button listener (this class)
        buttonPin11.setOnClickListener(this);
        buttonPin12.setOnClickListener(this);
        buttonPin13.setOnClickListener(this);


    }

    protected void handleException(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onClick(View view) {

        System.out.print("pressed something");
        // get the pin number
        String parameterValue = "";

        // get the pin number from the button that was clicked
        if(view.getId()==buttonPin11.getId())
        {
            parameterValue = "11";
        }
        else if(view.getId()==buttonPin12.getId())
        {
            parameterValue = "12";
        }
        else
        {
            parameterValue = "13";
        }

        final WifiManager manager = (WifiManager) super.getSystemService(WIFI_SERVICE);
        final DhcpInfo dhcp = manager.getDhcpInfo();
        address = Formatter.formatIpAddress(dhcp.gateway);

        // execute HTTP request
        new HttpRequestAsyncTask(parameterValue, "pin"
        ).execute();
    }

    /**
     * Description: Send an HTTP Get request to a specified ip address and port.
     * Also send a parameter "parameterName" with the value of "parameterValue".
     * @return The ip address' reply text, or an ERROR message is it fails to receive one
     */


    /**
     * An AsyncTask is needed to execute HTTP requests in the background so that they do not
     * block the user interface.
     */
    private class HttpRequestAsyncTask extends AsyncTask<Void, Void, Void> {

        // declare variables needed
        private String requestReply;
        private String parameterName;
        private String parameterValue;



        public HttpRequestAsyncTask( String parameterValue, String parameterName)
        {
            this.parameterValue = parameterValue;
            this.parameterName = parameterName;
        }


        /**
         * Name: doInBackground
         * Description: Sends the request to the ip address
         * @param voids
         * @return
         */
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                sendRequest(parameterName,parameterValue);
            } catch (Exception e){
                handleException(e);
            }
            return null;
        }

        public void sendRequest(String parameterValue, String parameterName ) throws IOException{
            String postBody = parameterName + " " + parameterValue + "\n";
            Log.d("STATE",postBody);
            Request request = new Request.Builder()
                    .url("http://"+ address + ":80/?" + parameterName + "=" + parameterValue)
                    //.post(RequestBody.create(text, postBody))
                    .build();

            client.newCall(request).execute();

        }
    }
}