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

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class UploadBillf extends Fragment {

    private boolean isProcess = false;

    private String billID = "";
    private String userID;
    private String userName;

    private ImageView iv;

    private static Bundle uploadBillBundle = new Bundle();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("헌혈증 업로드");
        super.onCreate(savedInstanceState);
        getActivity().setContentView(R.layout.fragment_uploadbill);

        Intent myIntent = getActivity().getIntent();
        userID = myIntent.getStringExtra("id");
        userName = myIntent.getStringExtra("name");

    }

    public void uploadBloodBill(View view){
        if(isProcess)
            return;
        isProcess = true;

        billID =  ((TextView)Objects.requireNonNull(getView()).findViewById(R.id.in_billID)).getText().toString();

        if(isEmpty()){
            Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "정보를 모두 입력해주세요", Toast.LENGTH_LONG).show();
            isProcess = false;
            return;
        }

        Query myTopPostsQuery = databaseReference.child("billData");
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("billData Count", "" + snapshot.getChildrenCount());

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    UploadBillf.billData post = postSnapshot.getValue(UploadBillf.billData.class);

                    assert post != null;
                    if (billID.equals(post.getBillID())) {
                        ((TextView)getView().findViewById(R.id.in_billID)).setText("이미 존재하는 헌혈증입니다");

                        String strColor = "#FF0000";
                        ((TextView)getView().findViewById(R.id.in_billID)).setTextColor(Color.parseColor(strColor));
                        Log.e("UploadBillPage","BillCode Exists " + post.getBillID());
                        isProcess = false;
                        return;
                    }
                }

                if(isProcess){
                    // Realtime Database는 Bitmap을 지원하지 x -> 사진으로 OCR 인식, 헌혈증의 데이터만 저장
                    billData tmpUserdata = new billData(userID, userName, billID);
                    databaseReference.child("billData").push().setValue(tmpUserdata);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UploadBillPage","" + databaseError.toString());
                Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "네트워크를 확인해주세요", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isEmpty(){
        return billID.isEmpty();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("UploadBillPage","OnResume");
        billID = uploadBillBundle.getString("ocrData");
        ((TextView)Objects.requireNonNull(getView()).findViewById(R.id.in_billID)).setText(billID);
        Bitmap bitmapimage = uploadBillBundle.getParcelable("BitmapImage");
        iv.setImageBitmap(bitmapimage);
        uploadBillBundle.clear();
    }

    public void startOCR(View view){
        Intent activityChangeIntent = new Intent(getActivity(), OCRPage.class);
        startActivity(activityChangeIntent);
    }


    public static class billData{
        private boolean isValid = true;
        private String uploaderID;
        private String uploader; // 업로더 이름
        String billID;

        public billData(){}

        billData(String id, String name, String bill){
            uploaderID = id;
            uploader = name;
            billID = bill;
        }

        public String getUploaderID(){
            return uploaderID;
        }

        public String getUploader(){
            return uploader;
        }

        String getBillID(){
            return billID;
        }

        public boolean isValidBill(){
            return isValid;
        }
    }

}
