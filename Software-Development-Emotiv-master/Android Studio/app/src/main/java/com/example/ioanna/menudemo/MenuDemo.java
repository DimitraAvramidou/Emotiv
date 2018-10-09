package com.example.ioanna.menudemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.Camera;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import android.support.v7.app.AppCompatActivity;
import org.eclipse.paho.client.mqttv3.MqttClient;
import static com.example.ioanna.menudemo.R.layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;


public class MenuDemo extends AppCompatActivity implements View.OnClickListener {
    //Variables
    MqttAndroidClient client;
    TextView subText;
    String topicStr = "MQTT Examples";
    MqttConnectOptions options;
    int qos= 0;
    Vibrator vibrator;


    //Flashlight's variables
    private Button onOffButton;
    private android.hardware.Camera.Parameters parameters;
    private boolean isFlashOn=false;
    private android.hardware.Camera _camera;
    public String var, myip, myport;
    String clientId, path;
    IMqttToken token;

    //Music's variables
    private boolean isMusicOn=false;
    MediaPlayer mysound;




    //ON CREATE//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_demo);

        //For checking internet
        this.registerReceiver(this.mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        //View of message
        subText = (TextView)findViewById(R.id.subText);

        onOffButton=(Button) findViewById(R.id.button2);
        onOffButton.setOnClickListener(this);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);




        //FOR FLASH'S FUNCTION//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
        if( getIntent().getBooleanExtra("Exit App", false)){
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }
    }


    //INTERNET CONNECTION//
    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if(currentNetworkInfo.isConnected()){
                Toast.makeText(getApplicationContext(), "Connected to the Internet", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "No Internet Connection. Please enable WiFi or Mobile Data", Toast.LENGTH_LONG).show();
            }
        }
    };




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.exit:
                finish();
                super.onDestroy();
                System.runFinalizersOnExit(true);
                System.exit(0);
                return (true);

            case R.id.settings:

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MenuDemo.this);
                View mView = getLayoutInflater().inflate(layout.dialog_op, null);
                final EditText mIP = (EditText) mView.findViewById(R.id.editText1);
                final EditText mPort = (EditText) mView.findViewById(R.id.editText);


                mBuilder.setMessage("Set IP and Port")
                        .setView(mView)
                        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!mIP.getText().toString().isEmpty() && !mPort.getText().toString().isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "Set IP & Port successfull" + mIP.getText().toString() + mPort.getText().toString(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please fill in empty fields", Toast.LENGTH_LONG).show();
                                }

                                myip = mIP.getText().toString();
                                myport = mPort.getText().toString();

                            }

                        })
                        .setNegativeButton("Cancel", null)
                        .setCancelable(false);


                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    break;

            case R.id.frequency:

                final EditText freq = new EditText(this);

                freq.setHint("Type here");

                AlertDialog.Builder mBuilder1 = new AlertDialog.Builder(this)
                        .setTitle("Set Frequency")
                        .setMessage("Frequency in seconds")
                        .setView(freq)
                        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String freqtime = freq.getText().toString();
                                var = freqtime;
                                //Toast.makeText(getApplicationContext(), "FREQUENCY TIME " +freqtime, Toast.LENGTH_LONG).show();

                            }
                        })
                        .setNegativeButton("Cancel", null);
                AlertDialog dialog1 = mBuilder1.create();
                dialog1.show();


        }
            return super.onOptionsItemSelected(item);
    }

        //EXIT BUTTON
        public void clickexit(View v){
            finish();
            super.onDestroy();
            System.runFinalizersOnExit(true);
            System.exit(0);
    }



    //ON BACK PRESSED//
    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MenuDemo.this);
        builder.setMessage("Are you sure want to do this?");
        builder.setCancelable(true);
        builder.setNegativeButton("No",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface,int i){
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Close!",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    //SET SUBSCRIPTION//
    private void setSubscription(){
        try {
            IMqttToken subToken = client.subscribe(topicStr, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // successfully subscribed
                    Toast.makeText(MenuDemo.this, "Successfully subscribed to: " + topicStr, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                    Toast.makeText(MenuDemo.this, "Couldn't subscribe to: " + topicStr, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public void getCamera(){
        if(_camera==null){
            try{
                _camera= Camera.open();
                parameters= _camera.getParameters();
            }
            catch (Exception ex){
                Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }


    //FLASH'S ONCLICK//
    @Override
    public void onClick(View view){
        if(view== onOffButton){
            if (isFlashOn){
                turnOffFlash();
            }
            else{
                getCamera();
                turnOnFlash();
            }
        }
    }


    //TURN ON FLASH FUNCTION//
    public void turnOnFlash(){
        try{
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            _camera.setParameters(parameters);
            _camera.startPreview();
            isFlashOn= true;
            onOffButton.setText("off");
        }
        catch (Exception ex){
            Toast.makeText(this, ex.toString(),Toast.LENGTH_LONG).show();
        }
    }



    //TURN OFF FLASH FUNCTION//
    public void turnOffFlash(){
        try{
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            _camera.setParameters(parameters);
            _camera.stopPreview();
            isFlashOn= false;
            onOffButton.setText("on");
        }
        catch (Exception ex){
            Toast.makeText(this, ex.toString(),Toast.LENGTH_LONG).show();
        }
    }


    //PUBLISHER//
    public void pub(View view){
        String topicStr2= "Examples";
        String message= var;
        try{
            client.publish(topicStr2, message.getBytes(), 0, false);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }



    //MUSIC//
    public void play(){
        mysound=MediaPlayer.create(this, R.raw.sound1);
        mysound.start();
        isMusicOn=true;
    }


    public void stop(){
        mysound.release();
        isMusicOn=false;
    }




    //CONNECT FUNCTION
    public void connect_op(View view){
         path="tcp://" + myip + ":" + myport;
        Toast.makeText(MenuDemo.this, "Path is: " + path, Toast.LENGTH_SHORT).show();

        clientId = MqttClient.generateClientId();
        options = new MqttConnectOptions();
        try {
            client = new MqttAndroidClient(this.getApplicationContext(),path,
                    clientId);
            token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MenuDemo.this,"Connected!",Toast.LENGTH_LONG).show();
                    setSubscription();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(MenuDemo.this,"Connection failed",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }



        //SET CALL BACK//
        client.setCallback(new MqttCallback(){
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connection lost!"+cause);
            }
            @Override
            public void messageArrived(String topicStr, MqttMessage message) throws Exception {
                subText.setText(new String(message.getPayload()));

                byte[] payload = message.getPayload();
                String fmessage = new String(payload);


                if (fmessage.equals("Execute EyesClosed") && isMusicOn==false && isFlashOn==false){
                    Toast.makeText(MenuDemo.this,"Flash & Music are already OFF",Toast.LENGTH_LONG).show();
                }else if (fmessage.equals("Execute EyesOpened") && isMusicOn==true && isFlashOn==true){
                    Toast.makeText(MenuDemo.this,"Flash & Music are already ON",Toast.LENGTH_LONG).show();

                }else if (fmessage.equals("Execute EyesOpened")){
                    getCamera();
                    play();
                    turnOnFlash();
                }else if (fmessage.equals( "Execute EyesClosed")){
                    getCamera();
                    turnOffFlash();
                    stop();

                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });

    }



    @Override
    public void onStop(){
        super.onStop();
        if( _camera!= null){
            _camera.release();
            _camera=null;
            parameters=null;
        }
    }


    @Override
    public void onRestart(){

        super.onRestart();
    }
}


