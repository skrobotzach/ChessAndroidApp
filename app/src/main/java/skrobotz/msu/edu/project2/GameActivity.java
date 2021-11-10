package skrobotz.msu.edu.project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import javax.xml.parsers.ParserConfigurationException;

import skrobotz.msu.edu.project2.Cloud.Cloud;

public class GameActivity extends AppCompatActivity {
    private PromotionDialogFragment newFragment;

    @Override
    protected void onCreate(Bundle bundle) {
        newFragment = PromotionDialogFragment.newInstance();
        super.onCreate(bundle);
        Cloud cloud = new Cloud();

        setContentView(R.layout.activity_game);
        GameView view1 = this.findViewById(R.id.gameView);

        view1.setNames(getIntent().getStringExtra("PLAYER1NAME"), getIntent().getStringExtra("PLAYER2NAME"));
        view1.setThisPlayer(getIntent().getStringExtra("THISNAME"));
        String p = getIntent().getStringExtra("THISNAME");
        String p2 = getIntent().getStringExtra("PLAYER2NAME");
        if (p.equals(p2))
        {
            view1.setWaiting();
            try {
                view1.setState();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
        else
        {
            view1.setTimer();
        }
        TextView txt = (TextView)findViewById(R.id.turnText);
        txt.setText(getIntent().getStringExtra("PLAYER1NAME")+ "'s turn");

        if(bundle != null) {
            // We have saved state
            GameView view = (GameView)this.findViewById(R.id.gameView);
            view.loadInstanceState(bundle);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        GameView view = this.findViewById(R.id.gameView);
        view.saveInstanceState(bundle);
    }

    void showDialog() {
        newFragment.show(getSupportFragmentManager(), "PromotionDialog");
        GameView view1 = this.findViewById(R.id.gameView);
        view1.invalidate();
    }

    public void onPromotion(View view)
    {
        int which = newFragment.getWhich();
        GameView view1 = this.findViewById(R.id.gameView);
        view1.promote(which);
        TextView txtView = findViewById(R.id.invalidMove);
        txtView.setText(R.string.blank);
        view1.invalidate();
    }

    public void onFinish(View view){
        GameView view1 = this.findViewById(R.id.gameView);
        String winner = view1.getWinner();
        if (winner != "")
        {
            Intent intent = new Intent(this, EndActivity.class);
            intent.putExtra("WINNER", winner);
            startActivity(intent);
        }

    }

    public void onDone(View view) throws ParserConfigurationException {
        GameView view1 = this.findViewById(R.id.gameView);
        view1.unBlock();
    }

    public void onBack(View view){
        GameView view1 = this.findViewById(R.id.gameView);
        view1.back();
        view1.invalidate();
    }

    public void onResign(View view){
        Intent intent = new Intent(this, EndActivity.class);
        GameView view1 = this.findViewById(R.id.gameView);
        String winner = view1.getResign();
        if (winner.equals(""))
        {
            return;
        }
        intent.putExtra("WINNER", winner);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameView game = findViewById(R.id.gameView);
        try {
            if (game.getWaiting())
            {

            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        game.getStartTime();
    }

    public void timeOut(String winner) {
        Intent intent = new Intent(this, EndActivity.class);
        intent.putExtra("WINNER", winner);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
