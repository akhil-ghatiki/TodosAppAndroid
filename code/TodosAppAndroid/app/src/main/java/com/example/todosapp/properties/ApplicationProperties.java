package com.example.todosapp.properties;

import com.example.todosapp.datamodel.Category;
import com.example.todosapp.datamodel.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aghatiki on 12/26/2017.
 */

public class ApplicationProperties {
    private static ApplicationProperties INSTANCE = null;

    private List<Category> categoryList = new ArrayList<>();

    private int selectedCategory = -1;
    private int mode = 0;     // Mode 0 indicates new item, Mode 1 indicates existing item for editing
    private int selectedItem = -1;   // Denotes which item is selected.

    private ApplicationProperties(){
    }

    public static ApplicationProperties getINSTANCE() {
        if(INSTANCE == null){
            INSTANCE = new ApplicationProperties();
        }
        return INSTANCE;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public int getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(int selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }
}
