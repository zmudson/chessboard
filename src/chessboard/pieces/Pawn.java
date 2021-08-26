package chessboard.pieces;

import utils.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Pawn extends Piece {
    public static final double power = 100;
    public static final String name = "pawn";
    private static final String filename = "";

    private int direction;

    public Pawn(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
        //if color is white direction is to go up
        direction = color == Colors.WHITE ? 1 : -1;
    }

    public List<Position> getPossibleMoves(Vector<Piece> pieces) {
        List<Position> positions = new ArrayList<>();
        //check positions
        //possible moves for pawn:
        // up and en passant


        return positions;
    }
}
