package com.delegator;

public class CategoryItem implements Item {
    public String title;
    public Boolean isCategory;
    
    public CategoryItem(String title){
        this.title = title;
        isCategory = true;
    }
    
    @Override
    public boolean isCategory() {
        return true;
    }
    
    public String toString() {
    	String s = "-Category Title: " + title + "-";
    	return s;
    }
}
