package com.example.unicarapp.utils.formvalidation;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class FormTextWatcher implements TextWatcher {
    private FormState formState;
    private EditText editText;

    public FormTextWatcher(FormState formState, EditText editText) {
        this.formState = formState;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        formState.getFieldState(editText.getId()).validate(editText.getText().toString());
    }
}