package ourcompany.mylovepet.petsitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ourcompany.mylovepet.R;

/**
 * Created by KDM on 2017-05-10.
 */

public class PetSitterFindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petsitter_find);
        Spinner spinner1 = (Spinner)findViewById(R.id.petsitter_spinner1);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(
                this,R.array.local,android.R.layout.simple_spinner_dropdown_item
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        Spinner spinner2 = (Spinner)findViewById(R.id.petsitter_spinner2);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(
                this,R.array.borough,android.R.layout.simple_spinner_dropdown_item
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
    }
}
