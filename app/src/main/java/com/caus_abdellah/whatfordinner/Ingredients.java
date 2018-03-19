package com.caus_abdellah.whatfordinner;

/**
 * Created by caus_abdellah on 3/6/18.
 */

public class Ingredients {

    private String name;
    private float quantity;
    private String unit;

    public Ingredients(String n, float qty, String u){
        name = n;
        quantity = qty;
        unit = u;
    }

    public void setName(String n){
        name = n;
    }

    public void setQuantity(float qty){
        quantity = qty;
    }

    public void setUnit(String u){
        unit = u;
    }

    public String getName(){
        return name;
    }

    public float getQuantity(){
        return quantity;
    }

    public void getUnit(String u){
        unit = u;
    }
}
