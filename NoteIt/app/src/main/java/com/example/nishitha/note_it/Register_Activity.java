package com.example.nishitha.note_it;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;


public class Register_Activity extends AppCompatActivity{

    private EditText Rname;
    private EditText RuName;
    private EditText Remail;
    private EditText Rnumber;
    private EditText Rpassword;
    private Button register;
    private TextView loginPage;
    DBHandler mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        setupUIViews();


        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register_Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()){
                    insert();
                }
                else{
                    Toast.makeText(Register_Activity.this,"Registration Failed", Toast.LENGTH_SHORT).show();
                }

                }
        });
    }

    private void setupUIViews(){
        Rname = findViewById(R.id.etRegName);
        RuName = findViewById(R.id.etRegUserName);
        Remail = findViewById(R.id.etRegEmail);
        Rnumber = findViewById(R.id.etPhone);
        Rpassword = findViewById(R.id.etRegPassword);
        register = findViewById(R.id.btnRegister);
        loginPage = findViewById(R.id.tvRegLogin);

        mydb = new DBHandler(this, null, null,1);

    }

    public void insert(){
        String fullName = Rname.getText().toString();
        String userName = RuName.getText().toString();
        String email = Remail.getText().toString();
        String number = Rnumber.getText().toString();
        String password = Rpassword.getText().toString();

        boolean result = mydb.insertUser(fullName,userName,email,number,password);

        if(result){
            Toast.makeText(this,"Registration Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Register_Activity.this,MainActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this,"Registration Failed", Toast.LENGTH_SHORT).show();
        }

        clear();
    }

    public void clear(){
        Rname.setText(null);
        RuName.setText(null);
        Remail.setText(null);
        Rnumber.setText(null);
        Rpassword.setText(null);
    }

    public  boolean validate(){
        String phone= "^[0-9]{10}$";
        Pattern patternPhone = Pattern.compile(phone);
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@"+
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";


        Pattern pat = Pattern.compile(emailRegex);
        boolean emailRepeat = mydb.isRegistered(Remail.getText().toString());
        boolean usernameRepeat = mydb.CheckUserName(RuName.getText().toString());

        if(Rpassword.getText().length()<8){
            Rpassword.setError("Minimum 8 characters");
        }

        if(patternPhone.matcher(Rnumber.getText().toString()).matches());
        else
            Rnumber.setError("Invalid");

        if(pat.matcher(Remail.getText().toString()).matches());
        else
            Remail.setError("Invalid Email");


        if(emailRepeat)
            Remail.setError("Email registered");

        if(RuName.getText().length()==0){
            RuName.setError("invalid");
        }

        if(usernameRepeat)
            RuName.setError("Name Already Registered");

        if(Rname.getText().length()==0){
            Rname.setError("invalid");
        }

        boolean Error = (Rname.getError()==null && RuName.getError()==null
                && Remail.getError()==null && Rnumber.getError()==null && Rpassword.getError()==null);
        if(Error)
            return true;
        else
            return false;

    }

}
