package com.caus_abdellah.whatfordinner;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;


/**
 * Created by Abdellah Cherfouh on 3/8/18.
 */
public class Recipe_portrait_fragment extends ListFragment{
    private static final String LOG_TAG = Recipe_portrait_fragment.class.getSimpleName();
    DBaseHelper helper;
    public ArrayList<String> recipeArrayList;
    ListFragmentItemClickListener iFaceItemClickListener;


    public interface ListFragmentItemClickListener{
        void onListFragmentItemClick(int position);
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        Activity a = null;
        if(context instanceof Activity){
            a = (Activity) context;
        }
        try{
            iFaceItemClickListener = (ListFragmentItemClickListener)a;
        }
        catch (Exception e){
            Toast.makeText(a.getBaseContext(), "Exception", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        helper = new DBaseHelper(getActivity().getApplicationContext());
        recipeArrayList = helper.getRecipeArrayList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(inflater.getContext(), R.layout.list_item_recipe, R.id.list_item_recipe_textview, recipeArrayList);
        setListAdapter(adapter);
        View root =  inflater.inflate(R.layout.recipe_portrait_fragment,container,false);

        return root;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Intent intent = new Intent(getActivity(), NewDish_activity.class);
                intent.putExtra("position", position);
                startActivity(intent);
                return true;
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG,"On create");
    }



    @Override
    public void onListItemClick(ListView l, View view, int position, long id){
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            FragmentManager fragmentManager = getActivity().getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment prevFragment = fragmentManager.findFragmentByTag("com.website.whatsfordinner.Recipes.details");

            if(prevFragment!=null){
                fragmentTransaction.remove(prevFragment);
            }

            Recipe_landscape_fragment informationFragment = new Recipe_landscape_fragment();
            Bundle b = new Bundle();
            b.putInt("position", position);
            informationFragment.setArguments(b);

            fragmentTransaction.add(R.id.detail_fragment_container, informationFragment, "com.website.whatsfordinner.Recipes.details");

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else
        {
            helper = new DBaseHelper(getActivity());
            helper.newAvailableMeal(position);

        }
    }
}
