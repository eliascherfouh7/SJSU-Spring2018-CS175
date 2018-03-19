package com.caus_abdellah.whatfordinner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;


public class Meals_activity extends Activity implements AdapterView.OnItemSelectedListener  {
    private static final String LOG_TAG = Meals_activity.class.getSimpleName();
    ArrayAdapter<String> adapter;
    DBaseHelper helper = new DBaseHelper(this);
    ArrayList<String> items;
    int check = 0;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        for (int i = 1; i <= 21; i++) {
            Spinner spinner = (Spinner) findViewById(getResources().getIdentifier("spinner" + i, "id", getPackageName()));
            outState.putInt("yourSpinner", spinner.getSelectedItemPosition());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meals_layout);

        items = new ArrayList<>();
        items = helper.uploadMealPlanDB();
        for (int i = 1; i < 22; i++) {
            TextView tv = (TextView) findViewById(getResources().getIdentifier("textView" + i, "id", getPackageName()));
            if(i<items.size()){
                tv.setText(items.get(i-1));
            }

        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.clear();
        adapter.add("");
        adapter.addAll(helper.getAvailableMeals());
        adapter.notifyDataSetChanged();

        for (int i = 1; i <= 21; i++) {

            Spinner spinner = (Spinner) findViewById(getResources().getIdentifier("spinner" + i, "id", getPackageName()));


            spinner.setOnItemSelectedListener(this);
            spinner.setAdapter(adapter);
            if (savedInstanceState != null) {
                /*spinner.setSelection(savedInstanceState.getInt("yourSpinner", 0));*/

            }
        }

    }

    public void saveMealPlan(View v){
        helper.deleteMealsPlan();
        for (int i = 1; i <= 21; i++) {
            TextView tv = (TextView) findViewById(getResources().getIdentifier("textView" + i, "id", getPackageName()));
            helper.saveNewMealPlan("textView"+i, tv.getText().toString());
        }
    }

    /**
     *
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String str;
        ArrayList<String> data;
        String item = adapter.getItem(position);
        check = check + 1;
        if(position == 0) {
            return;
        }
        if(check>21) {

            Spinner spinner = (Spinner) parent;
            if (getResources().getResourceName(spinner.getId()).contains("spinner")) {
                String temp = getResources().getResourceName(spinner.getId());
                str = String.valueOf(temp.charAt(temp.length() - 1));
                if (String.valueOf(temp.charAt(temp.length() -2)).equals("1")){
                    switch (String.valueOf(temp.charAt(temp.length() - 1))){
                        case "0":
                            str = "10";
                            break;
                        case "1":
                            str = "11";
                            break;
                        case "2":
                            str = "12";
                            break;
                        case "3":
                            str = "13";
                            break;
                        case "4":
                            str = "14";
                            break;
                        case "5":
                            str = "15";
                            break;
                        case "6":
                            str = "16";
                            break;
                        case "7":
                            str = "17";
                            break;
                        case "8":
                            str = "18";
                            break;
                        case "9":
                            str = "19";
                            break;
                    }
                }
                else if(String.valueOf(temp.charAt(temp.length() -2)).equals("2")){
                    switch (String.valueOf(temp.charAt(temp.length() - 1))){
                        case "0":
                            str = "20";
                            break;
                        case "1":
                            str = "21";
                            break;
                    }
                }

                TextView txt = (TextView) findViewById(getResources().getIdentifier("textView" + str, "id", getPackageName()));
                txt.setText(item);



                helper.verifyMeals();
                helper.deleteAvailableMeal(item);
                adapter.clear();
                adapter.add("");
                adapter.addAll(helper.getAvailableMeals());

                helper.verifyMeals();
                adapter.notifyDataSetChanged();
            }

        }
    }

    /**
     *
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}

