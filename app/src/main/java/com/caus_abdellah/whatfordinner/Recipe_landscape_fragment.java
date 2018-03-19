package com.caus_abdellah.whatfordinner;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by Abdellah Cherfouh on 3/11/18.
 */
public class Recipe_landscape_fragment extends Fragment{
    DBaseHelper helper;
    String recipeInstruction;
    Integer position;
    String recipeName;
    String recipeIngredients;
    String ingredients ="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        helper = new DBaseHelper(getActivity().getApplicationContext());
        View v =  inflater.inflate(R.layout.recipe_landscape_fragment, container, false);
        TextView recipeNameTv = (TextView) v.findViewById(R.id.recipeName);
        ImageView recipeImage = (ImageView) v.findViewById(R.id.recipePic);
        TextView ingredients = (TextView) v.findViewById(R.id.ingredients);
        TextView instruction = (TextView) v.findViewById(R.id.instruction);


        Bundle b = getArguments();
        position = Integer.parseInt(String.valueOf(b.getInt("position"))) + 1;

        recipeName = helper.getRecipeName(position);

        recipeIngredients = helper.getRecipeIngredients(position);
        recipeInstruction = helper.getRecipeInstruction(position);

        recipeNameTv.setText(recipeName);
        //byte[] path = helper.getRecipeImage(position);
        //recipeImage.setImageBitmap(BitmapFactory.decodeByteArray(path,0,path.length));
        //recipeImage.setImageBitmap(BitmapFactory.decodeFile(path));
        ingredients.setText(recipeIngredients);
        instruction.setText(recipeInstruction);
        return v;
    }

}
