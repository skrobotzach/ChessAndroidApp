package skrobotz.msu.edu.project2;

import android.content.Context;
import android.graphics.Canvas;

public class DarkKing extends ChessPiece {
    DarkKing(Context context, int id, int y, int x) {
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
        if (isMoveFirst())
        {
            if(row == super.getRow() && Math.abs(column - super.getColumn()) == 2)
            {
                if(!clearPath(row, column))
                {
                    return false;
                }
                if (column - super.getColumn() == 2 && super.getGame().getDarkRook2().isMoveFirst())
                {
                    for (ChessPiece piece : super.getGame().getPiecesL())
                    {
                        if (piece.validCapture(row, column, turnNumber))
                        {
                            return false;
                        }
                        if (piece.validCapture(row, column - 1, turnNumber))
                        {
                            return false;
                        }
                        super.getGame().getDarkRook2().setMoveFirst(false);
                        super.getGame().getDarkRook2().setColumn(column - 1);
                        super.getGame().getBoard()[super.getGame().getDarkRook2().getRow()][super.getGame().getDarkRook2().getColumn()] = null;
                        super.getGame().getBoard()[row][column - 1] = super.getGame().getDarkRook2();
                        super.setMoveFirst(false);
                        return true;
                    }
                }
                else if (column - super.getColumn() == -2 && super.getGame().getDarkRook1().isMoveFirst())
                {
                    for (ChessPiece piece : super.getGame().getPiecesL())
                    {
                        if (piece.validCapture(row, column, turnNumber))
                        {
                            return false;
                        }
                        if (piece.validCapture(row, column + 1, turnNumber))
                        {
                            return false;
                        }
                        super.getGame().getDarkRook1().setMoveFirst(false);
                        super.getGame().getDarkRook1().setColumn(column + 1);
                        super.getGame().getBoard()[super.getGame().getDarkRook1().getRow()][super.getGame().getDarkRook1().getColumn()] = null;
                        super.getGame().getBoard()[row][column + 1] = super.getGame().getDarkRook1();
                        super.setMoveFirst(false);
                        return true;
                    }
                }
                return false;
            }
            if (Math.abs(super.getColumn() - column) > 1 || Math.abs(super.getRow() - row) > 1)
            {
                return false;
            }
            if (Math.abs(super.getColumn() - column) == 1 && Math.abs(super.getRow() - row) == 1)
            {
                if (!super.clearPathDiagonal(row, column)) {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            else if (!super.clearPath(row, column)){
                return false;
            }
            return true;
        }
        else
        {
            if (Math.abs(super.getColumn() - column) > 1 || Math.abs(super.getRow() - row) > 1)
            {
                return false;
            }
            if (Math.abs(super.getColumn() - column) == 1 && Math.abs(super.getRow() - row) == 1)
            {
                if (!super.clearPathDiagonal(row, column)) {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            else if (!super.clearPath(row, column)){
                return false;
            }
            return true;
        }

    }
}
