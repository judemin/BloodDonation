package edaebugo.blooddonation_pro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

public class UploadPage extends AppCompatActivity {

    public static Bundle uploadPageBundle = new Bundle();

    private boolean isProcess = false;

    public String userID;
    public String userName;

    private static String billID;
    private static  String head;
    private String message;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadpage);

        Intent myIntent = getIntent();
        userID = myIntent.getStringExtra("id");
        userName = myIntent.getStringExtra("name");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("UploadPage","OnResume");
        billID = uploadPageBundle.getString("ocrData");
        ((TextView)findViewById(R.id.in_billID)).setText(billID);
    }

    public void startOCR(View view){
        Intent activityChangeIntent = new Intent(UploadPage.this, OCRPage.class);
        startActivity(activityChangeIntent);
    }

    public void upload(View view){
        if(isProcess == true)
            return;
        isProcess = true;

        billID = ((TextView)findViewById(R.id.in_billID)).getText().toString();
        head = ((TextView)findViewById(R.id.in_head)).getText().toString();
        message = ((TextView)findViewById(R.id.in_message)).getText().toString();

        if(isEmpty() == true){
            Toast.makeText(getApplicationContext(), "정보를 모두 입력해주세요", Toast.LENGTH_LONG).show();
            isProcess = false;
            return;
        }

        Query myTopPostsQuery = databaseReference.child("billData");
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("billData Count", "" + snapshot.getChildrenCount());

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    UploadPage.billData post = postSnapshot.getValue(UploadPage.billData.class);

                    if (billID.equals(post.getBillBarcode())) {
                        ((TextView)findViewById(R.id.in_billID)).setText("이미 존재하는 헌혈증입니다");

                        String strColor = "#FF0000";
                        ((TextView)findViewById(R.id.in_billID)).setTextColor(Color.parseColor(strColor));
                        Log.e("UploadPage","BillCode Exists " + post.getBillBarcode());
                        isProcess = false;
                        return;
                    }
                }

                if(isProcess == true){
                    // Realtime Database는 Bitmap을 지원하지 x -> 사진으로 OCR 인식, 헌혈증의 데이터만 저장
                    billData tmpUserdata = new billData(userID, userName, message, billID, head);
                    databaseReference.child("billData").push().setValue(tmpUserdata);
                    finish();
                }
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("UploadPage","" + databaseError.toString());
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요", Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean isEmpty(){
        if(billID.isEmpty())
            return true;
        else if(head.isEmpty())
            return true;
        else if(message.isEmpty())
            return true;

        return false;
    }

    public static class billData{
        private boolean isValid = true;
        private String uploaderID;
        private String uploader; // 업로더 이름
        private String head; // 게시글 제목
        private String message;
        private String billBarcode;

        public billData(){}

        public billData(String id,String name,String msg,String barCode,String tmHead){
            uploaderID = id;
            uploader = name;
            head = tmHead;
            message = msg;
            billBarcode = barCode;
        }

        public String getID(){
            return uploaderID;
        }

        public String getNmae(){
            return uploader;
        }

        public String getHead(){
            return head;
        }

        public String getMessage(){
            return message;
        }

        public String getBillBarcode(){ return billBarcode; }

        public boolean isValidBill(){
            return isValid;
        }
    }

}