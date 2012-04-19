package com.delegator;

import java.util.ArrayList;

/**
 * Describes a task
 * @author Adam Johansson
 *
 */
public class Task implements Item{
    public final String title;
    public int description;
    public int deadlineYear;
    public int deadlineMonth;
    public int deadlineDay;
    public int deadlineHour;
    public int deadlineMinute;
    public ArrayList<Contact> collaborators = new ArrayList<Contact>();
    private int[] collaboratorScore;
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


 
    

}
