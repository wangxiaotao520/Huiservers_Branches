package com.huacheng.libraryservice.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class ScrollviewListView extends ListView{

	int mLastMotionY;
	boolean bottomFlag;

	public ScrollviewListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		getParent().requestDisallowInterceptTouchEvent(true);

		return super.dispatchTouchEvent(ev);
	}

}
