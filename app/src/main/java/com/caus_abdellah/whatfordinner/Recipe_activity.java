package com.caus_abdellah.whatfordinner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


/**
 * Created by Abdellah Cherfouh on 3/10/18.
 */
public class Recipe_activity extends Activity implements Recipe_portrait_fragment.ListFragmentItemClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_layout);
    }

    public void BackToMainMenu(View v){
        finish();
    }
    @Override
    public void onListFragmentItemClick(int position) {
    }

}
