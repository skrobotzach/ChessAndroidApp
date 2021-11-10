package skrobotz.msu.edu.project2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import skrobotz.msu.edu.project2.Cloud.Cloud;

public class LoadingGameDLG extends DialogFragment {
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

        final EditText edit = (EditText) getActivity().findViewById(R.id.userText);
        final EditText edit2 = (EditText) getActivity().findViewById( R.id.passwordText);

        // Get a reference to the view we are going to load into
        final EditText view = (EditText)getActivity().findViewById(R.id.userText);

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

        if (edit.getText().toString() == "" || edit2.getText().toString() == "")
        {
            Toast.makeText(getActivity(),
                    R.string.pleaseEnterInfo,
                    Toast.LENGTH_SHORT).show();
            return dlg;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                Cloud cloud = new Cloud();

                final boolean worked = cloud.loginUserCloud(edit.getText().toString(), edit2.getText().toString());

                view.post(new Runnable() {

                    @Override
                    public void run() {
                        dlg.dismiss();
                        if (!worked) {
                            Toast.makeText(getActivity(),
                                    R.string.LoginFailed,
                                    Toast.LENGTH_SHORT).show();
                            dlg.dismiss();
                        } else {
                            // Success!

                            Intent intent = new Intent(getActivity(), GameActivity.class);
                            intent.putExtra("PLAYERNAME", edit.getText().toString());
                            startActivity(intent);
                        }
                    }
                });
            }
        }).start();

        return dlg;
    }
}
