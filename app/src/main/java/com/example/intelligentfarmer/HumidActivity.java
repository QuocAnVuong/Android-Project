package com.example.intelligentfarmer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.charts.CircularGauge;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.enums.Anchor;
import com.anychart.graphics.vector.text.HAlign;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class HumidActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;
    Switch waterBtn;
    TextView watertxt;
    Switch windBtn;
    TextView windtxt;
    ImageButton lastPage;
    ImageButton humidGraph;
    int count=6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humid);
        //First chart
        /*AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        Cartesian cartesian = AnyChart.line();
        anyChartView.setChart(cartesian);


        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry(1, 78));
        data.add(new ValueDataEntry(2, 79));
        data.add(new ValueDataEntry(3, 90));
        data.add(new ValueDataEntry(4, 85));
        data.add(new ValueDataEntry(5, 20));

        cartesian.data(data);

        cartesian.title("HUMIDITY");*/

        lastPage = findViewById(R.id.last_page);

        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        humidGraph = findViewById(R.id.humid_graph);

        humidGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HumidGraphActivity.class));
            }
        });


        AnyChartView anyChartView1 = findViewById(R.id.any_chart_view1);
        APIlib.getInstance().setActiveAnyChartView(anyChartView1);

        CircularGauge circularGauge = AnyChart.circular();
        anyChartView1.setChart(circularGauge);


        double currentValue = 13.8D;
        circularGauge.background().fill("#05103A");
        circularGauge.data(new SingleValueDataSet(new Double[] {currentValue}));

        circularGauge.axis(0).labels().position("outside");
        circularGauge.axis(0)
                .startAngle(-150)
                .radius(80)
                .sweepAngle(300)
                .width(3)
                .ticks("{ type: 'line', length: 4, position: 'outside' }");
        circularGauge.axis(0).scale()
                .minimum(0)
                .maximum(140);

        circularGauge.needle(0)
                .stroke(null)
                .startRadius("6%")
                .endRadius("38%")
                .startWidth("2%")
                .endWidth(0);
        circularGauge.cap()
                .radius("4%")
                .enabled(true)
                .stroke(null);

        circularGauge.label(0)
                .text("<span style=\"font-size: 20\">" + currentValue + "</span>")
                .useHtml(true)
                .hAlign(HAlign.CENTER);
        circularGauge.label(0)
                .anchor(Anchor.CENTER_TOP)
                .offsetY(-70)
                .background("{fill: 'none', stroke: '#c1c1c1', corners: 3, cornerType: 'ROUND'}");

        circularGauge.range(0,
                "{\n" +
                        "    from: 0,\n" +
                        "    to: 25,\n" +
                        "    position: 'inside',\n" +
                        "    fill: 'green 0.5',\n" +
                        "    stroke: '1 #000',\n" +
                        "    startSize: 6,\n" +
                        "    endSize: 6,\n" +
                        "    radius: 80,\n" +
                        "    zIndex: 1\n" +
                        "  }");

        circularGauge.range(1,
                "{\n" +
                        "    from: 80,\n" +
                        "    to: 140,\n" +
                        "    position: 'inside',\n" +
                        "    fill: 'red 0.5',\n" +
                        "    stroke: '1 #000',\n" +
                        "    startSize: 6,\n" +
                        "    endSize: 6,\n" +
                        "    radius: 80,\n" +
                        "    zIndex: 1\n" +
                        "  }");


        waterBtn=findViewById(R.id.water_switch);
        watertxt=findViewById(R.id.water_txt);
        windBtn=findViewById(R.id.wind_switch);
        windtxt=findViewById(R.id.wind_txt);

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
                String Water="VTrung/feeds/water";
                String Fan="VTrung/feeds/fan";
                if(topic.compareTo(Humid)==0){
                    APIlib.getInstance().setActiveAnyChartView(anyChartView1);
                    double currentValue = Integer.parseInt(message.toString());
                    circularGauge.data(new SingleValueDataSet(new Double[] {currentValue}));
                    circularGauge.label(0)
                            .text("<span style=\"font-size: 20\">" + currentValue + "</span>");

                }
                if(topic.compareTo(Water)==0){
                    if(message.toString().compareTo("ON")==0){
                        waterBtn.setChecked(true);
                        watertxt.setText("On");
                    }
                    else{waterBtn.setChecked(false);watertxt.setText("Off");}
                }
                if(topic.compareTo(Fan)==0){
                    if(message.toString().compareTo("ON")==0){
                        windBtn.setChecked(true);
                        windtxt.setText("On");
                    }
                    else{windBtn.setChecked(false);windtxt.setText("Off");}
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        waterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(waterBtn.isChecked()){
                    watertxt.setText("On");
                }
                else{watertxt.setText("Off");}
            }
        });

        windBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(windBtn.isChecked()){
                    windtxt.setText("On");
                }
                else{windtxt.setText("Off");}
            }
        });

    }
}