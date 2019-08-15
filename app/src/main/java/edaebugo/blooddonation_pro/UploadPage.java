package edaebugo.blooddonation_pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class UploadPage extends AppCompatActivity {

    public String userID;
    public String userName;
    public ImageView iv;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadpage);

        Intent myIntent = getIntent();
        userID = myIntent.getStringExtra("id");
        userName = myIntent.getStringExtra("name");

        TextView tv = (TextView)findViewById(R.id.testText);
        tv.setText("" + userID + "\n" + userName);
    }

    public void startOCR(View view){
        Intent activityChangeIntent = new Intent(UploadPage.this, OCRPage.class);
        startActivity(activityChangeIntent);
    }

    public void startCam2(View view){
        Intent activityChangeIntent = new Intent(UploadPage.this, Camera2Test.class);
        startActivity(activityChangeIntent);
    }

    public void upload(View view){
        // Realtime Database는 Bitmap을 지원하지 x -> 사진으로 OCR 인식, 헌혈증의 데이터만 저장
        billData tmpUserdata = new billData(userID, userName, "테스트 데이터 입니다");
        databaseReference.child("billData").push().setValue(tmpUserdata);
        finish();
    }

    public static class billData{
        private boolean isValid = true;
        private String uploaderID;
        private String uploader; // 업로더 이름
        private String message;

        public billData(){}

        public billData(String id,String name,String msg){
            uploaderID = id;
            uploader = name;
            message = msg;
        }

        public String getID(){
            return uploaderID;
        }

        public String getNmae(){
            return uploader;
        }

        public String getMessage(){
            return message;
        }

        public boolean isValidBill(){
            return isValid;
        }
    }

}