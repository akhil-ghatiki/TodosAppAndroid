package com.example.todosapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.todosapp.R;
import com.example.todosapp.datamodel.Category;

import java.util.List;

/**
 * Created by aghatiki on 12/26/2017.
 */

public class CategoryAdapter extends ArrayAdapter<Category>{
    private Activity m_activity;
    public CategoryAdapter(Activity activity, List<Category> data) {
        super(activity, R.layout.list_item,data);
        m_activity = activity;
    }

    @NonNull
    @Override
    public View getView(int i, @Nullable View view, @NonNull ViewGroup viewGroup) {
        final Category category = getItem(i);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, viewGroup, false);
        }

        TextView title = (TextView) view.findViewById(R.id.list_item_category);
        TextView count = (TextView) view.findViewById(R.id.list_item_count);

        title.setText(category.getCategoryName());
        count.setText(category.getItems().size()+" items");
        return view;
    }
}
