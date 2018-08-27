package com.example.nishitha.note_it;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DescriptionActivity extends AppCompatActivity {
    private Button add,done,addDescription;
    private ListView noteList;
    private TextView noteName;
    private EditText Dtext;
    private Cursor c;
    private  int id;
    private String description;
    boolean res = false;
    DBHandler mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        final View bottomSheet = findViewById(R.id.description_bottom);
        final BottomSheetBehavior behavior= BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(0);
        behavior.setHideable(false);

        setUIViews();

        //set Title of note on top
        String nameTitle = getIntent().getStringExtra("note");
        noteName.setText(nameTitle);



        //get id of the note selected
        id = mydb.getId(nameTitle);

        c =mydb.getDescriptions(id);



        if(c != null)
               display();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(DescriptionActivity.this,TitleActivity.class);
                startActivity(intent);
            }
        });


        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {

                switch(newState){
                    case BottomSheetBehavior.STATE_EXPANDED:
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float slideOffset) {

                if(behavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if(behavior.getState()==BottomSheetBehavior.STATE_COLLAPSED)
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                add.animate().scaleX(1-slideOffset).scaleY(1-slideOffset).setDuration(0).start();
                done.animate().scaleX(1-slideOffset).scaleY(1-slideOffset).setDuration(0).start();

            }
        });

               addDescription.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       String memo = Dtext.getText().toString();
                       if(!Dtext.getText().toString().isEmpty())
                       {Description description = new Description(memo);
                        res=mydb.insertDescription(description,id);}

                       c=mydb.getDescriptions(id);
                       if(!Dtext.getText().toString().isEmpty())
                           display();
                       Dtext.setText(null);
                       behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);



                   }
               });


    }

    private void display(){

        ArrayList<String> listDescrip = new ArrayList<>();
        ArrayList<String> listDate = new ArrayList<>();


        while (!c.isAfterLast()) {
                listDescrip.add(c.getString(c.getColumnIndex("DESCRIPTION")));
                listDate.add(c.getString(c.getColumnIndex("DATE")));
                        c.moveToNext();
            }

        ListAdapter myAdaptor = new CustomAdaptorDescription(DescriptionActivity.this,listDescrip.toArray(new String[0]),listDate.toArray(new String[0]));

        noteList.setAdapter(myAdaptor);
        registerForContextMenu(noteList);

        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                description = noteList.getItemAtPosition(i).toString();

            }
        });

    }


    public void setUIViews(){
        add = findViewById(R.id.btnNew);
        done = findViewById(R.id.btnDone);
        noteList = findViewById(R.id.list_description);
        noteName = findViewById(R.id.textView);
        addDescription = findViewById(R.id.btnDescription);
        Dtext = findViewById(R.id.etEnterDescription);
        mydb= new DBHandler(this,null,null,1);
    }

    private void ShowDialog(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this?");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mydb.deleteDescription(description);
                c=mydb.getDescriptions(id);
                display();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ShowDialog(noteList);
    }
}
