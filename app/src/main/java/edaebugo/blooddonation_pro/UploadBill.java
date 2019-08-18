package edaebugo.blooddonation_pro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

public class UploadBill  extends AppCompatActivity {

    private boolean isProcess = false;

    public String billID = "";
    public String userID;
    public String userName;

    public ImageView iv;

    public static Bundle uploadBillBundle = new Bundle();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadbill);

        Intent myIntent = getIntent();
        userID = myIntent.getStringExtra("id");
        userName = myIntent.getStringExtra("name");

        iv = ((ImageView)findViewById(R.id.ocrImage));
    }

    public void uploadBloodBill(View view){
        if(isProcess == true)
            return;
        isProcess = false;

        billID =  ((TextView)findViewById(R.id.in_billID)).getText().toString();

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
                    UploadBill.billData post = postSnapshot.getValue(UploadBill.billData.class);

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
                    billData tmpUserdata = new billData(userID, userName, billID);
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
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("UploadBill","OnResume");
        billID = uploadBillBundle.getString("ocrData");
        ((TextView)findViewById(R.id.in_billID)).setText(billID);
        Bitmap bitmapimage = uploadBillBundle.getParcelable("BitmapImage");
        iv.setImageBitmap(bitmapimage);
        uploadBillBundle.clear();
    }

    public void startOCR(View view){
        Intent activityChangeIntent = new Intent(UploadBill.this, OCRPage.class);
        startActivity(activityChangeIntent);
    }


    public static class billData{
        private boolean isValid = true;
        private String uploaderID;
        private String uploader; // 업로더 이름
        public String billID;

        public billData(){}

        public billData(String id,String name,String bill){
            uploaderID = id;
            uploader = name;
            billID = bill;
        }

        public String getID(){
            return uploaderID;
        }

        public String getUploader(){
            return uploader;
        }

        public String getBillBarcode(){
            return billID;
        }

        public boolean isValidBill(){
            return isValid;
        }
    }

}
