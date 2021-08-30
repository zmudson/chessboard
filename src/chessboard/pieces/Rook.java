package chessboard.pieces;

import utils.Move;
import utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public static final double power = 500;
    public static final String name = "rook";
    private static final String filename = "";

    public Rook(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
    }

    public List<Move> getPossibleMoves(List<Piece> pieces){
        /* TODO */
        // castling
        if(!canMove()) return new ArrayList<>();

        return getStraightMoves(pieces);
    }
}
