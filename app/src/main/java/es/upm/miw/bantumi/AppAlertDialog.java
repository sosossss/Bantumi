package es.upm.miw.bantumi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AppAlertDialog extends AppCompatDialogFragment {

    interface SuccessCallback {
        void onSuccess();
    }

    int title;
    int message;
    SuccessCallback success;

    public AppAlertDialog(int titleText, int messageText, SuccessCallback successCallback) {
        title = titleText;
        message = messageText;
        success = successCallback;
    }

    @NonNull
    @Override
	public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {
		final MainActivity main = (MainActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(
                        getString(R.string.txtDialogoAlertAfirmativo),
                        (dialog, which) -> {
                            success.onSuccess();
                        }
                )
                .setNegativeButton(
                        getString(R.string.txtDialogoAlertNegativo),
                        (dialog, which) -> {}
                );

		return builder.create();
	}
}
