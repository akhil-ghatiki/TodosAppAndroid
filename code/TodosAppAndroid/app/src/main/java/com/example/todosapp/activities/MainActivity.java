package com.example.todosapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todosapp.R;
import com.example.todosapp.adapters.CategoryAdapter;
import com.example.todosapp.datamodel.Category;
import com.example.todosapp.datamodel.Item;
import com.example.todosapp.properties.ApplicationProperties;
import com.example.todosapp.sharedPreferences.SharedValues;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView m_listView;
    CategoryAdapter m_categoryAdapter;
    EditText m_category;
    List<Category> m_categoryList = new ArrayList<>();
    TextView m_noCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.categories));
        ApplicationProperties.getINSTANCE().getCategoryList().clear();
        SharedValues.getINSTANCE().setContext(getApplicationContext());
        if(SharedValues.getINSTANCE().getCategoryList() != null) {
            ApplicationProperties.getINSTANCE().getCategoryList().addAll(SharedValues.getINSTANCE().getCategoryList());
        }
        m_listView = (ListView) findViewById(R.id.main_list);
        m_categoryAdapter = new CategoryAdapter(MainActivity.this,ApplicationProperties.getINSTANCE().getCategoryList());
        m_listView.setAdapter(m_categoryAdapter);
        m_categoryAdapter.notifyDataSetChanged();
        m_noCategories = (TextView) findViewById(R.id.nocategories);
        if(ApplicationProperties.getINSTANCE().getCategoryList().size() == 0){
            m_noCategories.setVisibility(View.VISIBLE);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_category);
                final EditText category = (EditText) dialog.findViewById(R.id.category_name);
                Button add = (Button) dialog.findViewById(R.id.category_add);
                Button cancel = (Button) dialog.findViewById(R.id.category_cancel);
                final TextView error = (TextView) dialog.findViewById(R.id.error);
                error.setVisibility(View.GONE);
                dialog.show();
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean flag = true;
                        String catName = category.getText().toString();
                        if (catName.isEmpty()) {
                            Toast.makeText(MainActivity.this,getString(R.string.categoryEmpty), Toast.LENGTH_SHORT).show();
                        } else {
                            for (Category cat : ApplicationProperties.getINSTANCE().getCategoryList()) {
                                if (catName.equals(cat.getCategoryName())) {
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag) {
                                List<Item> items = new ArrayList<>();
                                Category cat = new Category(category.getText().toString(), items);
                                ApplicationProperties.getINSTANCE().getCategoryList().add(cat);
                                m_categoryAdapter = new CategoryAdapter(MainActivity.this, ApplicationProperties.getINSTANCE().getCategoryList());
                                m_listView.setAdapter(m_categoryAdapter);
                                m_categoryAdapter.notifyDataSetChanged();
                                m_noCategories.setVisibility(View.GONE);
                                dialog.dismiss();
                            } else {
                                error.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        m_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,ItemsActivity.class);
                ApplicationProperties.getINSTANCE().setSelectedCategory(i);
                startActivityForResult(intent,30);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 30:
                m_categoryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedValues.getINSTANCE().setCategoryList(ApplicationProperties.getINSTANCE().getCategoryList());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedValues.getINSTANCE().setCategoryList(ApplicationProperties.getINSTANCE().getCategoryList());
        finish();
    }
}
