package edaebugo.blooddonation_pro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

public class MainPage  extends AppCompatActivity{
    int cnt = 0;
    public String param;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        Intent myIntent = getIntent();
        param = myIntent.getStringExtra("tmp");
        //Toast.makeText(getApplicationContext(),param,Toast.LENGTH_LONG).show();
    }

    public void cntPlus(View view){
        cnt++;
        FirebaseDatabase.getInstance().getReference().push().setValue("" + param + cnt);
        firebaseDB();
    }

    public void firebaseDB(){
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Toast.makeText(getApplicationContext(),snapshot.getValue().toString(),Toast.LENGTH_LONG).show();
                    //Log.d("MainActivity", "ValueEventListener : " + snapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"onCancelled",Toast.LENGTH_LONG).show();
            }
        });

        //Single Value event listner
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("MainActivity", "Single ValueEventListener : " + snapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


