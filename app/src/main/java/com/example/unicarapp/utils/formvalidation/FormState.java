package com.example.unicarapp.utils.formvalidation;

import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FormState {
    private Map<Integer, FormFieldState> fieldStates = new HashMap<>();
    private MutableLiveData<FormState> formStateLiveData = new MutableLiveData<>();

    public MutableLiveData<FormState> getFormStateLiveData() {
        return formStateLiveData;
    }

    public FormFieldState addField(int fieldId) {
        if (!fieldStates.containsKey(fieldId)) {
            fieldStates.put(fieldId, new FormFieldState(this));
        }
        return getFieldState(fieldId);
    }

    public FormFieldState addField(int fieldId, Pattern regexPattern) {
        if (!fieldStates.containsKey(fieldId)) {
            fieldStates.put(fieldId, new FormFieldState(this, regexPattern));
        }
        return getFieldState(fieldId);
    }

    public FormFieldState addField(int fieldId, Pattern regexPattern, FormFieldState.ValidationCallback validationCallback) {
        if (!fieldStates.containsKey(fieldId)) {
            fieldStates.put(fieldId, new FormFieldState(this, regexPattern, validationCallback));
        }
        return getFieldState(fieldId);
    }

    public FormFieldState getFieldState(int fieldId) {
        if (!fieldStates.containsKey(fieldId)) {
            return null;
        }
        return fieldStates.get(fieldId);
    }

    public boolean isFormValid() {
        for (FormFieldState fieldState : fieldStates.values()) {
            if (fieldState.getStatus() != FormFieldState.Status.VALID) {
                return false;
            }
        }
        return true;
    }

    public void updateLiveData() {
        formStateLiveData.setValue(this);
    }
}
