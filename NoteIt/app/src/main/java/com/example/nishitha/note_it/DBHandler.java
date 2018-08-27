package com.example.nishitha.note_it;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NoteIt.db";
    private static final String REGISTRATION_TABLE = "Registration_Table";
    private static final String NOTES_TABLE = "Notes_Table";
    private static final String DESCRIPTION_TABLE = "Description_Table";


    //common columns
    private static final String USER_ID = "ID";
    private static final String NOTE_ID = "NID";


    //registration table columns
    private static final String REGISTRATION_NAME = "NAME";
    private static final String REGISTRATION_EMAIL = "EMAIL";
    private static final String REGISTRATION_PHONE = "PHONE";
    private static final String REGISTRATION_USERNAME = "USER_NAME";
    private static final String REGISTRATION_PASSWORD = "PASSWORD";

    //note table columns
    private static final String NOTE_NAME = "NOTE_TITLE";

    //description table colmns
    private static final String ENTER_DATE = "DATE";
    private static final String NOTE_DESCRIPTION = "DESCRIPTION";
    private static final String DESCRIPTION_ID= "DID";

    //Table creation statements
    private static final String CREATE_TABLE_NOTES= " CREATE TABLE " + NOTES_TABLE +
            "( NID INTEGER PRIMARY KEY AUTOINCREMENT ," +
            "NOTE_TITLE TEXT, ID INTEGER, FOREIGN KEY (ID) REFERENCES "
            +REGISTRATION_TABLE+ "(ID) );";

    private static final String  CREATE_DESCRIPTION_TABLE=" CREATE TABLE "+DESCRIPTION_TABLE+
            "(DID INTEGER PRIMARY KEY AUTOINCREMENT,DESCRIPTION TEXT, DATE INTEGER, NID INTERGER,FOREIGN KEY (NID) REFERENCES " +NOTES_TABLE+" (NID));";

    private static final String CREATE_TABLE_USERS=" CREATE TABLE " + REGISTRATION_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,EMAIL TEXT,PHONE INTEGER,USER_NAME TEXT,PASSWORD PASSWORD);";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_NOTES);
        db.execSQL(CREATE_DESCRIPTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + REGISTRATION_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DESCRIPTION_TABLE+ ";");
        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE + ";");
        onCreate(db);
    }


        //insert a new user in Registration table
    public boolean insertUser(String name1, String username, String email, String number, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REGISTRATION_NAME, name1);
        contentValues.put(REGISTRATION_EMAIL, email);
        contentValues.put(REGISTRATION_PHONE, number);
        contentValues.put(REGISTRATION_USERNAME, username);
        contentValues.put(REGISTRATION_PASSWORD, password);

        long result = db.insert(REGISTRATION_TABLE, null, contentValues);
        db.close();

        //to check if data is inserted
        return result != -1;
    }


    //insert a note in Notes table
    public boolean insertNote(Notes note,int uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_NAME, note.getNoteTitle());
        contentValues.put(USER_ID,uid);

        long result = db.insert(NOTES_TABLE, null, contentValues);
        db.close();

        //to check if data is inserted
        return result != -1;
    }

    //insert a description in Description table
    public boolean insertDescription(Description description,int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_DESCRIPTION, description.getDescription());
        contentValues.put(ENTER_DATE,description.getDate());
        contentValues.put(NOTE_ID,id);

        long result = db.insert(DESCRIPTION_TABLE, null, contentValues);
        db.close();

        //to check if data is inserted
        return result != -1;
    }




    //checks if user trying to login is registered
    public boolean checkLogin(String uName, String uPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c =db.query(REGISTRATION_TABLE, new String[]{"USER_NAME","PASSWORD"},
                "USER_NAME=? AND PASSWORD=?",new String[]{uName,uPassword},null,null,null);
        if(uName.equals(null) || uPassword.equals(null));
        else
            return c.moveToFirst();

        return false;

        }


    //checks if email has already been registered
    public boolean isRegistered(String uEmail){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c =db.query(REGISTRATION_TABLE,new String[]{"EMAIL"}, "EMAIL=?",new String[]{uEmail},null,null,null);
        return c.moveToFirst();
    }


    //checks if username has already been registered
    public boolean CheckUserName(String uUName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c =db.query(REGISTRATION_TABLE,new String[]{"USER_NAME"}, "USER_NAME=?",new String[]{uUName},null,null,null);
        return c.moveToFirst();
    }

    //retrieve notes to display based on the user logged in
    public Cursor getNotes(int uid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c= db.rawQuery(" SELECT * FROM " + NOTES_TABLE+" WHERE " +USER_ID+ "=\""+uid+ "\";" ,null);
            c.moveToFirst();
        return c;
    }

    //retrieve the descriptions based on the note selected
    public Cursor getDescriptions(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        //String query = " SELECT * FROM Description_Table";
        String query = " SELECT * FROM Description_Table WHERE NID=" +id;
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        return c;


    }


    //delete description
    public  void deleteDescription(String description){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " +DESCRIPTION_TABLE+ " WHERE " +NOTE_DESCRIPTION+ "=\"" +description+ "\";";
        db.execSQL(query);

    }

    //delete note
    public  void deleteNote(String note){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " +NOTES_TABLE+ " WHERE " +NOTE_NAME+ "=\"" +note+ "\";";
        db.execSQL(query);

    }

    //get id of the note
    public  int getId(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " +NOTE_ID + " FROM "+NOTES_TABLE+ " WHERE " +NOTE_NAME+"=\"" + name +"\";";
     // Cursor c=db.query(NOTES_TABLE,new String[]{"NID"},"NOTE_NAME=?",new String[]{name},null,null,null);
        Cursor c = db.rawQuery(query,null);
        if(c!=null){
            c.moveToFirst();
        }
        String res = c.getString(c.getColumnIndex(NOTE_ID));
        return Integer.parseInt(res);
    }

    //get id of user logged in
    public String getUserIds(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        //String query = " SELECT * FROM " +REGISTRATION_TABLE;
        String query = "SELECT " +USER_ID+ " FROM "+REGISTRATION_TABLE+ " WHERE " +REGISTRATION_USERNAME+"=\"" + name +"\";";
        Cursor c = db.rawQuery(query,null);
        if(c!=null){
            c.moveToFirst();
        }

        String res = c.getString(c.getColumnIndex(USER_ID));
        return res;
    }

}

