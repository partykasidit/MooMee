package com.party.moomee;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class FirestoreUtils {

    public static final int ADD = 1, REMOVE = 2;

    public static ArrayList<FoodInStock> getArrayList(DocumentSnapshot documentSnapshot) {
        ArrayList<FoodInStock> foodList = new ArrayList<>();
        for(Object object : documentSnapshot.getData().keySet()) {
            String key = (String) object;
            HashMap<String,Object> food = (HashMap<String, Object>) documentSnapshot.get(key);
            foodList.add(getFoodInStock(food));
        }
        return foodList;
    }

    public static ArrayList<FoodInStock> getArrayListWithoutZero(DocumentSnapshot documentSnapshot) {
        ArrayList<FoodInStock> foodList = new ArrayList<>();
        for(Object object : documentSnapshot.getData().keySet()) {
            String key = (String) object;
            HashMap<String,Object> food = (HashMap<String, Object>) documentSnapshot.get(key);
            FoodInStock foodInStock = getFoodInStock(food);
            if(foodInStock.getQuantityLeft()!=0) {
                foodList.add(getFoodInStock(food));
            }
        }
        return foodList;
    }

    public static FoodInStock getFoodInStock(HashMap<String,Object> food) {
        //Log.d("MooMee-FU",food.toString());
        return new FoodInStock((String)food.get("foodName"), Double.valueOf(food.get("quantityLeft").toString()), FoodInStock.QualitativeNoun.valueOf(food.get("qualitativeNoun").toString()));
    }

    public static HashMap<String,Object> getHashMap(FoodInStock foodInStock) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("foodName",foodInStock.getFoodName());
        map.put("quantityLeft",foodInStock.getQuantityLeft());
        map.put("qualitativeNoun",foodInStock.getQualitativeNounAsString());
        return map;
    }


    public static FoodInStock addFood(FoodInStock currentFood,FoodInStock foodToAdd) {
        //may cause the original one to change
        currentFood.addAmount(foodToAdd.getQuantityLeft());
        return currentFood;
    }

    public static FoodInStock removeFood(FoodInStock currentFood,FoodInStock foodToRemove) {
        //may cause the original one to change
        currentFood.removeAmount(foodToRemove.getQuantityLeft());
        return currentFood;
    }

    public static void saveToDatabase(final Map<String,FoodInStock> amountToAddOrRemove, final int addOrRemove) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.collection("Food").document("Jhpoaa8Vqw731S8rtfUE");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {

                        Map<String,Object> currentAmount = documentSnapshot.getData();

                        switch (addOrRemove) {
                            case ADD :
                                for(Map.Entry entry : amountToAddOrRemove.entrySet()) {
                                    FoodInStock foodToAdd = (FoodInStock) entry.getValue();
                                    FoodInStock currentFood = FirestoreUtils.getFoodInStock((HashMap<String, Object>) currentAmount.get(entry.getKey()));
                                    currentAmount.put((String) entry.getKey(),FirestoreUtils.getHashMap(FirestoreUtils.addFood(currentFood,foodToAdd)));
                                }
                                break;
                            case REMOVE :
                                for(Map.Entry entry : amountToAddOrRemove.entrySet()) {
                                    FoodInStock foodToAdd = (FoodInStock) entry.getValue();
                                    FoodInStock currentFood = FirestoreUtils.getFoodInStock((HashMap<String, Object>) currentAmount.get(entry.getKey()));
                                    currentAmount.put((String) entry.getKey(),FirestoreUtils.getHashMap(FirestoreUtils.removeFood(currentFood,foodToAdd)));
                                }
                                break;
                        }

                        documentReference.set(currentAmount);

                    } else {
                        Log.d("MooMee", "No such document");
                    }
                } else {
                    Log.d("MooMee", "get failed with ", task.getException());
                }
            }
        });
        final DocumentReference documentReference2 = db.collection("Food").document("Date");
        documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {

                        Map<String,Object> currentData = documentSnapshot.getData();

                        switch(addOrRemove) {
                            case ADD :
                                for(Map.Entry entry : amountToAddOrRemove.entrySet()) {
                                    for(Map.Entry entryOfCurrentData : currentData.entrySet()) {
                                        if(entry.getKey().equals(entryOfCurrentData.getKey())) {
                                            FoodInStock food = (FoodInStock) entry.getValue();
                                            Map<String,Object> map = (Map<String, Object>) entryOfCurrentData.getValue();
                                            Date date = new Date();
                                            String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(date);
                                            map.put(dateString,((FoodInStock) entry.getValue()).getQuantityLeft());
                                            entryOfCurrentData.setValue(map);
                                        }
                                    }
                                }
                                break;
                            case REMOVE :
                                for(Map.Entry entry : amountToAddOrRemove.entrySet()) {
                                    for(Map.Entry entryOfCurrentData : currentData.entrySet()) {
                                        if(entry.getKey().equals(entryOfCurrentData.getKey())) {
                                            FoodInStock food = (FoodInStock) entry.getValue();
                                            double amountToRemove = food.getQuantityLeft();
                                            Map<String,Double> map = (Map<String, Double>) entryOfCurrentData.getValue();
                                            TreeMap<String,Double> treeMap = new TreeMap<>();
                                            treeMap.putAll(map);
                                            while(amountToRemove > 0) {
                                                Map.Entry<String,Double> firstEntry = treeMap.firstEntry();
                                                double firstValue = firstEntry.getValue();
                                                if(firstValue > amountToRemove) {
                                                    firstValue = firstValue - amountToRemove;
                                                    treeMap.put(firstEntry.getKey(),firstValue);
                                                    amountToRemove = 0;
                                                } else {
                                                    treeMap.remove(firstEntry.getKey());
                                                    amountToRemove -= firstValue;
                                                }
                                            }
                                            entryOfCurrentData.setValue(treeMap);
                                        }
                                    }
                                }
                        }

                        documentReference2.set(currentData);

                    }
                }
            }
        });
    }

    public static String getMessage(Map<String,FoodInStock> amount) {
        String message = "";
        for(FoodInStock food : amount.values()) {
            message +=  food.getFoodName() + " " + food.getQuantityLeft() + " " + food.getQualitativeNoun() + "\n";
        }
        return message;
    }
}
