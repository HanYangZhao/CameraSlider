package bobthesheep.cameraslider;

import android.app.Activity;
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
import android.widget.Switch;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements View.OnClickListener {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    final MediaType text = MediaType.parse("text/x-markdown; charset=utf-8");

    String address = "";


    public final static String PREF_IP = "PREF_IP_ADDRESS";
    public final static String PREF_PORT = "PREF_PORT_NUMBER";
    // declare buttons and text inputs
    private Button button_moveLeft,button_moveRight, button_setSpeed, button_stop , button_reset , button_setSteps , button_setTime , button_setAccel;
    Switch timelapse_switch;
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
        button_moveLeft = (Button)findViewById(R.id.button_moveLeft);
        button_moveRight = (Button)findViewById(R.id.button_moveRight);
        button_setSpeed = (Button)findViewById(R.id.button_setSpeed);
        button_stop = (Button)findViewById(R.id.button_stop);
        button_reset = (Button) findViewById(R.id.button_reset);
        button_setTime = (Button) findViewById(R.id.button_setTime);
        button_setSteps = (Button) findViewById(R.id.button_setSteps);
        button_setAccel = (Button) findViewById(R.id.button_setAccel);
        timelapse_switch = (Switch) findViewById(R.id.switch_timelapse);
        final WifiManager manager = (WifiManager) super.getSystemService(WIFI_SERVICE);
        final DhcpInfo dhcp = manager.getDhcpInfo();
        address = Formatter.formatIpAddress(dhcp.gateway);

        // set button listener (this class)
//        button_moveLeft.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String parameterValue = "1";
//                new HttpRequestAsyncTask("left", parameterValue
//                ).execute();
//            }
//        }));
//        button_MoveRight.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String parameterValue = "1";
//                new HttpRequestAsyncTask("right", parameterValue
//                ).execute();
//            }
//        }));

        button_moveLeft.setOnClickListener(this);
        button_moveRight.setOnClickListener(this);
        button_setSpeed.setOnClickListener(this);
        button_stop.setOnClickListener(this);
        button_setSteps.setOnClickListener(this);
        button_setTime.setOnClickListener(this);
        button_setAccel.setOnClickListener(this);


    }

    protected void handleException(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onClick(View view) {

        System.out.print("pressed something");
        // get the pin number
        String parameterValue = "";
        String parameterName = "";
        // get the pin number from the button that was clicked



        if (view.getId()== button_setSpeed.getId()) {
            EditText speedText = (EditText)findViewById(R.id.field_motorSpeed);
            parameterValue = String.valueOf(speedText.getText());
            parameterName = "speed";
        }
        else if (view.getId() == button_setSteps.getId()){
            EditText speedText = (EditText)findViewById(R.id.field_steps);
            parameterValue = String.valueOf(speedText.getText());
            parameterName = "steps";
        }
        else if (view.getId() == button_setTime.getId()){
            EditText speedText = (EditText)findViewById(R.id.field_time);
            parameterValue = String.valueOf(speedText.getText());
            parameterName = "mins";
        }
        else if (view.getId() == button_setAccel.getId()){
            EditText speedText = (EditText)findViewById(R.id.field_accel);
            parameterValue = String.valueOf(speedText.getText());
            parameterName = "accel";
        }
        else if (view.getId() == button_moveRight.getId()){
            if(timelapse_switch.isChecked()){
                parameterName = "timelapse_right";
            }
            else{
                parameterName =  "right";
            }
            disableLeftButtons();
            parameterValue = "1";

        }
        else if (view.getId() == button_moveLeft.getId()){
            if(timelapse_switch.isChecked()){
                parameterName =  "timelapse_left";
            }
            else{
                parameterName = "left";
            }
            disableRightButtons();
            parameterValue = "1";

        }
        else if (view.getId() == button_reset.getId()){
            parameterValue = "0";
            parameterName = "reset_pos";
            enableAllButtons();
        }
        else if(view.getId() == button_stop.getId()){
            parameterValue = "0";
            parameterName = "stop";
            enableAllButtons();
        }
        // execute HTTP request
        new HttpRequestAsyncTask(parameterName, parameterValue
        ).execute();
    }

    public void disableRightButtons(){
        button_moveRight.setClickable(false);
        button_moveLeft.setClickable(false);
        button_moveRight.setAlpha(.5f);
        disableAllButtons();
    }

    public void disableLeftButtons(){
        button_moveLeft.setClickable(false);
        button_moveRight.setClickable(false);
        button_moveLeft.setAlpha(.5f);
        disableAllButtons();
    }

    public void disableAllButtons(){
        button_setAccel.setClickable(false);
        button_setTime.setClickable(false);
        button_setSteps.setClickable(false);
        button_setSpeed.setClickable(false);
        button_reset.setClickable(false);
        button_setAccel.setAlpha(.5f);
        button_setTime.setAlpha(.5f);
        button_setSteps.setAlpha(.5f);
        button_setSpeed.setAlpha(.5f);
        button_reset.setAlpha(.5f);

    }

    public void enableAllButtons(){
        button_setAccel.setClickable(true);
        button_setTime.setClickable(true);
        button_setSteps.setClickable(true);
        button_setSpeed.setClickable(true);
        button_setAccel.setClickable(true);
        button_moveLeft.setClickable(true);
        button_moveRight.setClickable(true);
        button_reset.setClickable(true);
        button_setAccel.setAlpha(1);
        button_setTime.setAlpha(1);
        button_setSteps.setAlpha(1);
        button_setSpeed.setAlpha(1);
        button_moveLeft.setAlpha(1);
        button_moveRight.setAlpha(1);
        button_reset.setAlpha(1);

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



        public HttpRequestAsyncTask( String parameterName, String parameterValue)
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

        public void sendRequest(String parameterName, String parameterValue ) throws IOException{
            String postBody = parameterName + " " + parameterValue + "\n\r";
            Log.d("STATE",postBody);
            Request request = new Request.Builder()
                    .url("http://"+ "192.168.4.1" + ":80/?" + parameterName+"="+parameterValue)
                    //.post(RequestBody.create(text, postBody))
                    .build();

            client.newCall(request).execute();

        }
    }
}

