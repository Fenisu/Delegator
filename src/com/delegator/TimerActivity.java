package com.delegator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimerActivity extends Activity {
	private boolean ASCENDING = false;
	private boolean forfeit = true;
	private int TIMETORUN = 1;
	private Task currentTask;
	private TextView clock;
	private Clock clockHelper;
	private Handler mHandler = new Handler();
    private boolean paused = false;	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timerview);
		clock = (TextView)findViewById(R.id.clock);
		Intent i = this.getIntent();
		int pos = i.getExtras().getInt(DelegatorActivity.LIST_POSITION);
		currentTask = (Task)DelegatorActivity.items.get(pos);
		startTimer();
	}
	
	@Override
	public void onDestroy(){
		if (isFinishing()){
			if (!forfeit){
				currentTask.addProgress(clockHelper.getElapsedTime(), DelegatorActivity.localUser);
				String filePath = getExternalFilesDir(null) + "/data.json";
				InOutHelper.updateJSON(currentTask);
			}
		}
	      super.onDestroy();
	}
	
	public void onButtonClick(View v){
	    int id = v.getId();
	    switch (id){
	    case R.id.pause_button:
	        Button b = (Button) findViewById(R.id.pause_button);
	        if (!paused ){
	            mHandler.removeCallbacks(mUpdateTimeTask);
	            b.setText(R.string.resume_button_text);
	            paused = true;
	        }
	        else {
	            mHandler.postDelayed(mUpdateTimeTask, 1000);
	            b.setText(R.string.pause_button_text);
	            paused = false;
	        }
	        break;
	    case R.id.stop_button:
	        //Sets up the alertdialog to save progress or not
	        mHandler.removeCallbacks(mUpdateTimeTask);
	        AlertDialog.Builder builder;
	        builder = new AlertDialog.Builder(TimerActivity.this);
	        builder.setCancelable(true);
	        builder.setInverseBackgroundForced(true);
	        builder.setTitle(R.string.timer_dialog_save_title);
	        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                forfeit = false;
	                finish();
	                //dialog.dismiss();
	            }
	        });
	        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                forfeit = true;
	                finish();
	                //dialog.dismiss();    
	            }
	        });
	        
	        AlertDialog d = builder.create();
	        d.show();
	        
	        break;
	    default:
	        break;
	    }
	}
	
	
	Runnable mUpdateTimeTask = new Runnable() {
		   public void run() {
		       timeUpdate();
		       //call timeUpdate() in one second
		       mHandler.postAtTime(this, System.currentTimeMillis() + 1000 );
		   }
		};
	
	private void startTimer(){
		clockHelper= new Clock(TIMETORUN, ASCENDING);
		clock.setText(clockHelper.toString());
        
		//call timeUpdate() in one second
        mHandler.postDelayed(mUpdateTimeTask, 1000);
	
		//Turn off incoming calls etc according to settings
		startSilentState();
	}
	
	private void timeUpdate(){		
		if (!ASCENDING){
			(clock).setText(clockHelper.tickDown());
		}
		else {
			(clock).setText(clockHelper.tickUp());
		}
		if (!clockHelper.finished){
			mHandler.postDelayed(mUpdateTimeTask, 1000);
		}
		else{
		    onButtonClick(findViewById(R.id.stop_button));
		}
	}
	
	private void startSilentState(){
		//TODO Implement silentState
	}

}
