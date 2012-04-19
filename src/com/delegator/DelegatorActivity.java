package com.delegator;

import java.util.ArrayList;
import java.util.Iterator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DelegatorActivity extends Activity {
    public static ArrayList<Item> items = new ArrayList<Item>();
    //public ArrayList<Item> items = new ArrayList<Item>();
    ListView l;
    public TaskAdapter adapter;
    //Workaround for bug #7139 remembers the item# of listview
    private View lastMenuView = null; 

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        items.add(new CategoryItem("Kategori1"));
        items.add(new Task("Task1"));
        items.add(new Task("Task2"));
        
        items.add(new CategoryItem("Kategori2"));
        items.add(new Task("Task3"));
        items.add(new Task("Task4"));
        
        items.add(new CategoryItem("Kategori3"));
        items.add(new Task("Task4"));
        items.add(new Task("Task5"));
        items.add(new Task("Task6"));
        items.add(new Task("Task7"));
        
        adapter = new TaskAdapter(this, items);

        l = (ListView) findViewById(R.id.list);
        l.setAdapter(adapter);
        l.setItemsCanFocus(true);
    }
    
    protected void onStart(){
        super.onStart();

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar_menu, menu);
        return true;
    }
    
    
    //Might be broken.
    protected void onListItemClick(ListView l, View v, int position, long id) {

        
        if(items.get(position).isCategory()){
            // TODO Allow here to change name of category
        }
        else{ //Must be a Task item
            
            Task item = (Task)items.get(position);
            // TODO Do something when a task is clicked in list        
        }
        
        //super.onListItemClick(l, v, position, id);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.taskmenu, menu);
        // Keep track of the current MenuInfo (bug #7139)
        lastMenuView = v; 
    }
    
    
    @Override
    public boolean onContextItemSelected(MenuItem item){
        //Save this if bug #7139 ever is resolved
        //AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        //This is the workaround...
        ListView parent = (ListView)lastMenuView.getParent();
        int pos = (int)parent.getPositionForView(lastMenuView);

        switch (item.getItemId()) {
            case R.id.list_item_menu_finished:
                ((Task) items.get(pos)).finished = true;       
                adapter.notifyDataSetChanged();
                return true;
            case R.id.list_item_menu_remove:
                adapter.remove(items.get(pos));            
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    
    /**
     * Private method to clear finished tasks
     * from the listview and arraylist.
     */
    private void clearTasks(){
        ArrayList<Item> iteratorList = new ArrayList<Item>(items);
        Iterator<Item> it = iteratorList.iterator();
        while(it.hasNext()){
            Item i = it.next();
            if (!i.isCategory()){
                Task t = (Task) i;
                if(t.finished){
                    adapter.remove(i);
                }
            }
        }
    }
    
    /**
     * What to do when an options item has been clicked
     * (These are defined in bar_menu)
     * 
     * @param item the item that was clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.bar_menu_add:
                Intent i = new Intent(getBaseContext(), AddActivity.class);
                startActivity(i);
                return true;
            case R.id.bar_menu_clear:
                // TODO add AlertDialog
                clearTasks();
                return true;
            case R.id.bar_menu_settings:
                // TODO start SettingsActivity
                return true;
            case R.id.bar_menu_share:
                // TODO start BatchShareActivity?
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * Assists in population of listview with
     * items (Tasks and CategoryItems)
     * @author Luceat
     *
     */
    public class TaskAdapter extends ArrayAdapter<Item> {

        private ArrayList<Item> items;
        private LayoutInflater vi;
       
        public TaskAdapter(Context context,ArrayList<Item> items) {
            super(context,0, items);
            this.items = items;
            vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            final Item i = items.get(position);
            if (i != null) {
                if(i.isCategory()){
                    CategoryItem si = (CategoryItem)i;
                    v = vi.inflate(R.layout.list_item_category, null);

                    v.setOnClickListener(null);
                    v.setOnLongClickListener(null);
                    v.setLongClickable(false);
                    
                    final TextView sectionView = (TextView) v.findViewById(R.id.list_item_category_text);
                    sectionView.setText(si.title);
                }else{
                    Task ei = (Task)i;
                    v = vi.inflate(R.layout.list_item_task, null);
                    v.setClickable(true); 
                    v.setFocusable(true); 
                    v.setBackgroundResource(android.R.drawable.menuitem_background); 
                    
                    //Makes this item a candidate for longclick menu
                    registerForContextMenu(v); 
                    
                    final TextView title = (TextView)v.findViewById(R.id.list_item_task_title);
                    final Button button = (Button)v.findViewById(R.id.list_item_task_button);
                    if (title != null){
                        title.setText((ei.title));      
                    }
                    if (ei.finished){
                        title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); //ugly
                        v.setBackgroundColor(Color.DKGRAY);
                        button.setVisibility(View.GONE);
                    }
                    
                    //if (preferences.getBoolean("HIDE_TIMER_BUTTON", true)){
                    button.setText("Start timer");
                    /*}
                    else {
                        button.setVisibility(0);
                    }*/
                }
            }
            return v;
        }
    }
}
