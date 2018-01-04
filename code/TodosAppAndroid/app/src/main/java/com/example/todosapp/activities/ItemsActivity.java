package com.example.todosapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todosapp.R;
import com.example.todosapp.adapters.ItemsAdapter;
import com.example.todosapp.properties.ApplicationProperties;
import com.example.todosapp.utilities.RecyclerItemClickUtility;
import com.example.todosapp.utilities.RecyclerItemTouchUtility;

public class ItemsActivity extends AppCompatActivity implements RecyclerItemTouchUtility.RecyclerItemTouchHelperListener {

    private static final int DETAILS_INTENT = 99;

    private RecyclerView m_recycerView;
    private ItemsAdapter m_itemsAdapter;
    private CoordinatorLayout m_coordinatorLayout;
    int m_categorySelected = -1;
    private TextView m_noitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        m_recycerView = findViewById(R.id.recycler_view);
        m_coordinatorLayout = findViewById(R.id.coordinator_layout);
        m_noitems = (TextView) findViewById(R.id.noitems);
        m_categorySelected = ApplicationProperties.getINSTANCE().getSelectedCategory();
        getSupportActionBar().setTitle(ApplicationProperties.getINSTANCE().getCategoryList().get(m_categorySelected).getCategoryName());
        if(ApplicationProperties.getINSTANCE().getCategoryList().get(m_categorySelected).getItems().size() == 0){
            m_noitems.setVisibility(View.VISIBLE);
        }
        m_itemsAdapter = new ItemsAdapter(ItemsActivity.this, ApplicationProperties.getINSTANCE().getCategoryList().get(m_categorySelected).getItems());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        m_recycerView.setLayoutManager(layoutManager);
        m_recycerView.setItemAnimator(new DefaultItemAnimator());
        m_recycerView.setAdapter(m_itemsAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchUtility(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(m_recycerView);
        m_itemsAdapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                ApplicationProperties.getINSTANCE().setMode(0);
                Intent intent = new Intent(ItemsActivity.this,DetailsActivity.class);
                startActivityForResult(intent,DETAILS_INTENT);
            }
        });

        m_recycerView.addOnItemTouchListener(new RecyclerItemClickUtility(getApplicationContext(),m_recycerView, new RecyclerItemClickUtility.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ApplicationProperties.getINSTANCE().setMode(1);
                ApplicationProperties.getINSTANCE().setSelectedItem(position);
                Intent intent = new Intent(ItemsActivity.this,DetailsActivity.class);
                startActivityForResult(intent,DETAILS_INTENT);
            }
        }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            setResult(RESULT_OK);     // Home button to move back.
            finish();
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof ItemsAdapter.MyViewHolder){
            if(direction == ItemTouchHelper.RIGHT){                  // Detecting the right swipe
                m_itemsAdapter.pendingItem(viewHolder.getAdapterPosition(),m_categorySelected);
            }
            else if(direction == ItemTouchHelper.LEFT){              // Detecting the left swipe
                m_itemsAdapter.doneItem(viewHolder.getAdapterPosition(),m_categorySelected);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case DETAILS_INTENT:
                m_itemsAdapter.notifyItemChanged(ApplicationProperties.getINSTANCE().getSelectedItem());
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(ApplicationProperties.getINSTANCE().getCategoryList().get(m_categorySelected).getItems().size() == 0){
            m_noitems.setVisibility(View.VISIBLE);
        }
        else {
            m_noitems.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_itemsAdapter.notifyDataSetChanged();
    }
}
