package ourcompany.mylovepet.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.user.User;
import ourcompany.mylovepet.task.RequestTask;
import ourcompany.mylovepet.task.TaskListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by REOS on 2017-05-26.
 */

public class PetInfoFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    TextView textViewTemperature, textViewWalk, textViewHeartrate;

    SwipeRefreshLayout swipeRefreshLayout;

    int petIndex;

    //포토
    public static final int REQUEST_CODE_IMAGE_CAPTURE = 0;
    public static final int REQUEST_CODE_TAKE_PHOTO = 1;
    public static final int REQUEST_CODE_IMAGE_CROP = 2;

    Uri originalURI, outPutUri = null;
    CircularImageView profile;

    Request request;

    TaskListener getConditionTask, profileUploadTask;


    public PetInfoFragment(){
    }

    public void setPetIndex(int petIndex){
        this.petIndex = petIndex;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_petinfo,container,false);

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
        view.findViewById(R.id.viewActiveMass).setOnClickListener(this);
        view.findViewById(R.id.viewHeartrate).setOnClickListener(this);
        view.findViewById(R.id.button_self_diagnosis).setOnClickListener(this);

        taskInit();

        getConditionExecute();

        return view;
    }


    private void taskInit(){
        getConditionTask = new TaskListener() {
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
        };

        profileUploadTask = new TaskListener() {
            @Override
            public void preTask() {
            }
            @Override
            public void postTask(Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    jsonObject = jsonObject.getJSONObject("report");
                    boolean isSuccess = jsonObject.getBoolean("result");
                    if(isSuccess){
                        Toast.makeText(getContext(), "프로필 업로드 완료",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "다시 시도 해주세요",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | IOException e ) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void cancelTask() {
            }

            @Override
            public void fairTask() {
            }
        };
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
                intent.putExtra("petNo",User.getIstance().getPet(petIndex).getPetNo());
                startActivity(intent);
                break;
            case R.id.viewVaccination:
                intent = new Intent(getContext(),VaccineActivity.class);
                intent.putExtra("petNo",User.getIstance().getPet(petIndex).getPetNo());
                startActivity(intent);
                break;
            case R.id.viewTemperature:
                intent = new Intent(getContext(),StatisticsActivity.class);
                intent.putExtra("serialNo",User.getIstance().getPet(petIndex).getSerialNo());
                intent.putExtra("pageType",StatisticsActivity.TEMP_PAGE);
                startActivity(intent);
                break;
            case R.id.viewActiveMass:
                intent = new Intent(getContext(),StatisticsActivity.class);
                intent.putExtra("serialNo",User.getIstance().getPet(petIndex).getSerialNo());
                intent.putExtra("pageType",StatisticsActivity.WALK_PAGE);
                startActivity(intent);
                break;
            case R.id.viewHeartrate:
                intent = new Intent(getContext(),StatisticsActivity.class);
                intent.putExtra("serialNo",User.getIstance().getPet(petIndex).getSerialNo());
                intent.putExtra("pageType",StatisticsActivity.HEART_PAGE);
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
                            captureImage();
                        }else if(items[id]=="갤러리"){
                            getAlbumImage();
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
            case R.id.button_self_diagnosis:
                intent = new Intent(getContext(),Self_DiagnosisActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap image_bitmap = null;
        switch (requestCode){
            case REQUEST_CODE_TAKE_PHOTO: // 앨범 이미지 가져오기
                if(resultCode == RESULT_OK){
                    File albumFile = null;
                    try {
                        albumFile = createImageFile();
                    }catch (IOException e){}
                    if(albumFile != null){
                        outPutUri = Uri.fromFile(albumFile);
                    }
                    originalURI = data.getData();
                    cropImage(originalURI, outPutUri);
                }
                break;
            case REQUEST_CODE_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK){
                    cropImage(originalURI, outPutUri);
                }
                break;
            case REQUEST_CODE_IMAGE_CROP:
                if(resultCode == RESULT_OK){
                    proFileUploadExecute();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getAlbumImage() { // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    private void captureImage() {
        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null)
        {
            File photoFile = null;
            try {
                photoFile = createImageFile(); // 사진찍은 후 저장할 임시 파일//
            } catch (IOException ex) {
                Toast.makeText(getContext(), "createImageFile Failed", Toast.LENGTH_LONG).show();
            }
            if (photoFile != null) {
                //originalURI = Uri.fromFile(photoFile); // 임시 파일의 위치,경로 가져옴
                originalURI = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, originalURI); // 임시 파일 위치에 저장
                startActivityForResult(takePictureIntent, REQUEST_CODE_IMAGE_CAPTURE);
            }
        }*/
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            Intent takeImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takeImageIntent.resolveActivity(getActivity().getPackageManager()) != null){
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                }catch (IOException e){e.printStackTrace();}

                if(photoFile != null){
                    originalURI =
                            FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName(), photoFile);
                    Log.d("1234",originalURI.getPath());
                    outPutUri = originalURI;
                    takeImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, originalURI);
                    startActivityForResult(takeImageIntent, REQUEST_CODE_IMAGE_CAPTURE);
                }
            }
        }else {
            Toast.makeText(getContext(),"저장소 오류",Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException{
        String imageFileName = "profileTemp.jpg";
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/mylovepet/temp/");

        if(!dir.exists()){
            boolean dd = dir.mkdirs();
            Log.d("qwe22",dd+"");
        }
        File imgFile = new File(dir,imageFileName);

        Log.d("qwerqwer",Environment.getExternalStorageDirectory().getAbsolutePath());
        Log.d("qwerqwer",dir.getAbsolutePath());

        Log.d("d222",imgFile.getAbsolutePath());

        return imgFile;
    }

    private void cropImage(Uri inputUri, Uri outPutUri){
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(inputUri,"image/*");
        cropIntent.putExtra("scale",true);
        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        cropIntent.putExtra("output", outPutUri);
        startActivityForResult(cropIntent, REQUEST_CODE_IMAGE_CROP);
    }

    private void proFileUploadExecute(){
        File file = new File(outPutUri.getPath());
        if(file.exists()){
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file",file.getName(),RequestBody.create(MediaType.parse("image/jpg"), file))
                    .build();

            request = new Request.Builder()
                    .url("http://58.237.8.179/upload")
                    .post(body)
                    .build();
            new RequestTask(request, profileUploadTask, getContext().getApplicationContext()).execute();
        }
    }

    //화면을 당겨서 새로고침
    @Override
    public void onRefresh() {
        getConditionExecute();
    }

    private void getConditionExecute(){
        int serialNo = User.getIstance().getPet(petIndex).getSerialNo();
        RequestBody body= new FormBody.Builder()
                .add("serialNo",serialNo+"")
                .build();
        request = new Request.Builder()
                .url("http://58.237.8.179/Servlet/getCondition")
                .post(body)
                .build();
        new RequestTask(request, getConditionTask, getContext().getApplicationContext()).execute();
    }



}
