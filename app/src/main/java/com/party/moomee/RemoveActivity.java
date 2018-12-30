package com.party.moomee;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Map;

public class RemoveActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RemoveItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.feed_button);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.rv_list_available_for_removing);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Food").document("Jhpoaa8Vqw731S8rtfUE");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        mAdapter = new RemoveItemAdapter(FirestoreUtils.getArrayListWithoutZero(documentSnapshot));
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        mRecyclerView.setHasFixedSize(true);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(mAdapter);
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
                    mAdapter = new RemoveItemAdapter(FirestoreUtils.getArrayListWithoutZero(documentSnapshot));
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Log.d("MooMee", "Current data: null");
                }
            }
        });

        Button confirmButton = findViewById(R.id.bt_submit_removing);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        if(RemoveItemAdapter.getAmountToRemove().isEmpty()) {
            Intent intent = new Intent(RemoveActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this,"ไม่มีการลบรายการอาหาร",Toast.LENGTH_SHORT).show();
        } else {
             DialogFragment dialogFragment = new ConfirmRemovingDialogFragment();
             dialogFragment.show(getSupportFragmentManager(),"confirmRemoving");
        }
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
