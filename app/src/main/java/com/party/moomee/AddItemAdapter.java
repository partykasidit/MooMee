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

public class AddItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<FoodInStock> foodList;
    private static Map<String,FoodInStock> amountToAdd;

    public AddItemAdapter(ArrayList<FoodInStock> foodList) {
        this.foodList = foodList;
        amountToAdd = new HashMap<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch(viewType) {
            case 1 :
                View view = inflater.inflate(R.layout.add_or_remove_item,parent,false);
                return new ViewHolder(view);
            case 2 :
                View view2 = inflater.inflate(R.layout.add_or_remove_item_2,parent,false);
                return new ViewHolder2(view2);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch(holder.getItemViewType()) {
            case 1 :
                ViewHolder viewHolder1 = (ViewHolder) holder;
                viewHolder1.bind(position);
                break;
            case 2 :
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                viewHolder2.bind(position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(foodList.get(position).getQualitativeNoun()==null) {
            return 2;
        }
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mFoodNameTextView, mQualitativeNounTextView;
        private Spinner mQuantitySpinner;
        private ArrayAdapter<CharSequence> mSpinnerAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            mFoodNameTextView = itemView.findViewById(R.id.tv_food_name);
            mQualitativeNounTextView = itemView.findViewById(R.id.tv_qualitative_noun);
            mQuantitySpinner = itemView.findViewById(R.id.sp_number_selector);
            mSpinnerAdapter = ArrayAdapter.createFromResource(itemView.getContext(),R.array.integers,R.layout.my_spinner_item);
            mSpinnerAdapter.setDropDownViewResource(R.layout.my_spinner_item);
            mQuantitySpinner.setAdapter(mSpinnerAdapter);
        }

        public void bind(int listIndex) {
            final FoodInStock food = foodList.get(listIndex);
            mFoodNameTextView.setText(food.getFoodName());
            mQualitativeNounTextView.setText(food.getQualitativeNoun().toString());
            mQuantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(position!=0) {
                        amountToAdd.put(food.getFoodName(),new FoodInStock(food.getFoodName(),food.getNameInDatabase(),position,food.getQualitativeNoun()));
                    } else {
                        amountToAdd.remove(food.getFoodName());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {

        TextView mFoodNameTextView;
        Spinner mQuantitySpinner, mQualitativeNounSpinner;
        ArrayAdapter<CharSequence> mQuantitySpinnerAdapter, mQualitativeNounSpinnerAdapter;

        public ViewHolder2(View itemView) {
            super(itemView);
            mFoodNameTextView = itemView.findViewById(R.id.tv_food_name_2);
            mQuantitySpinner = itemView.findViewById(R.id.sp_number_selector);
            mQualitativeNounSpinner = itemView.findViewById(R.id.sp_qualitative_noun_selector);
            mQuantitySpinnerAdapter = ArrayAdapter.createFromResource(itemView.getContext(),R.array.integers,R.layout.my_spinner_item);
            mQuantitySpinnerAdapter.setDropDownViewResource(R.layout.my_spinner_item);
            mQualitativeNounSpinnerAdapter = ArrayAdapter.createFromResource(itemView.getContext(),R.array.qualitativeNoun,R.layout.my_spinner_item);
            mQualitativeNounSpinnerAdapter.setDropDownViewResource(R.layout.my_spinner_item);
            mQuantitySpinner.setAdapter(mQuantitySpinnerAdapter);
            mQualitativeNounSpinner.setAdapter(mQualitativeNounSpinnerAdapter);
        }

        public void bind(int listIndex) {
            final FoodInStock food = foodList.get(listIndex);
            mFoodNameTextView.setText(food.getFoodName());
            mQuantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    /*if(position!=0) {
                        amountToAdd.put(food.getFoodName(),new FoodInStock(food.getFoodName(),food.getNameInDatabase(),position,food.getQualitativeNoun()));
                    } else {
                        amountToAdd.remove(food.getFoodName());
                    }*/
                    if(amountToAdd.containsKey(food.getFoodName())) {
                        FoodInStock foodInStock = amountToAdd.get(food.getFoodName());
                        foodInStock.setQuantityLeft(position);
                        amountToAdd.replace(food.getFoodName(),foodInStock);
                    } else {
                        amountToAdd.put(food.getFoodName(),new FoodInStock(food.getFoodName(),position, FoodInStock.QualitativeNoun.ถุง));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            mQualitativeNounSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 0) {
                        if(amountToAdd.containsKey(food.getFoodName())) {
                            FoodInStock foodInStock = amountToAdd.get(food.getFoodName());
                            foodInStock.setQualitativeNoun(FoodInStock.QualitativeNoun.ถุง);
                            amountToAdd.put(food.getFoodName(),foodInStock);
                        } else {
                            amountToAdd.put(food.getFoodName(),new FoodInStock(food.getFoodName(),0, FoodInStock.QualitativeNoun.ถุง));
                        }
                    } else if(position == 1) {
                        if(amountToAdd.containsKey(food.getFoodName())) {
                            FoodInStock foodInStock = amountToAdd.get(food.getFoodName());
                            foodInStock.setQualitativeNoun(FoodInStock.QualitativeNoun.ไม้);
                            amountToAdd.put(food.getFoodName(),foodInStock);
                        } else {
                            amountToAdd.put(food.getFoodName(),new FoodInStock(food.getFoodName(),0, FoodInStock.QualitativeNoun.ไม้));
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public static Map<String, FoodInStock> getAmountToAdd() {
        return amountToAdd;
    }
}
