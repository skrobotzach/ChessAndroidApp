package skrobotz.msu.edu.project2;

import android.content.Context;
import android.graphics.Canvas;

public class LightQueen extends ChessPiece {
    LightQueen(Context context, int id, int y, int x) {
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
        if(!super.getGame().getPiecesD().contains(super.getGame().getBoard()[row][column]))
        {
            return false;
        }
        else
        {
            if (getGame().getDarkKing() == super.getGame().getBoard()[row][column])
            {
                return true;
            }
            super.getGame().getPiecesD().remove(super.getGame().getBoard()[row][column]);
            super.getGame().getBoard()[row][column] = null;
        }
        return true;
    }

    @Override
    public boolean validMove(int row, int column, int turnNumber)
    {
        if ((Math.abs(super.getColumn() - column) != Math.abs(super.getRow() - row)) && (super.getColumn() != column && super.getRow() != row))
        {
            return false;
        }
        if (!(Math.abs(super.getColumn() - column) != Math.abs(super.getRow() - row)))
        {
            if (!super.clearPathDiagonal(row, column)) {
                return false;
            }
            else
            {
                return true;
            }
        }
        if (!super.clearPath(row, column)) {
            return false;
        }
        return true;
    }
}
