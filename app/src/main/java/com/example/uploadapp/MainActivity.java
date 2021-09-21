package com.example.uploadapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.SharedPreferences.*;

public class MainActivity extends AppCompatActivity {

    Button btnadd,scan;
    TextView id,Textdate,tvtemp,tvoxygen,pl;
    EditText place;
    Calendar calendar=Calendar.getInstance();
    ListView listView;
    private List l;
    SimpleDateFormat dateFormat;
    SharedPreferences sp;

    DatabaseReference tempref=FirebaseDatabase.getInstance().getReference("Temp");
    DatabaseReference oxyref=FirebaseDatabase.getInstance().getReference("oxy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = Calendar.getInstance();
        btnadd=findViewById(R.id.btnadd);
        id=findViewById(R.id.tvcovid);
        Textdate=findViewById(R.id.tvdate);
        place=findViewById(R.id.etplace);
        tvtemp=findViewById(R.id.tvtempupload);
        tvoxygen=findViewById(R.id.tvoxyupload);
        pl=findViewById(R.id.tvplaceupload);


        scan=findViewById(R.id.button);
        Intent i=getIntent();
        String covidid=i.getStringExtra("covid id");
        id.setText("Covid Id : "+covidid);
scan.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor e=sp.edit();
        int date=calendar.get(Calendar.DATE);
        int mon=calendar.get(Calendar.MONTH);
        int month=mon+1;
        int year=calendar.get(Calendar.YEAR);

        String day = date+"."+month+"."+year;
        System.out.println(day);
        Textdate.setText("Date :"+day);
        String p=place.getText().toString();
        pl.setText("Place : "+p);
        tempref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String temperature= (String) snapshot.getValue().toString();

                tvtemp.setText("Temp : "+temperature);
                e.putString("t",temperature);
                e.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        oxyref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String oxygen= (String) snapshot.getValue().toString();

                tvoxygen.setText("Oxygen : "+oxygen);
                e.putString("o",oxygen);
                e.commit();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
});





        l= new ArrayList();

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=1;
                int date=calendar.get(Calendar.DATE);
                int mon=calendar.get(Calendar.MONTH);
                int month=mon;
                int year=calendar.get(Calendar.YEAR);
                String day = date+"d"+month+"m"+year;
                System.out.println(day);
                String splace=place.getText().toString();
                upload up =new upload(sp.getString("o","no value"),sp.getString("t","no value"),splace);
                DatabaseReference myRef=FirebaseDatabase.getInstance().getReference(covidid);
                DatabaseReference ref1=myRef.child(day);

                DatabaseReference ref2=ref1.child(splace);

                ref1.push().setValue(up)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"successfully uploaded",Toast.LENGTH_LONG).show();

                            }
                            else {
                                Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });




    }


}