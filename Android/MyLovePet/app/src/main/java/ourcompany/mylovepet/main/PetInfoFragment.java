package ourcompany.mylovepet.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.user.PetManager;
import ourcompany.mylovepet.main.user.User;
import ourcompany.mylovepet.task.RequestTask;
import ourcompany.mylovepet.task.TaskListener;

/**
 * Created by REOS on 2017-05-26.
 */

public class PetInfoFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener, TaskListener{

    TextView textViewTemperature, textViewWalk, textViewHeartrate;

    SwipeRefreshLayout swipeRefreshLayout;

    int petIndex;

    //포토
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final int REQUEST_IMAGE_CROP = 2;

    String mCurrentPhotoPath;
    Uri photoURI, albumURI = null;
    CircularImageView profile;
    Boolean album = false;

    Request request;


    public PetInfoFragment(){
    }

    public void setPetIndex(int petIndex){
        this.petIndex = petIndex;
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
        ((TextView)view.findViewById(R.id.textViewPetName)).setText(User.getIstance().getPet(petIndex).getName());

        profile = (CircularImageView)view.findViewById(R.id.profile_picture);
        profile.setOnClickListener(this);

        view.findViewById(R.id.viewPetWalk).setOnClickListener(this);
        view.findViewById(R.id.viewMeal).setOnClickListener(this);
        view.findViewById(R.id.viewVaccination).setOnClickListener(this);
        view.findViewById(R.id.viewTemperature).setOnClickListener(this);

        int serialNo = User.getIstance().getPet(petIndex).getSerialNo();
        RequestBody body= new FormBody.Builder().add("serialNo",serialNo+"").build();
        request = new Request.Builder()
                .url("http://58.237.8.179/Servlet/getCondition")
                .post(body)
                .build();

        getConditionExecute();

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
            case R.id.profile_picture:
                final CharSequence[] items = { "사진촬영", "갤러리","취소" };
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                // 제목셋팅
                alertDialogBuilder.setTitle("선택해 주세요");
                alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 프로그램을 종료한다
                        if(items[id]=="사진촬영"){
                            dispatchTakePictureIntent();
                        }else if(items[id]=="갤러리"){
                            doTakeAlbumAction();
                        }else{
                            dialog.dismiss();
                        }
                        Toast.makeText(getContext(), items[id] + " 선택했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();
                // 다이얼로그 보여주기
                alertDialog.show();
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_TAKE_PHOTO: // 앨범 이미지 가져오기
                album = true;
                File albumFile = null;
                try {
                    albumFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (albumFile != null) {
                    albumURI = Uri.fromFile(albumFile); // 앨범 이미지 Crop한 결과는 새로운 위치 저장
                }

                photoURI = data.getData(); // 앨범 이미지의 경로
                /* profile에 띄우기*/
                Bitmap image_bitmap = null;
                try {
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoURI);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                profile.setImageBitmap(image_bitmap);
                break;
            case REQUEST_IMAGE_CAPTURE:
                Bitmap image_bitmap2 = null;

                try {
                    image_bitmap2 = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoURI);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                profile.setImageBitmap(image_bitmap2);
                break;
            /*case REQUEST_IMAGE_CROP:
                Bitmap photo = BitmapFactory.decodeFile(photoURI.getPath());
                profile.setImageBitmap(photo);

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE); // 동기화
                if (album == false) {
                    mediaScanIntent.setData(photoURI); // 동기화
                } else if (album == true) {
                    album = false;
                    mediaScanIntent.setData(albumURI); // 동기화
                }
                this.sendBroadcast(mediaScanIntent); // 동기화
                break;*/
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null)
        {
            File photoFile = null;
            try {
                photoFile = createImageFile(); // 사진찍은 후 저장할 임시 파일//
            } catch (IOException ex) {
                Toast.makeText(getContext(), "createImageFile Failed", Toast.LENGTH_LONG).show();
            }
            if (photoFile != null) {
                //photoURI = Uri.fromFile(photoFile); // 임시 파일의 위치,경로 가져옴
                photoURI = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); // 임시 파일 위치에 저장
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException{
        String imageFileName = "tmp_"+String.valueOf(System.currentTimeMillis())+".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory(),imageFileName);
        mCurrentPhotoPath = storageDir.getAbsolutePath();
        return storageDir;
    }

    private void doTakeAlbumAction() { // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    @Override
    public void onRefresh() {
        getConditionExecute();
    }

    private void getConditionExecute(){
        new RequestTask(request,this,getContext().getApplicationContext()).execute();
    }


    // tskListener 메소드
    @Override
    public void preTask() {
        textViewTemperature.setText("갱신중...");
        textViewWalk.setText("갱신중...");
        textViewHeartrate.setText("갱신중...");
    }

    @Override
    public void postTask(Response response) {
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

    @Override
    public void cancelTask() {
        textViewTemperature.setText("실패");
        textViewWalk.setText("실패");
        textViewHeartrate.setText("실패");
    }

    @Override
    public void fairTask() {

    }
    // tskListener 메소드 end


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
            int serialNo = User.getIstance().getPet(petIndex).getSerialNo();
            RequestBody body= new FormBody.Builder().add("serialNo",serialNo+"").build();
            Request request = new Request.Builder()
<<<<<<< HEAD
                    .url("http://58.237.8.179/Servlet/getCondition")
                    .post(body)
=======
>>>>>>> parent of 936c985... URL 클래스
                    .url("http://58.226.2.45/Servlet/animalProfileUpload")
                    .post(multipartBody)
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
            if(response == null || response.code() != 200) {
                Toast.makeText(getContext(), "업데이트 실패 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

    private void proFileDownloadExecute(){
        String strFileNo = User.getIstance().getPetManager().getPet(petIndex).getPhotoFileNo();
        Picasso.with(getContext())
                .load("http://58.226.2.45/Servlet/animalProfileDownload?fileNo="+strFileNo)
                .error(R.drawable.defaultprofileimage)
                .into(profile);


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

    private void getConditionExecute(){
        int serialNo = petManager.getPet(petIndex).getSerialNo();
        RequestBody body= new FormBody.Builder()
                .add("serialNo",serialNo+"")
                .build();
        Request request = new Request.Builder()
                .url("http://58.226.2.45/Servlet/getCondition")
                .post(body)
                .build();
        new ServerTaskManager(request, getConditionTaskListener, getContext().getApplicationContext()).execute();

    }

}
