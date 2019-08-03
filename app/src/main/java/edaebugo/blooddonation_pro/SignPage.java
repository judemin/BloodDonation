package edaebugo.blooddonation_pro;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class SignPage extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signpage);

        UserData tmpUserdata = new UserData("judemin2087@naver.com", "12345", "민상연", "O", "01032320437");
        databaseReference.child("users").push().setValue(tmpUserdata);
        Toast.makeText(getApplicationContext(),"Child added " + tmpUserdata.getId(),Toast.LENGTH_LONG);
    }

    public static class UserData {
        private boolean isAdmin = false;
        private String id;
        private String email;
        private String password;
        private String name;
        private String bloodType;
        private String phoneNumber;

        public UserData() { }

        public UserData(String temail, String tpassword,String tname,String tbloodType,String tphoneNumber) {
            Random generator = new Random();
            isAdmin = false;
            id = "" + generator.nextInt() + generator.nextFloat(); // id를 만들어 보안적 요소  추가
            email = temail;
            password = tpassword;
            name = tname;
            bloodType = tbloodType;
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

        public String getPhoneNumber() {
            return phoneNumber;
        }
    }
}
