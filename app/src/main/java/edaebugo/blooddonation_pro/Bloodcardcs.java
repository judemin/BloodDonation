package edaebugo.blooddonation_pro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class Bloodcardcs extends AppCompatActivity {
    public String userID;
    public String userName;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    ArrayList<Bloodcard> mData = new ArrayList<Bloodcard>();
    GridView mGridView = null;
    BaseAdapterEx mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("마이페이지");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloodcardcs);

        Intent myIntent = getIntent();
        userID = myIntent.getStringExtra("id");
        userName = myIntent.getStringExtra("name");
    }

    public void uploadBill(View view){
        Intent activityChangeIntent = new Intent(Bloodcardcs.this, UploadBill.class);
        activityChangeIntent.putExtra("id", userID);
        activityChangeIntent.putExtra("name", userName);
        startActivity(activityChangeIntent);
    }

    @Override
    protected void onResume(){
        super.onResume();

        final Context context = this;
        Query myTopPostsQuery = databaseReference.child("billData");
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int cnt = 0;
                ArrayList<Bloodcard> tmpData = new ArrayList<Bloodcard>();
                Log.e("Bloodcardcs", "Count " + snapshot.getChildrenCount());

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    UploadBill.billData post = postSnapshot.getValue(UploadBill.billData.class);
                    Log.e("Bloodcardcs","" + post.getUploaderID());
                    if (userID.equals(post.getUploaderID())) {
                        Bloodcard bloodcard = new Bloodcard();
                        bloodcard.mNumber = "BCID : " + post.getBillID();
                        bloodcard.mOrder = "" + (cnt + 1);
                        tmpData.add(bloodcard);
                        cnt++;
                    }
                }
                if(cnt == 0){
                    Bloodcard bloodcard = new Bloodcard();
                    bloodcard.mNumber = "헌혈증 정보가 존재하지 않습니다!";
                    bloodcard.mOrder = "0";
                    //Log.e("Bloodcardcs","" + bloodcard.mNumber);
                    //Log.e("Bloodcardcs","" + bloodcard.mOrder);
                    tmpData.add(bloodcard);
                }
                Log.e("Bloodcardcs","tmpData " + tmpData.size());
                Log.e("Bloodcardcs","" + userID + " " + cnt);

                mAdapter = new BaseAdapterEx(context, tmpData);
                mGridView = (GridView) findViewById(R.id.gridView);
                mGridView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Bloodcardcs","" + databaseError.toString());
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요", Toast.LENGTH_LONG).show();
            }
        });
    }
}

