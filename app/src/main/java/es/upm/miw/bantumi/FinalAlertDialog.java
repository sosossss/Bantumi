package es.upm.miw.bantumi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class FinalAlertDialog extends AppCompatDialogFragment {

    interface SuccessCallback {
        void onSuccess();
    }

    SuccessCallback success;

    public FinalAlertDialog(SuccessCallback successCallback) {
        success = successCallback;
    }

    @NonNull
    @Override
	public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {
		final MainActivity main = (MainActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder
                .setTitle(R.string.txtDialogoFinalTitulo)
                .setMessage(R.string.txtDialogoFinalPregunta)
                .setPositiveButton(
                        getString(R.string.txtDialogoFinalAfirmativo),
                        (dialog, which) -> {
                            success.onSuccess();
                            main.juegoBantumi.inicializar(JuegoBantumi.Turno.turnoJ1);
                        }
                )
                .setNegativeButton(
                        getString(R.string.txtDialogoFinalNegativo),
                        (dialog, which) -> {
                            main.finish();
                        }
                );

		return builder.create();
	}
}
