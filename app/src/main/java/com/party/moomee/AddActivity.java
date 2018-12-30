package com.party.moomee;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    private Spinner ข้าวถุงเล็ก_Spinner, ข้าวถุงใหญ่_Spinner;
    private ConstraintLayout เพิ่มข้าวถุงใหญ่, เพิ่มข้าวถุงเล็ก;
    private static Map<String,FoodInStock> amountToAdd;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("บันทึกรายการอาหาร");
        actionBar.setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.integers, R.layout.my_spinner_item);
        adapter.setDropDownViewResource(R.layout.my_spinner_item);

        amountToAdd = new HashMap<>();

        เพิ่มข้าวถุงเล็ก = findViewById(R.id.เพิ่มข้าวถุงเล็ก);
        TextView ข้าวถุงเล็ก_TextView = เพิ่มข้าวถุงเล็ก.findViewById(R.id.tv_food_name);
        ข้าวถุงเล็ก_Spinner = เพิ่มข้าวถุงเล็ก.findViewById(R.id.sp_number_selector);
        ข้าวถุงเล็ก_TextView.setText("ข้าวถุงเล็ก");
        ข้าวถุงเล็ก_Spinner.setAdapter(adapter);
        ข้าวถุงเล็ก_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    amountToAdd.put("ข้าวถุงเล็ก",new FoodInStock("ข้าวถุงเล็ก",position, FoodInStock.QualitativeNoun.ถุง));
                } else {
                    amountToAdd.remove("ข้าวถุงเล็ก");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        เพิ่มข้าวถุงใหญ่ = findViewById(R.id.เพิ่มข้าวถุงใหญ่);
        TextView ข้าวถุงใหญ่_TextView = เพิ่มข้าวถุงใหญ่.findViewById(R.id.tv_food_name);
        ข้าวถุงใหญ่_TextView.setText("ข้าวถุงใหญ่");
        ข้าวถุงใหญ่_Spinner = เพิ่มข้าวถุงใหญ่.findViewById(R.id.sp_number_selector);
        ข้าวถุงใหญ่_Spinner.setAdapter(adapter);
        ข้าวถุงใหญ่_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    amountToAdd.put("ข้าวถุงใหญ่",new FoodInStock("ข้าวถุงใหญ่",position, FoodInStock.QualitativeNoun.ถุง));
                } else {
                    amountToAdd.remove("ข้าวถุงใหญ่");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<FoodInStock> foodList = new ArrayList<>();
        foodList.add(new FoodInStock("ไก่สับ","ไก่สับ",0, FoodInStock.QualitativeNoun.ถุง));
        foodList.add(new FoodInStock("ไก่บด","ไก่บด",0, FoodInStock.QualitativeNoun.ถุง));
        foodList.add(new FoodInStock("ตับ",0));
        //foodList.add(new FoodInStock("อาหารกระป๋อง","อาหารกระป๋อง",0, FoodInStock.QualitativeNoun.กระป๋อง));

        mRecyclerView = findViewById(R.id.rv_add_food);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(layoutManager);
        AddItemAdapter addItemAdapter = new AddItemAdapter(foodList);
        mRecyclerView.setAdapter(addItemAdapter);

        Button confirmButton = findViewById(R.id.bt_submit_addition);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

    }

    private void submit() {
        if(getAddingAmount().isEmpty()) {
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this,"ไม่มีการเพิ่มรายการอาหาร",Toast.LENGTH_SHORT).show();
        }else {
            DialogFragment dialogFragment = new ConfirmAddingDialogFragment();
            dialogFragment.show(getSupportFragmentManager(),"confirmAdding");
        }
    }

    public static Map<String, FoodInStock> getAddingAmount() {

        Map<String, FoodInStock> totalAmountToAdd = new HashMap<>();
        totalAmountToAdd.putAll(amountToAdd);
        totalAmountToAdd.putAll(AddItemAdapter.getAmountToAdd());

        Iterator<String> iterator = totalAmountToAdd.keySet().iterator();

        while(iterator.hasNext()) {
            String key = iterator.next();
            if(totalAmountToAdd.get(key).getQuantityLeft() == 0) {
                iterator.remove();
            }
        }

        ArrayList<String> foodThatCanChooseQualitativeNoun = new ArrayList<>();
        foodThatCanChooseQualitativeNoun.add("ตับ");

        for(String food : foodThatCanChooseQualitativeNoun) {
            if(totalAmountToAdd.containsKey(food)) {
                FoodInStock foodInStock = totalAmountToAdd.remove(food);
                String newKey = foodInStock.getFoodName() + "-" + foodInStock.getQualitativeNounAsString();
                totalAmountToAdd.put(newKey,foodInStock);
            }
        }

        Log.d("MooMee-AA",totalAmountToAdd.toString());

        return totalAmountToAdd;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.slide_in,R.anim.right_slide_out);
    }
}

