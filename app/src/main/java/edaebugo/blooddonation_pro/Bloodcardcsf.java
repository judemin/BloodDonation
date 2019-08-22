package edaebugo.blooddonation_pro;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class Bloodcardcsf extends Fragment {
    public String userID;
    public String userName;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    ArrayList<Bloodcard> mData = new ArrayList<Bloodcard>();
    GridView mGridView = null;
    BaseAdapterEx mAdapter = null;

    public Bloodcardcsf(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        process();
        return inflater.inflate(R.layout.fragment_bloodcardcs, container, false);
    }

    public void process(){
        final Context context = getActivity();
        Query myTopPostsQuery = databaseReference.child("billData");
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int cnt = 0;
                ArrayList<Bloodcard> tmpData = new ArrayList<Bloodcard>();
                Log.e("Bloodcardcsf", "Count " + snapshot.getChildrenCount());

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    UploadBill.billData post = postSnapshot.getValue(UploadBill.billData.class);
                    Log.e("Bloodcardcsf","" + post.getUploaderID());
                    if (userID.equals(post.getUploaderID())) {
                        Bloodcard bloodcard = new Bloodcard();
                        bloodcard.mNumber = "BCID_" + post.getBillID();
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
                Log.e("Bloodcardcsf","tmpData " + tmpData.size());
                Log.e("Bloodcardcsf","" + userID + " " + cnt);

                mAdapter = new BaseAdapterEx(context, tmpData);
                mGridView = (GridView) getView().findViewById(R.id.gridView);
                mGridView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Bloodcardcsf","" + databaseError.toString());
                Toast.makeText(getContext().getApplicationContext(), "네트워크를 확인해주세요", Toast.LENGTH_LONG).show();
            }
        });
    }
}

