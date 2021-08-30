package chessboard.pieces;

import chessboard.ChessboardGenerator;
import chessboard.Main;
import utils.Move;
import utils.MoveHandler;
import utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public static final double power = 300;
    public static final String name = "knight";
    private static final String filename = "";
    public static final int maxPossibleMovesNumber = 8;

    public Knight(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
    }

    public List<Move> getPossibleMoves(List<Piece> pieces){
        
        List<Move> possibleMoves = new ArrayList<>();

        if(isPinned()||!canMove()) return possibleMoves;

        for(int i = 0, rowMove = -2, columnMove; i < maxPossibleMovesNumber; i++){

            // set column move
            columnMove = (1 + Math.abs(rowMove % 2)) * (i % 2 == 0 ? -1 : 1);

            // change current position
            int row = this.row + rowMove;
            int column = this.column + columnMove;

            Piece piece = ChessboardGenerator.getPiece(row, column, pieces);

            // add move to an array if is legal
            if(row >= 0 && row <= Main.rows - 1 && column >= 0 && column <= Main.columns - 1 && MoveHandler.isValid(this, piece))
                possibleMoves.add(new Move(this, getPosition(), new Position(row, column)));

            // set row move
            if(i % 2 != 0){
                rowMove++;
                if(rowMove == 0)
                    rowMove++;
            }
        }

        return possibleMoves;
    }
}
