package skrobotz.msu.edu.project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import skrobotz.msu.edu.project2.Cloud.Cloud;

public class WaitingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        WaitingDlg dlg2 = new WaitingDlg();
        dlg2.show(getSupportFragmentManager(), "waiting1");
        return;
    }

    public void onBack(View view1)
    {
        final String user =  getIntent().getStringExtra("PLAYER1NAME");
        final WaitingActivity act = this;
        final TextView view = findViewById(R.id.waitingText);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Cloud cloud = new Cloud();


                final boolean worked = cloud.cancelUserCloud(user);

                view.post(new Runnable() {

                    @Override
                    public void run() {
                        if (!worked) {
                            Toast.makeText(act,
                                    R.string.LoginFailed,
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            Intent intent = new Intent(act, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        }).start();

    }
}
