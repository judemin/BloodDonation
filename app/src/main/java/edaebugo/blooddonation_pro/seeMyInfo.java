package edaebugo.blooddonation_pro;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;


public class seeMyInfo extends Fragment {

    private TextView Uname;
    private TextView Uid;
    private TextView UphoneNumber;
    private TextView UbloodType;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    public String id;
    public String name;
    public String phoneNumber;
    public String bloodType;

    public seeMyInfo() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_seemyinfo, container, false);

            Uname = (TextView) view.findViewById(R.id.username);
            Uname.setText(name);
            Uid = (TextView) view.findViewById(R.id.userid);
            Uid.setText(id);
            UphoneNumber = (TextView) view.findViewById(R.id.userphonenumber);
            UphoneNumber.setText(phoneNumber);
            UbloodType = (TextView) view.findViewById(R.id.userbloodtype);
            UbloodType.setText(bloodType);

        return inflater.inflate(R.layout.fragment_seemyinfo, container, false);
    }




}
