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

}
