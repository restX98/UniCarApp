package com.example.unicarapp.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.unicarapp.data.model.RealtimeDatabaseFilter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RealtimeDatabaseRepository<T> {
    private final DatabaseReference databaseReference;
    private final String collectionName;

    public RealtimeDatabaseRepository(String collectionName) {
        this.databaseReference = FirebaseDatabase.getInstance().getReference(collectionName);
        this.collectionName = collectionName;
    }

    public MutableLiveData<List<T>> getAllData(Class<T> type) {
        return _getAllData(databaseReference, type);
    }

    public MutableLiveData<List<T>> getAllData(Class<T> type, RealtimeDatabaseFilter filter) {
        Query query = filter.applyAll(databaseReference);
        return _getAllData(query, type);
    }

    private MutableLiveData<List<T>> _getAllData(Query query, Class<T> type) {
        MutableLiveData<List<T>> dataListLiveData = new MutableLiveData<>();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<T> data = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    T item = dataSnapshot.getValue(type);
                    data.add(item);
                }
                dataListLiveData.setValue(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dataListLiveData.setValue(null);
            }
        });

        return dataListLiveData;
    }
}
