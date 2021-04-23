package com.finddreams.baselib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @Description:自定义无滚动条的ListView
 * @author blin
 * @since 2015年12月14日
 */ 
public class NoScrollListView extends ListView {
 	       
	    public NoScrollListView(Context context)    
	    {   
	        super(context);    
	    }   
	       
	    public NoScrollListView(Context context, AttributeSet attrs)    
	    {   
	        super(context, attrs);   
	    }   
	       
	    public NoScrollListView(Context context, AttributeSet attrs, int defStyle)    
	    {   
	        super(context, attrs, defStyle);     
	    }   
	    @Override 
	    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

	        int expandSpec = MeasureSpec.makeMeasureSpec( 
	                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
	        super.onMeasure(widthMeasureSpec, expandSpec); 
	    }     
}
