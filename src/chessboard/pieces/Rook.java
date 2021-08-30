package chessboard.pieces;

import chessboard.ChessboardGenerator;
import utils.Move;
import utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public static final double power = 500;
    public static final String name = "rook";
    private static final String filename = "";

    public Rook(int row, int column, Piece.Colors color, ChessboardGenerator chessboardGenerator) {
        super(row, column, name, filename, power, color, chessboardGenerator);
    }

    public List<Move> getPossibleMoves(List<Piece> pieces){
        /* TODO */
        // castling
        if(!canMove()) return new ArrayList<>();
        List<Move> possibleMoves = getStraightMoves(pieces);
        removeIllegalMoves(possibleMoves);
        return possibleMoves;
    }
}
