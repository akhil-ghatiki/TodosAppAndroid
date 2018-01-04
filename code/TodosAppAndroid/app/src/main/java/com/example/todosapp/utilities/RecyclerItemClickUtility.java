package com.example.todosapp.utilities;

import android.content.Context;
import android.gesture.Gesture;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.todosapp.activities.ItemsActivity;

/**
 * Created by aghatiki on 12/27/2017.
 */

public class RecyclerItemClickUtility implements RecyclerView.OnItemTouchListener {

    public interface ClickListener{
        void onClick(View view, int position);
    }

    private GestureDetector gestureDetector;
    private ClickListener clickListener;

    public RecyclerItemClickUtility(Context context,final RecyclerView recyclerView,final ClickListener clickListener){
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
/*
            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(child != null && clickListener != null){
                    clickListener.onLongClick(child,recyclerView.getChildAdapterPosition(child));
                }
            }*/
        });
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(),e.getY());
        if(child != null && clickListener != null && gestureDetector.onTouchEvent(e)){
            clickListener.onClick(child,rv.getChildPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
