package com.party.moomee;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.FoodInStockViewHolder> {

    private ArrayList<FoodInStock> foodList;

    public ListAdapter(ArrayList<FoodInStock> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodInStockViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //Log.d("MooMee","onCreateViewHolder called");
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item,viewGroup,false);
        return new FoodInStockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodInStockViewHolder holder, int position) {
        //Log.d("MooMee","onBindViewHolder called");
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        //Log.d("MooMee",foodList.size()+"");
        return foodList.size();
    }

    class FoodInStockViewHolder extends RecyclerView.ViewHolder {

        private TextView mFoodNameTextView, mQuantityLeftTextView, mQualitativeNounTextView;

        public FoodInStockViewHolder(View itemView) {
            super(itemView);
            mFoodNameTextView = itemView.findViewById(R.id.tv_food_name);
            mQuantityLeftTextView = itemView.findViewById(R.id.tv_quantity_left);
            mQualitativeNounTextView = itemView.findViewById(R.id.tv_qualitative_noun);
        }

        public void bind(int listIndex) {
            FoodInStock food = foodList.get(listIndex);
            mFoodNameTextView.setText(food.getFoodName());
            mQuantityLeftTextView.setText(food.getQuantityLeft()+"");
            mQualitativeNounTextView.setText(food.getQualitativeNoun().toString());
        }

    }

}
