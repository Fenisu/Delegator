package com.delegator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class AddActivity extends Activity {
    private Spinner s;
    private HashMap<Integer, String> category = new HashMap<Integer, String>();
    private ArrayList<Item> shortItems = DelegatorActivity.items;
    private Task t;
    
    
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addview);
        s = (Spinner)findViewById(R.id.add_view_category);
        findCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
                                               R.layout.addview, 
                                               new ArrayList<String>(category.values()));
        s.setAdapter(adapter);

        
    }
    
    private void findCategories() {
        for (int i=0; i < shortItems.size(); i++){
            if (shortItems.get(i).isCategory()){
                category.put(new Integer(i), 
                             ((CategoryItem)shortItems.get(i)).title);
            }
        }

        
    }

    public void onFinish(Bundle savedInstanceState){
        shortItems.add(t);
    }


}
