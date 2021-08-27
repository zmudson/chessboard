package chessboard.pieces;

import chessboard.ChessboardGenerator;
import chessboard.Main;
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

        List<Position> possibleMoves = new ArrayList<>();

        // get possible moves from current position to the top of first diagonal \
        for(int column = this.column - 1, row = this.row - 1; column >= 0 && row >= 0; column--, row--){
            Piece piece = ChessboardGenerator.getPiece(row, column, pieces);
            if(!getPossibleMove(row, column, possibleMoves, piece))
                break;
        }

        // get possible moves from current position to the bottom of first diagonal \
        for(int column = this.column + 1, row = this.row + 1; column <= Main.columns - 1 && row <= Main.columns - 1; column++, row++){
            Piece piece = ChessboardGenerator.getPiece(row, column, pieces);
            if(!getPossibleMove(row, column, possibleMoves, piece))
                break;
        }

        // get possible moves from current position to the top of second diagonal /
        for(int column = this.column + 1, row = this.row - 1; column <= Main.columns - 1 && row >= 0; column++, row--){
            Piece piece = ChessboardGenerator.getPiece(row, column, pieces);
            if(!getPossibleMove(row, column, possibleMoves, piece))
                break;
        }

        // get possible moves from current position to the bottom of second diagonal /
        for(int column = this.column - 1, row = this.row + 1; column >= 0 && row <= Main.rows - 1; column--, row++){
            Piece piece = ChessboardGenerator.getPiece(row, column, pieces);
            if(!getPossibleMove(row, column, possibleMoves, piece))
                break;
        }

        return possibleMoves;
    }
}
