package com.example.unicarapp.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreRepository<T> {
    private final FirebaseFirestore firestore;
    private final String collectionName;

    public FirestoreRepository(String collectionName) {
        this.firestore = FirebaseFirestore.getInstance();
        this.collectionName = collectionName;
    }

    public void addDocument(String documentId, T data, FirestoreCallback firestoreCallback) {
        firestore.collection(collectionName)
                .document(documentId)
                .set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onCompleteHandler(task, firestoreCallback);
                    }
                });
    }

    public void addDocument(T data, FirestoreCallback firestoreCallback) {
        firestore.collection(collectionName)
                .add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        onCompleteHandler(task, firestoreCallback);
                    }
                });
    }

    private void onCompleteHandler(Task<?> task, FirestoreCallback firestoreCallback) {
        if (task.isSuccessful()) {
            firestoreCallback.onLoadSuccess();
        } else {
            firestoreCallback.onLoadFailure("Something went wrong!");
        }
    }

    public MutableLiveData<List<T>> getAllDocumentData(Class<T> type) {
        MutableLiveData<List<T>> documentListLiveData = new MutableLiveData<List<T>>();

        firestore.collection(collectionName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<T> documents = new ArrayList<>();

                        if (task.isSuccessful()) {
                            QuerySnapshot queryDocumentSnapshots = task.getResult();
                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                                T document = documentSnapshot.toObject(type);
                                documents.add(document);
                            }
                        }
                        documentListLiveData.setValue(documents);
                    }
                });

        return documentListLiveData;
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

    public interface FirestoreCallback {
        void onLoadSuccess();
        void onLoadFailure(String errorMessage);
    }
}
