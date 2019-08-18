package edaebugo.blooddonation_pro;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class devInfo extends AppCompatActivity {
    public int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devinfo);

        String string = "";
        TextView tv = findViewById(R.id.devin);
        string = "BloodDonation_project\n" +
                "by E-PPT (EWUHS - Project & Programming Team)\n\n" +
                "Devs : \n" +
                "이대부속고등학교 31108 민상연 \n" +
                "이대부속고등학교 30905 김가현 \n" +
                "이대부속고등학교 20820 유소진 \n" +
                "이대부속고등학교 20620 이유진 \n" +
                "이대부속고등학교 21134 홍윤걸 \n" +
                "이대부속고등학교 10622 이혜정 \n" +
                "Github : https://github.com/judemin/BloodDonation";
        tv.setText(string);
    }

    public void easterEgg(View view){
        ImageView iv = findViewById(R.id.pineImage);
        if(cnt == 0)
            iv.setImageDrawable(getDrawable(R.drawable.pineapple1));
        if(cnt == 1)
            iv.setImageDrawable(getDrawable(R.drawable.pineapple2));
        if(cnt == 2)
            iv.setImageDrawable(getDrawable(R.drawable.pineapple3));
        if(cnt == 3)
            iv.setImageDrawable(getDrawable(R.drawable.pineapple4));
        if(cnt == 4)
            iv.setImageDrawable(getDrawable(R.drawable.pineapple5));
        cnt++;
        cnt %= 5;
    }
}
