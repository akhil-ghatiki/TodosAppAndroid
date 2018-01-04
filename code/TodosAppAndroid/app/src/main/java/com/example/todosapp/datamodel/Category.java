package com.example.todosapp.datamodel;

import java.util.List;

/**
 * Created by aghatiki on 12/26/2017.
 */

public class Category {

    private String categoryName;
    private List<Item> items;

    public Category(String categoryName, List<Item> items) {
        this.categoryName = categoryName;
        this.items = items;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Item> getItems() {
        return items;
    }

}
