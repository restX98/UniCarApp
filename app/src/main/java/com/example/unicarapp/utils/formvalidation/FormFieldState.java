package com.example.unicarapp.utils.formvalidation;

import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FormFieldState {
    public enum Status {
        VALID,
        EMPTY,
        INVALID,
        INVALID_CUSTOM
    }

    private final FormState form;
    private final MutableLiveData<Status> statusLiveData = new MutableLiveData<>();
    private final Map<Status, String> errorMessages = new HashMap<>();
    private Pattern regexPattern;
    private ValidationCallback validationCallback;
    private boolean isEmptyCheckEnabled;

    public FormFieldState(FormState form) {
        this.form = form;
        errorMessages.put(Status.EMPTY, "Field cannot be empty");
        errorMessages.put(Status.INVALID, "Invalid format");
        isEmptyCheckEnabled = true;
    }

    public FormFieldState(FormState form, boolean isEmptyCheckEnabled) {
        this(form);
        this.isEmptyCheckEnabled = isEmptyCheckEnabled;
    }

    public FormFieldState(FormState form, Pattern regexPattern) {
        this(form);
        this.regexPattern = regexPattern;
    }

    public FormFieldState(FormState form, Pattern regexPattern, ValidationCallback validationCallback) {
        this(form);
        this.regexPattern = regexPattern;
        this.validationCallback = validationCallback;
    }

    public FormFieldState(FormState form, ValidationCallback validationCallback) {
        this(form);
        this.validationCallback = validationCallback;
    }

    public FormFieldState(FormState form, Pattern regexPattern, boolean isEmptyCheckEnabled) {
        this(form);
        this.regexPattern = regexPattern;
        this.isEmptyCheckEnabled = isEmptyCheckEnabled;
    }

    public FormFieldState(FormState form, ValidationCallback validationCallback, boolean isEmptyCheckEnabled) {
        this(form);
        this.validationCallback = validationCallback;
        this.isEmptyCheckEnabled = isEmptyCheckEnabled;
    }

    public FormFieldState(FormState form, Pattern regexPattern, ValidationCallback validationCallback, boolean isEmptyCheckEnabled) {
        this(form);
        this.regexPattern = regexPattern;
        this.validationCallback = validationCallback;
        this.isEmptyCheckEnabled = isEmptyCheckEnabled;
    }

    public void validate(String value) {
        if (isEmptyCheckEnabled && (value == null || value.trim().isEmpty())) {
            setStatus(Status.EMPTY);
        } else if (regexPattern != null && !regexPattern.matcher(value).matches()) {
            setStatus(Status.INVALID);
        } else if (validationCallback != null) {
            setStatus(validationCallback.validate(value));
        } else {
            setStatus(Status.VALID);
        }
    }

    public String getError() {
        return errorMessages.get(getStatus());
    }

    public void setErrorMessage(Status errorStatus, String error) {
        this.errorMessages.put(errorStatus, error);
    }

    public Status getStatus() {
        return statusLiveData.getValue();
    }

    public void setStatus(Status status) {
        if(getStatus() != status) {
            statusLiveData.setValue(status);
            this.form.updateLiveData();
        }
    }

    public boolean isValid() {
        return statusLiveData.getValue() == Status.VALID;
    }

    public void setEmptyCheckEnabled(boolean emptyCheckEnabled) {
        isEmptyCheckEnabled = emptyCheckEnabled;
    }

    public void setRegexPattern(Pattern regexPattern) {
        this.regexPattern = regexPattern;
    }

    public void setValidationCallback(ValidationCallback validationCallback) {
        this.validationCallback = validationCallback;
    }

    public interface ValidationCallback {
        Status validate(String value);
    }
}
