package com.caus_abdellah.whatfordinner;

import android.provider.BaseColumns;

/**
 * Created by caus_abdellah on 3/4/18.
 */

public interface WFDColumns extends BaseColumns {

    public static final String GROCERIES_TABLE = "groceries";
    public static final String GROCERY_NAME = "name";
    public static final String GROCERY_QUANTITY= "qty";
    public static final String GROCERY_UNIT = "unit";


    public static final String RECIPES_TABLE = "recipes";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String RECIPE_IMG_URL = "img_url";
    public static final String RECIPE_IMG_PATH = "img_path";
    public static final String RECIPE_DIRECTIONS = "directions";
    public static final String RECIPE_INGREDIENTS_LIST = "ingredients";
    public static final String RECIPES_COUNT = "recipes_count";

    public static final String MEAL_TABLE_PLAN = "meal_plan_table";
    public static final String TEXTVIEW = "tv";
    public static final String MEAL_NAME_PLAN = "meal_name_plan";

    public static final String AVAILABLE_MEALS_TABLE = "available_meals";
    public static final String MEAL_NAME = "meal_name";


}
