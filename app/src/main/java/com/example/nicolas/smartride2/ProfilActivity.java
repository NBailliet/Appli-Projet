package com.example.nicolas.smartride2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.nicolas.smartride2.BDD.BDD;
import com.example.nicolas.smartride2.BDD.User;

public class ProfilActivity extends AppCompatActivity {
    private BDD bdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bdd = new BDD(this);
        setContentView(R.layout.activity_profil);
        setTitle("Profile");

        User user = bdd.getUserWithLogin("");

        if(user!=null) {
            TextView loginP = (TextView) findViewById(R.id.textlogin);
            loginP.setText(user.getLogin());
            TextView pswdP = (TextView) findViewById(R.id.textpswd);
            pswdP.setText("**********");
            TextView nameP = (TextView) findViewById(R.id.textnomprofil);
            nameP.setText(user.getName());
            TextView surnameP = (TextView) findViewById(R.id.textprenomprofil);
            surnameP.setText(user.getSurname());
            TextView ageP = (TextView) findViewById(R.id.textageprofil);
            ageP.setText(user.getAge());
        }

    }

}