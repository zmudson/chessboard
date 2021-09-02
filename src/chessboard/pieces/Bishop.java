package chessboard.pieces;

import chessboard.ChessboardGenerator;
import utils.Move;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public static final double power = 300;
    public static final String name = "bishop";
    private static final String filename = "";

    public Bishop(int row, int column, Piece.Colors color, ChessboardGenerator chessboardGenerator) {
        super(row, column, name, filename, power, color, chessboardGenerator);
    }

    public List<Move> getPossibleMoves(){
        if(!canMove) return new ArrayList<>();
        List<Move> possibleMoves = getDiagonalMoves();
        removeIllegalMoves(possibleMoves);
        return possibleMoves;
    }
}
