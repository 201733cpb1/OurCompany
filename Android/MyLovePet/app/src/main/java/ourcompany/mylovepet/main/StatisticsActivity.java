package ourcompany.mylovepet.main;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ourcompany.mylovepet.R;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {
    LineChart lineChart;
    List<Entry> entries;
    LineDataSet lineDataSet;
    LineData lineData;
    XAxis xAxis;
    YAxis yAxis;
    YAxis yAxisLeft;
    EditText start_temp,end_temp;
    LocalDate s_Date, e_Date;
    DateTimeFormatter dateTimeFormat;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        lineChart = (LineChart) findViewById(R.id.chart);
        start_temp = (EditText) findViewById(R.id.start_temp_statistic);
        end_temp = (EditText) findViewById(R.id.end_temp_statistic);
        dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

        start_temp.setOnClickListener(this);
        end_temp.setOnClickListener(this);

        s_Date = LocalDate.now();
        e_Date = LocalDate.now();

    }
    private void updateDate(){
        start_temp.setText(s_Date.toString(dateTimeFormat));
        end_temp.setText(e_Date.toString(dateTimeFormat));
        count = Days.daysBetween(s_Date,e_Date).getDays()+1;
        graph(count);
    }
    private void graph(int count){
        int cnt;
        cnt = count;
        entries = new ArrayList<>();
        for(int i = 0;i<cnt;i++){
            entries.add(new Entry(i, cnt)); // x축 y축 값
        }
        lineDataSet = new LineDataSet(entries, "temperature");
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

        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(16, 12, 0);
        xAxis.setLabelCount(cnt); // x 축 갯수
        xAxis.setGranularity(1);
        xAxis.setTextSize(20);

        yAxis = lineChart.getAxisRight();
        yAxis.setDrawLabels(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        yAxis.setSpaceTop(25);
        yAxis.setSpaceBottom(25);

        yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setDrawGridLines(false);
        yAxis.setSpaceTop(25);
        yAxis.setSpaceBottom(25);

        lineChart.invalidate();
    }
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.start_temp_statistic) {
            //다이얼 로그 시작
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override //달력 설정시 동작 정의
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, dayOfMonth);
                    s_Date = LocalDate.fromCalendarFields(cal);

                    if (LocalDate.now().isBefore(s_Date)) {
                        Toast.makeText(getApplicationContext(), "시작일이 현재 일보다 클 수 없습니다.", Toast.LENGTH_SHORT).show();
                        s_Date = LocalDate.now();
                    }
                    if (s_Date.isBefore(s_Date)) {
                        s_Date = s_Date.plusDays(1);
                    }
                    //날짜 수정
                    updateDate();
                }
            }, s_Date.getYear(), s_Date.getMonthOfYear() - 1, s_Date.getDayOfMonth()).show();
            //다이얼 로그 끝
        } else if (v.getId() == R.id.end_temp_statistic) {
            //다이얼 로그 시작
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override //달력 설정시 동작 정의
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, dayOfMonth);
                    e_Date = LocalDate.fromCalendarFields(cal);

                    if (e_Date.isBefore(s_Date) || LocalDate.now().isBefore((e_Date))) {
                        Toast.makeText(getApplicationContext(), "해당 범위에서 벗어납니다.", Toast.LENGTH_SHORT).show();
                        e_Date = s_Date.plusDays(1);
                    }
                    //날짜 수정
                    updateDate();
                }
            }, e_Date.getYear(), e_Date.getMonthOfYear() - 1, e_Date.getDayOfMonth()).show();
            //다이얼 로그 끝
        }
    }
}




