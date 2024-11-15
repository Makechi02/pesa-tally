package com.makechi.pesa.tally;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.appcompat.widget.AppCompatSpinner;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddContributionActivity extends BaseActivity {

    private TextInputLayout amountInputLayout;
    private TextInputLayout dateInputLayout;

    private TextInputEditText amountInputField;
    private TextInputEditText dateInputField;
    private AppCompatSpinner transactionTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        amountInputLayout = findViewById(R.id.layout_input_amount);
        dateInputLayout = findViewById(R.id.layout_input_date);

        amountInputField = findViewById(R.id.edit_text_amount);
        dateInputField = findViewById(R.id.edit_text_date);
        dateInputField.setOnClickListener(view -> showDatePickerDialog());

        transactionTypeSpinner = findViewById(R.id.spinner_type_selection);

        MaterialButton actionBtn = findViewById(R.id.save_button);
        actionBtn.setOnClickListener(view -> addContribution());
    }

    private void addContribution() {
        String date = Objects.requireNonNull(dateInputField.getText()).toString().trim();
        String amount = Objects.requireNonNull(amountInputField.getText()).toString().trim();
        String type = (String) transactionTypeSpinner.getSelectedItem();

        if (validateFields()) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("date", date);
            resultIntent.putExtra("amount", Double.parseDouble(amount));
            resultIntent.putExtra("type", type.toUpperCase());
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    private boolean validateFields() {
        String date = Objects.requireNonNull(dateInputField.getText()).toString().trim();
        String amount = Objects.requireNonNull(amountInputField.getText()).toString().trim();

        if (TextUtils.isEmpty(date)) {
            dateInputLayout.setError("Date is required");
            dateInputLayout.requestFocus();
            return false;
        } else
            dateInputLayout.setError(null);

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

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddContributionActivity.this,
                (view, year1, monthOfYear, dayOfMonth1) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth1);
                    dateInputField.setText(selectedDate);
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_contribution;
    }
}