package com.delegator;

public class CategoryItem implements Item {
    public String title;
    
    public CategoryItem(String title){
        this.title = title;
    }
    
    @Override
    public boolean isCategory() {
        return true;
    }
}
