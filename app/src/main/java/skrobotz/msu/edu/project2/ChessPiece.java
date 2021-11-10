package skrobotz.msu.edu.project2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ChessPiece {


    /**
     * The image for the actual piece.
     */
    private Bitmap piece;

    public Game getGame() {
        return game;
    }

    void setGame(Game game) {
        this.game = game;
    }

    /**
     * association to the game to get pieces on the board
     */
    private Game game;

    /**
     * says if the piece has moved
     */
    private boolean moveFirst = true;

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    boolean isMoveFirst() {
        return moveFirst;
    }

    void setMoveFirst(boolean moveFirst) {
        this.moveFirst = moveFirst;
    }

    int getLastMove() {
        return lastMove;
    }

    void setLastMove(int lastMove) {
        this.lastMove = lastMove;
    }

    /**
     * The last turn that the piece moved
     */
    private int lastMove = -1;

    /**
     * x location.
     * We use relative x locations in the range 0-1 for the center
     * of the puzzle piece.
     */
    private int row;

    /**
     * y location
     */
    private int column;

    /**
     * The chess piece ID
     */
    private int id;

    private boolean hit = false;

    public ChessPiece(Context context, int id,int y, int x) {
        this.id = id;
        this.row = y;
        this.column = x;

        piece = BitmapFactory.decodeResource(context.getResources(), id);


    }


    public void setRow(int row) {
        this.row = row;
    }


    public void setColumn(int column) {
        this.column = column;
    }

    void hit()
    {
        if (hit)
        {
            hit = false;
        }
        else
        {
            hit = true;
        }
    }

    public boolean validMove(int row, int column, int turnNumber)
    {
        return true;
    }


    public boolean validCapture(int row, int column, int turnNumber) {return true;}
    /**
     * Draw the Chess piece
     * @param canvas Canvas we are drawing on
     * @param marginX Margin x value in pixels
     * @param marginY Margin y value in pixels
     * @param gameSize Size we draw the puzzle in pixels
     * @param scaleFactor Amount we scale the puzzle pieces when we draw them
     */
    public void draw(Canvas canvas, int marginX, int marginY,
                     int gameSize, float scaleFactor) {
        canvas.save();

        double x1 = this.column * .244 + .07;
        double y1 = this.row * .244 + .07;
        float x = (float)x1;
        float y = (float)y1;



        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(marginX + x * gameSize, marginY + y * gameSize);

        // Scale it to the right size
        canvas.scale(scaleFactor/5*3, scaleFactor/5*3);

        // This magic code makes the center of the piece at 0, 0
        canvas.translate(-piece.getWidth() / 2f, -piece.getHeight() / 2f);

        Paint paint = new Paint();
        paint.setAlpha(255);
        if (hit)
        {
            paint.setAlpha(120);
        }
        // Draw the bitmap
        canvas.drawBitmap(piece, 0, 0, paint);
        canvas.restore();


    }

    boolean clearPathDiagonal(int moveRow, int moveCol){

        if (moveRow > row && moveCol > column)
        {
            int curRow = row + 1;
            int curCol = column + 1;
            while(curRow != moveRow)
            {
                if (game.getBoard()[curRow][curCol] != null)
                {
                    return false;
                }
                curCol++;
                curRow++;
            }
        }
        else if (moveRow < row && moveCol > column)
        {
            int curRow = row - 1;
            int curCol = column + 1;
            while(curRow != moveRow)
            {
                if (game.getBoard()[curRow][curCol] != null)
                {
                    return false;
                }
                curCol++;
                curRow--;
            }
        }
        else if (moveRow < row && moveCol < column)
        {
            int curRow = row - 1;
            int curCol = column - 1;
            while(curRow != moveRow)
            {
                if (game.getBoard()[curRow][curCol] != null)
                {
                    return false;
                }
                curCol--;
                curRow--;
            }
        }
        else if (moveRow > row && moveCol < column)
        {
            int curRow = row + 1;
            int curCol = column - 1;
            while(curRow != moveRow)
            {
                if (game.getBoard()[curRow][curCol] != null)
                {
                    return false;
                }
                curCol--;
                curRow++;
            }
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public boolean clearPath(int moveRow, int moveCol){

        if (moveRow > row)
        {
            int curRow = row + 1;
            int curCol = column;
            while(curRow != moveRow)
            {
                if (game.getBoard()[curRow][curCol] != null)
                {
                    return false;
                }
                curRow++;
            }
        }
        else if (moveRow < row)
        {
            int curRow = row - 1;
            int curCol = column;
            while(curRow != moveRow)
            {
                if (game.getBoard()[curRow][curCol] != null)
                {
                    return false;
                }
                curRow--;
            }
        }
        else if (moveCol > column)
        {
            int curRow = row;
            int curCol = column + 1;
            while(curCol != moveCol)
            {
                if (game.getBoard()[curRow][curCol] != null)
                {
                    return false;
                }
                curCol++;
            }
        }
        else if (moveCol < column)
        {
            int curRow = row;
            int curCol = column - 1;
            while(curCol != moveCol)
            {
                if (game.getBoard()[curRow][curCol] != null)
                {
                    return false;
                }
                curCol--;
            }
        }
        return true;
    }
}
