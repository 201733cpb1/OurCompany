package ourcompany.mylovepet.main;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.userinfo.Pet;
import ourcompany.mylovepet.main.userinfo.User;

/**
 * Created by KDM on 2017-05-16.
 */

public class PetUpdateDialog extends Dialog implements View.OnClickListener{


    Pet pet;


    public PetUpdateDialog(@NonNull Context context,Pet pet) {
        super(context);
        this.pet = pet;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pet_update);

        EditText editTextPetName = (EditText)findViewById(R.id.editTextPetName);
        EditText editTextSerialNo = (EditText)findViewById(R.id.editTextSerialNo);

        editTextPetName.setText(pet.getName());
        editTextSerialNo.setText(pet.getSerialNo()+"");

        findViewById(R.id.buttonYes).setOnClickListener(this);
        findViewById(R.id.buttonCancel).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId){
            case R.id.buttonYes:
                break;
            case R.id.buttonCancel:
                dismiss();
                break;
        }
    }


    private class UpdatePet extends AsyncTask<String, Void, Response> {

        private OkHttpClient client = new OkHttpClient();


        @Override
        public Response doInBackground(String... params) {
            RequestBody body = new FormBody.Builder().build();
            Request request = new Request.Builder()
                    .addHeader("Cookie", User.getIstance().getCookie())
                    .url("http://58.237.8.179/Servlet/animalInfo")
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
            if (response == null | response.code() != 200) {
                Toast.makeText(getContext(), "서버 통신 실패", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray jsonArray;
                jsonArray = jsonObject.getJSONArray("AnimalList");

                if (jsonArray != null) {
                    User user = User.getIstance();
                    int length = jsonArray.length();
                    Pet[] pets = new Pet[length];
                    for (int i = 0; i < length; i++) {
                        try {
                            Pet pet = new Pet();
                            JSONObject object = jsonArray.getJSONObject(i);
                            pet.setPetNo(object.getInt("iAnimalNo"));
                            pet.setPetKind(object.getInt("iAnimalIndex"));
                            pet.setSerialNo(object.getInt("iSerialNo"));
                            pet.setName(object.getString("strName"));
                            pet.setGender(object.getString("strGender"));
                            pet.setBirth(object.getString("strBirth"));
                            pet.setPhoto_URL(object.getString("strPhoto"));
                            pets[i] = pet;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    user.setPets(pets);

                }

            } catch (JSONException | IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
