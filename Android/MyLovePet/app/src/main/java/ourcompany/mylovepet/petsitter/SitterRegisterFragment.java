package ourcompany.mylovepet.petsitter;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashSet;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import ourcompany.mylovepet.R;

import ourcompany.mylovepet.ServerURL;
import ourcompany.mylovepet.main.HomeFragment;
import ourcompany.mylovepet.main.user.Pet;
import ourcompany.mylovepet.main.user.PetManager;
import ourcompany.mylovepet.main.user.User;
import ourcompany.mylovepet.task.ServerTaskManager;
import ourcompany.mylovepet.task.TaskListener;

public class SitterRegisterFragment extends Fragment implements View.OnClickListener, TaskListener{


    EditText editTextPetCount, editTextBody, editTextTitle;
    EditText startDateEditText, endDateEditText, totalDay;
    LocalDate sDate, eDate;
    DateTimeFormatter dateTimeFormat;

    ViewPager viewPager;

    HashSet<Integer> petNoSet;

    View leftCursor, rightCursor;

    PetManager petManager;

    public SitterRegisterFragment(){
        petManager = User.getIstance().getPetManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_petsitter_register,container,false);
        init(view);
        return view;
    }

    private void init(View view) {
        //펫 카운트 뷰 찾기
        editTextPetCount = (EditText)view.findViewById(R.id.animal_count);
        editTextBody = (EditText)view.findViewById(R.id.editTextBody);
        editTextTitle = (EditText)view.findViewById(R.id.editTextTitle);

        //날짜 관련 뷰 찾기
        startDateEditText = (EditText) view.findViewById(R.id.input_sDate);
        endDateEditText = (EditText) view.findViewById(R.id.input_eDate);
        totalDay = (EditText)view.findViewById(R.id.total_day);

        leftCursor = view.findViewById(R.id.leftCursor);
        rightCursor = view.findViewById(R.id.rightCursor);


        //타임 포맷 지정
        dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd");

        sDate = LocalDate.now();
        eDate = LocalDate.now();

        //현재 날짜로 설정
        startDateEditText.setText(sDate.toString(dateTimeFormat));
        endDateEditText.setText(eDate.toString(dateTimeFormat));

        //리스너 등록
        startDateEditText.setOnClickListener(this);
        endDateEditText.setOnClickListener(this);
        //findViewById(R.id.findPost_button).setOnClickListener(this);
        view.findViewById(R.id.buttonAddBoard).setOnClickListener(this);

        viewPager = (ViewPager)view.findViewById(R.id.viewPagerPetList);

        Pet[] pets = petManager.getPets();

        viewPager.setAdapter(new PetViewPager(getActivity().getLayoutInflater(), pets));
        //좌우 페이지를 저장해두는 최대 갯수를 설정
        viewPager.setOffscreenPageLimit(pets.length);

        //오른쪽 커서를 보이게 한다.
        if(pets.length > 1){
            rightCursor.setVisibility(View.VISIBLE);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pageSize = viewPager.getAdapter().getCount();

                if( position == 0 ){
                    leftCursor.setVisibility(View.INVISIBLE);
                    rightCursor.setVisibility(View.VISIBLE);
                }else if(position == (pageSize-1)){
                    leftCursor.setVisibility(View.VISIBLE);
                    rightCursor.setVisibility(View.INVISIBLE);
                }else {
                    leftCursor.setVisibility(View.VISIBLE);
                    rightCursor.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        petNoSet = new HashSet<>();

    }

    private void updateDate(){
        startDateEditText.setText(sDate.toString(dateTimeFormat));
        endDateEditText.setText(eDate.toString(dateTimeFormat));
        updateTotalDay();
    }

    private void updateTotalDay() {
        totalDay.setText((Days.daysBetween(sDate, eDate).getDays()+1)+"일");
    }

    private void updatePetCount(){
        int size = petNoSet.size();
        editTextPetCount.setText(size+" 마리");
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.input_sDate) {
            //다이얼 로그 시작
            new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override //달력 설정시 동작 정의
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, dayOfMonth);
                    sDate = LocalDate.fromCalendarFields(cal);

                    if(sDate.isBefore(LocalDate.now())){
                        Toast.makeText(getContext(),"시작일이 현재 일보다 작을 수 없습니다.",Toast.LENGTH_SHORT).show();
                        sDate = LocalDate.now();
                    }
                    if(eDate.isBefore(sDate)){
                        eDate = sDate.plusDays(1);
                    }
                    //날짜 수정
                    updateDate();
                }
            }, sDate.getYear(), sDate.getMonthOfYear() - 1, sDate.getDayOfMonth()).show();
            //다이얼 로그 끝
        } else if (v.getId() == R.id.input_eDate) {
            //다이얼 로그 시작
            new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override //달력 설정시 동작 정의
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, dayOfMonth);
                    eDate = LocalDate.fromCalendarFields(cal);

                    if(eDate.isBefore(sDate)){
                        Toast.makeText(getContext(),"시작일보다 종료일이 작을 수 없습니다.",Toast.LENGTH_SHORT).show();
                        eDate = sDate.plusDays(1);
                    }
                    //날짜 수정
                    updateDate();
                }
            }, eDate.getYear(), eDate.getMonthOfYear() - 1, eDate.getDayOfMonth()).show();
            //다이얼 로그 끝
        }else if (v.getId() == R.id.buttonAddBoard){
            if(petNoSet.size() == 0){
                Toast.makeText(getContext(),"펫을 추가 해주세요",Toast.LENGTH_SHORT).show();
            }else {
                registerExecute();
            }
        }

    }

    private void registerExecute(){
        String strSDate,strEDate,strBody, strTitle;
        JSONArray jsonArray;

        strSDate = startDateEditText.getText().toString();
        strEDate = endDateEditText.getText().toString();
        strTitle = editTextTitle.getText().toString();
        strBody = editTextBody.getText().toString();

        jsonArray = new JSONArray();

        for(int no : petNoSet){
            jsonArray.put(no);
        }

        RequestBody body= new FormBody.Builder()
                .add("Date", strSDate)
                .add("Term", strEDate)
                .add("Title", strTitle)
                .add("Feedback", strBody)
                .add("petList", jsonArray.toString())
                .build();

        Request request = new Request.Builder()
                .url(ServerURL.PET_SITTER_ADD_URL)
                .post(body)
                .build();

        new ServerTaskManager(request,this,getContext().getApplicationContext()).execute();
    }

    private void finish(){
        getFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
    }


    // taskListener 메소드
    @Override
    public void preTask() {
    }

    @Override
    public void postTask(byte[] bytes) {
        try {
            String body = new String(bytes, Charset.forName("utf-8"));
            JSONObject jsonObject = new JSONObject(body);
            jsonObject = jsonObject.getJSONObject("AddPetSitter");
            boolean isSuccessed = false;
            isSuccessed = jsonObject.getBoolean("isSuccessed");

            if(isSuccessed){
                Toast.makeText(getContext(),"글 등록 완료",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getContext(),"등록 실패",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "서버 통신 실패", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public void fairTask() {

    }
    // taskListener 메소드 end


    private class PetViewPager extends PagerAdapter{

        Pet[] pets;
        LayoutInflater inflater;

        View.OnClickListener addListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                petNoSet.add(pets[position].getPetNo());
                ((Button)v).setText("취소");
                v.setOnClickListener(deleteListener);
                updatePetCount();
            }
        };

        View.OnClickListener deleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                petNoSet.remove(pets[position].getPetNo());
                ((Button)v).setText("추가");
                v.setOnClickListener(addListener);
                updatePetCount();
            }
        };


        public PetViewPager(LayoutInflater inflater, Pet[] pets){
            this.pets = pets;
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return pets.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = inflater.inflate(R.layout.pet,container,false);

            TextView textViewPetName = (TextView)view.findViewById(R.id.textViewPetName);
            textViewPetName.setText(pets[position].getName());
            ImageView profilePicture = (ImageView)view.findViewById(R.id.profile_picture);

            String strFileNo = User.getIstance().getPetManager().getPet(position).getPhotoFileNo();
            Picasso.with(getContext())
                    .load(ServerURL.PROFILE_DOWNLOAD_URL + strFileNo)
                    .error(R.drawable.defaultprofileimage)
                    .into(profilePicture);

            view.findViewById(R.id.buttonAdd).setOnClickListener(addListener);

            container.addView(view);

            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

}

