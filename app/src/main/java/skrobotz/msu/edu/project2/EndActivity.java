package skrobotz.msu.edu.project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import skrobotz.msu.edu.project2.Cloud.Cloud;

public class EndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        TextView txt = (TextView) this.findViewById(R.id.win_textView);
        txt.setText(getIntent().getStringExtra("WINNER") + " WINS!");
        txt.invalidate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        wait(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Cloud cloud = new Cloud();
                cloud.endGame();
            }
        }).start();
    }

    public void onNewGame(View view) throws InterruptedException {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
