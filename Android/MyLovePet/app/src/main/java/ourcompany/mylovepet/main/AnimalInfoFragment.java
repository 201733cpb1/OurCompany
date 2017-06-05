package ourcompany.mylovepet.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.userinfo.Pet;

/**
 * Created by REOS on 2017-05-26.
 */

public class AnimalInfoFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    TextView textViewTemperature, textViewWalk, textViewHeartrate;

    SwipeRefreshLayout swipeRefreshLayout;

    Pet pet;


    public AnimalInfoFragment(){
    }

    public void setPet(Pet pet){
        this.pet = pet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animalinfo,container,false);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        textViewTemperature = (TextView)view.findViewById(R.id.textViewTemperature);
        textViewWalk = (TextView)view.findViewById(R.id.textViewWalk);
        textViewHeartrate = (TextView)view.findViewById(R.id.textViewHeartrate);

        view.findViewById(R.id.viewPetWalk).setOnClickListener(this);
        view.findViewById(R.id.viewMeal).setOnClickListener(this);
        view.findViewById(R.id.viewVaccination).setOnClickListener(this);
        view.findViewById(R.id.viewTemperature).setOnClickListener(this);
        onRefresh();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.viewPetWalk:
                intent = new Intent(getContext(), PetWalkActivity.class);
                startActivity(intent);
                break;
            case R.id.viewMeal:
                intent = new Intent(getContext(), MealCalendarActivity.class);
                startActivity(intent);
                break;
            case R.id.viewVaccination:
                intent = new Intent(getContext(),VaccineActivity.class);
                startActivity(intent);
                break;
            case R.id.viewTemperature:
                intent = new Intent(getContext(),StatisticsActivity.class);
                startActivity(intent);
                break;
        }

    }
    @Override
    public void onRefresh() {
        new GetCondition().execute();
    }

    private class GetCondition extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            textViewTemperature.setText("갱신중...");
            textViewWalk.setText("갱신중...");
            textViewHeartrate.setText("갱신중...");
        }

        @Override
        public JSONObject doInBackground(String... params) {
            InputStream inputStream = null;
            JSONObject jsonObject = null;
            int serialNo = pet.getSerialNo();
            String parameter = "serialNo="+serialNo;

            try {
                //HttpURLConnection을 이용해 url에 연결하기 위한 설정
                String url = "http://58.237.8.179/Servlet/getCondition";
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                //커넥션에 각종 정보 설정
                conn.setRequestMethod("POST");
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);
                conn.setDoOutput(true);


                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                writer.write(parameter);
                writer.flush();
                writer.close();


                //응답 http코드를 가져옴
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String str;
                    StringBuilder strBuffer = new StringBuilder();
                    while ((str = bufferedReader.readLine()) != null) {
                        strBuffer.append(str);
                    }
                    jsonObject = new JSONObject(strBuffer.toString());
                    inputStream.close();
                    conn.disconnect();
                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.i("errorInfo", "error occured!" + e.getMessage());
            }

            return jsonObject;

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    jsonObject = jsonObject.getJSONObject("Condition");
                    int temperate = jsonObject.getInt("avgtemp");
                    int step = jsonObject.getInt("step");
                    int heartrate = jsonObject.getInt("avgheart");

                    textViewTemperature.setText(temperate+"");
                    textViewWalk.setText(step+"");
                    textViewHeartrate.setText(heartrate+"");

                    // -1 이라면 정보가 없는것.
                    if(temperate == -1 && step == -1 && heartrate == -1){
                        Toast.makeText(getContext(), "정보가 존재 하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "업데이트 완료", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "업데이트 실패 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }

            //새로고침 애니메이션 취소하기
            swipeRefreshLayout.setRefreshing(false);
        }

    }

}
