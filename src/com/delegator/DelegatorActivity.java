package com.delegator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import android.widget.Toast;

public class DelegatorActivity extends Activity {
	public static final String LIST_POSITION = "LIST_POSITION";
    public static ArrayList<Item> items = new ArrayList<Item>();
    public final static Collaborator localUser = new Collaborator("Adam Johansson", "luceatadam@gmail.com", "+46736001187");
    private static final int ADD_TASK = 128;
    private static final boolean FIRST_RUN = true;
    //public ArrayList<Item> items = new ArrayList<Item>();
    ListView l;
    public TaskAdapter adapter;
    //Workaround for bug #7139 remembers the item# of listview
    private View lastMenuView = null; 

    /**
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (FIRST_RUN){
            items = new ArrayList<Item>();

			List<Task> taskList = InOutHelper.loadToTask();
			for(Task taskItem : taskList) {
				items.add(taskItem);
			}
        }
        
        adapter = new TaskAdapter(this, items);

        l = (ListView) findViewById(R.id.list);
        l.setAdapter(adapter);
        l.setItemsCanFocus(true);
        
    }
    
    /**
     * (non-Javadoc)
     * @see android.app.Activity#onStart()
     */
    protected void onStart(){
        super.onStart();

    }
    
    /**
     * (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
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
    
    /**
     * (non-Javadoc)
     * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.taskmenu, menu);
        // Keep track of the current MenuInfo (bug #7139)
        lastMenuView = v; 
    }
    
    /**
     * (non-Javadoc)
     * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onContextItemSelected(MenuItem item){
        //Save this if bug #7139 ever is resolved
        //AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        //This is the workaround...
        ListView parent = (ListView)lastMenuView.getParent();
        int pos = (int)parent.getPositionForView(lastMenuView);
        String filePath;
        switch (item.getItemId()) {
            case R.id.list_item_menu_finished:
                ((Task) items.get(pos)).finished = true;    
                
                InOutHelper.updateJSON((Task) items.get(pos));
                adapter.notifyDataSetChanged();
                return true;
            case R.id.list_item_menu_remove:
            	
            	InOutHelper.removeJSON((Task) items.get(pos));
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
                	String filePath = getExternalFilesDir(null) + "/data.json";
                	InOutHelper.removeJSON(t);
                    adapter.remove(i);
                }
            }
        }
    }
    
    /**
     * Handles a click on a "Show timer" button
     * 
     * @param v the view (button) being clicked
     */
    public void timerClickHandler(View v){
    	//Position in ListView of button being clicked
    	int pos = ((ListView)(v.getParent().getParent().getParent())).getPositionForView(v);
    	Intent i = new Intent(getBaseContext(), TimerActivity.class);
    	i.putExtra(LIST_POSITION, pos);
    	startActivity(i);
    	adapter.notifyDataSetChanged();
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
                startActivityForResult(i, ADD_TASK); 
                adapter.notifyDataSetChanged();
                return true;
            case R.id.bar_menu_clear:
                // TODO add AlertDialog
                clearTasks();
                return true;
            case R.id.bar_menu_settings:
                Toast.makeText(this.getBaseContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
                // TODO start SettingsActivity
                return true;
            case R.id.bar_menu_share:
                Toast.makeText(this.getBaseContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
                // TODO start BatchShareActivity?
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
        case ADD_TASK: //If AddActivity has returned result
            if (resultCode == RESULT_CANCELED){
                Toast.makeText(this.getBaseContext(), "RESULT_CANCELED", Toast.LENGTH_SHORT).show();
            }
            else {
                
                Task t = new Task(data.getStringExtra("TITLE"), localUser);
                t.description = data.getStringExtra("DESCRIPTION");
                t.estimatedTime = data.getIntExtra("ESTIMATED_TIME", 0);
                long l = data.getLongExtra("DATE", 0);
                if (l != 0){
                    t.deadline = new Date(l);
                }
                int pos = data.getIntExtra("CATEGORY_POS", -1);
                adapter.insert(t, pos + 1);
            }
            break;
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

        /**
         * (non-Javadoc)
         * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
         */
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
                    final TextView descr = (TextView)v.findViewById(R.id.list_item_task_descr);
                    final TextView progress = (TextView)v.findViewById(R.id.list_item_task_progress);
                    final Button button = (Button)v.findViewById(R.id.list_item_task_button);
               
                    title.setText((ei.title));  
                    descr.setText(ei.description);
                    DecimalFormat df = new DecimalFormat("#.##");
                    progress.setText(getString(R.string.list_item_task_progress_prefix) + " " +
                                     df.format(((double) ei.getTotalMinutes()* 100 / ei.estimatedTime)) + "%");
                
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
