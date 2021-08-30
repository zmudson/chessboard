package chessboard.pieces;

import utils.Move;
import utils.Position;

import java.util.List;

public class Queen extends Piece {
    public static final double power = 900;
    public static final String name = "queen";
    private static final String filename = "";

    public Queen(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
    }

    public List<Move> getPossibleMoves(List<Piece> pieces){

        // get all straight moves
        List<Move> possibleMoves = getStraightMoves(pieces);

        // append to array all diagonal moves
        possibleMoves.addAll(getDiagonalMoves(pieces));

        return possibleMoves;
    }
}
