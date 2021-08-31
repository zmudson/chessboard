package chessboard.pieces;

import chessboard.ChessboardGenerator;
import chessboard.Main;
import utils.Move;
import utils.MoveHandler;
import utils.Position;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public static final double power = 1000;
    public static final String name = "king";
    private static final String filename = "";

    public King(int row, int column, Piece.Colors color, ChessboardGenerator chessboardGenerator) {
        super(row, column, name, filename, power, color, chessboardGenerator);
    }

    public List<Move> getPossibleMoves(List<Piece> pieces){
        /* TODO */
        // castling
        // check potential check

        List<Move> possibleMoves = new ArrayList<>();

        /* consider remove this line */
        if(!canMove()) return possibleMoves;

        int leftBorder = column - 1;
        int rightBorder = column + 1;
        int topBorder = row - 1;
        int bottomBorder = row + 1;

        // check if king is on wing and handling overbound index problem
        if(column == 0)
            leftBorder++;
        else if(column == Main.columns - 1)
            rightBorder--;

        // check if king is on top or bottom side and handling overbound index problem
        if(row == 0)
            topBorder++;
        else if(row == Main.rows - 1)
            bottomBorder--;

        // iterate through available positions and the legals positions to list
        Colors enemyColor = color == Colors.WHITE ? Colors.BLACK : Colors.WHITE;
        ArrayList<Move>[][] movesBoard = chessboardGenerator.getMoveGenerator().generateMoves(enemyColor);
        for(int row = topBorder; row <= bottomBorder; row++){
            for(int column = leftBorder; column <= rightBorder; column++){
                if(movesBoard[row][column] == null){
                    Piece piece = chessboardGenerator.getPiece(row, column);
                    if(MoveHandler.isValid(this, piece)) {
                        possibleMoves.add(new Move(this, getPosition(), new Position(row, column)));
                    }
                }
            }
        }
        removeIllegalMoves(possibleMoves);
        return possibleMoves;
    }
}
