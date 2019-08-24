package edaebugo.blooddonation_pro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class UploadBillf extends Fragment {

    private boolean isProcess = false;

    private String billID = "";
    public String userID = "";
    public String userName = "";

    private ImageView iv;

    public static Bundle fuploadBillBundle = new Bundle();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    public UploadBillf(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View view = inflater.inflate(R.layout.fragment_uploadbill, container, false);
        buttonSetting(view);
        return view;
    }

    public void buttonSetting(View view){
        Button button = (Button) view.findViewById(R.id.focrStartButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent activityChangeIntent = new Intent(getActivity(), OCRPage.class);
                startActivity(activityChangeIntent);
            }
        });

        button = (Button) view.findViewById(R.id.fuploadButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isProcess)
                    return;
                isProcess = true;

                billID =  ((TextView)Objects.requireNonNull(getView()).findViewById(R.id.in_f_billID)).getText().toString();

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
                                ((TextView)getView().findViewById(R.id.in_f_billID)).setText("이미 존재하는 헌혈증입니다");

                                String strColor = "#FF0000";
                                ((TextView)getView().findViewById(R.id.in_f_billID)).setTextColor(Color.parseColor(strColor));
                                Log.e("UploadBillf","BillCode Exists " + post.getBillID());
                                isProcess = false;
                                return;
                            }
                        }

                        if(isProcess) {
                            // Realtime Database는 Bitmap을 지원하지 x -> 사진으로 OCR 인식, 헌혈증의 데이터만 저장
                            billData tmpUserdata = new billData(userID, userName, billID);
                            databaseReference.child("billData").push().setValue(tmpUserdata);

                            Fragment fragment = new Bloodcardcsf();
                            ((Bloodcardcsf) fragment).userID = userID;
                            ((Bloodcardcsf) fragment).userName = userName;
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.content_fragment_layout, fragment);
                            ft.commit();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("UploadBillf","" + databaseError.toString());
                        Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "네트워크를 확인해주세요", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private boolean isEmpty(){
        if(billID == null)
            return true;
        if(billID.isEmpty())
            return true;
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        iv = ((ImageView)Objects.requireNonNull(getView()).findViewById(R.id.focrImage));
        iv.setImageBitmap(null);

        Log.e("UploadBillf","OnResume");
        billID = fuploadBillBundle.getString("ocrData");
        if(!isEmpty())
            ((TextView)Objects.requireNonNull(getView()).findViewById(R.id.in_f_billID)).setText(billID);
        Bitmap bitmapimage = fuploadBillBundle.getParcelable("BitmapImage");
        if(bitmapimage != null)
            iv.setImageBitmap(bitmapimage);
        fuploadBillBundle.clear();
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
