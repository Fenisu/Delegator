package com.delegator;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TaskActivity extends Activity {
    Task task;
    int position;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskview);
        Intent i = getIntent();
        position = i.getIntExtra(DelegatorActivity.LIST_POSITION, -1);
        if (position == -1){
            finish();
        }
        task = (Task) DelegatorActivity.items.get(position);
        ((TextView) findViewById(R.id.taskview_title)).setText(task.title);
        ((TextView) findViewById(R.id.taskview_descr)).setText(task.description);
        TextView deadline = (TextView) findViewById(R.id.taskview_deadline);

        java.text.DateFormat df = android.text.format.DateFormat.getTimeFormat(getApplicationContext());
        if (task.deadline != null){
            deadline.setText(df.format(task.deadline.getTime()));
        } else {
            deadline.setText(getText(R.string.taskview_no_deadline_text));
        } 
    }
    
    public void onClickHandler(View v){
        int id = v.getId();
        switch (id){
        case R.id.taskview_edit_button:
            Intent i = new Intent(getBaseContext(), AddActivity.class);
            //startActivityForResult(i, DelegatorActivity.EDIT_TASK); 
            //finish(); //problem?
        }
    }
    /**
     * (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
        
        case DelegatorActivity.EDIT_TASK: //If AddActivity returns result through TaskActivity for example
            if (resultCode != RESULT_CANCELED){
                task.description = data.getStringExtra("DESCRIPTION");
                task.estimatedTime = data.getIntExtra("ESTIMATED_TIME", 0);
                long l = data.getLongExtra("DATE", 0);
                if (l != 0){
                    task.deadline = new Date(l);
                }
            }
            break;
        }
    }

}
