package chessboard.pieces;

import utils.Position;
import java.util.List;

public class Bishop extends Piece {
    public static final double power = 300;
    public static final String name = "bishop";
    private static final String filename = "";

    public Bishop(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
    }

    public List<Position> getPossibleMoves(List<Piece> pieces){
        return getDiagonalMoves(pieces);
    }
}
