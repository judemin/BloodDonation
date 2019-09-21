package edaebugo.blooddonation_pro;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class detailPosting extends AppCompatActivity {


    public String userID;
    public String uploaderID;
    public String uploader;
    public String postHead;
    public String postCont;
    public String postUrl;
    public String postingID;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailposting);

        Intent myIntent = getIntent();
        uploaderID = myIntent.getStringExtra("upid");
        userID = myIntent.getStringExtra("id");
        uploader = myIntent.getStringExtra("uploader");
        postHead = myIntent.getStringExtra("head");
        postCont = myIntent.getStringExtra("content");
        postUrl = myIntent.getStringExtra("url");
        postingID = myIntent.getStringExtra("postid");
    }
}
