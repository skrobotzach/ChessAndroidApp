package skrobotz.msu.edu.project2;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import javax.xml.parsers.ParserConfigurationException;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    /**
     *The game itself
     */
    private Game game;


    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * Save the puzzle to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        game.saveInstanceState(bundle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return game.onTouchEvent(this, event);
    }

    private void init(AttributeSet attrs, int defStyle) {
        game = new Game(getContext());
        game.setGameView(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        game.draw(canvas);

    }

    public String getResign()
    {
        return game.getThisPlayer();
    }

    public void setNames(String name1, String name2)
    {
        game.setPlayer1Name(name1);
        game.setPlayer2Name(name2);
    }

    public void setThisPlayer(String player) { game.setThisPlayer(player);}
    public void unBlock() throws ParserConfigurationException {
        game.unBlock();
    }

    public void back()
    {
        game.back();
    }

    public String getWinner()
    {
        return game.getWinner();
    }

    public void promote(int which)
    {
        if (game.getPromoteDark() != null)
        {
            game.promoteDark(which);
        }
        else if (game.getPromoteLight() != null)
        {
            game.promoteLight(which);
        }
        this.invalidate();
    }

    /**
     * Load the puzzle from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        game.loadInstanceState(bundle);
    }

    public void setImagePath(String imagePath) {

    }

    public void setWaiting() {
        game.setWaiting();
        invalidate();
    }

    public void setState() throws ParserConfigurationException {
        game.setGameState();
    }

    public boolean getWaiting() throws ParserConfigurationException {
        return game.getWaiting();
    }

    public void setTimer() {
        game.startTimer(System.currentTimeMillis());
    }

    public void getStartTime() {
        game.getStartTime();
    }
}

