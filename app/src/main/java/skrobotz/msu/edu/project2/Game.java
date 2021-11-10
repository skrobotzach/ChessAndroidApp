package skrobotz.msu.edu.project2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import skrobotz.msu.edu.project2.Cloud.Cloud;

public class Game {


    /**
     * The name of the bundle keys to save the puzzle
     */
    private final static String LOCATIONS = "Game.locations";
    private final static String IDS = "Game.ids";
    private final static String MOVING = "Game.Moving";
    private final static String MOVINGROWCOL= "Game.MRC";
    private final static String PROMOTEDARKRC = "Game.PDRC";
    private final static String PROMOTELIGHTRC = "Game.PLRC";
    private final static String FIRSTMOVES = "Game.FM";
    private final static String LASTTURNS = "Game.LT";
    private final static String TURN = "Game.Turn";
    private final static String PLAYER1 = "Game.P1";
    private final static String PLAYER2 = "Game.P2";

    /**
     * Percentage of the display width or height that
     * is occupied by the game.
     */
    private final static float SCALE_IN_VIEW = 0.9f;

    private String thisPlayer = "";
    private boolean waiting;

    private String myLastXML = "";

    public void setThisPlayer(String thisPlayer)
    {
        this.thisPlayer = thisPlayer;
        if (thisPlayer.equals(player1Name)){
            startTurnTime = System.currentTimeMillis();
        }
    }

    private String player1Name = "Player1";
    private String player2Name = "Player2";
    private String winner = "";
    private long startTurnTime = 0;

    private boolean block = false;

    private Bundle myBundle = null;

    public Bundle getMyBundle() {
        return myBundle;
    }

    /**
     * board's id
     */
    private final int id;

    private String turn = "";
    /**
     * board's id
     */
    private int turnNumber = 0;
    /**
     * bitmap for the board
     */
    private Bitmap game;



    /**
     * arrays that make up the board
     */
    private ChessPiece[] row1 = {null, null, null, null, null, null, null, null};
    private ChessPiece[] row2 = {null, null, null, null, null, null, null, null};
    private ChessPiece[] row3 = {null, null, null, null, null, null, null, null};
    private ChessPiece[] row4 = {null, null, null, null, null, null, null, null};
    private ChessPiece[] row5 = {null, null, null, null, null, null, null, null};
    private ChessPiece[] row6 = {null, null, null, null, null, null, null, null};
    private ChessPiece[] row7 = {null, null, null, null, null, null, null, null};
    private ChessPiece[] row8 = {null, null, null, null, null, null, null, null};
    private ChessPiece[][] board = {row1, row2, row3, row4, row5, row6, row7, row8};

    /**
     * The size of the puzzle in pixels
     */
    private int gameSize;


    /**
     * Context for the app to be able to display string variables
     */
    private Context mContext;

    /**
     * How much we scale the chess pieces
     *
     */

    private float scaleFactor = 1;

    /**
     * Image left margin in pixels
     */
    private float marginLeft = 0;

    /**
     * Image top margin in pixels
     */
    private float marginTop = 0;

    /**
     * This variable is set to a piece we are moving. If
     * we are not moving the variable is null.
     */
    private ChessPiece moving = null;

    /**
     * These variable is set to where we will move the piece
     */
    private int movingRow = 10;
    private int movingCol = 10;

    /**
     * Our custom view for the game board
     */
    private GameView gameView;

    private ChessPiece promoteLight;
    private ChessPiece promoteDark;

    private DarkKing darkKing;
    private DarkRook darkRook1;
    private DarkRook darkRook2;
    private LightKing lightKing;
    private LightRook lightRook1;
    private LightRook lightRook2;
    private boolean gameOver = false;

    /**
     * array for the chess pieces
     */
    private ArrayList<ChessPiece> piecesD = new ArrayList<ChessPiece>();

    private ArrayList<ChessPiece> piecesL = new ArrayList<ChessPiece>();

    void setGameView(GameView gameView){this.gameView = gameView;}

    public ChessPiece[][] getBoard() {
        return board;
    }

    ArrayList<ChessPiece> getPiecesD() {
        return piecesD;
    }

    ArrayList<ChessPiece> getPiecesL() {
        return piecesL;
    }


    public void promoteLight(int which)
    {
        piecesL.remove(promoteLight);
        if (which == 0)
        {
            LightQueen queen = new LightQueen(mContext, R.drawable.chess_qlt45, promoteLight.getRow(), promoteLight.getColumn());
            piecesL.add(queen);
            board[promoteLight.getRow()][promoteLight.getColumn()] = queen;
            queen.setGame(this);
        }
        else if (which == 1)
        {
            LightRook rook = new LightRook(mContext, R.drawable.chess_rlt45, promoteLight.getRow(), promoteLight.getColumn());
            piecesL.add(rook);
            board[promoteLight.getRow()][promoteLight.getColumn()] = rook;
            rook.setGame(this);
        }
        else if (which == 2)
        {
            LightKnight knight = new LightKnight(mContext, R.drawable.chess_nlt45, promoteLight.getRow(), promoteLight.getColumn());
            piecesL.add(knight);
            board[promoteLight.getRow()][promoteLight.getColumn()] = knight;
            knight.setGame(this);
        }
        else if (which == 3)
        {
            LightBishop bishop = new LightBishop(mContext, R.drawable.chess_blt45, promoteLight.getRow(), promoteLight.getColumn());
            piecesL.add(bishop);
            board[promoteLight.getRow()][promoteLight.getColumn()] = bishop;
            bishop.setGame(this);
        }
        promoteLight = null;
    }

    public ChessPiece getPromoteLight() {
        return promoteLight;
    }

    public ChessPiece getPromoteDark() {
        return promoteDark;
    }

    public void setPromoteLight(LightPawn promoteLight) {
        this.promoteLight = promoteLight;
    }

    public void setPromoteDark(DarkPawn promoteDark) {
        this.promoteDark = promoteDark;
    }


    public void promoteDark(int which)
    {
        piecesD.remove(promoteDark);
        if (which == 0)
        {
            DarkQueen queen = new DarkQueen(mContext, R.drawable.chess_qdt45, promoteDark.getRow(), promoteDark.getColumn());
            piecesD.add(queen);
            board[promoteDark.getRow()][promoteDark.getColumn()] = queen;
            queen.setGame(this);
        }
        else if (which == 1)
        {
            DarkRook rook = new DarkRook(mContext, R.drawable.chess_rdt45, promoteDark.getRow(), promoteDark.getColumn());
            piecesD.add(rook);
            board[promoteDark.getRow()][promoteDark.getColumn()] = rook;
            rook.setGame(this);
        }
        else if (which == 2)
        {
            DarkKnight knight = new DarkKnight(mContext, R.drawable.chess_ndt45, promoteDark.getRow(), promoteDark.getColumn());
            piecesD.add(knight);
            board[promoteDark.getRow()][promoteDark.getColumn()] = knight;
            knight.setGame(this);
        }
        else if (which == 3)
        {
            DarkBishop bishop = new DarkBishop(mContext, R.drawable.chess_bdt45, promoteDark.getRow(), promoteDark.getColumn());
            piecesD.add(bishop);
            board[promoteDark.getRow()][promoteDark.getColumn()] = bishop;
            bishop.setGame(this);
        }
        promoteDark = null;
        moving = null;
    }
    /**
     * Save the puzzle to a bundle
     * @param bundle The bundle we save to
     */
    void saveInstanceState(Bundle bundle) {
        int [] locations = new int[piecesL.size() * 2 + piecesD.size()*2];
        int [] ids = new int[piecesL.size() + piecesD.size()];
        int [] lastMoves = new int[piecesL.size() + piecesD.size()];
        boolean [] firstMoves = new boolean[piecesL.size() + piecesD.size()];
        int [] promoteLightRC = new int[2];
        int [] promoteDarkRC = new int[2];
        int [] movingRC = new int[2];
        int [] turn1 = new int[1];

        turn1[0] = turnNumber;
        if (moving != null)
        {
            movingRC[0] = moving.getRow();
            movingRC[1] = moving.getColumn();
        }
        else
        {
            movingRC[0] = -1;
            movingRC[1] = -1;
        }
        if (promoteDark != null)
        {
            promoteDarkRC[0] = promoteDark.getRow();
            promoteDarkRC[1] = promoteDark.getColumn();
        }
        else
        {
            promoteDarkRC[0] = -1;
            promoteDarkRC[1] = -1;
        }
        if (promoteLight != null)
        {
            promoteLightRC[0] = promoteLight.getRow();
            promoteLightRC[1] = promoteLight.getColumn();
        }
        else
        {
            promoteLightRC[0] = -1;
            promoteLightRC[1] = -1;
        }
        for (int i = 0; i < piecesL.size(); i++)
        {
            locations[i*2] = piecesL.get(i).getRow();
            locations[i*2+1] = piecesL.get(i).getColumn();
            lastMoves[i] = piecesL.get(i).getLastMove();
            ids[i] = piecesL.get(i).getId();
            firstMoves[i] = piecesL.get(i).isMoveFirst();
        }
        for (int i = 0; i < piecesD.size(); i++)
        {
            int j = i + piecesL.size();
            locations[j*2] = piecesD.get(i).getRow();
            locations[j*2+1] = piecesD.get(i).getColumn();
            lastMoves[j] = piecesD.get(i).getLastMove();
            ids[j] = piecesD.get(i).getId();
            firstMoves[j] = piecesD.get(i).isMoveFirst();
        }

        bundle.putIntArray(LOCATIONS, locations);
        bundle.putIntArray(IDS,  ids);
        bundle.putIntArray(LASTTURNS, lastMoves);
        bundle.putBooleanArray(FIRSTMOVES,  firstMoves);
        bundle.putIntArray(PROMOTEDARKRC, promoteDarkRC);
        bundle.putIntArray(PROMOTELIGHTRC, promoteLightRC);
        bundle.putIntArray(MOVINGROWCOL, movingRC);
        bundle.putIntArray(TURN, turn1);
        bundle.putLong("STARTTIME", startTurnTime);
        bundle.putString("TURN1", turn);
        bundle.putString(PLAYER1, player1Name);
        bundle.putString(PLAYER2, player2Name);
        bundle.putBoolean("BLOCK", block);
        bundle.putBoolean("WAITING", waiting);
        bundle.putString("THISPLAYER", thisPlayer);
        bundle.putString("XML", myLastXML);
    }

    public String getTurn() {
        if (turnNumber % 2 == 1)
        {
            return player2Name;
        }
        else
        {
            return player1Name;
        }
    }

    public String getWinner() {
        return winner;
    }


    /**
     * Read the puzzle from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {

        int [] locations = bundle.getIntArray(LOCATIONS);
        int [] ids = bundle.getIntArray(IDS);
        int [] lastMoves = bundle.getIntArray(LASTTURNS);
        boolean [] firstMoves = bundle.getBooleanArray(FIRSTMOVES);
        int [] promoteLightRC = bundle.getIntArray(PROMOTELIGHTRC);
        int [] promoteDarkRC = bundle.getIntArray(PROMOTEDARKRC);
        int [] movingRC = bundle.getIntArray(MOVINGROWCOL);
        int [] turn1 = bundle.getIntArray(TURN);
        player1Name = bundle.getString(PLAYER1);
        player2Name = bundle.getString(PLAYER2);
        block = bundle.getBoolean("BLOCK");
        turn = bundle.getString("TURN1");
        startTurnTime = bundle.getLong("STARTTIME");
        thisPlayer = bundle.getString("THISPLAYER");
        waiting = bundle.getBoolean("WAITING");
        myLastXML = bundle.getString("XML");

        int dr = 1;
        int lr = 1;
        ChessPiece[] newRow1 = {null, null, null, null, null, null, null, null};
        ChessPiece[] newRow2 = {null, null, null, null, null, null, null, null};
        ChessPiece[] newRow3 = {null, null, null, null, null, null, null, null};
        ChessPiece[] newRow4 = {null, null, null, null, null, null, null, null};
        ChessPiece[] newRow5 = {null, null, null, null, null, null, null, null};
        ChessPiece[] newRow6 = {null, null, null, null, null, null, null, null};
        ChessPiece[] newRow7 = {null, null, null, null, null, null, null, null};
        ChessPiece[] newRow8 = {null, null, null, null, null, null, null, null};
        ChessPiece[][] newBoard = {newRow1, newRow2, newRow3, newRow4, newRow5, newRow6, newRow7, newRow8};

        ArrayList<ChessPiece> newPiecesD = new ArrayList<ChessPiece>();

        ArrayList<ChessPiece> newPiecesL = new ArrayList<ChessPiece>();
        for (int i = 0; i < ids.length; i++)
        {
            int j = i*2;
            if (ids[i] == R.drawable.chess_plt45)
            {
                LightPawn pawn = new LightPawn(mContext, R.drawable.chess_plt45, locations[j], locations[j + 1]);
                newBoard[locations[j]][locations[j+1]] = pawn;
                newPiecesL.add(pawn);
                pawn.setLastMove(lastMoves[i]);
                pawn.setMoveFirst(firstMoves[i]);
                pawn.setGame(this);
            }
            else if (ids[i] == R.drawable.chess_pdt45)
            {
                DarkPawn pawn = new DarkPawn(mContext, R.drawable.chess_pdt45, locations[j], locations[j + 1]);
                newBoard[locations[j]][locations[j+1]] = pawn;
                newPiecesD.add(pawn);
                pawn.setLastMove(lastMoves[i]);
                pawn.setMoveFirst(firstMoves[i]);
                pawn.setGame(this);
            }
            else if (ids[i] == R.drawable.chess_klt45)
            {
                LightKing king = new LightKing(mContext, R.drawable.chess_klt45, locations[j], locations[j + 1]);
                newBoard[locations[j]][locations[j+1]] = king;
                newPiecesL.add(king);
                king.setLastMove(lastMoves[i]);
                king.setMoveFirst(firstMoves[i]);
                king.setGame(this);
                lightKing = king;
            }
            else if (ids[i] == R.drawable.chess_kdt45)
            {
                DarkKing king = new DarkKing(mContext, R.drawable.chess_kdt45, locations[j], locations[j + 1]);
                newBoard[locations[j]][locations[j+1]] = king;
                newPiecesD.add(king);
                king.setLastMove(lastMoves[i]);
                king.setMoveFirst(firstMoves[i]);
                king.setGame(this);
                darkKing = king;
            }
            else if (ids[i] == R.drawable.chess_nlt45)
            {
                LightKnight knight = new LightKnight(mContext, R.drawable.chess_nlt45, locations[j], locations[j + 1]);
                newBoard[locations[j]][locations[j+1]] = knight;
                newPiecesL.add(knight);
                knight.setLastMove(lastMoves[i]);
                knight.setMoveFirst(firstMoves[i]);
                knight.setGame(this);
            }
            else if (ids[i] == R.drawable.chess_ndt45)
            {
                DarkKnight knight = new DarkKnight(mContext, R.drawable.chess_ndt45, locations[j], locations[j + 1]);
                newBoard[locations[j]][locations[j+1]] = knight;
                newPiecesD.add(knight);
                knight.setLastMove(lastMoves[i]);
                knight.setMoveFirst(firstMoves[i]);
                knight.setGame(this);
            }
            else if (ids[i] == R.drawable.chess_blt45)
            {
                LightBishop bishop = new LightBishop(mContext, R.drawable.chess_blt45, locations[j], locations[j + 1]);
                newBoard[locations[j]][locations[j+1]] = bishop;
                newPiecesL.add(bishop);
                bishop.setLastMove(lastMoves[i]);
                bishop.setMoveFirst(firstMoves[i]);
                bishop.setGame(this);
            }
            else if (ids[i] == R.drawable.chess_bdt45)
            {
                DarkBishop bishop = new DarkBishop(mContext, R.drawable.chess_bdt45, locations[j], locations[j + 1]);
                newBoard[locations[j]][locations[j+1]] = bishop;
                newPiecesD.add(bishop);
                bishop.setLastMove(lastMoves[i]);
                bishop.setMoveFirst(firstMoves[i]);
                bishop.setGame(this);
            }
            else if (ids[i] == R.drawable.chess_rlt45)
            {
                LightRook rook = new LightRook(mContext, R.drawable.chess_rlt45, locations[j], locations[j + 1]);
                newBoard[locations[j]][locations[j+1]] = rook;
                newPiecesL.add(rook);
                rook.setLastMove(lastMoves[i]);
                rook.setMoveFirst(firstMoves[i]);
                rook.setGame(this);
                if (lr == 1)
                {
                    lightRook1 = rook;
                    lr++;
                }
                else
                {
                    lightRook2 = rook;
                }

            }
            else if (ids[i] == R.drawable.chess_rdt45)
            {
                DarkRook rook = new DarkRook(mContext, R.drawable.chess_rdt45, locations[j], locations[j + 1]);
                newBoard[locations[j]][locations[j+1]] = rook;
                newPiecesD.add(rook);
                rook.setLastMove(lastMoves[i]);
                rook.setMoveFirst(firstMoves[i]);
                rook.setGame(this);
                if (dr == 1)
                {
                    darkRook1 = rook;
                    dr++;
                }
                else
                {
                    darkRook2 = rook;
                }
            }
            else if (ids[i] == R.drawable.chess_qlt45)
            {
                LightQueen queen = new LightQueen(mContext, R.drawable.chess_qlt45, locations[j], locations[j + 1]);
                newBoard[locations[j]][locations[j+1]] = queen;
                newPiecesL.add(queen);
                queen.setLastMove(lastMoves[i]);
                queen.setMoveFirst(firstMoves[i]);
                queen.setGame(this);
            }
            else if (ids[i] == R.drawable.chess_qdt45)
            {
                DarkQueen queen = new DarkQueen(mContext, R.drawable.chess_qdt45, locations[j], locations[j + 1]);
                newBoard[locations[j]][locations[j+1]] = queen;
                newPiecesD.add(queen);
                queen.setLastMove(lastMoves[i]);
                queen.setMoveFirst(firstMoves[i]);
                queen.setGame(this);
            }

        }
        if (promoteDarkRC[0] != -1)
        {
            promoteDark = newBoard[promoteDarkRC[0]][promoteDarkRC[1]];
        }
        if (promoteLightRC[0] != -1)
        {
            promoteLight = newBoard[promoteLightRC[0]][promoteLightRC[1]];
        }
        if (movingRC[0] != -1)
        {
            moving = newBoard[movingRC[0]][movingRC[1]];
        }
        turnNumber = turn1[0];
        piecesL = newPiecesL;
        piecesD = newPiecesD;
        board = newBoard;

        for(ChessPiece piece : piecesL){
            piece.setGame(this);
        }
        for(ChessPiece piece : piecesD){
            piece.setGame(this);
        }


        TextView textView = ((Activity)mContext).findViewById(R.id.turnText);
        textView.setText(turn +"'s turn");

    }

    public Context getmContext() {
        return mContext;
    }

    public DarkRook getDarkRook1() {
        return darkRook1;
    }

    public DarkRook getDarkRook2() {
        return darkRook2;
    }

    Game(Context context){
        this.id = R.drawable.chess_board;

        mContext = context;

        game = BitmapFactory.decodeResource(context.getResources(), id);

        LightPawn lightPawn1 = new LightPawn(context,R.drawable.chess_plt45,6, 0);
        piecesL.add(lightPawn1);
        board[6][0] = lightPawn1;

        LightPawn lightPawn2 = new LightPawn(context,R.drawable.chess_plt45,6, 1);
        piecesL.add(lightPawn2);
        board[6][1] = lightPawn2;

        LightPawn lightPawn3 = new LightPawn(context,R.drawable.chess_plt45,6, 2);
        piecesL.add(lightPawn3);
        board[6][2] = lightPawn3;

        LightPawn lightPawn4 = new LightPawn(context,R.drawable.chess_plt45,6, 3);
        piecesL.add(lightPawn4);
        board[6][3] = lightPawn4;

        LightPawn lightPawn5 = new LightPawn(context,R.drawable.chess_plt45,6, 4);
        piecesL.add(lightPawn5);
        board[6][4] = lightPawn5;

        LightPawn lightPawn6 = new LightPawn(context,R.drawable.chess_plt45,6, 5);
        piecesL.add(lightPawn6);
        board[6][5] = lightPawn6;

        LightPawn lightPawn7 = new LightPawn(context,R.drawable.chess_plt45,6, 6);
        piecesL.add(lightPawn7);
        board[6][6] = lightPawn7;

        LightPawn lightPawn8 = new LightPawn(context,R.drawable.chess_plt45,6, 7);
        piecesL.add(lightPawn8);
        board[6][7] = lightPawn8;

        DarkPawn darkPawn1 = new DarkPawn(context,R.drawable.chess_pdt45,1, 0);
        piecesD.add(darkPawn1);
        board[1][0] = darkPawn1;

        DarkPawn darkPawn2 = new DarkPawn(context,R.drawable.chess_pdt45,1, 1);
        piecesD.add(darkPawn2);
        board[1][1] = darkPawn2;

        DarkPawn darkPawn3 = new DarkPawn(context,R.drawable.chess_pdt45,1, 2);
        piecesD.add(darkPawn3);
        board[1][2] = darkPawn3;

        DarkPawn darkPawn4 = new DarkPawn(context,R.drawable.chess_pdt45,1, 3);
        piecesD.add(darkPawn4);
        board[1][3] = darkPawn4;

        DarkPawn darkPawn5 = new DarkPawn(context,R.drawable.chess_pdt45,1, 4);
        piecesD.add(darkPawn5);
        board[1][4] = darkPawn5;

        DarkPawn darkPawn6 = new DarkPawn(context,R.drawable.chess_pdt45,1, 5);
        piecesD.add(darkPawn6);
        board[1][5] = darkPawn6;

        DarkPawn darkPawn7 = new DarkPawn(context,R.drawable.chess_pdt45,1, 6);
        piecesD.add(darkPawn7);
        board[1][6] = darkPawn7;

        DarkPawn darkPawn8 = new DarkPawn(context,R.drawable.chess_pdt45,1, 7);
        piecesD.add(darkPawn8);
        board[1][7] = darkPawn8;

        darkRook1 = new DarkRook(context,R.drawable.chess_rdt45, 0, 0);
        piecesD.add(darkRook1);
        board[0][0] = darkRook1;

        darkRook2 = new DarkRook(context,R.drawable.chess_rdt45, 0, 7);
        piecesD.add(darkRook2);
        board[0][7] = darkRook2;

        lightRook1 = new LightRook(context,R.drawable.chess_rlt45, 7, 0);
        piecesL.add(lightRook1);
        board[7][0] = lightRook1;

        lightRook2 = new LightRook(context,R.drawable.chess_rlt45, 7, 7);
        piecesL.add(lightRook2);
        board[7][7] = lightRook2;

        DarkKnight darkKnight1 = new DarkKnight(context, R.drawable.chess_ndt45, 0, 1);
        piecesD.add(darkKnight1);
        board[0][1] = darkKnight1;

        DarkKnight darkKnight2 = new DarkKnight(context, R.drawable.chess_ndt45, 0, 6);
        piecesD.add(darkKnight2);
        board[0][6] = darkKnight2;

        LightKnight lightKnight1 = new LightKnight(context, R.drawable.chess_nlt45, 7, 1);
        piecesL.add(lightKnight1);
        board[7][1] = lightKnight1;

        LightKnight lightKnight2 = new LightKnight(context, R.drawable.chess_nlt45, 7, 6);
        piecesL.add(lightKnight2);
        board[7][6] = lightKnight2;

        DarkBishop darkBishop1 = new DarkBishop(context, R.drawable.chess_bdt45, 0, 2);
        piecesD.add(darkBishop1);
        board[0][2] = darkBishop1;

        DarkBishop darkBishop2 = new DarkBishop(context, R.drawable.chess_bdt45, 0, 5);
        piecesD.add(darkBishop2);
        board[0][5] = darkBishop2;

        LightBishop lightBishop1 = new LightBishop(context, R.drawable.chess_blt45, 7, 2);
        piecesL.add(lightBishop1);
        board[7][2] = lightBishop1;

        LightBishop lightBishop2 = new LightBishop(context, R.drawable.chess_blt45, 7, 5);
        piecesL.add(lightBishop2);
        board[7][5] = lightBishop2;

        DarkQueen darkQueen = new DarkQueen(context, R.drawable.chess_qdt45, 0, 4);
        piecesD.add(darkQueen);
        board[0][4] = darkQueen;

        LightQueen lightQueen = new LightQueen(context, R.drawable.chess_qlt45, 7, 4);
        piecesL.add(lightQueen);
        board[7][4] = lightQueen;

        darkKing = new DarkKing(context, R.drawable.chess_kdt45, 0, 3);
        piecesD.add(darkKing);
        board[0][3] = darkKing;

        lightKing = new LightKing(context, R.drawable.chess_klt45, 7, 3);
        piecesL.add(lightKing);
        board[7][3] = lightKing;


        for(ChessPiece piece : piecesL){
            piece.setGame(this);
        }
        for(ChessPiece piece : piecesD){
            piece.setGame(this);
        }
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
        turn = player1Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    DarkKing getDarkKing() {
        return darkKing;
    }

    LightKing getLightKing() {
        return lightKing;
    }

    LightRook getLightRook1() {
        return lightRook1;
    }

    LightRook getLightRook2() {
        return lightRook2;
    }

    public void setMoving(ChessPiece moving) {
        this.moving = moving;
    }

    public void unBlock() throws ParserConfigurationException {
        if (block == false || promoteDark != null || promoteLight != null)
        {
            return;
        }
        block = false;
        turnNumber++;
        myBundle = null;


        if (player1Name.equals(thisPlayer)) {
            turn = player2Name;
            TextView textView = ((Activity)mContext).findViewById(R.id.turnText);
            textView.setText(player2Name +"'s turn");
        }
        else
        {
            turn = player1Name;
            TextView textView = ((Activity)mContext).findViewById(R.id.turnText);
            textView.setText(player1Name +"'s turn");
        }
        gameView.invalidate();
        waiting = true;
        final String xml = createGameXML(false);
        myLastXML = xml;

        new Thread(new Runnable() {
            @Override
            public void run() {

                Cloud cloud = new Cloud();
                synchronized (this) {
                    while (!cloud.saveGame(xml)) {
                        try {
                            wait(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        setGameState();

    }

    private String createGameXML(boolean resign)
    {
        // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        try {
            xml.setOutput(writer);

            xml.startDocument("UTF-8", true);

            xml.startTag(null, "game");
            if (gameOver) {
                xml.attribute(null, "gameOver", "true1" );
            }
            else if (resign)
            {
                xml.attribute(null, "gameOver", "true2" );
            }
            else
            {
                xml.attribute(null, "gameOver", "false" );
            }
            xml.attribute(null, "turn", Integer.toString(turnNumber));
            xml.startTag(null, "pieces");

            for (int i = 0; i < piecesD.size(); i++)
            {
                xml.startTag(null, "piece");
                xml.attribute(null, "row", Integer.toString(piecesD.get(i).getRow()));
                xml.attribute(null, "col", Integer.toString(piecesD.get(i).getColumn()));
                xml.attribute(null, "id", Integer.toString(piecesD.get(i).getId()));
                xml.attribute(null, "lastmove", Integer.toString(piecesD.get(i).getLastMove()));
                if (piecesD.get(i).isMoveFirst())
                {
                    xml.attribute(null, "firstmove", "1");
                }
                else
                {
                    xml.attribute(null, "firstmove", "0");
                }
                xml.endTag(null, "piece");
            }

            for (int i = 0; i < piecesL.size(); i++)
            {
                xml.startTag(null, "piece");
                xml.attribute(null, "row", Integer.toString(piecesL.get(i).getRow()));
                xml.attribute(null, "col", Integer.toString(piecesL.get(i).getColumn()));
                xml.attribute(null, "id", Integer.toString(piecesL.get(i).getId()));
                xml.attribute(null, "lastmove", Integer.toString(piecesL.get(i).getLastMove()));
                if (piecesL.get(i).isMoveFirst())
                {
                    xml.attribute(null, "firstmove", "1");
                }
                else
                {
                    xml.attribute(null, "firstmove", "0");
                }
                xml.endTag(null, "piece");
            }
            xml.endTag(null, "pieces");



//            gameView.saveXml(username, xml); USE THIS IN SAVING GAME STATE

            xml.endTag(null, "game");

            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return "";
        }

        final String xmlStr = writer.toString();
        myLastXML = xmlStr;
        return xmlStr;
    }


    public void back()
    {
        if (myBundle != null)
        {
            loadInstanceState(myBundle);
        }
    }

    /**
     * Handle a touch event from the view.
     * @param view The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled.
     */
    boolean onTouchEvent(View view, MotionEvent event) {

        if (waiting)
        {
            return false;
        }
        if (block)
        {
            return false;
        }
        else if (moving == null)
        {
            myBundle = new Bundle();
            saveInstanceState(myBundle);
        }
        if (gameOver)
        {
            return false;
        }
        if (promoteLight != null || promoteDark != null)
        {
            TextView txtView = ((Activity)mContext).findViewById(R.id.invalidMove);
            txtView.setText(R.string.pressPromote);
            gameView.invalidate();
            return false;
        }

        //
        // Convert an x,y location to a relative location in the
        // puzzle.
        //

        float relX = (event.getX() - marginLeft) / gameSize;
        float relY = (event.getY() - marginTop) / gameSize;

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                return onTouched(relX, relY);


            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_CANCEL:
                break;

            case MotionEvent.ACTION_MOVE:
                break;
        }

        return false;
    }

    /**
     * Handle a touch message
     * @param x x location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @param y y location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @return true if the touch is handled
     */
    private boolean onTouched(float x, float y) {


        int col = (int) ((x) / .125);
        int row = (int) ((y) / .125);
        if (col > 7 || col < 0 || row < 0 || row > 7)
        {
            return false;
        }
        else if (moving == null && board[row][col] != null)
        {
            ArrayList<ChessPiece> pieces;
            if (turnNumber % 2 == 0)
            {
                pieces = piecesL;
            }
            else
            {
                pieces = piecesD;
            }
            for (ChessPiece piece : pieces) {
                if (board[row][col] == piece)
                {
                    board[row][col].hit();
                    movingCol = col;
                    movingRow = row;
                    moving = board[row][col];
                    gameView.invalidate();
                    return true;
                }
            }
            return false;
        }
        else if (moving != null && board[row][col] == null)
        {
            if (moving.validMove(row, col, turnNumber)) {
                TextView txtView = ((Activity)mContext).findViewById(R.id.invalidMove);
                txtView.setText(R.string.blank);
                moving.hit();
                moving.setRow(row);
                moving.setColumn(col);
                board[movingRow][movingCol] = null;
                board[row][col] = moving;
                moving.setLastMove(turnNumber);
                moving = null;
                gameView.invalidate();
                block = true;
                return true;
            }
            else
            {
                TextView textView = ((Activity)mContext).findViewById(R.id.invalidMove);
                textView.setText(R.string.invalid);
                gameView.invalidate();
                return false;
            }
        }
        else if (moving != null && board[row][col] != null){
            if (moving == board[row][col])
            {
                moving.hit();
                moving = null;
                TextView textView = ((Activity)mContext).findViewById(R.id.invalidMove);
                textView.setText(R.string.blank);
                gameView.invalidate();
                return true;
            }
            if (moving.validCapture(row, col, turnNumber)) {
                moving.hit();
                moving.setRow(row);
                moving.setColumn(col);
                board[movingRow][movingCol] = null;
                board[row][col] = moving;
                moving.setLastMove(turnNumber);
                moving = null;
                gameView.invalidate();
                block = true;
                return true;
            }
            else
            {
                TextView textView = ((Activity)mContext).findViewById(R.id.invalidMove);
                textView.setText(R.string.invalid);
                gameView.invalidate();
                return false;
            }
        }

        return false;
    }


    public void draw(Canvas canvas){
        /*
         * Determine the margins and scale to draw the image
         * centered and scaled to maximum size on any display
         */
        // Get the canvas size
        float wid = canvas.getWidth();
        float hit = canvas.getHeight();


        // Use the lesser of the two
        float min =  wid < hit ? wid : hit;

        // What is the scaled image size?
        gameSize = (int) (min * SCALE_IN_VIEW);

        // Determine the top and left margins to center
        marginLeft = (wid - gameSize) / 2;
        marginTop = (hit - gameSize) / 2;

        float scaleFactor = (float)gameSize /
                (float)game.getWidth();
        /*
         * Draw the image bitmap
         */
        canvas.save();
        canvas.translate(marginLeft,  0);
        canvas.scale(scaleFactor, scaleFactor);
        canvas.drawBitmap(game,0,0,null);


        for(ChessPiece piece : piecesL){
            piece.draw(canvas, (int)marginLeft, (int)marginTop, gameSize, scaleFactor);
        }
        for(ChessPiece piece : piecesD){
            piece.draw(canvas, (int)marginLeft, (int)marginTop, gameSize, scaleFactor);
        }
    }

    public Document converToXML(String xml)
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public void setGameState() throws ParserConfigurationException {
        final Game game = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        wait(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Cloud cloud = new Cloud();
                String result = cloud.loadGame().toString();
                synchronized (this) {
                    while (result.equals(myLastXML) || result.equals("") || result.equals("<>")) {
                        try {
                            wait(2000);
                            result = cloud.loadGame();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                final String xmlString = result;

                gameView.post(new Runnable() {

                    @Override
                    public void run() {



                        ChessPiece[] newRow1 = {null, null, null, null, null, null, null, null};
                        ChessPiece[] newRow2 = {null, null, null, null, null, null, null, null};
                        ChessPiece[] newRow3 = {null, null, null, null, null, null, null, null};
                        ChessPiece[] newRow4 = {null, null, null, null, null, null, null, null};
                        ChessPiece[] newRow5 = {null, null, null, null, null, null, null, null};
                        ChessPiece[] newRow6 = {null, null, null, null, null, null, null, null};
                        ChessPiece[] newRow7 = {null, null, null, null, null, null, null, null};
                        ChessPiece[] newRow8 = {null, null, null, null, null, null, null, null};
                        ChessPiece[][] newBoard = {newRow1, newRow2, newRow3, newRow4, newRow5, newRow6, newRow7, newRow8};

                        ArrayList<ChessPiece> newPiecesD = new ArrayList<ChessPiece>();

                        ArrayList<ChessPiece> newPiecesL = new ArrayList<ChessPiece>();

                        int dr = 1;
                        int lr = 1;

                        //Get Document Builder
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        try {
                            DocumentBuilder builder = factory.newDocumentBuilder();
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        }

                        //Build Document
                        Document document = converToXML(xmlString);

                        //Normalize the XML Structure; It's just too important !!
                        document.getDocumentElement().normalize();

                        //Here comes the root node
                        Element root = document.getDocumentElement();

                        String gameOver = root.getAttribute("gameOver");
                        String turnS = root.getAttribute("turn");

                        int turn = Integer.parseInt(turnS);
                        //Get all employees
                        NodeList nList = document.getElementsByTagName("piece");


                        for (int temp = 0; temp < nList.getLength(); temp++) {
                            Node node = nList.item(temp);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                //Print each employee's detail
                                Element eElement = (Element) node;
                                String ids = eElement.getAttribute("id");
                                String rowS = eElement.getAttribute("row");
                                String colS = eElement.getAttribute("col");
                                String firstMoveS = eElement.getAttribute("firstmove");
                                String lastMoveS = eElement.getAttribute("lastmove");

                                int id = Integer.parseInt(ids);
                                int row = Integer.parseInt(rowS);
                                int col = Integer.parseInt(colS);
                                boolean firstMove;
                                if (firstMoveS.equals("1"))
                                {
                                    firstMove = true;
                                }
                                else
                                {
                                    firstMove = false;
                                }
                                int lastMove = Integer.parseInt(lastMoveS);

                                if (id == R.drawable.chess_plt45)
                                {
                                    LightPawn pawn = new LightPawn(mContext, R.drawable.chess_plt45, row, col);
                                    newBoard[row][col] = pawn;
                                    newPiecesL.add(pawn);
                                    pawn.setLastMove(lastMove);
                                    pawn.setMoveFirst(firstMove);
                                    pawn.setGame(game);
                                }
                                else if (id == R.drawable.chess_pdt45)
                                {
                                    DarkPawn pawn = new DarkPawn(mContext, R.drawable.chess_pdt45, row, col);
                                    newBoard[row][col] = pawn;
                                    newPiecesD.add(pawn);
                                    pawn.setLastMove(lastMove);
                                    pawn.setMoveFirst(firstMove);
                                    pawn.setGame(game);
                                }
                                else if (id == R.drawable.chess_klt45)
                                {
                                    LightKing king = new LightKing(mContext, R.drawable.chess_klt45, row, col);
                                    newBoard[row][col] = king;
                                    newPiecesL.add(king);
                                    king.setLastMove(lastMove);
                                    king.setMoveFirst(firstMove);
                                    king.setGame(game);
                                    lightKing = king;
                                }
                                else if (id == R.drawable.chess_kdt45)
                                {
                                    DarkKing king = new DarkKing(mContext, R.drawable.chess_kdt45, row, col);
                                    newBoard[row][col] = king;
                                    newPiecesD.add(king);
                                    king.setLastMove(lastMove);
                                    king.setMoveFirst(firstMove);
                                    king.setGame(game);
                                    darkKing = king;
                                }
                                else if (id == R.drawable.chess_nlt45)
                                {
                                    LightKnight knight = new LightKnight(mContext, R.drawable.chess_nlt45, row, col);
                                    newBoard[row][col] = knight;
                                    newPiecesL.add(knight);
                                    knight.setLastMove(lastMove);
                                    knight.setMoveFirst(firstMove);
                                    knight.setGame(game);
                                }
                                else if (id == R.drawable.chess_ndt45)
                                {
                                    DarkKnight knight = new DarkKnight(mContext, R.drawable.chess_ndt45, row, col);
                                    newBoard[row][col] = knight;
                                    newPiecesD.add(knight);
                                    knight.setLastMove(lastMove);
                                    knight.setMoveFirst(firstMove);
                                    knight.setGame(game);
                                }
                                else if (id == R.drawable.chess_blt45)
                                {
                                    LightBishop bishop = new LightBishop(mContext, R.drawable.chess_blt45, row, col);
                                    newBoard[row][col] = bishop;
                                    newPiecesL.add(bishop);
                                    bishop.setLastMove(lastMove);
                                    bishop.setMoveFirst(firstMove);
                                    bishop.setGame(game);
                                }
                                else if (id == R.drawable.chess_bdt45)
                                {
                                    DarkBishop bishop = new DarkBishop(mContext, R.drawable.chess_bdt45, row, col);
                                    newBoard[row][col] = bishop;
                                    newPiecesD.add(bishop);
                                    bishop.setLastMove(lastMove);
                                    bishop.setMoveFirst(firstMove);
                                    bishop.setGame(game);
                                }
                                else if (id == R.drawable.chess_rlt45)
                                {
                                    LightRook rook = new LightRook(mContext, R.drawable.chess_rlt45, row, col);
                                    newBoard[row][col] = rook;
                                    newPiecesL.add(rook);
                                    rook.setLastMove(lastMove);
                                    rook.setMoveFirst(firstMove);
                                    rook.setGame(game);
                                    if (lr == 1)
                                    {
                                        lightRook1 = rook;
                                        lr++;
                                    }
                                    else
                                    {
                                        lightRook2 = rook;
                                    }

                                }
                                else if (id == R.drawable.chess_rdt45)
                                {
                                    DarkRook rook = new DarkRook(mContext, R.drawable.chess_rdt45, row, col);
                                    newBoard[row][col] = rook;
                                    newPiecesD.add(rook);
                                    rook.setLastMove(lastMove);
                                    rook.setMoveFirst(firstMove);
                                    rook.setGame(game);
                                    if (dr == 1)
                                    {
                                        darkRook1 = rook;
                                        dr++;
                                    }
                                    else
                                    {
                                        darkRook2 = rook;
                                    }
                                }
                                else if (id == R.drawable.chess_qlt45)
                                {
                                    LightQueen queen = new LightQueen(mContext, R.drawable.chess_qlt45, row, col);
                                    newBoard[row][col] = queen;
                                    newPiecesL.add(queen);
                                    queen.setLastMove(lastMove);
                                    queen.setMoveFirst(firstMove);
                                    queen.setGame(game);
                                }
                                else if (id == R.drawable.chess_qdt45)
                                {
                                    DarkQueen queen = new DarkQueen(mContext, R.drawable.chess_qdt45, row, col);
                                    newBoard[row][col] = queen;
                                    newPiecesD.add(queen);
                                    queen.setLastMove(lastMove);
                                    queen.setMoveFirst(firstMove);
                                    queen.setGame(game);
                                }
                            }
                        }
                        piecesD.clear();
                        piecesL.clear();
                        turnNumber = turn;
                        piecesL = newPiecesL;
                        piecesD = newPiecesD;
                        board = newBoard;
                        waiting = false;

                        for(ChessPiece piece : piecesL){
                            piece.setGame(game);
                        }
                        for(ChessPiece piece : piecesD){
                            piece.setGame(game);
                        }

                        if (gameOver.equals("true1"))
                        {
                            game.gameOver = true;
                            TextView textView1 = ((Activity)mContext).findViewById(R.id.invalidMove);
                            textView1.setText(R.string.YouLost);
                            gameView.invalidate();
                            game.gameOver = true;
                            if(thisPlayer == player1Name){
                                winner = player2Name;
                            }
                            else
                            {
                                winner = player1Name;
                            }

                        }
                        if (gameOver.equals("true2"))
                        {
                            game.gameOver = true;
                            TextView textView1 = ((Activity)mContext).findViewById(R.id.invalidMove);
                            textView1.setText("You won from a forfeit");
                            gameView.invalidate();
                            game.gameOver = true;
                            winner = thisPlayer;
                        }

                        TextView textView = ((Activity)mContext).findViewById(R.id.turnText);
                        game.turn = thisPlayer;
                        textView.setText(thisPlayer + "'s turn");
                        gameView.invalidate();

                        if (player1Name.equals(thisPlayer))
                        {
                            for (ChessPiece piece : piecesL)
                            {
                                if (piece.validCapture(darkKing.getRow(), darkKing.getColumn(), turnNumber)){
                                    TextView textView1 = ((Activity)mContext).findViewById(R.id.invalidMove);
                                    textView1.setText(R.string.Player1Wins);
                                    gameView.invalidate();
                                    game.gameOver = true;
                                    winner = thisPlayer;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            waiting = true;
                                            final String xml = createGameXML(false);
                                            myLastXML = xml;

                                            Cloud cloud = new Cloud();
                                            synchronized (this) {
                                                while (!cloud.saveGame(xml)) {
                                                    try {
                                                        wait(2000);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    }).start();
                                }
                            }


                        }
                        else
                        {
                            for (ChessPiece piece : piecesD)
                            {
                                if (piece.validCapture(lightKing.getRow(), lightKing.getColumn(), turnNumber)){

                                    TextView textView1 = ((Activity)mContext).findViewById(R.id.invalidMove);
                                    textView1.setText(R.string.Player2Wins);
                                    gameView.invalidate();
                                    game.gameOver = true;
                                    winner = player2Name;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            waiting = true;
                                            final String xml = createGameXML(false);
                                            myLastXML = xml;

                                            Cloud cloud = new Cloud();
                                            synchronized (this) {
                                                while (!cloud.saveGame(xml)) {
                                                    try {
                                                        wait(2000);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    }).start();


                                }
                            }


                        }

                        game.startTimer(System.currentTimeMillis());
                    }
                });
            }
        }).start();
    }

    public void startTimer(long time) {
        startTurnTime = time;
        final int threadTurn = turnNumber;
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    while (System.currentTimeMillis() - startTurnTime < 300000 && turn.equals(thisPlayer) && turnNumber == threadTurn && !gameOver) {
                        try {
                            wait(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!turn.equals(thisPlayer) || turnNumber != threadTurn || gameOver) {
                        return;
                    }
                    synchronized (this) {
                        waiting = true;
                        final String xml = createGameXML(true);
                        myLastXML = xml;

                        Cloud cloud = new Cloud();
                        while (!cloud.saveGame(xml)) {
                            try {
                                wait(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (thisPlayer.equals(player1Name)) {
                            winner = "You timed out. " + player2Name;
                        } else {
                            winner = "You timed out. " + player1Name;
                        }
                        GameActivity gameActivity = (GameActivity) mContext;
                        gameActivity.timeOut(winner);

                    }
                }
            }
        }).start();

    }

    public void setWaiting() {
        waiting = true;
    }

    public String getThisPlayer() {

        if (!turn.equals(thisPlayer) || gameOver)
        {
            return "";
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                waiting = true;
                final String xml = createGameXML(true);
                myLastXML = xml;

                Cloud cloud = new Cloud();
                synchronized (this) {
                    while (!cloud.saveGame(xml)) {
                        try {
                            wait(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        if (thisPlayer.equals(player1Name))
        {
            return player2Name;
        }
        else
        {
            return player1Name;
        }
    }

    public boolean getWaiting() throws ParserConfigurationException {
        if(waiting)
        {
            setGameState();
        }
        return true;
    }

    public void getStartTime() {
        if (turn.equals(thisPlayer) && startTurnTime != 0)
        {
            startTimer(startTurnTime);
        }
    }
}
