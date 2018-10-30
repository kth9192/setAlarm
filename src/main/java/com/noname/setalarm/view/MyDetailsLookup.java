package com.noname.setalarm.view;

import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

final class MyDetailsLookup extends ItemDetailsLookup {

    private final RecyclerView mRecyclerView;

    MyDetailsLookup(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public ItemDetails getItemDetails(MotionEvent e) {
        View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
//        if (view != null) {
//            RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
//            if (holder instanceof ClockAdapter.ClockViewHodler) {
//                return ((ClockAdapter.ClockViewHodler) holder).getClockItemDetails(e);
//            }
//        }
        return null;
    }
}

