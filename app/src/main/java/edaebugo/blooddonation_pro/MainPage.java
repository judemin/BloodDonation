package edaebugo.blooddonation_pro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainPage  extends AppCompatActivity{
    public String id;
    public String name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        Intent myIntent = getIntent();
        id = myIntent.getStringExtra("id");
        name = myIntent.getStringExtra("name");
        Toast.makeText(getApplicationContext(),"로그인 성공\n환영합니다 " + name + "님",Toast.LENGTH_LONG).show();
    }
}


