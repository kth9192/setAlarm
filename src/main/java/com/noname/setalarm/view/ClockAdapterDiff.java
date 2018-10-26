package com.noname.setalarm.view;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noname.setalarm.R;
import com.noname.setalarm.databinding.RecyclerClockItemBinding;
import com.noname.setalarm.model.ClockModel;

public class ClockAdapterDiff extends ListAdapter<ClockModel, ClockAdapterDiff.ClockViewHodler> {

    private static String TAG = ClockAdapterDiff.class.getSimpleName();
    private int selectedID;

    protected ClockAdapterDiff() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ClockViewHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_clock_item, viewGroup,false);
        return new ClockViewHodler(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClockViewHodler viewHodler, int i) {
        viewHodler.getRecyclerClockItemBinding().setObserver(new ClockObserver(getItem(i)));

        if(getItemCount() == 1){
             selectedID = getItem(0).getId();
        }

        viewHodler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, i + " 번 선택");
                selectedID = getItem(i).getId();
            }
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

        public ClockViewHodler(View itemView) {
            super(itemView);
            recyclerClockItemBinding = DataBindingUtil.bind(itemView);
            recyclerClockItemBinding.executePendingBindings();
        }

        public RecyclerClockItemBinding getRecyclerClockItemBinding() {
            return recyclerClockItemBinding;
        }
    }
}
