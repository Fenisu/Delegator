package com.delegator;

import java.util.ArrayList;
import java.util.Date;

/**
 * Describes a task
 * @author Adam Johansson
 *
 */
public class Task implements Item{
    public String title;
    public String description;
    Date deadline;
    private ArrayList<Collaborator> collaborators = new ArrayList<Collaborator>();
    public int[] collaboratorTime = new int[1];
    boolean finished = false;
	public int estimatedTime;
    public String category;
    public boolean isCategory;
    
    public Task(String title, Collaborator owner){
        this.title = title;
        collaborators.add(owner);
        isCategory = false;
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
    
    /**
     * Adds progress in minutes for the Collaborator collaborator.
     * 
     * @param int progress to add
     * @param Collaborator the collaborator who contributed
     */
    public void addProgress(int progress, Collaborator collaborator){
    	// Forcing collaborator = LocalUser
    	// int pos = collaborators.indexOf(collaborator);
    	// collaboratorTime[pos] += progress;
    	collaboratorTime[0] += progress;
    	if(collaboratorTime[0] == estimatedTime){
    		finished = true;
    	}
    }  
    
    public String toString() {
    	String s = "-Task Title: " + title + ", description: " + description + ", estimatedTime: " + estimatedTime + "-";
    	return s;
    }

}
