package edaebugo.blooddonation_pro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class UploadPage extends AppCompatActivity {

    public String userID;
    public String userName;
    public Bitmap picBitmap;
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


    static final int REQUEST_IMAGE_CAPTURE = 1;
    public void takePicture(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        iv = (ImageView)this.findViewById(R.id.imageDisplay);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            iv.setImageBitmap(imageBitmap);
            picBitmap = imageBitmap;
        }
    }


    public void upload(View view){
        // Realtime Database는 Bitmap을 지원하지 x -> 업로드 방법 필요
        billData tmpUserdata = new billData(userID, userName, "테스트 데이터 입니다", picBitmap);
        databaseReference.child("billData").push().setValue(tmpUserdata);
        finish();
    }

    public static class billData{
        private boolean isValid = true;
        private String uploaderID;
        private String uploader; // 업로더 이름
        private String message;
        //private Bitmap bloodbill;

        public billData(){}

        public billData(String id,String name,String msg, Bitmap pic){
            uploaderID = id;
            uploader = name;
            message = msg;
           // bloodbill = pic;
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

        /*
        public Bitmap getBill(){
            return bloodbill;
        }
        */

        public boolean isValidBill(){
            return isValid;
        }
    }

}
