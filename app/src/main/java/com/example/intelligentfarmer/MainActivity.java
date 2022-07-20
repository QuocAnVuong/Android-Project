package com.example.intelligentfarmer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    ImageView temperature;
    ImageView humidity;
    TextView temptxt;
    TextView humidtxt;
    Button btnTemp;
    Button btnHum;
    MQTTHelper mqttHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHum = findViewById(R.id.humidButton);
        btnTemp = findViewById(R.id.tempButton);
        temperature = findViewById(R.id.temp_icon);
        humidity = findViewById(R.id.humid_icon);
        temptxt = findViewById(R.id.temp_mess);
        humidtxt = findViewById(R.id.humid_mess);

        btnHum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HumidActivity.class));
            }
        });
        btnTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TempActivity.class));
            }
        });

        mqttHelper = new MQTTHelper(this, "VuongQuocAn");
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String humidMsg = "VTrung/feeds/message";
                String tempMsg = "VTrung/feeds/temperature-message";
                if (topic.compareTo(humidMsg)==0){
                    if(message.toString().compareTo("Just right!!!")==0){
                        humidtxt.setText("Just right");
                        humidity.setImageResource(R.drawable.right);
                    }
                    else if(message.toString().compareTo("Too wet")==0){
                        humidtxt.setText("Too wet");
                        humidity.setImageResource(R.drawable.rain);
                    }
                    else {
                        humidtxt.setText("Too dry");
                        humidity.setImageResource(R.drawable.dry);
                    }
                }
                if (topic.compareTo(tempMsg)==0){
                    if(message.toString().compareTo("Just right!!!")==0){
                        temptxt.setText("Just right");
                        temperature.setImageResource(R.drawable.right);
                    }
                    else if(message.toString().compareTo("Too hot")==0){
                        temptxt.setText("Too hot");
                        temperature.setImageResource(R.drawable.hot);
                    }
                    else {
                        temptxt.setText("Too cold");
                        temperature.setImageResource(R.drawable.snow);
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}