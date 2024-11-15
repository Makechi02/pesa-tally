package com.makechi.pesa.tally;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import org.jetbrains.annotations.NotNull;

public class DeleteConfirmationBottomSheet extends BottomSheetDialogFragment {

	private OnDeleteConfirmListener listener;

	public static DeleteConfirmationBottomSheet newInstance(String title, String message, String item) {
		DeleteConfirmationBottomSheet fragment = new DeleteConfirmationBottomSheet();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", message);
		args.putString("item", item);
		fragment.setArguments(args);
		return fragment;
	}

	@Nullable
	@org.jetbrains.annotations.Nullable
	@Override
	public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bottom_sheet_delete_confirmation, container, false);

		TextView titleTextView = view.findViewById(R.id.delete_title);
		TextView messageTextView = view.findViewById(R.id.delete_message);
		TextView itemTextView = view.findViewById(R.id.item_text);

		Bundle bundle = getArguments();
		if (bundle != null) {
			String title = bundle.getString("title");
			String message = bundle.getString("message");
			String itemName = bundle.getString("item");

			titleTextView.setText(title);
			messageTextView.setText(message);
			itemTextView.setText(itemName);
		}

		MaterialButton cancelBtn = view.findViewById(R.id.cancel_btn);
		MaterialButton deleteBtn = view.findViewById(R.id.delete_btn);

		cancelBtn.setOnClickListener(v -> dismiss());

		deleteBtn.setOnClickListener(v -> {
			if (listener != null) {
				listener.onDeleteConfirmed();
			}
			dismiss();
		});

		return view;
	}

	public void setOnDeleteConfirmListener(OnDeleteConfirmListener listener) {
		this.listener = listener;
	}

	public interface OnDeleteConfirmListener {
		void onDeleteConfirmed();
	}

}
