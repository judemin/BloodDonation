package edaebugo.blooddonation_pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class UploadPage extends AppCompatActivity {

    private boolean isProcess = false;

    public String userID;
    public String userName;

    private static  String head;
    private String message;
    private String url;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("요청글 작성");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadpage);

        Intent myIntent = getIntent();
        userID = myIntent.getStringExtra("id");
        userName = myIntent.getStringExtra("name");
    }

    public void upload(View view){
        if(isProcess == true)
            return;
        isProcess = true;

        head = ((TextView)findViewById(R.id.in_head)).getText().toString();
        message = ((TextView)findViewById(R.id.in_message)).getText().toString();
        url = ((TextView)findViewById(R.id.in_url)).getText().toString();

        if(isEmpty() == true){
            Toast.makeText(getApplicationContext(), "정보를 모두 입력해주세요", Toast.LENGTH_LONG).show();
            isProcess = false;
            return;
        }

        posting tmpUserdata = new posting(userID, userName, message, head, url);
        databaseReference.child("posting").child(tmpUserdata.getPostID()).setValue(tmpUserdata);
        finish();
    }

    public boolean isEmpty(){
        if(head.isEmpty())
            return true;
        else if(message.isEmpty())
            return true;
        else if(url.isEmpty())
            return true;

        return false;
    }

    public static class posting{
        private boolean isValid = true;
        private String postID;
        private String uploaderID;
        private String uploader; // 업로더 이름
        private String head; // 게시글 제목
        private String message;
        private String url;
        private int lookCnt;

        public posting(){}

        public posting(String id,String name,String msg,String tmHead, String turl){
            Random generator = new Random();
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            postID = "" + alphabet.charAt(generator.nextInt(26))+ alphabet.charAt(generator.nextInt(26))
                    + alphabet.charAt(generator.nextInt(26)) + generator.nextInt() * generator.nextInt() * generator.nextInt();

            uploaderID = id;
            uploader = name;
            head = tmHead;
            message = msg;
            url = turl;
            lookCnt = 0;
        }

        public String getUploaderID(){
            return uploaderID;
        }

        public String getUploader(){
            return uploader;
        }

        public String getHead(){
            return head;
        }

        public String getMessage(){
            return message;
        }

        public String getPostID(){ return postID; }

        public String getUrl(){ return url; }

        public int getLookCnt(){return lookCnt;}

        public boolean isValidBill(){
            return isValid;
        }
    }

}