package com.example.unicarapp.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreRepository<T> {
    private final FirebaseFirestore firestore;
    private final String collectionName;

    public FirestoreRepository(String collectionName) {
        this.firestore = FirebaseFirestore.getInstance();
        this.collectionName = collectionName;
    }

    public MutableLiveData<T> getDocumentData(String documentId, Class<T> type) {
        MutableLiveData<T> resultLiveData = new MutableLiveData<>();

        firestore.collection(collectionName)
                .document(documentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            T result = document.toObject(type);
                            resultLiveData.setValue(result);
                        } else {
                            resultLiveData.setValue(null);
                        }
                    } else {
                        resultLiveData.setValue(null);
                    }
                });

        return resultLiveData;
    }
}
