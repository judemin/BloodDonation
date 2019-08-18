package edaebugo.blooddonation_pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class Bloodcardcs extends AppCompatActivity {
    public String userID;
    public String userName;

    ArrayList<Bloodcard> mData = null;
    GridView mGridView = null;
    BaseAdapterEx mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloodcardcs);

        Intent myIntent = getIntent();
        userID = myIntent.getStringExtra("id");
        userName = myIntent.getStringExtra("name");

        mData = new ArrayList<Bloodcard>();
        for(int i = 0; i < 6 ; i++){
            Bloodcard bloodcard = new Bloodcard();
            bloodcard.mNumber = "9500001"+i;
            bloodcard.mOrder = "1" + i;
            mData.add(bloodcard);
        }

        mAdapter = new BaseAdapterEx(this, mData);
        mGridView = (GridView) findViewById(R.id.gridView);
        mGridView.setAdapter(mAdapter);

    }

    public void uploadBill(View view){
        Intent activityChangeIntent = new Intent(Bloodcardcs.this, UploadBill.class);
        activityChangeIntent.putExtra("id", userID);
        activityChangeIntent.putExtra("name", userName);
        startActivity(activityChangeIntent);
    }
}

