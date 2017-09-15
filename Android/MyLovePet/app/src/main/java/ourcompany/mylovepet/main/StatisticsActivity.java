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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import ourcompany.mylovepet.R;
<<<<<<< HEAD
<<<<<<< HEAD


=======
>>>>>>> parent of 936c985... URL 클래스
=======
>>>>>>> parent of 936c985... URL 클래스
import ourcompany.mylovepet.task.ServerTaskManager;
import ourcompany.mylovepet.task.TaskListener;
import static ourcompany.mylovepet.R.id.chart;

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

        lineChart = (LineChart) findViewById(chart);
        start_temp = (EditText) findViewById(R.id.start_temp_statistic);
        end_temp = (EditText) findViewById(R.id.end_temp_statistic);
        dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

        start_temp.setOnClickListener(this);
        end_temp.setOnClickListener(this);

        s_Date = LocalDate.now();
        e_Date = LocalDate.now();

    }
    private void updateDate(){
        count = Days.daysBetween(s_Date,e_Date).getDays()+1;
        graph(count,s_Date,e_Date);
    }
    private void graph(int count,LocalDate start, LocalDate end){
        int cnt;
        cnt = count;
        entries = new ArrayList<>();
        int[] numArr = new int[count];
        final HashMap<Integer, String> numMap = new HashMap<>();
        String[] token = start.toString(dateTimeFormat).split("-");
        int year = Integer.parseInt(token[0]);
        int month = Integer.parseInt(token[1]);
        int day = Integer.parseInt(token[2]);
        String[] token2 = end.toString(dateTimeFormat).split("-");
        int year2 = Integer.parseInt(token2[0]);
        int month2 = Integer.parseInt(token2[1]);
        int day2 = Integer.parseInt(token2[2]);

        for(int i = 0;i<cnt;i++){

            numMap.put(i,day+""); // 여기 i 값에 온도를 넣어주면 됨

            if(month ==1||month==3||month==5||month==7||month==8||month==10||month==12){
                if(day == 31){
                    day = 1;
                    month = month +1;
                }else{
                    day = day + 1;
                }
            }else if(month==2){
                if(day == 28){
                    day = 1;
                    month = month +1;
                }else{
                    day = day + 1;
                }
            }else{
                if(day == 30){
                    day = 1;
                    month = month +1;
                }else{
                    day = day + 1;
                }
            }
        }

        for(int i = 0 ; i< count ; i++) {
            numArr[i] = i;
        }

        for(int num : numArr){ entries.add(new Entry(num, num)); }

        lineDataSet = new LineDataSet(entries, "temperature");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setValueTextSize(16);
        lineDataSet.setCircleColor(Color.LTGRAY);
        lineDataSet.setCircleColorHole(Color.BLUE);
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
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override public String getFormattedValue(float value, AxisBase axis) {
                return numMap.get((int)value);
            }
            public int getDecimalDigits() {
                return 0; } });

        lineChart.setData(lineData);

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
                    start_temp.setText(s_Date.toString(dateTimeFormat));
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
                    }else{
                        end_temp.setText(e_Date.toString(dateTimeFormat));
                        updateDate();
                    }
                    //날짜 수정
                }
            }, e_Date.getYear(), e_Date.getMonthOfYear() - 1, e_Date.getDayOfMonth()).show();
            //다이얼 로그 끝
        }
    }


    private void getStatistics(String strStart, String strEnd, int dateType){
        RequestBody body = new FormBody.Builder()
                .add("serialNo", serialNo+"")
                .add("startDate",strStart)
                .add("endDate",strEnd)
                .add("date",dateType+"").build();

        Request request = new Request.Builder()
                .url("http://58.226.2.45/Servlet/avgCondition")
                .post(body)
                .build();

        new ServerTaskManager(request,this,getApplicationContext()).execute();
    }

    @Override
    public void preTask() {
    }

    @Override
    public void postTask(byte[] bytes) {
        try {
            String body = new String(bytes, Charset.forName("utf-8"));
            JSONObject jsonObject = new JSONObject(body);
            jsonObject = jsonObject.getJSONObject("report");
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            statistics.clear();
            if(jsonArray.length() == 0){
                Toast.makeText(this,"정보 없음",Toast.LENGTH_SHORT).show();
            }
            for(int i = 0; i < jsonArray.length(); i++){
                jsonObject = jsonArray.getJSONObject(i);
                String date = jsonObject.getString("date");

                double avgStep = jsonObject.getDouble("avgStep");
                double avgTemp = jsonObject.getDouble("avgTemp");
                double avgHeart = jsonObject.getDouble("avgHeart");

                AvgStatistic statistic = new AvgStatistic();
                statistic.setDate(LocalDate.parse(date));
                statistic.setAvgStep(avgStep);
                statistic.setAvgTemp(avgTemp);
                statistic.setAvgHeart(avgHeart);
                statistics.add(statistic);
            }
            updateChart();
        } catch (JSONException e ) {
            e.printStackTrace();
            Toast.makeText(this, "서버 통신 오류", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void fairTask() {

    }

    //툴바에 있는 뒤로가기 버튼이 눌렀을때 해야할 동작을 정의
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class AvgStatistic{

        private LocalDate date;
        private double avgStep;
        private double avgTemp;
        private double avgHeart;

        public double getAvgStep() {
            return avgStep;
        }

        public void setAvgStep(double avgStep) {
            this.avgStep = avgStep;
        }

        public double getAvgTemp() {
            return avgTemp;
        }

        public void setAvgTemp(double avgTemp) {
            this.avgTemp = avgTemp;
        }

        public double getAvgHeart() {
            return avgHeart;
        }

        public void setAvgHeart(double avgHeart) {
            this.avgHeart = avgHeart;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }
    }
}




