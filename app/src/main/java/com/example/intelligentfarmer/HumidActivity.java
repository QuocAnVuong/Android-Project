package com.example.intelligentfarmer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.CircularGauge;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.enums.Anchor;
import com.anychart.graphics.vector.text.HAlign;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class HumidActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;
    ToggleButton waterBtn;
    ToggleButton windBtn;
    int count=6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humid);
        //First chart
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
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

        cartesian.title("HUMIDITY");


        AnyChartView anyChartView1 = findViewById(R.id.any_chart_view1);
        APIlib.getInstance().setActiveAnyChartView(anyChartView1);

        CircularGauge circularGauge = AnyChart.circular();
        anyChartView1.setChart(circularGauge);


        double currentValue = 13.8D;
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
                .text("<span style=\"font-size: 25\">Humidity</span>")
                .useHtml(true)
                .hAlign(HAlign.CENTER);
        circularGauge.label(0)
                .anchor(Anchor.CENTER_TOP)
                .offsetY(100)
                .padding(15, 20, 0, 0);

        circularGauge.label(1)
                .text("<span style=\"font-size: 20\">" + currentValue + "</span>")
                .useHtml(true)
                .hAlign(HAlign.CENTER);
        circularGauge.label(1)
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
                    APIlib.getInstance().setActiveAnyChartView(anyChartView1);
                    double currentValue = Integer.parseInt(message.toString());
                    circularGauge.data(new SingleValueDataSet(new Double[] {currentValue}));
                    circularGauge.label(1)
                            .text("<span style=\"font-size: 20\">" + currentValue + "</span>");
                    APIlib.getInstance().setActiveAnyChartView(anyChartView);
                    data.add(new ValueDataEntry(count,Integer.parseInt(message.toString())));
                    cartesian.data(data);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        waterBtn=findViewById(R.id.water_togglebtn);
        waterBtn.setText("Water Off".toString());

        waterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(waterBtn.isChecked()){
                    waterBtn.setText("Water On".toString());
                }
                else{
                    waterBtn.setText("Water Off".toString());
                }
            }
        });

        windBtn=findViewById(R.id.wind_togglebtn);
        windBtn.setText("Fan Off".toString());

        windBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(windBtn.isChecked()){
                    windBtn.setText("Fan On".toString());
                }
                else{
                    windBtn.setText("Fan Off".toString());
                }
            }
        });
    }
}