package chessboard.pieces;

import chessboard.ChessboardGenerator;
import utils.Move;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public static final double power = 500;
    public static final String name = "rook";
    private static final String filename = "";

    private boolean canCastle = true;

    public static int LONG_CASTLING_ROOK_COLUMN = 3;
    public static int SHORT_CASTLING_ROOK_COLUMN = 5;

    public Rook(int row, int column, Piece.Colors color, ChessboardGenerator chessboardGenerator) {
        super(row, column, name, filename, power, color, chessboardGenerator);
    }

    public List<Move> getPossibleMoves(){
        if(!canMove) return new ArrayList<>();
        List<Move> possibleMoves = getStraightMoves();
        removeIllegalMoves(possibleMoves);
        return possibleMoves;
    }

    public void castle() {
        if(!canCastle) return;
        if(column==0) { //long castling
            changePosition(row, LONG_CASTLING_ROOK_COLUMN);
        }
        else { //column==Main.columns-1
            changePosition(row, SHORT_CASTLING_ROOK_COLUMN);
        }
        canCastle = false;
    }

    public boolean canCastle() {
        return canCastle;
    }
}
