package com.caus_abdellah.whatfordinner;

import static android.provider.BaseColumns._ID;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by caus_abdellah on 3/4/18.
 */

public class DBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "whatsfordinner.db";

    public static final int DATABASE_VERSION = 2;


    private static final String LOG_TAG = DBaseHelper.class.getSimpleName();


    public DBaseHelper(Context context){super(context, DATABASE_NAME, null, DATABASE_VERSION);}
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + WFDColumns.GROCERIES_TABLE + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WFDColumns.GROCERY_NAME + " TEXT NOT NULL, " +
                ""
                + WFDColumns.GROCERY_QUANTITY + " REAL, "
                + WFDColumns.GROCERY_UNIT + " TEXT);");

        sqLiteDatabase.execSQL("CREATE TABLE " + WFDColumns.RECIPES_TABLE + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WFDColumns.RECIPE_NAME + " TEXT NOT NULL, "
                + WFDColumns.RECIPE_INGREDIENTS_LIST + " TEXT, "
                + WFDColumns.RECIPE_DIRECTIONS + " TEXT, "
                + WFDColumns.RECIPE_IMG_URL + " TEXT, "
                + WFDColumns.RECIPE_IMG_PATH + " TEXT, "
                + WFDColumns.RECIPES_COUNT + " INTEGER);");

        sqLiteDatabase.execSQL("CREATE TABLE " + WFDColumns.AVAILABLE_MEALS_TABLE + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  WFDColumns.MEAL_NAME + " TEXT);");

        sqLiteDatabase.execSQL("CREATE TABLE " + WFDColumns.MEAL_TABLE_PLAN + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + WFDColumns.TEXTVIEW + " TEXT, "
                + WFDColumns.MEAL_NAME_PLAN +  " TEXT);");

    }


    public ArrayList<String> getRecipeArrayList() {

        SQLiteDatabase db = getWritableDatabase();
        ArrayList<String> temp = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + WFDColumns.RECIPES_TABLE, null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToPosition(i);
            temp.add(c.getString(1));
            Log.v(LOG_TAG, "Recipe_List" + c.getString(0));
        }
        c.close();
        db.close();

        return temp;
    }

    /* this fuunction returns the recipe name given the ID */
    public String getRecipeName(Integer id){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT recipe_name FROM " + WFDColumns.RECIPES_TABLE + " WHERE _ID = " + id, null);
        c.moveToFirst();
        String temp = c.getString(0);
        c.close();
        db.close();
        return temp;
    }

    public String getRecipeIngredients(Integer id) {

        SQLiteDatabase dBase = getWritableDatabase();
        Cursor crsr = dBase.rawQuery("SELECT ingredients FROM " + WFDColumns.RECIPES_TABLE + " WHERE _ID = " + id, null);
        //startManagingCursor(crsr);
        crsr.moveToFirst();
        String str = crsr.getString(crsr.getColumnIndex(WFDColumns.RECIPE_INGREDIENTS_LIST));

        crsr.close();
        dBase.close();
        return str;
    }

    public String getRecipeInstruction(Integer id) {

        SQLiteDatabase db = getWritableDatabase();
        //ArrayList<String> temp = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT directions FROM " +  WFDColumns.RECIPES_TABLE + " WHERE _ID = " + id, null);
        c.moveToFirst();
        String temp = c.getString(0);
        c.close();
        db.close();
        return temp;
    }

    public void reneweMealsPlan(int id, String meal_name){
        SQLiteDatabase db = getWritableDatabase();
        String update_meal_plan = "UPDATE " + WFDColumns.MEAL_TABLE_PLAN + " SET MEAL_NAME = '" + meal_name +"' WHERE ID = " + id;
        db.execSQL(update_meal_plan);
        db.close();
    }

    public ArrayList<String> uploadMealPlanDB(){

        SQLiteDatabase db = getWritableDatabase();
        ArrayList<String> items = new ArrayList<>();
        Cursor crsr = db.rawQuery("SELECT " + WFDColumns.MEAL_NAME_PLAN + " FROM " + WFDColumns.MEAL_TABLE_PLAN, null);
        crsr.moveToFirst();
        for(int i =0; i<crsr.getCount();i++){
            crsr.moveToPosition(i);
            items.add(crsr.getString(0));
        }
        db.close();
        crsr.close();
        return items;

    }


    public void saveNewMealPlan(String textView, String meal){
        Log.v(LOG_TAG, "textView:" + textView);
        SQLiteDatabase dBase = getWritableDatabase();
        String insert = "INSERT INTO " + WFDColumns.MEAL_TABLE_PLAN + " (TEXTVIEW, MEAL_NAME) VALUES ('" + textView + "', '" + meal + "')";
        dBase.execSQL(insert);
        dBase.close();
    }


    public void deleteMealsPlan(){
        SQLiteDatabase dBase = getWritableDatabase();
        String deleteAll = "DELETE FROM " + WFDColumns.MEAL_TABLE_PLAN;
        dBase.execSQL(deleteAll);
        dBase.close();
    }

    //methods used in Meals_activity

    public void newAvailableMeal(int position) {
        SQLiteDatabase dBase = getWritableDatabase();
        int new_pos = position + 1;
        Cursor c = dBase.rawQuery("SELECT RECIPE_NAME from recipenew WHERE ID = " + new_pos, null);

        if (c != null && c.moveToFirst()) {

            String temp = c.getString(0);
            updateMealsArray(temp);
            c.close();

        }

        dBase.close();
    }

    public void updateMealsArray(String newMeal){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c =db.rawQuery("INSERT INTO " + WFDColumns.AVAILABLE_MEALS_TABLE + " (MEAL_NAME) VALUES ('" + newMeal + "')", null);
        c.moveToFirst();
        c.close();
        db.close();
    }


    public void verifyMeals(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c =db.rawQuery("SELECT min(ID), MEAL_NAME FROM " + WFDColumns.AVAILABLE_MEALS_TABLE + " GROUP BY MEAL_NAME", null);
        c.moveToFirst();
        for(int i = 0; i<c.getCount(); i++){
            c.moveToPosition(i);
            Log.v(LOG_TAG, "Checkmeal:" + c.getString(0) +": " + c.getString(1));
        }
        c.close();
        db.close();
    }

    public boolean deleteAvailableMeal(String meal){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(WFDColumns.AVAILABLE_MEALS_TABLE,"MEAL_NAME IS '" + meal + "' AND ID = (SELECT min(ID) FROM " + WFDColumns.AVAILABLE_MEALS_TABLE + " WHERE MEAL_NAME = '" + meal + "')",new String[]{} );
        db.close();
        return true;
//
    }

    public ArrayList<String> getAvailableMeals(){

        SQLiteDatabase dBase = getWritableDatabase();
        ArrayList<String> temp = new ArrayList<>();

        Cursor c = dBase.rawQuery("SELECT MEAL_NAME FROM " + WFDColumns.AVAILABLE_MEALS_TABLE, null);
        Log.v(LOG_TAG, "Count in getFromAvaiMeals" + c.getCount());
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
            c.moveToPosition(i);
            temp.add(c.getString(0));
        }
        c.close();
        dBase.close();
        Log.v(LOG_TAG,"Meals available: " + temp.toString());
        return temp;
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + WFDColumns.GROCERIES_TABLE + ";");
        sqLiteDatabase.execSQL("DROP TABLE " + WFDColumns.RECIPES_TABLE + ";");
        onCreate(sqLiteDatabase);

    }
}
