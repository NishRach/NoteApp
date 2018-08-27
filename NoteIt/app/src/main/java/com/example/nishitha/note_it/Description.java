package com.example.nishitha.note_it;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Description {
    String description;
    Date date;
    private static DateFormat dateFormat = new SimpleDateFormat("hh:mm a \ndd/MM/yy");

    public Description(String description) {
        this.description = description;
        this.date = new Date();
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return dateFormat.format(date);
    }

}
