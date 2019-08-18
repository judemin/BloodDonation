package edaebugo.blooddonation_pro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

public class Bloodcardcs extends AppCompatActivity {
    ArrayList<Bloodcard> mData = null;
    GridView mGridView = null;
    BaseAdapterEx mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloodcardcs);

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
}

