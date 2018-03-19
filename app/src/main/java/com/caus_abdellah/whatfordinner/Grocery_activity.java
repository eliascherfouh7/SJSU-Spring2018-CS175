package com.caus_abdellah.whatfordinner;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import java.util.ArrayList;


public class Grocery_activity extends ListActivity implements View.OnClickListener {

    private DBaseHelper helper;
    ArrayAdapter<String> adpter;
    ArrayList<String> items = new ArrayList<>();

    private static String[] origin = {"_id", WFDColumns.GROCERY_NAME, WFDColumns.GROCERY_QUANTITY, WFDColumns.GROCERY_UNIT};

    private static int[] destination = {R.id.grocery_item_id, R.id.grocery_item, R.id.grocery_quantity, R.id.grocery_unit};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grocery_layout);

        adpter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        setListAdapter(adpter);

        helper = new DBaseHelper(this);
        try {
            Cursor crsr = retrieveGroceries();
            displayGroceries(crsr);
        } finally {
            helper.close();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
    }

    public void displayGroceries(Cursor crsr) {

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            items.add(crsr.getString(crsr.getColumnIndex(WFDColumns.GROCERY_NAME)) + "\t\t\t" + crsr.getString(crsr.getColumnIndex(WFDColumns.GROCERY_QUANTITY)) + "\t\t\t" + crsr.getString(crsr.getColumnIndex(WFDColumns.GROCERY_UNIT)));
            crsr.moveToNext();
        }

        crsr.close();
        adpter.notifyDataSetChanged();
    }

    private Cursor retrieveGroceries(){
        SQLiteDatabase dBase = helper.getReadableDatabase();
        Cursor crsr = dBase.query(WFDColumns.GROCERIES_TABLE, origin, null, null, null, null, null);
        //startManagingCursor(crsr);
        return crsr;
    }

}
