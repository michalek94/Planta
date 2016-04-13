package pl.planta.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import pl.planta.R;

public class ExitDialog extends DialogFragment {

    private Button btnPositive;
    private Button btnNegative;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exit_alertdialog, container, false);

        getDialog().setCanceledOnTouchOutside(false); // you can't close dialog when you click outside of it
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE); // don't create dialog's title
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.exit_dialog_bg); // set new background file for dialog

        btnPositive = (Button) view.findViewById(R.id.btnPositive);
        btnNegative = (Button) view.findViewById(R.id.btnNegative);

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }
}
