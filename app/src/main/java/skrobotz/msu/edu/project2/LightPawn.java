package skrobotz.msu.edu.project2;

import android.content.Context;
import android.graphics.Canvas;

public class LightPawn extends ChessPiece {
    public LightPawn(Context context, int id,int y,int x) {
        super(context, id, y, x);
    }

    @Override
    public void draw(Canvas canvas, int marginX, int marginY, int gameSize, float scaleFactor) {
        super.draw(canvas, marginX, marginY, gameSize, scaleFactor);
    }

    @Override
    public boolean validCapture(int row, int column, int turnNumber)
    {
        if(Math.abs(super.getColumn() - column) != 1 || (super.getRow() - row) != 1 )
        {
            return false;
        }
        if(!super.getGame().getPiecesD().contains(super.getGame().getBoard()[row][column]))
        {
            if(super.getGame().getPiecesD().contains(super.getGame().getBoard()[row + 1][column]))
            {
                if (turnNumber - 1 == super.getGame().getBoard()[row + 1][column].getLastMove()) {
                    super.getGame().getPiecesD().remove(super.getGame().getBoard()[row + 1][column]);
                    super.getGame().getBoard()[row - 1][column] = null;
                    return true;
                }
            }
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
        if (super.isMoveFirst())
        {
            if(super.getColumn() != column)
            {
                return false;
            }
            else if ((super.getRow() - row) == 1 || (super.getRow() - row) == 2)
            {
                if (!super.clearPath(row, column))
                {
                    return false;
                }
                super.setMoveFirst(false);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            if (validCapture(row, column, turnNumber))
            {
                if(row == 0) {
                    GameActivity activity = (GameActivity) super.getGame().getmContext();
                    activity.showDialog();
                    super.getGame().setPromoteLight(this);
                }
                return true;
            }
            if(super.getColumn() != column)
            {
                return false;
            }
            else if ((super.getRow() - row) == 1)
            {
                if (super.clearPath(row, column) != false)
                {
                    if(row == 0)
                    {
                        GameActivity activity = (GameActivity) super.getGame().getmContext();
                        activity.showDialog();
                        super.getGame().setPromoteLight(this);
                    }
                    return true;
                }
                else
                {
                    return false;
                }

            }
            else
            {
                return false;
            }
        }
    }
}
