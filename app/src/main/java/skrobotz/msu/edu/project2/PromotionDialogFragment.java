package skrobotz.msu.edu.project2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class PromotionDialogFragment extends DialogFragment {

    private int which1 = 4;
    public static PromotionDialogFragment newInstance() {
        PromotionDialogFragment f = new PromotionDialogFragment();
        return f;
    }

    public int getWhich()
    {
        return which1;
    }

    public void setWhich1(int i) {which1 = i;}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.PickPromotion)
                .setItems(R.array.promotion_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        which1 = which;
                    }
                });
        return builder.create();
    }
}
