package edaebugo.blooddonation_pro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    boolean isLogin = false;
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
    private void loginUser(String email, String password) {
        if(isLogin == true)
            return;

        isLogin = true;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        final String tmpEmail = email;
        final String tmpPW = password;
        Query myTopPostsQuery = databaseReference.child("users");
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    SignPage.UserData post = postSnapshot.getValue(SignPage.UserData.class);
                    if (tmpEmail.equals(post.getEmail()) && tmpPW.equals(post.getPassword())) {
                        Intent activityChangeIntent = new Intent(SignupActivity.this, MainPage.class);
                        activityChangeIntent.putExtra("id", post.getId());
                        activityChangeIntent.putExtra("name", post.getName());
                        startActivity(activityChangeIntent);
                        finish();
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(), "이메일과 비밀번호를 확인하세요", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        isLogin = false;
    }
}