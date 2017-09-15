package ourcompany.mylovepet.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import ourcompany.mylovepet.R;
import ourcompany.mylovepet.main.user.Pet;
import ourcompany.mylovepet.main.user.User;
import ourcompany.mylovepet.task.ServerTaskManager;
import ourcompany.mylovepet.task.TaskListener;

/**
 * Created by KDM on 2017-05-16.
 */

public class PetDeleteDialog extends Dialog implements View.OnClickListener, TaskListener{

    Pet pet;

    ServerTaskManager deleteTask;

    public PetDeleteDialog(@NonNull Context context, Pet pet) {
        super(context);
        this.pet = pet;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pet_delete);

        TextView editTextPetName = (TextView)findViewById(R.id.editTextPetName);

        editTextPetName.setText("이름:"+pet.getName());

        findViewById(R.id.buttonYes).setOnClickListener(this);
        findViewById(R.id.buttonCancel).setOnClickListener(this);
    }

    @Override
    public void hide() {
        if(deleteTask != null){
            deleteTask.cancel(true);
        }
        super.hide();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId){
            case R.id.buttonYes:
                RequestBody body= new FormBody.Builder()
                        .add("animalNo",pet.getPetNo()+"")
                        .build();
                Request request = new Request.Builder()
                        .url("http://58.226.2.45/Servlet/deleteAnimal")
                        .post(body)
                        .build();
                deleteTask = new ServerTaskManager(request,this,getContext().getApplicationContext());
                deleteTask.execute();
                break;
            case R.id.buttonCancel:
                dismiss();
                break;
        }
    }


    @Override
    public void preTask() {

    }

    @Override
    public void postTask(byte[] bytes) {
        try {
            String body = new String(bytes, Charset.forName("utf-8"));
            JSONObject jsonObject = new JSONObject(body);
            boolean pushState = jsonObject.getJSONObject("report").getBoolean("result");
            if(pushState) {
                Toast.makeText(getContext(), "삭제 성공", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(), "삭제 실패", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            dismiss();
        }
    }

    @Override
    public void fairTask() {

    }
}
