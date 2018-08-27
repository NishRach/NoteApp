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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;


import java.util.ArrayList;

public class TitleActivity extends AppCompatActivity {

    private Button newNote, addNewNote;
    private Button exit;
    private ListView listView;
    private String note;
    private String UId;
    private int index;
    private EditText enterText;
    DBHandler mydb;
    private Cursor c;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);


        final String name = getIntent().getStringExtra("loggedInUser");


        View bottomSheet = findViewById(R.id.design_bottom_sheet);
        final BottomSheetBehavior behavior= BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(0);
        behavior.setHideable(false);

        setUIViews();

        UId=mydb.getUserIds(name);

        c = mydb.getNotes(Integer.parseInt(UId));
        if(c != null)
           display();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                note = listView.getItemAtPosition(position).toString();
                Intent intent = new Intent(TitleActivity.this,DescriptionActivity.class);
                intent.putExtra("note",note);
                startActivity(intent);

            }
        });



        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TitleActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

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

                newNote.animate().scaleX(1-slideOffset).scaleY(1-slideOffset).setDuration(0).start();
                exit.animate().scaleX(1-slideOffset).scaleY(1-slideOffset).setDuration(0).start();

            }
        });


        addNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Notes note = new Notes(enterText.getText().toString());
                if(!enterText.getText().toString().isEmpty())
                    mydb.insertNote(note,Integer.parseInt(UId));
                 c =mydb.getNotes(Integer.parseInt(UId));
                  display();
                enterText.setText(null);

            }
        });

    }
    private void display(){

        ArrayList<String> list = new ArrayList<String>();

        while (!c.isAfterLast()) {
            list.add(c.getString(c.getColumnIndex("NOTE_TITLE")));
            c.moveToNext();
        }

        ListAdapter myAdaptor = new CustomAdaptor(TitleActivity.this,list.toArray(new String[0]));

        listView.setAdapter(myAdaptor);
        registerForContextMenu(listView);


    }


    public void setUIViews(){
        newNote = findViewById(R.id.btnAdd);
        exit = findViewById(R.id.btnExit);
        enterText=findViewById(R.id.etEnter);
        addNewNote =findViewById(R.id.btnScrollDown);
        mydb=new DBHandler(this,null,null,1);
        listView = findViewById(R.id.listView);
    }

    private void ShowDialog(final View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete this?");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteNote();
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
        menu.add(0, v.getId(), 0, "Delete");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
         index = info.position;
        ShowDialog(listView);
        return true;
    }

    private void deleteNote(){
        mydb.deleteNote(listView.getItemAtPosition(index).toString());
        c=mydb.getNotes(Integer.parseInt(UId));
        display();
    }
}
