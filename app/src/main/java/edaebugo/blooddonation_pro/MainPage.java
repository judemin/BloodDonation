package edaebugo.blooddonation_pro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

public class MainPage  extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener  {
    public String userID;
    public String name;
    androidx.fragment.app.Fragment fragment = null;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            //this is what runs when you click the button
        }

    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        Intent myIntent = getIntent();
        userID = myIntent.getStringExtra("id");
        name = myIntent.getStringExtra("name");
        Toast.makeText(getApplicationContext(),"환영합니다 " + name + "님",Toast.LENGTH_LONG).show();

        //Navigation Drawer Activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_mainpage1_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.e("MainPage","" + id);
        Log.e("MainPage","" + R.id.nav_camera);

        if (id == R.id.nav_camera) {
            getSupportActionBar().setTitle("헌혈증 등록");

        }
        else if (id == R.id.nav_gallery) {
            getSupportActionBar().setTitle("나의 헌혈증");
            fragment = new Bloodcardcsf();
            ((Bloodcardcsf) fragment).userID = userID;
            ((Bloodcardcsf) fragment).userName = name;
        }
        else if (id == R.id.nav_slideshow)  {
            getSupportActionBar().setTitle("나의 작성글");

        }
        else if (id == R.id.nav_share) {
            getSupportActionBar().setTitle("나의 정보");

        }
        else if (id == R.id.nav_send) {
            sendEmail();
        }

        if (fragment!=null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout,fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }



    public void uploadPage(View view){
        Intent activityChangeIntent = new Intent(MainPage.this, UploadPage.class);
        activityChangeIntent.putExtra("id", userID);
        activityChangeIntent.putExtra("name", name);
        startActivity(activityChangeIntent);
    }

    @Override
    public void onClick(View v) {

    }

    public void sendEmail(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"judemin2087@naver.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "BloodDonation_project 건의하기");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
    }
}


