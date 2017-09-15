package ourcompany.mylovepet.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.ServerURL;
import ourcompany.mylovepet.main.user.PetManager;
import ourcompany.mylovepet.main.user.User;
import ourcompany.mylovepet.task.ServerTaskManager;
import ourcompany.mylovepet.task.TaskListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by REOS on 2017-05-26.
 */

public class PetInfoFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    private TextView textViewTemperature, textViewWalk, textViewHeartrate;

    private  SwipeRefreshLayout swipeRefreshLayout;

    private int petIndex;

    //포토
    public static final int REQUEST_CODE_IMAGE_CAPTURE = 0;
    public static final int REQUEST_CODE_TAKE_PHOTO = 1;
    public static final int REQUEST_CODE_IMAGE_CROP = 2;

    private Uri originalUri, outPutUri = null;
    private CircularImageView profile;

    private TaskListener getConditionTaskListener, profileUploadTaskListener;

    PetManager petManager;

    public PetInfoFragment(){
        petManager = User.getIstance().getPetManager();
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
        ((TextView)view.findViewById(R.id.textViewPetName)).setText(petManager.getPet(petIndex).getName());

        if(!petManager.getPet(petIndex).getLastMealDate().equals("null")){
            ((TextView)view.findViewById(R.id.textViewMeal)).setText(petManager.getPet(petIndex).getLastMealDate());
        }else {
            ((TextView)view.findViewById(R.id.textViewMeal)).setText("최근 정보 없음");
        }

        int lastDayOfMonth = DateTime.now().dayOfMonth().withMaximumValue().getDayOfMonth();

        ((TextView)view.findViewById(R.id.textViewWalkCount)).setText(petManager.getPet(petIndex).getWalkCount() +"/" + lastDayOfMonth);

        profile = (CircularImageView)view.findViewById(R.id.profile_picture);
        profile.setOnClickListener(this);

        view.findViewById(R.id.viewPetWalk).setOnClickListener(this);
        view.findViewById(R.id.viewMeal).setOnClickListener(this);
        view.findViewById(R.id.viewVaccination).setOnClickListener(this);
        view.findViewById(R.id.viewTemperature).setOnClickListener(this);
        view.findViewById(R.id.viewActiveMass).setOnClickListener(this);
        view.findViewById(R.id.viewHeartrate).setOnClickListener(this);
        view.findViewById(R.id.button_self_diagnosis).setOnClickListener(this);

        listenerInit();

        getConditionExecute();
        proFileDownloadExecute();

        return view;
    }

    private void listenerInit(){
        getConditionTaskListener = new TaskListener() {
            @Override
            public void preTask() {
                textViewTemperature.setText("갱신중...");
                textViewWalk.setText("갱신중...");
                textViewHeartrate.setText("갱신중...");
            }

            @Override
            public void postTask(byte[] bytes) {
                try {
                    String body = new String(bytes, Charset.forName("utf-8"));
                    JSONObject jsonObject = new JSONObject(body);

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

                } catch (JSONException e ) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }


            @Override
            public void fairTask() {

            }
        };

        profileUploadTaskListener = new TaskListener() {
            @Override
            public void preTask() {
            }

            @Override
            public void postTask(byte[] bytes) {
                try {
                    String body = new String(bytes, Charset.forName("utf-8"));
                    JSONObject jsonObject = new JSONObject(body);
                    jsonObject = jsonObject.getJSONObject("report");
                    boolean isSuccess = jsonObject.getBoolean("result");
                    if(isSuccess){
                        Toast.makeText(getContext(), "프로필 업로드 완료",Toast.LENGTH_SHORT).show();
                        ((HomeFragment)getParentFragment()).getPetsExecute();
                    }else {
                        Toast.makeText(getContext(), "다시 시도 해주세요",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void fairTask() {
            }
        };
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.viewPetWalk:
                intent = new Intent(getContext(), PetWalkActivity.class);
                intent.putExtra("petNo",petManager.getPet(petIndex).getPetNo());
                startActivity(intent);
                break;
            case R.id.viewMeal:
                intent = new Intent(getContext(), MealCalendarActivity.class);
                intent.putExtra("petNo",petManager.getPet(petIndex).getPetNo());
                startActivity(intent);
                break;
            case R.id.viewVaccination:
                intent = new Intent(getContext(),VaccineActivity.class);
                intent.putExtra("petNo",petManager.getPet(petIndex).getPetNo());
                startActivity(intent);
                break;
            case R.id.viewTemperature:
                intent = new Intent(getContext(),StatisticsActivity.class);
                intent.putExtra("serialNo",petManager.getPet(petIndex).getSerialNo());
                intent.putExtra("pageType",StatisticsActivity.TEMP_PAGE);
                startActivity(intent);
                break;
            case R.id.viewActiveMass:
                intent = new Intent(getContext(),StatisticsActivity.class);
                intent.putExtra("serialNo",petManager.getPet(petIndex).getSerialNo());
                intent.putExtra("pageType",StatisticsActivity.WALK_PAGE);
                startActivity(intent);
                break;
            case R.id.viewHeartrate:
                intent = new Intent(getContext(),StatisticsActivity.class);
                intent.putExtra("serialNo",petManager.getPet(petIndex).getSerialNo());
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
        switch (requestCode){
            case REQUEST_CODE_TAKE_PHOTO: // 앨범 이미지 가져오기
                if(resultCode == RESULT_OK){
                    originalUri = data.getData();
                    cropImage(originalUri);
                }
                break;
            case REQUEST_CODE_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK){
                    cropImage(originalUri);
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
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            Intent takeImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takeImageIntent.resolveActivity(getActivity().getPackageManager()) != null){
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                }catch (IOException e) {
                    e.printStackTrace();
                }
                if(photoFile != null){
                    originalUri =
                            FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName(), photoFile);
                    takeImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);
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
        }
        File imgFile = new File(dir,imageFileName);

        return imgFile;
    }

    private void cropImage(Uri inputUri){
        try {
            File file = createImageFile();
            outPutUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(outPutUri != null){
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(inputUri,"image/*");
            cropIntent.putExtra("scale",true);
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            cropIntent.putExtra("output", outPutUri);
            startActivityForResult(cropIntent, REQUEST_CODE_IMAGE_CROP);
        }

    }

    //화면을 당겨서 새로고침
    @Override
    public void onRefresh() {
        getConditionExecute();
    }

    private void proFileUploadExecute(){
        File file = new File(outPutUri.getPath());
        if(file.exists()){
            RequestBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("animalNo", petManager.getPet(petIndex).getPetNo()+"")
                    .addFormDataPart("file",file.getName(),RequestBody.create(MediaType.parse("image/jpg"), file))
                    .build();
            Request request = new Request.Builder()
                    .url(ServerURL.PROFILE_UPLOAD_URL)
                    .post(multipartBody)
                    .build();
            new ServerTaskManager(request, profileUploadTaskListener, getContext().getApplicationContext()).execute();
        }
    }

    private void proFileDownloadExecute(){
        String strFileNo = User.getIstance().getPetManager().getPet(petIndex).getPhotoFileNo();
        Picasso.with(getContext())
                .load(ServerURL.PROFILE_DOWNLOAD_URL + strFileNo)
                .error(R.drawable.defaultprofileimage)
                .into(profile);

    }

    private void getConditionExecute(){
        int serialNo = petManager.getPet(petIndex).getSerialNo();
        RequestBody body= new FormBody.Builder()
                .add("serialNo",serialNo+"")
                .build();
        Request request = new Request.Builder()
                .url(ServerURL.GET_CONDITION_URL)
                .post(body)
                .build();
        new ServerTaskManager(request, getConditionTaskListener, getContext().getApplicationContext()).execute();
    }

}
