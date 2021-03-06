package chessboard.pieces;

import chessboard.Chessboard;
import utils.Move;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public static final double power = 900;
    public static final String name = "queen";
    private static final String filename = "";

    public Queen(int row, int column, Piece.Colors color, Chessboard chessboard) {
        super(row, column, name, filename, power, color, chessboard);
    }

    public List<Move> getPossibleMoves(){

        if(!canMove) return new ArrayList<>();
        // get all straight moves
        List<Move> possibleMoves = getStraightMoves();
        // append to array all diagonal moves
        possibleMoves.addAll(getDiagonalMoves());
        removeIllegalMoves(possibleMoves);
        return possibleMoves;
    }
}
