package ourcompany.mylovepet.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.task.ServerTaskManager;
import ourcompany.mylovepet.task.TaskListener;

import static ourcompany.mylovepet.R.id.chart;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener,TaskListener {
    LineChart lineChart;
    EditText start_temp,end_temp;
    TextView textViewTitle;
    LocalDate sDate, eDate;
    DateTimeFormatter dateTimeFormat;
    DateTimeFormatter dayFormat;

    int pageType;
    int serialNo;

    RadioGroup radioGroup;

    ArrayList<String> xLabels;

    public final static int TEMP_PAGE = 0;
    public final static int WALK_PAGE = 1;
    public final static int HEART_PAGE = 2;

    private String[] strTitles = {"온도","걸음수","심박수"};

    ArrayList<AvgStatistic> statistics = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setTitle("통계");
        actionBar.setDisplayHomeAsUpEnabled(true);

        lineChart = (LineChart) findViewById(chart);
        start_temp = (EditText) findViewById(R.id.start_temp_statistic);
        end_temp = (EditText) findViewById(R.id.end_temp_statistic);
        textViewTitle = (TextView) findViewById(R.id.title);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
        dayFormat = DateTimeFormat.forPattern("dd");

        start_temp.setOnClickListener(this);
        end_temp.setOnClickListener(this);

        Intent intent = getIntent();

        pageType = intent.getIntExtra("pageType",-1);
        serialNo = intent.getIntExtra("serialNo",-1);

        if(pageType == TEMP_PAGE){
            radioGroup.check(R.id.radioBtnTemp);
        }else if(pageType == WALK_PAGE){
            radioGroup.check(R.id.radioBtnWalk);
        }else {
            radioGroup.check(R.id.radioBtnHeart);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.radioBtnTemp:
                        pageType = TEMP_PAGE;
                        break;
                    case R.id.radioBtnWalk:
                        pageType = WALK_PAGE;
                        break;
                    case R.id.radioBtnHeart:
                        pageType = HEART_PAGE;
                        break;
                }
                changeTitle();
                updateChart();
            }
        });

        chartInit();
        changeTitle();

        eDate = LocalDate.now();
        sDate = eDate.minusDays(7);
        changeDateTextView();
        getStatistics(dateTimeFormat.print(sDate), dateTimeFormat.print(eDate), 0);
    }

    private void chartInit(){
        lineChart.setData(new LineData());
        lineChart.getData().addDataSet(createSet());
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getData().setHighlightEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(16, 12, 0);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.BLACK);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(0, 0, 0));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }


    private void changeTitle(){
        textViewTitle.setText(strTitles[pageType]);
    }

    private void updateChart(){
       graph();
    }

    private void changeDateTextView(){
        start_temp.setText(dateTimeFormat.print(sDate));
        end_temp.setText(dateTimeFormat.print(eDate));
    }

    private void graph(){

        LineData lineData = lineChart.getData();
        ILineDataSet set = lineData.getDataSetByIndex(0);
        set.clear();

        set.setLabel(strTitles[pageType]);

        xLabels = new ArrayList<>();

        float maxY = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;
        for(AvgStatistic avgStatistic : statistics){
            float y;
            if(pageType == TEMP_PAGE){
                y = (float) avgStatistic.getAvgTemp();
            }else if(pageType == WALK_PAGE){
                y = (float) avgStatistic.getAvgStep();
            }else {
                y = (float) avgStatistic.getAvgHeart();
            }
            if(y> maxY){
                maxY = y;
            }
            if(y < minY){
                minY = y;
            }
            set.addEntry(new Entry(set.getEntryCount(),y));
            xLabels.add(dayFormat.print(avgStatistic.getDate()));
        }

        lineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(xLabels.size() <= value || value < 0)
                    return "";
                return xLabels.get((int)value)+"일";
            }
            public int getDecimalDigits() {
                return 0;
            }
        });


        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();

        lineChart.getAxisLeft().setAxisMaximum(maxY + (maxY * 0.07f));
        lineChart.getAxisLeft().setAxisMinimum(minY - (minY * 0.07f));
        lineChart.getXAxis().setAxisMaximum((float)set.getEntryCount()-0.8f);

        lineChart.fitScreen();

     /* lineDataSet = new LineDataSet(entries, strTitles[pageType]);
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

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(16, 12, 0);

        xAxis.setLabelCount(statistics.size()); // x 축 갯수

        xAxis.setGranularity(1);
        xAxis.setTextSize(20);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return Float.toString(value);
            }
            public int getDecimalDigits() {
                return 0;
            }
        });

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
*/
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
                    sDate = LocalDate.fromCalendarFields(cal);

                    if (sDate.isAfter(eDate)) {
                        Toast.makeText(getApplicationContext(), "끝 날짜보다 클 수 없습니다.", Toast.LENGTH_SHORT).show();
                        sDate = eDate.minusDays(1);
                    }
                    else if (sDate.isAfter(LocalDate.now())) {
                        Toast.makeText(getApplicationContext(), "현재 일보다 클 수 없습니다.", Toast.LENGTH_SHORT).show();
                        sDate = eDate.minusDays(1);
                    }
                    getStatistics(dateTimeFormat.print(sDate), dateTimeFormat.print(eDate), 0);
                    changeDateTextView();
                }
            }, sDate.getYear(), sDate.getMonthOfYear() - 1, sDate.getDayOfMonth()).show();
            //다이얼 로그 끝
        } else if (v.getId() == R.id.end_temp_statistic) {
            //다이얼 로그 시작
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override //달력 설정시 동작 정의
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, dayOfMonth);
                    eDate = LocalDate.fromCalendarFields(cal);

                    if (eDate.isAfter(LocalDate.now())) {
                        Toast.makeText(getApplicationContext(), "현재 일보다 클 수 없습니다.", Toast.LENGTH_SHORT).show();
                        eDate = LocalDate.now();
                    }else if(eDate.isBefore(sDate)){
                        eDate = sDate.plusDays(1);
                        Toast.makeText(getApplicationContext(), "시작일 보다 작을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }

                    getStatistics(dateTimeFormat.print(sDate),dateTimeFormat.print(eDate),0);
                    changeDateTextView();
                    //날짜 수정
                }
            }, eDate.getYear(), eDate.getMonthOfYear() - 1, eDate.getDayOfMonth()).show();
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




