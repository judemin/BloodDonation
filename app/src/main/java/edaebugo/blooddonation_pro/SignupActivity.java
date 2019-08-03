package edaebugo.blooddonation_pro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 이메일과 비밀번호
    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        //Toast.makeText(getApplicationContext(),firebaseAuth.getInstance().toString(),Toast.LENGTH_LONG);

        editTextEmail = findViewById(R.id.et_eamil);
        editTextPassword = findViewById(R.id.et_password);
    }

    public void singUp(View view) {
        Intent activityChangeIntent = new Intent(SignupActivity.this, SignPage.class);
        startActivity(activityChangeIntent);
    }

    public void signIn(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            loginUser(email, password);
        }
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            Toast.makeText(getApplicationContext(),"이메일을 입력하세요",Toast.LENGTH_LONG).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            Toast.makeText(getApplicationContext(),"올바르지 않은 이메일입니다",Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            Toast.makeText(getApplicationContext(),"비밀번호를 입력하세요",Toast.LENGTH_LONG).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            Toast.makeText(getApplicationContext(),"올바르지 않은 비밀번호입니다",Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    // 로그인
    private void loginUser(String email, String password)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        //특정 이메일 이용 -> 데이터 가져오기
        Query myTopPostsQuery = databaseReference.child("users").child(email);
        myTopPostsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SignPage.UserData userData = dataSnapshot.getValue(SignPage.UserData.class);  // chatData를 가져오고
                Toast.makeText(getApplicationContext(),userData.getId(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });


        //가장 최근의 데이터만 가져옴
        databaseReference.child("users").addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SignPage.UserData userData = dataSnapshot.getValue(SignPage.UserData.class);  // chatData를 가져오고
                Toast.makeText(getApplicationContext(),userData.getId(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        if(email.equals(password)){ //로그인 성공
            Intent activityChangeIntent = new Intent(SignupActivity.this, MainPage.class);
            activityChangeIntent.putExtra("id",email);
            startActivity(activityChangeIntent);
            finish();
        }
        else //로그인 실패
            Toast.makeText(getApplicationContext(),"이메일과 비밀번호를 확인하세요",Toast.LENGTH_LONG).show();
    }
}