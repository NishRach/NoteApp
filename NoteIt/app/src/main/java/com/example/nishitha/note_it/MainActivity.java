package com.example.nishitha.note_it;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public EditText name;
    private EditText password;
    private Button login;
    private TextView registerPage;
    DBHandler mydb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUIViews();

        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register_Activity.class);

                startActivity(intent);
            }
        });



        Toast.makeText(this, "Please make the entries", Toast.LENGTH_SHORT).show();

        login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       String cName = name.getText().toString();
                       String cPassword = password.getText().toString();
                       boolean result = mydb.checkLogin(cName,cPassword);

                           if(result){
                               Intent intent = new Intent(MainActivity.this,TitleActivity.class);
                               intent.putExtra("loggedInUser",cName);
                               startActivity(intent);
                           }
                           else {
                               Toast.makeText(MainActivity.this,"Invalid Username or Password",Toast.LENGTH_SHORT).show();
                           }


                    }
                });

                name.setText(null);
                password.setText(null);

        }




    private void setupUIViews(){

        name = findViewById(R.id.etName);
        password = findViewById(R.id.etPassword);
        login = findViewById(R.id.button);
        registerPage = findViewById(R.id.tvSignIn);
        mydb = new DBHandler(this,null,null,1);

    }


}
