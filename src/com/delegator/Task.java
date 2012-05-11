package com.delegator;

import java.util.ArrayList;
import java.util.Date;

/**
 * Describes a task
 * @author Adam Johansson
 *
 */
public class Task implements Item{
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the deadline
     */
    public Date getDeadline() {
        return deadline;
    }

    /**
     * @param deadline the deadline to set
     */
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    /**
     * @return the finished
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * @param finished the finished to set
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * @return the estimatedTime
     */
    public int getEstimatedTime() {
        return estimatedTime;
    }

    /**
     * @param estimatedTime the estimatedTime to set
     */
    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
    
    /**
     * @return an array storing collaborators spent time
     */
    public int[] getCollaboratorTime(){
        return collaboratorTime.clone();
    }
    
    /**
     * @param newCollaboratorTime the new array for collaborators spent time
     */
    public void setCollaboratorTime(int[] newCollaboratorTime) {
        collaboratorTime = newCollaboratorTime;
    }

    private String title;
    private String description;
    private Date deadline;
    private ArrayList<Collaborator> collaborators = new ArrayList<Collaborator>();
    private int[] collaboratorTime = new int[1];
    boolean finished = false;
    public int category; //UNUSED
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
    
    /**
     * Gets the progress of Collaborator c.
     * 
     * @param c the collaborator you want the progress of
     */
    public int getProgressOf(String collaborator){
        //TODO Make sane method
        return collaboratorTime[0];
    }
    
    public String toString() {
    	String s = "-Task Title: " + title + ", description: " + description + ", estimatedTime: " + estimatedTime + "-";
    	return s;
    }



}
