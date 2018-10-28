package com.noname.setalarm.view;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.noname.setalarm.R;
import com.noname.setalarm.databinding.RecyclerClockItemBinding;
import com.noname.setalarm.model.ClockModel;

import java.util.ArrayList;
import java.util.List;

public class ClockAdapterDiff extends ListAdapter<ClockModel, ClockAdapterDiff.ClockViewHodler> {

    private static String TAG = ClockAdapterDiff.class.getSimpleName();
    private int selectedID = -1;
    private Context context;

    private OnClockClickListener clockClickListener;

    public void setClockClickListener(OnClockClickListener clockClickListener) {
        this.clockClickListener = clockClickListener;
    }

    protected ClockAdapterDiff(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public ClockViewHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == getItemCount()-1) {
            selectedID = getItem(i).getId();
        }
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_clock_item, viewGroup,false);
        return new ClockViewHodler(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ClockViewHodler viewHodler, int i) {

        viewHodler.getRecyclerClockItemBinding().setObserver(new ClockObserver(getItem(i)));

        if (selectedID == getItem(i).getId()) {
            viewHodler.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_background));
            viewHodler.getRecyclerClockItemBinding().hour.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            viewHodler.getRecyclerClockItemBinding().minute.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            viewHodler.getRecyclerClockItemBinding().divider.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        }else {
            viewHodler.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.default_background));
            viewHodler.getRecyclerClockItemBinding().hour.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            viewHodler.getRecyclerClockItemBinding().minute.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            viewHodler.getRecyclerClockItemBinding().divider.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }

        viewHodler.itemView.setOnClickListener(v -> {
//            Log.d(TAG, i + " 번 선택");
            selectedID = getItem(i).getId();

            if (clockClickListener != null){
                clockClickListener.OnClockItemClick(getItem(i).getHour(), getItem(i).getMinute());
            }
            notifyDataSetChanged();
        });
    }

    public static final DiffUtil.ItemCallback<ClockModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ClockModel>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull ClockModel oldModel, @NonNull ClockModel newModel) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return (oldModel.getId() == newModel.getId());
                }
                @Override
                public boolean areContentsTheSame(
                        @NonNull ClockModel oldModel, @NonNull ClockModel newModel) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldModel.equals(newModel);
                }
            };

    public int getSelectedID(){
        return selectedID;
    }

    static class ClockViewHodler extends RecyclerView.ViewHolder{

        private RecyclerClockItemBinding recyclerClockItemBinding;

//        private MyItemDetail myItemDetail;
//
//        public void setMyItemDetail(MyItemDetail myItemDetail) {
//            this.myItemDetail = myItemDetail;
//        }

        public ClockViewHodler(View itemView) {
            super(itemView);
            recyclerClockItemBinding = DataBindingUtil.bind(itemView);
            recyclerClockItemBinding.executePendingBindings();
        }

        public RecyclerClockItemBinding getRecyclerClockItemBinding() {
            return recyclerClockItemBinding;
        }

//        public ItemDetailsLookup.ItemDetails<String> getClockItemDetails(@NonNull MotionEvent motionEvent) {
//            return myItemDetail;
//        }
    }

//    public class MyItemDetail extends ItemDetailsLookup.ItemDetails {
//        private final int adapterPosition;
//        private final int selectionKey;
//
//        public MyItemDetail(int adapterPosition, ClockModel clockModel) {
//            this.adapterPosition = adapterPosition;
//            this.selectionKey = clockModel.getId();
//        }
//
//        @Override
//        public int getPosition() {
//            return adapterPosition;
//        }
//
//        @Nullable
//        @Override
//        public Object getSelectionKey() {
//            return selectionKey;
//        }
//
//        @Override
//        public boolean inSelectionHotspot(@NonNull MotionEvent e) {
//            return true;
//        }
//
//        @Override
//        public boolean inDragRegion(@NonNull MotionEvent e) {
//            return true;
//        }
//    }
//
//    interface ClockClickListener {
//        void onCLick(ClockModel clockModel);
//    }
}
