package com.caus_abdellah.whatfordinner;



import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class NewDish_activity extends AppCompatActivity {

    private static String[] origin = {"_id", WFDColumns.RECIPE_NAME};
    private static String[] origin_grcries = {"_id", WFDColumns.GROCERY_NAME, WFDColumns.GROCERY_QUANTITY, WFDColumns.GROCERY_UNIT};

    private DBaseHelper helper;
    ImageView img1;
    ImageView img2;
    private final static int PICK_IMG = 100;
    Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_dish_layout);


        helper = new DBaseHelper(this);

        final Button add_ingredient = (Button) findViewById(R.id.add_ingredient);
        final Button save_dish = (Button) findViewById(R.id.save_dish_button);

        final ArrayList<Ingredients> ingredients = new ArrayList<>();

        final ListAdapter adpter = new ListAdapter(this, ingredients);

        final NonScrollListViewActivity listView = (NonScrollListViewActivity) findViewById(R.id.ll);
        listView.setAdapter(adpter);

        final Context context = this.getApplicationContext();


        img1 = (ImageView) findViewById(R.id.add_pic);
        img2 = (ImageView) findViewById(R.id.background);

        img1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                openGallery();
            }

        });



        add_ingredient.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int numItems = listView.getChildCount();
                ingredients.clear();

                for (int i = 0; i < numItems; i++) {

                    View view = listView.getChildAt(i);
                    String itemQuantity = ((EditText) view.findViewById(R.id.item_quantity)).getText().toString();
                    String itemDesc = ((EditText) view.findViewById(R.id.dish_name)).getText().toString();
                    String itemUnit = ((Spinner) view.findViewById(R.id.unit)).getSelectedItem().toString();


                    if (Float.parseFloat(itemQuantity) <= 0) {
                        Toast.makeText(context, "Your entry is invalid \n unable to save!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (itemDesc.isEmpty()) {
                        Toast.makeText(context, "The description is missing \n please, enter description!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    ingredients.add(new Ingredients(itemDesc, Float.parseFloat(itemQuantity), itemUnit));
                }

                ingredients.add(new Ingredients("", 0, ""));
                adpter.notifyDataSetChanged();
            }
        });

        save_dish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String recipe_name = ((EditText) findViewById(R.id.dish_name)).getText().toString();
                String recipe_directions = ((EditText) findViewById(R.id.directions_text)).getText().toString();
                if (recipe_name.isEmpty()) {
                    Toast.makeText(context, "The recipe name is missing \n please, enter recipe name!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (recipe_directions.isEmpty()) {
                    Toast.makeText(context, "The description is missing \n please, enter direction!", Toast.LENGTH_LONG).show();
                    return;
                }

                int numItems = listView.getChildCount();
                String ingredients = "";

                for (int i = 0; i < numItems; i++) {
                    View view = listView.getChildAt(i);
                    String itemQuantity = ((EditText) view.findViewById(R.id.item_quantity)).getText().toString();
                    String itemDesc = ((EditText) view.findViewById(R.id.dish_name)).getText().toString();
                    String itemUnit = ((Spinner) view.findViewById(R.id.unit)).getSelectedItem().toString();


                    if (Float.parseFloat(itemQuantity) <= 0) {
                        Toast.makeText(context, "Your entry is invalid \n unable to save!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (itemDesc.isEmpty()) {
                        Toast.makeText(context, "The description is missing \n please, enter description!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    ingredients += itemDesc + "," + itemQuantity + "," + itemUnit + "\n";
                }

                newRecipe(recipe_name, ingredients,recipe_directions, "", "", 0);
                addIngredients(ingredients);
                finish();
                Toast.makeText(context, "recipe added successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void openGallery(){

        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMG);
    }

    public void onActivityResult(int requestCode, int resultCode,Intent data){

        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK  && requestCode == PICK_IMG){
            imgUri = data.getData();
            img2.setImageURI(imgUri);
        }
    }


    public void addIngredients(String ingredients) {
        SQLiteDatabase dBase = helper.getWritableDatabase();
        Cursor crsr = dBase.query(WFDColumns.GROCERIES_TABLE, origin_grcries, null, null, null, null, null);
        crsr.moveToFirst();
        JSONArray groceries = getJSONArray(crsr);
        List<GroceryItem> items = new ArrayList<GroceryItem>();

        String[] listOfIngredients = ingredients.split("\n");
        for (String item : listOfIngredients) {
            String[] itemContents = item.split(",");
            items.add(new GroceryItem(itemContents[0], itemContents[1], itemContents[2]));
        }

        boolean exists;
        for (int i = 0; i < groceries.length(); i++) {

            try {
                JSONObject item = groceries.getJSONObject(i);
                Log.d("DEBUG", item.toString());
                String name = item.getString("name");
                String qty = item.getString("qty");
                String unit = item.getString("unit");
                exists = false;
                for (GroceryItem g : items) {
                    Log.d("DEBUG", g.name + " :: " + name);
                    if ((g.name).equals(name)) {
                        g.quantity = String.valueOf(Float.parseFloat(g.quantity) + Float.parseFloat(qty));
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    items.add(new GroceryItem(name, qty, unit));
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }

        dBase.execSQL("delete from " + WFDColumns.GROCERIES_TABLE);
        for (GroceryItem item : items) {
            ContentValues updated_vlues = new ContentValues();
            updated_vlues.put(WFDColumns.GROCERY_NAME, item.name);
            updated_vlues.put(WFDColumns.GROCERY_QUANTITY, item.quantity);
            updated_vlues.put(WFDColumns.GROCERY_UNIT, item.unit);
            dBase.insertOrThrow(WFDColumns.GROCERIES_TABLE, null, updated_vlues);
        }
    }


    public void newRecipe(String name, String ingredientList, String directions, String imgPath, String imgUrl, int numRecipes) {
        SQLiteDatabase dBase = helper.getWritableDatabase();

        ContentValues vals = new ContentValues();
        vals.put(WFDColumns.RECIPE_NAME, name);
        vals.put(WFDColumns.RECIPE_INGREDIENTS_LIST, ingredientList);
        vals.put(WFDColumns.RECIPE_DIRECTIONS, directions);
        vals.put(WFDColumns.RECIPE_IMG_PATH, imgPath);
        vals.put(WFDColumns.RECIPE_IMG_URL, imgUrl);
        vals.put(WFDColumns.RECIPES_COUNT, numRecipes);
        dBase.insertOrThrow(WFDColumns.RECIPES_TABLE, null, vals);
    }

    private JSONArray getJSONArray(Cursor crsr) {
        JSONArray jArray = new JSONArray();
        crsr.moveToFirst();

        while (crsr.isAfterLast() == false) {
            int numColumns = crsr.getColumnCount();

            JSONObject rows = new JSONObject();
            for (int i = 0; i < numColumns; i++) {
                if (crsr.getColumnName(i) != null) {
                    try {
                        if (crsr.getString(i) != null) {
                            Log.d("TAG_NAME", crsr.getString(i));
                            rows.put(crsr.getColumnName(i) ,  crsr.getString(i) );

                        } else {
                            rows.put(crsr.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }
            }
            jArray.put(rows);
            crsr.moveToNext();
        }
        return jArray;
    }

    private class ListAdapter extends ArrayAdapter<Ingredients> {

        public ListAdapter(Context context, ArrayList<Ingredients> list) {
            super(context, 0, list);
        }


        @Override
        public View getView(int position, View view, ViewGroup vGroup) {

            Ingredients item = getItem(position);


            if (view == null) {
                view = LayoutInflater.from(getBaseContext()).inflate(R.layout.non_scroll_list_view_layout, vGroup, false);
            }

            EditText name = (EditText) view.findViewById(R.id.dish_name);
            EditText qty = (EditText) view.findViewById(R.id.item_quantity);

            name.setText(item.getName());
            qty.setText(Float.toString(item.getQuantity()));

            //spinner
            Spinner spinner = (Spinner) view.findViewById(R.id.unit);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adpView, View view, int i, long l) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> adpView) {

                }
            });

            //spinner drop down
            List<String> measures = new ArrayList<>();
            measures.add("Pounds(lb)");
            measures.add("Pieces(pcs)");
            measures.add("Dozens(dz)");
            measures.add("Grams(g)");
            measures.add("Ounces(oz)");
            measures.add("Liters(l)");


            ArrayAdapter<String> aAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, measures);
            aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(aAdapter);

            return view;
        }
    }



}




