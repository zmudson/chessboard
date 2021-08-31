package chessboard.pieces;

import chessboard.ChessboardGenerator;
import utils.Move;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public static final double power = 900;
    public static final String name = "queen";
    private static final String filename = "";

    public Queen(int row, int column, Piece.Colors color, ChessboardGenerator chessboardGenerator) {
        super(row, column, name, filename, power, color, chessboardGenerator);
    }

    public List<Move> getPossibleMoves(List<Piece> pieces){

        if(!canMove()) return new ArrayList<>();
        // get all straight moves
        List<Move> possibleMoves = getStraightMoves();
        // append to array all diagonal moves
        possibleMoves.addAll(getDiagonalMoves(pieces));
        removeIllegalMoves(possibleMoves);
        return possibleMoves;
    }
}
