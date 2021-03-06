package com.sicco.erp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class VerticalScrollViewCustomized extends ScrollView{

	public VerticalScrollViewCustomized(Context context){
		super(context);
	}
	
	 public VerticalScrollViewCustomized(Context context, AttributeSet attrs) {
         super(context, attrs);
     }

     public VerticalScrollViewCustomized(Context context, AttributeSet attrs, int defStyle) {
         super(context, attrs, defStyle);
     }
     
     @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	 final int action = ev.getAction();
         switch (action)
         {
             case MotionEvent.ACTION_DOWN:
                     Log.i("DungHV", "onInterceptTouchEvent: DOWN super false" );
                     super.onTouchEvent(ev);
                     break;

             case MotionEvent.ACTION_MOVE:
                     return false; // redirect MotionEvents to ourself

             case MotionEvent.ACTION_CANCEL:
                     Log.i("DungHV", "onInterceptTouchEvent: CANCEL super false" );
                     super.onTouchEvent(ev);
                     break;

             case MotionEvent.ACTION_UP:
                     Log.i("DungHV", "onInterceptTouchEvent: UP super false" );
                     return false;

             default: Log.i("DungHV", "onInterceptTouchEvent: " + action ); break;
         }

    	return false;
    }
     @Override
     public boolean onTouchEvent(MotionEvent ev) {
         super.onTouchEvent(ev);
         Log.i("VerticalScrollview", "onTouchEvent. action: " + ev.getAction() );
          return true;
     }
}
