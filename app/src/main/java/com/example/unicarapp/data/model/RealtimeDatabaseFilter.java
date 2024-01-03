package com.example.unicarapp.data.model;

import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class RealtimeDatabaseFilter {
    private final String attributeName;
    private final List<FilterCallback> filters;

    public RealtimeDatabaseFilter(String attributeName) {
        filters = new ArrayList<>();
        this.attributeName = attributeName;
        orderByChild();
    }

    private void orderByChild() {
        filters.add(query -> query.orderByChild(attributeName));
    }

    public void startAt(String value) {
        filters.add(query -> query.startAt(value));
    }

    public void startAt(double value) {
        filters.add(query -> query.startAt(value));
    }

    public void endAt(String value) {
        filters.add(query -> query.endAt(value));
    }

    public void endAt(double value) {
        filters.add(query -> query.endAt(value));
    }

    public Query applyAll(Query query) {

        Query _query = query;

        for (FilterCallback filter: filters) {
            _query = filter.apply(_query);
        }

        return _query;
    }

    public interface FilterCallback {
        Query apply(Query query);
    }
}
