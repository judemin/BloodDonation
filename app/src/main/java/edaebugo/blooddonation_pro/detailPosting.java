package edaebugo.blooddonation_pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    public int lookCnt = 0;

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

        TextView tmpTV = (TextView) findViewById(R.id.head) ;
        tmpTV.setText("" + postHead) ;
        tmpTV = (TextView) findViewById(R.id.postingCont) ;
        tmpTV.setText("" + postCont) ;
        tmpTV = (TextView) findViewById(R.id.url) ;
        tmpTV.setText("" + postUrl) ;
        tmpTV = (TextView) findViewById(R.id.uploader) ;
        tmpTV.setText("작성자 : " + uploader) ;
        tmpTV = (TextView) findViewById(R.id.lookCnt) ;
        tmpTV.setText("조회수 : " + lookCnt) ;

        Button delBtn = (Button) findViewById(R.id.deleteBtn) ;
        if(!userID.equals(uploaderID)) {
            delBtn.setEnabled(false);
            delBtn.setVisibility(View.GONE);
            addViews();
        }
    }

    void addViews(){

    }
}
