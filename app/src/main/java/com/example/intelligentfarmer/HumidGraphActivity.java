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

public class HumidGraphActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;
    ImageButton lastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humid_graph);
        //First chart
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        Cartesian cartesian = AnyChart.line();
        cartesian.background().fill("#05103A");
        anyChartView.setChart(cartesian);


        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Jul 15", 78));
        data.add(new ValueDataEntry("Jul 16", 79));
        data.add(new ValueDataEntry("Jul 17", 90));
        data.add(new ValueDataEntry("Jul 18", 85));
        data.add(new ValueDataEntry("Jul 19", 20));

        cartesian.data(data);

        lastPage = findViewById(R.id.last_page1);

        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HumidActivity.class));
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
                String Humid="VTrung/feeds/humidity";
                if(topic.compareTo(Humid)==0){
                    data.add(new ValueDataEntry("Jul 20",Integer.parseInt(message.toString())));
                    cartesian.data(data);
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}