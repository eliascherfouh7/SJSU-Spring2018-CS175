package com.caus_abdellah.whatfordinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView copyRight = (ImageView) findViewById(R.id.whatsfordinner);

        copyRight.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                Copyright_Activity dialogFragment = new Copyright_Activity ();
                dialogFragment.show(fm, "Sample Fragment");
            }
        });

        TextView new_dish = (TextView) findViewById(R.id.new_dish_icon);

        new_dish.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent newDI = new Intent(MainActivity.this, NewDish_activity.class);
                startActivity(newDI);
            }
        });

        TextView grocery = (TextView) findViewById(R.id.groceries_icon);

        grocery.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent grocery_intent = new Intent(MainActivity.this, Grocery_activity.class);
                startActivity(grocery_intent);
            }
        });

        TextView meals = (TextView) findViewById(R.id.meals_icon);

        meals.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent newDI = new Intent(MainActivity.this, Meals_activity.class);
                startActivity(newDI);
            }
        });

        TextView recipes = (TextView) findViewById(R.id.recipes_icon);

        recipes.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent newDI = new Intent(MainActivity.this, Recipe_activity.class);
                startActivity(newDI);
            }
        });
    }


}

