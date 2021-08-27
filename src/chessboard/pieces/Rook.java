package chessboard.pieces;

import chessboard.ChessboardGenerator;
import chessboard.Main;
import utils.MoveHandler;
import utils.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Rook extends Piece {
    public static final double power = 500;
    public static final String name = "rook";
    private static final String filename = "";

    public Rook(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
    }

    // add move to possible moves array and return true if next position is available or false otherwise
    private boolean getPossibleMove(int row, int column, List<Position> possibleMoves, Piece piece){
        boolean isNext = false;
        if(MoveHandler.isValid(this, piece)){
            possibleMoves.add(new Position(row, column));
            if(piece == null)
                isNext = true;
        }
        return isNext;
    }

    public List<Position> getPossibleMoves(Vector<Piece> pieces){
        /* TODO */
        // castling

        List<Position> possibleMoves = new ArrayList<>();

        // get possible moves from current position to left border
        for(int column = this.column - 1; column >= 0; column--){
            Piece piece = ChessboardGenerator.getPiece(this.row, column, pieces);
            if(!getPossibleMove(this.row, column, possibleMoves, piece))
                break;
        }

        // get possible moves from current position to right border
        for(int column = this.column + 1; column <= Main.columns - 1; column++){
            Piece piece = ChessboardGenerator.getPiece(this.row, column, pieces);
            if(!getPossibleMove(this.row, column, possibleMoves, piece))
                break;
        }

        // get possible moves from current position to top border
        for(int row = this.row - 1; row >= 0; row--){
            Piece piece = ChessboardGenerator.getPiece(row, this.column, pieces);
            if(!getPossibleMove(row, this.column, possibleMoves, piece))
                break;
        }

        // get possible moves from current position to bottom border
        for(int row = this.row + 1; row <= Main.rows - 1; row++){
            Piece piece = ChessboardGenerator.getPiece(row, this.column, pieces);
            if(!getPossibleMove(row, this.column, possibleMoves, piece))
                break;
        }

        return possibleMoves;
    }
}
