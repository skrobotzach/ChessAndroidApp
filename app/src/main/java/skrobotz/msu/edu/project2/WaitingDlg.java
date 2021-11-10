package skrobotz.msu.edu.project2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

import skrobotz.msu.edu.project2.Cloud.Cloud;

public class WaitingDlg extends DialogFragment {

    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

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

        final String edit =  getActivity().getIntent().getStringExtra("PLAYER1NAME");

        // Get a reference to the view we are going to load into
        final TextView view = (TextView)getActivity().findViewById(R.id.waitingText);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        cancel = true;
                    }
                });

        builder.setTitle("Waiting for opponent...");

        // Create the dialog box
        final AlertDialog dlg = builder.create();

        new Thread(new Runnable() {
            @Override
            public void run() {

                final Cloud cloud = new Cloud();

                final ArrayList<String> users = cloud.getUsers();

                boolean full = false;

                if(users.get(0) != null && users.get(1) != null )
                {
                    full = true;
                }
                final boolean full1 = full;

                if(users.get(0) == null || users.get(1) == null)
                {
                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w("BAD", "getInstanceId failed", task.getException());
                                        return;
                                    }

                                    // Get new Instance ID token
                                    String token = task.getResult().getToken();

                                    final String token1 = token.toString();

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            cloud.joinUser(edit, token1);
                                        }
                                    }).start();

                                }
                            });

                }

                ArrayList<String> users1 = users;

                while (users1.get(0) == null || users1.get(1) == null)
                {
                    users1 = cloud.getUsers();
                    try {
                        synchronized (this){
                            wait(2000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                final ArrayList<String> users2 = users1;
                view.post(new Runnable() {

                    @Override
                    public void run() {

                        if(full1)
                        {
                            Toast.makeText(getActivity(), "Sorry game in progress. Try again later", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            if(isAdded()) {
                                Intent intent = new Intent(context, GameActivity.class);
                                intent.putExtra("THISNAME", edit);
                                intent.putExtra("PLAYER1NAME", users2.get(0));
                                intent.putExtra("PLAYER2NAME", users2.get(1));
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        }).start();

        return dlg;
    }
}
