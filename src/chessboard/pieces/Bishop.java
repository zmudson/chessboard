package chessboard.pieces;

import utils.Move;
import utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public static final double power = 300;
    public static final String name = "bishop";
    private static final String filename = "";

    public Bishop(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
    }

    public List<Move> getPossibleMoves(List<Piece> pieces){
        if(!canMove()) return new ArrayList<>();
        return getDiagonalMoves(pieces);
    }
}
