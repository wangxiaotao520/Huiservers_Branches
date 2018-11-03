package com.huacheng.huiservers.fragment.adapter;


import android.support.v7.widget.CardView;

public interface HomeCardAdapter {

    int MAX_ELEVATION_FACTOR = 3;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
