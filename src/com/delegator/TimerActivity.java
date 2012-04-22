package com.delegator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class TimerActivity extends Activity {
	private boolean ASCENDING = false;
	private boolean forfeit = false;
	private int TIMETORUN = 1;
	private Task currentTask;
	private TextView clock;
	private Clock clockHelper;
	private Handler mHandler = new Handler();
	private long mStartTime;
	
	
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
			if (!forfeit && clockHelper.finished){
				currentTask.addProgress(clockHelper.getElapsedTime(), DelegatorActivity.localUser); 
			}
		}
	}
	
	
	Runnable mUpdateTimeTask = new Runnable() {
		   public void run() {
		       final long start = mStartTime;
		       timeUpdate();
		       //Every 1 seconds, call timerUpdate()
		       mHandler.postAtTime(this, System.currentTimeMillis() + 1000 );
		   }
		};
	
	private void startTimer(){
		clockHelper= new Clock(TIMETORUN, ASCENDING);
		clock.setText(clockHelper.toString());
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
	}
	
	private void startSilentState(){
		//TODO depends on settings
	}

}
