package skrobotz.msu.edu.project2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import skrobotz.msu.edu.project2.Cloud.Cloud;

public class CreatingUserDlg extends DialogFragment {

    /**
     * Set true if we want to cancel
     */
    private volatile boolean cancel = false;

    /**
     * Called when the view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cancel = true;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle){

        EditText ed = (EditText) getActivity().findViewById( R.id.createUserEditText);
        EditText ed2 = (EditText) getActivity().findViewById( R.id.createPasswordEditText);
        EditText ed3 = (EditText) getActivity().findViewById( R.id.createPasswordEditText2);

        final String edit = ed.getText().toString();
        final String edit2 = ed2.getText().toString();
        final String edit3 = ed3.getText().toString();

        cancel = false;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title
        builder.setTitle(R.string.loading);

        builder.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        cancel = true;
                    }
                });

        // Create the dialog box
        final AlertDialog dlg = builder.create();

        // Get a reference to the view we are going to load into
        final EditText view = (EditText)getActivity().findViewById(R.id.createPasswordEditText);

        final Context context = this.getActivity();
        if (edit == "" || edit2 == "" || edit3 == "")
        {
            Toast.makeText(this.getActivity(),
                    R.string.pleaseEnterInfo,
                    Toast.LENGTH_SHORT).show();
            return dlg;
        }
        if (!edit3.equals(edit2))
        {
            ed.setText("");
            ed2.setText("");
            ed3.setText("");

            Toast.makeText(this.getActivity(),
                    R.string.noMatch,
                    Toast.LENGTH_SHORT).show();
            return dlg;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Cloud cloud = new Cloud();

                final boolean worked = cloud.createUserCloud(edit, edit2);

                view.post(new Runnable() {

                    @Override
                    public void run() {
                        dlg.dismiss();
                        if (!worked) {
                            Toast.makeText(context,
                                    R.string.CreationFailed,
                                    Toast.LENGTH_SHORT).show();
                            dlg.dismiss();
                        } else {
                            // Success!
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        }).start();



        return dlg;
    }


}
