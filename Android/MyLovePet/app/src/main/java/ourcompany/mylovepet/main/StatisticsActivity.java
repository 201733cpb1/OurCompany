package ourcompany.mylovepet.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import ourcompany.mylovepet.R;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(22, 5)); // (x축값, y축값)
        entries.add(new Entry(23, 1));
        entries.add(new Entry(24, 2));
        entries.add(new Entry(25, 9));
        entries.add(new Entry(26, 8));
        LineDataSet lineDataSet = new LineDataSet(entries, "temperature");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setValueTextSize(16);
        lineDataSet.setCircleColor(Color.LTGRAY);
        lineDataSet.setCircleColorHole(Color.BLUE);
        //lineDataSet.setColor(Color.BLUE);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(16, 12, 0);
        xAxis.setLabelCount(5); // x 축 갯수
        xAxis.setGranularity(1);
        xAxis.setTextSize(20);
        YAxis yAxis = lineChart.getAxisRight();
        yAxis.setDrawLabels(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setSpaceTop(25);
        yAxis.setSpaceBottom(25);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setDrawGridLines(false);
        yAxis.setSpaceTop(25);
        yAxis.setSpaceBottom(25);

        lineChart.invalidate();
    }

}




