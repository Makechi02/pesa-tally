package com.makechi.pesa.tally;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddGoalActivity extends BaseActivity {

    private TextInputLayout nameInputLayout;
    private TextInputLayout descriptionInputLayout;
    private TextInputLayout amountInputLayout;

    private TextInputEditText nameInputField;
    private TextInputEditText descriptionInputField;
    private TextInputEditText amountInputField;
    private TextInputEditText deadlineInputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nameInputLayout = findViewById(R.id.layout_input_name);
        descriptionInputLayout = findViewById(R.id.layout_input_description);
        amountInputLayout = findViewById(R.id.layout_target_amount);

        nameInputField = findViewById(R.id.edit_text_name);
        descriptionInputField = findViewById(R.id.edit_text_description);
        amountInputField = findViewById(R.id.edit_text_target_amount);
        deadlineInputField = findViewById(R.id.edit_text_deadline);
        deadlineInputField.setOnClickListener(view -> showDatePickerDialog());

        MaterialButton actionBtn = findViewById(R.id.save_button);
        actionBtn.setOnClickListener(view -> saveGoal());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_goal;
    }

    private void saveGoal() {
        String name = Objects.requireNonNull(nameInputField.getText()).toString().trim();
        String description = Objects.requireNonNull(descriptionInputField.getText()).toString().trim();
        String amount = Objects.requireNonNull(amountInputField.getText()).toString().trim();
        String deadline = Objects.requireNonNull(deadlineInputField.getText()).toString().trim();

        if (validateFields()) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("description", description);
            resultIntent.putExtra("targetAmount", amount);
            resultIntent.putExtra("deadline", deadline);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    private boolean validateFields() {
        String name = Objects.requireNonNull(nameInputField.getText()).toString().trim();
        String description = Objects.requireNonNull(descriptionInputField.getText()).toString().trim();
        String amount = Objects.requireNonNull(amountInputField.getText()).toString().trim();

        if (TextUtils.isEmpty(name)) {
            nameInputLayout.setError("Name is required");
            nameInputLayout.requestFocus();
            return false;
        } else
            nameInputLayout.setError(null);

        if (TextUtils.isEmpty(description)) {
            descriptionInputLayout.setError("Description is required");
            descriptionInputLayout.requestFocus();
            return false;
        } else
            descriptionInputLayout.setError(null);

        if (TextUtils.isEmpty(amount)) {
            amountInputLayout.setError("Amount is required");
            amountInputLayout.requestFocus();
            return false;
        } else
            amountInputLayout.setError(null);

        return true;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddGoalActivity.this,
                (view, year1, monthOfYear, dayOfMonth1) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth1);
                    deadlineInputField.setText(selectedDate);
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }
}