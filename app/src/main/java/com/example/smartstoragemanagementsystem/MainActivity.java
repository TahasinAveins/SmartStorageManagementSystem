package com.example.smartstoragemanagementsystem;

import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String url_Address = "http://iot.uysys.net/app/ws.php";
    String[] product_id;
    String[] product_name;

    ListView listView;

    BufferedInputStream is;
    String line= null;
    String result = null;
    int i;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private TextView userNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_item);

       // StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        collectData();

        CustomListView customListView = new CustomListView(this,product_name,product_id);

        listView.setAdapter(customListView);


        auth = FirebaseAuth.getInstance();
        userNameView = (TextView)findViewById(R.id.userNameView);

        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference(auth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                userNameView.setText("Welcome " + userProfile.getNameUser());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void collectData(){
        try
        {
            URL url = new URL(url_Address);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            is = new BufferedInputStream(connection.getInputStream());

        }catch (Exception ex){
            ex.printStackTrace();
        }


        try{

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            while ((line=br.readLine())!= null){

                sb.append(line+"\n");
            }

            is.close();
            result = sb.toString();


        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


        try{

            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;
            product_name = new String[ja.length()];
            product_id = new String[ja.length()];

            for (i=0;i<=ja.length();i++){
                jo = ja.getJSONObject(i);
                product_name[i] = jo.getString("humidity");
                product_id[i] = jo.getString("humidity");


            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){


            case(R.id.settingID):
            {
                auth.signOut();
                Intent intent = new  Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }

            case (R.id.report):
            {
                Intent intent = new  Intent(MainActivity.this,Report.class);
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
