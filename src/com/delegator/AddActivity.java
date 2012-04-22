package com.delegator;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Activity to add tasks and set values to fields of Task.
 * 
 * May call on InviteContactActivity to add collaborators.
 * 
 * @author Luceat
 * 
 */
public class AddActivity extends Activity {
    private Spinner s;
    private ArrayList<String> category = new ArrayList<String>();
    private ArrayList<Item> shortItems = DelegatorActivity.items;
    private ArrayList<Integer> categoryPositions = new ArrayList<Integer>();
    private Task t;
    private int chosenCategoryPos;
    private boolean wantToSave = false;
    
    /**
     * Do some things when the activity is created.
     * This includes setting a new view (addview)
     * Creating and populating spinners etc.
     * 
     * @author Luceat
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addview);
        s = (Spinner)findViewById(R.id.add_view_category);
        findCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
                                       android.R.layout.simple_spinner_dropdown_item, 
                                       category);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new CustomOnItemSelectedListener());      
    }
    

    @Override
    public void onDestroy(){
    	if (isFinishing() && wantToSave){
    		shortItems.add(t);
    	}
    }
    
    /**
     * Searches the items arraylist for CategoryItems and
     * adds them to the arraylist category.
     * 
     * @author Luceat
     */
    private void findCategories() {
        for (int i=0; i < shortItems.size(); i++){
            if (shortItems.get(i).isCategory()){
                categoryPositions.add(new Integer(i));
                category.add(((CategoryItem)shortItems.get(i)).title);
            }
        }
    }
    
    /**
     * Defines what happens when an item is clicked in 
     * the Categoryspinner. It will set chosenCategoryPos
     * to the index of spinner.
     * 
     * @author Luceat
     */
    public class CustomOnItemSelectedListener implements OnItemSelectedListener {
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                long id) {
            chosenCategoryPos = pos;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            chosenCategoryPos = -1;
        }
    }
}
