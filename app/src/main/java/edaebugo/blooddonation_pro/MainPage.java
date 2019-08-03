package edaebugo.blooddonation_pro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainPage  extends AppCompatActivity{
    public String id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        Intent myIntent = getIntent();
        id = myIntent.getStringExtra("id");
        Toast.makeText(getApplicationContext(),"Id from Activity : " + id,Toast.LENGTH_LONG).show();
    }
}


