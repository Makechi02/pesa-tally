package com.makechi.pesa.tally.util;

import android.content.Intent;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BetterActivityResult<Input, Result> {

    private final ActivityResultLauncher<Input> launcher;
    @Nullable
    private OnActivityResult<Result> onActivityResult;

    private BetterActivityResult(
            @NonNull ActivityResultCaller caller,
            @NonNull ActivityResultContract<Input, Result> contract,
            @Nullable OnActivityResult<Result> onActivityResult
    ) {
        this.onActivityResult = onActivityResult;
        this.launcher = caller.registerForActivityResult(contract, this::callOnActivityResult);
    }

    @NonNull
    public static <Input, Result> BetterActivityResult<Input, Result> registerForActivityResult(
            @NonNull ActivityResultCaller caller,
            @NonNull ActivityResultContract<Input, Result> contract,
            @Nullable OnActivityResult<Result> onActivityResult
    ) {
        return new BetterActivityResult<>(caller, contract, onActivityResult);
    }

    @NonNull
    public static <Input, Result> BetterActivityResult<Input, Result> registerForActivityResult(
            @NonNull ActivityResultCaller caller,
            @NonNull ActivityResultContract<Input, Result> contract
    ) {
        return registerForActivityResult(caller, contract, null);
    }

    @NonNull
    public static BetterActivityResult<Intent, ActivityResult> registerForActivityResult(
            @NonNull ActivityResultCaller caller
    ) {
        return registerForActivityResult(
                caller,
                new ActivityResultContracts.StartActivityForResult(),
                null
        );
    }

    public void setOnActivityResult(@Nullable OnActivityResult<Result> onActivityResult) {
        this.onActivityResult = onActivityResult;
    }

    public void launch(Input input, @Nullable OnActivityResult<Result> onActivityResult) {
        if (onActivityResult != null) {
            this.onActivityResult = onActivityResult;
        }
        launcher.launch(input);
    }

    public void launch(Input input) {
        launch(input, this.onActivityResult);
    }

    private void callOnActivityResult(Result result) {
        if (onActivityResult != null)
            onActivityResult.onActivityResult(result);
    }

    public interface OnActivityResult<O> {
        void onActivityResult(O result);
    }

}
