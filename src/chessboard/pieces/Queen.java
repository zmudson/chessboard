package chessboard.pieces;

import utils.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Queen extends Piece {
    public static final double power = 900;
    public static final String name = "queen";
    private static final String filename = "";

    public Queen(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
    }

    public List<Position> getPossibleMoves(Vector<Piece> pieces){

        // get all straight moves
        List<Position> possibleMoves = getStraightMoves(pieces);

        // append to array all diagonal moves
        possibleMoves.addAll(getDiagonalMoves(pieces));

        return possibleMoves;
    }
}
