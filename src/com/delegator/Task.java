package com.delegator;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Describes a task
 * @author Adam Johansson
 *
 */
public class Task implements Item{
    public final String title;
    public String description;
    Calendar deadline;
    private ArrayList<Collaborator> collaborators = new ArrayList<Collaborator>();
    private int[] collaboratorTime;
    public int timeWorkedOnTask;
    boolean finished = false;
    
    public Task(String title){
        this.title = title;
    }
    
    /**
     * A task is never a category
     */
    @Override
    public boolean isCategory() {
        return false;
    }
    
    /**
     * Returns the total number of minutes worked on
     * this task by all collaborators.
     * 
     * @return the total number of minutes
     */
    public int getTotalMinutes(){
        int total = 0;
        for(int i=0; i < collaboratorTime.length; i++){
            total = total + collaboratorTime[i];
        }
        return total;
    }


 
    

}
