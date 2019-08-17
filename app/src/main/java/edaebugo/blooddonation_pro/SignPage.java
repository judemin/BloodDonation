package edaebugo.blooddonation_pro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

public class SignPage extends AppCompatActivity {
    boolean isProcess = false;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private String newUserID = "";

    private String name = "";
    private String email = "";
    private String password = "";
    private String phoneNum = "";
    private String bloodType = "";
    private String rhType = "";
    Spinner spinner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signpage);

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter Adapter = ArrayAdapter.createFromResource(this,
                R.array.blood, android.R.layout.simple_spinner_item);
        spinner.setAdapter(Adapter);

        ChipGroup choiceChipGroup = findViewById(R.id.choice_chip_group);
        choiceChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, @IdRes int i) {
                if(i == 2131296259)
                    rhType = "RH+";
                else if(i == 2131296260)
                    rhType = "RH-";

                Log.i("ChipGroup","" + i);
            }
        });
    }

    public void signInpa(View view){
        if(isProcess == true)
            return;
        isProcess = true;

        name = ((TextView)findViewById(R.id.in_name)).getText().toString();
        email = ((TextView)findViewById(R.id.in_email)).getText().toString();
        password = ((TextView)findViewById(R.id.in_password)).getText().toString();
        phoneNum = ((TextView)findViewById(R.id.in_phone)).getText().toString();
        bloodType = spinner.getSelectedItem().toString();
        Log.e("SignPage","" + email  + " " + bloodType + " " + rhType);

        if(isEmpty() == true) {
            Toast.makeText(getApplicationContext(), "정보를 모두 입력해주세요", Toast.LENGTH_LONG).show();
            isProcess = false;
            return;
        }

        final String tmpEmail = email;
        final String tmpName = name;
        final String tmpPhone = phoneNum;
        Query myTopPostsQuery = databaseReference.child("users");

        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    SignPage.UserData post = postSnapshot.getValue(SignPage.UserData.class);

                    if (tmpEmail.equals(post.getEmail()) || tmpName.equals(post.getName()) || tmpPhone.equals(post.getPhoneNumber())) {
                        if(newUserID.equals(post.getId()))
                            return;
                        Toast.makeText(getApplicationContext(), "이미 존재하는 사용자 정보입니다", Toast.LENGTH_LONG).show();
                        Log.e("SignPage","User Exists " + post.getId());
                        isProcess = false;
                        return;
                    }
                }
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("SignPage","" + databaseError.toString());
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요", Toast.LENGTH_LONG).show();
            }
        });

        UserData tmpUserdata = new UserData(email, password, name, bloodType, rhType, phoneNum);
        Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다", Toast.LENGTH_LONG).show();
        newUserID = tmpUserdata.getId();
        databaseReference.child("users").push().setValue(tmpUserdata);
        finish();
    }

    public boolean isEmpty(){
        if(name.isEmpty())
            return true;
        else if(email.isEmpty())
            return true;
        else if(password.isEmpty())
            return true;
        else if(phoneNum.isEmpty())
            return true;
        else if(bloodType.isEmpty())
            return true;
        else if(rhType.isEmpty())
            return true;

        return false;
    }

    public static class UserData {
        private boolean isAdmin = false;
        private String id;
        private String email;
        private String password;
        private String name;
        private String bloodType;
        private String rhType;
        private String phoneNumber;

        public UserData() { }

        public UserData(String temail, String tpassword,String tname,String tbloodType, String trhType,String tphoneNumber) {
            Random generator = new Random();
            isAdmin = false;
            id = "" + generator.nextInt() * generator.nextFloat(); // id를 만들어 보안적 요소  추가
            email = temail;
            password = tpassword;
            name = tname;
            bloodType = tbloodType;
            rhType = trhType;
            phoneNumber = tphoneNumber;
        }

        public boolean getAdmin() {
            return isAdmin;
        }

        public String getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getName() {
            return name;
        }

        public String getBloodType() {
            return bloodType;
        }

        public String getRhType() {
            return rhType;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }
    }
}
