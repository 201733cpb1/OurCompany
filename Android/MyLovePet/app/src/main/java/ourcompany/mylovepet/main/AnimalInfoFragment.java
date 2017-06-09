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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.userinfo.Pet;
import ourcompany.mylovepet.main.userinfo.User;

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
        ((TextView)view.findViewById(R.id.textViewPetName)).setText(pet.getName());

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


    private class GetCondition extends AsyncTask<String, Void, Response> {

        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            textViewTemperature.setText("갱신중...");
            textViewWalk.setText("갱신중...");
            textViewHeartrate.setText("갱신중...");
        }

        @Override
        public Response doInBackground(String... params) {
            int serialNo = pet.getSerialNo();
            RequestBody body= new FormBody.Builder().add("serialNo",serialNo+"").build();
            Request request = new Request.Builder()
                    .url("http://58.237.8.179/Servlet/getCondition")
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Response response) {
            if(response == null | response.code() != 200) {
                Toast.makeText(getContext(), "업데이트 실패 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(response.body().string());

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

            } catch (JSONException | IOException e ) {
                e.printStackTrace();
                Toast.makeText(getContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
