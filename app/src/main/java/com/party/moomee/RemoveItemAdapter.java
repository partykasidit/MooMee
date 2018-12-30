package com.party.moomee;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemoveItemAdapter extends RecyclerView.Adapter<RemoveItemAdapter.ViewHolder> {

    private ArrayList<FoodInStock> foodList;
    private static Map<String,FoodInStock> amountToRemove;

    public RemoveItemAdapter(ArrayList<FoodInStock> foodList) {
        this.foodList = foodList;
        amountToRemove = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.add_or_remove_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mFoodNameTextView, mQualitativeNounTextView;
        private Spinner mQuantitySpinner;
        private ArrayAdapter<String> mSpinnerAdapter;
        private View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mFoodNameTextView = itemView.findViewById(R.id.tv_food_name);
            mQuantitySpinner = itemView.findViewById(R.id.sp_number_selector);
            mQualitativeNounTextView = itemView.findViewById(R.id.tv_qualitative_noun);
            mView = itemView;
        }

        public void bind(int listIndex) {
            final FoodInStock food = foodList.get(listIndex);
            mFoodNameTextView.setText(food.getFoodName());
            mQualitativeNounTextView.setText(food.getQualitativeNoun().toString());

            mSpinnerAdapter = new ArrayAdapter<>(mView.getContext(),R.layout.my_spinner_item,getAvailableNumbers(food.getQuantityLeft()));
            mSpinnerAdapter.setDropDownViewResource(R.layout.my_spinner_item);
            mQuantitySpinner.setAdapter(mSpinnerAdapter);
            mQuantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position!=0) {
                        amountToRemove.put(food.getNameInDatabase(),new FoodInStock(food.getFoodName(),food.getNameInDatabase(),position/2.0,food.getQualitativeNoun()));
                    } else {
                        amountToRemove.remove(food.getNameInDatabase());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        private ArrayList<String> getAvailableNumbers(double quantityLeft) {
            ArrayList<String> availableNumbers = new ArrayList<>();
            for(double i=0;i<=quantityLeft;i=i+0.5) {
                if(i==Math.floor(i)) availableNumbers.add((int)i+"");
                else availableNumbers.add(i+"");

            }
            return availableNumbers;
        }

    }

    public static Map<String,FoodInStock> getAmountToRemove() {
        Log.d("MooMee",amountToRemove.toString());

        ArrayList<String> foodThatCanChooseQualitativeNoun = new ArrayList<>();
        foodThatCanChooseQualitativeNoun.add("ตับ");

        for(String food : foodThatCanChooseQualitativeNoun) {
            if(amountToRemove.containsKey(food)) {
                FoodInStock foodInStock = amountToRemove.remove(food);
                String newKey = foodInStock.getFoodName() + "-" + foodInStock.getQualitativeNounAsString();
                amountToRemove.put(newKey,foodInStock);
            }
        }
        return amountToRemove;
    }

}
