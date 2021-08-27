package chessboard.pieces;

import utils.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Bishop extends Piece {
    public static final double power = 300;
    public static final String name = "bishop";
    private static final String filename = "";

    public Bishop(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
    }

    public List<Position> getPossibleMoves(Vector<Piece> pieces){
        /* TODO */
        return new ArrayList<>();
    }
}
