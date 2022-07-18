package com.example.intelligentfarmer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.LinearGauge;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.enums.Anchor;
import com.anychart.enums.Orientation;
import com.anychart.enums.Position;
import com.anychart.scales.Base;
import com.anychart.scales.Linear;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class TempActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;
    ToggleButton heaterBtn;
    TextView mess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
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

        cartesian.title("TEMPERATURE");

//Second Chart
        AnyChartView anyChartView1 = findViewById(R.id.any_chart_view1);
        APIlib.getInstance().setActiveAnyChartView(anyChartView1);

        LinearGauge linearGauge = AnyChart.linear();

        linearGauge.data(new SingleValueDataSet(new Integer[]{2} ));

        linearGauge.tooltip()
                .useHtml(true)
                .format(
                        "function () {\n" +
                                "          return this.value + '&deg;' + 'C' +\n" +
                                "            ' (' + (this.value * 1.8 + 32).toFixed(1) +\n" +
                                "            '&deg;' + 'F' + ')'\n" +
                                "    }");

        linearGauge.label(0).useHtml(true);
        linearGauge.label(0)
                .text("C&deg;")
                .position(Position.LEFT_BOTTOM)
                .anchor(Anchor.LEFT_BOTTOM)
                .offsetY("5px")
                .offsetX("33%")
                .fontColor("black")
                .fontSize(15);

        linearGauge.label(1)
                .useHtml(true)
                .text("F&deg;")
                .position(Position.RIGHT_BOTTOM)
                .anchor(Anchor.RIGHT_BOTTOM)
                .offsetY("5px")
                .offsetX("40%")
                .fontColor("black")
                .fontSize(15);

        Base scale = linearGauge.scale()
                .minimum(-30)
                .maximum(40);
//                .setTicks

        linearGauge.axis(0).scale(scale);
        linearGauge.axis(0)
                .offset("-1%")
                .width("0.5%");

        linearGauge.axis(0).labels()
                .format("{%Value}&deg;")
                .useHtml(true);

        linearGauge.thermometer(0)
                .name("Thermometer")
                .id(1);

        linearGauge.axis(0).minorTicks(true);
        linearGauge.axis(0).labels()
                .format(
                        "function () {\n" +
                                "    return '<span style=\"color:black;\">' + this.value + '&deg;</span>'\n" +
                                "  }")
                .useHtml(true);

        linearGauge.axis(1).minorTicks(true);
        linearGauge.axis(1).labels()
                .format(
                        "function () {\n" +
                                "    return '<span style=\"color:black;\">' + this.value + '&deg;</span>'\n" +
                                "  }")
                .useHtml(true);
        linearGauge.axis(1)
                .offset("3.5%")
                .orientation(Orientation.RIGHT);

        Linear linear = Linear.instantiate();
        linear.minimum(-20)
                .maximum(100);
//                .setTicks
        linearGauge.axis(1).scale(linear);
        anyChartView1.setChart(linearGauge);

        mess = findViewById(R.id.txtMessage);
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
                String Temp="VTrung/feeds/temperature";
                String Mess="VTrung/feeds/message";
                if (topic.compareTo(Temp)==0){
                    APIlib.getInstance().setActiveAnyChartView(anyChartView1);
                    linearGauge.data(new SingleValueDataSet(new Integer[]{Integer.parseInt(message.toString())} ));
                }
                if (topic.compareTo(Mess)==0){
                    mess.setText(message.toString());
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        heaterBtn=findViewById(R.id.heater_togglebtn);
        heaterBtn.setText("Heater Off".toString());

        heaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(heaterBtn.isChecked()){
                    heaterBtn.setText("Heater On".toString());
                }
                else{
                    heaterBtn.setText("Heater Off".toString());
                }
            }
        });
    }
}