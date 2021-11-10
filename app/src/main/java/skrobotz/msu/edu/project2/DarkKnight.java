package skrobotz.msu.edu.project2;

import android.content.Context;
import android.graphics.Canvas;

public class DarkKnight extends ChessPiece {
    DarkKnight(Context context, int id, int y, int x) {
        super(context, id, y, x);
    }

    @Override
    public void draw(Canvas canvas, int marginX, int marginY, int gameSize, float scaleFactor) {
        super.draw(canvas, marginX, marginY, gameSize, scaleFactor);
    }

    @Override
    public boolean validCapture(int row, int column, int turnNumber)
    {
        if(!validMove(row, column, turnNumber))
        {
            return false;
        }
        if(!super.getGame().getPiecesL().contains(super.getGame().getBoard()[row][column]))
        {
            return false;
        }
        else
        {
            if (getGame().getLightKing() == super.getGame().getBoard()[row][column])
            {
                return true;
            }
            super.getGame().getPiecesL().remove(super.getGame().getBoard()[row][column]);
            super.getGame().getBoard()[row][column] = null;
        }
        return true;
    }

    @Override
    public boolean validMove(int row, int column, int turnNumber)
    {
        if(((1 + super.getColumn()) == column || (super.getColumn() - 1) == column )&& ((2 + super.getRow()) == row || (super.getRow() - 2) == row))
        {
            return true;
        }
        else if (((2 + super.getColumn()) == column || (super.getColumn() - 2) == column )&& ((1 + super.getRow()) == row || (super.getRow() - 1) == row))
        {
            return true;
        }
        return false;
    }
}
