package com.party.moomee;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ListFragment extends Fragment {

    private ListAdapter mAdapter;
    private RecyclerView mFoodList;
    private LinearLayoutManager mLayoutManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFoodList = view.findViewById(R.id.rv_food_list);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Food").document("Jhpoaa8Vqw731S8rtfUE");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        mAdapter = new ListAdapter(FirestoreUtils.getArrayListWithoutZero(documentSnapshot));
                        mLayoutManager = new LinearLayoutManager(getContext());
                        mFoodList.setHasFixedSize(true);
                        mFoodList.setLayoutManager(mLayoutManager);
                        mFoodList.setAdapter(mAdapter);
                    } else {
                        Log.d("MooMee","Document does not exist");
                    }
                } else {
                    Log.d("MooMee","get failed with " + task.getException());
                }
            }
        });

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("MooMee", "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    mAdapter = new ListAdapter(FirestoreUtils.getArrayListWithoutZero(documentSnapshot));
                    mLayoutManager = new LinearLayoutManager(getContext());
                    mFoodList.setHasFixedSize(true);
                    mFoodList.setLayoutManager(mLayoutManager);
                    mFoodList.setAdapter(mAdapter);
                } else {
                    Log.d("MooMee", "Current data: null");
                }
            }
        });
    }
}
