package chessboard.pieces;

import chessboard.ChessboardGenerator;
import utils.Move;
import utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public static final double power = 300;
    public static final String name = "bishop";
    private static final String filename = "";

    public Bishop(int row, int column, Piece.Colors color, ChessboardGenerator chessboardGenerator) {
        super(row, column, name, filename, power, color, chessboardGenerator);
    }

    public List<Move> getPossibleMoves(List<Piece> pieces){
        if(!canMove()) return new ArrayList<>();
        List<Move> possibleMoves = getDiagonalMoves(pieces);
        removeIllegalMoves(possibleMoves);
        return possibleMoves;
    }
}
