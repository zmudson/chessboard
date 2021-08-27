package chessboard.pieces;

import chessboard.ChessboardGenerator;
import chessboard.Main;
import utils.MoveHandler;
import utils.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Knight extends Piece {
    public static final double power = 300;
    public static final String name = "knight";
    private static final String filename = "";
    public static final int maxPossibleMovesNumber = 8;

    public Knight(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
    }

    public List<Position> getPossibleMoves(Vector<Piece> pieces){
        /* TODO */
        List<Position> possibleMoves = new ArrayList<>();

        // arrays of possible moves in right direction
        int[] rowMoves = new int[] {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] columnMoves = new int[] {-1, 1, -2, 2, -2, 2, -1, 1};

        for(int i = 0; i < maxPossibleMovesNumber; i++){
            // change current position
            int row = this.row + rowMoves[i];
            int column = this.column + columnMoves[i];
            Piece piece = ChessboardGenerator.getPiece(row, column, pieces);

            // add move to an array if is legal
            if(row >= 0 && row <= Main.rows - 1 && column >= 0 && column <= Main.columns - 1 && MoveHandler.isValid(this, piece))
                possibleMoves.add(new Position(row, column));
        }

        return possibleMoves;
    }
}
