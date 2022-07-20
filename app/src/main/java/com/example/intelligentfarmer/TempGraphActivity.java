package com.example.intelligentfarmer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.chart.common.dataentry.ValueDataEntry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class TempGraphActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;
    ImageButton lastPage;
    int currentData = 0;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_graph);

        lastPage = findViewById(R.id.last_page);

        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TempActivity.class));
            }
        });

        //First chart
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        final Cartesian cartesian = AnyChart.line();
        anyChartView.setChart(cartesian);


        cartesian.background().fill("#05103A");
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Jul 15", 30));
        data.add(new ValueDataEntry("Jul 16", 27));
        data.add(new ValueDataEntry("Jul 17", 22));
        data.add(new ValueDataEntry("Jul 18", 24));
        data.add(new ValueDataEntry("Jul 19", 20));

        cartesian.data(data);


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
                String Temp = "VTrung/feeds/temperature";
                if(topic.compareTo(Temp)==0){
                    data.add(new ValueDataEntry("Jul 20", Integer.parseInt(message.toString())));
                    cartesian.data(data);
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}