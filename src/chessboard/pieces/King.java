package chessboard.pieces;

import chessboard.Chessboard;
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
    private boolean hasMoved = false;
    private Position castlingRightPosition;
    private Position castlingLeftPosition;
    private Piece rookRight;
    private Piece rookLeft;
    private boolean canCastleNow;

    public King(int row, int column, Piece.Colors color, Chessboard chessboard) {
        super(row, column, name, filename, power, color, chessboard);
    }

    public List<Move> getPossibleMoves(){

        List<Move> possibleMoves = new ArrayList<>();

        if(!canMove) return possibleMoves;

        // castling
        canCastleNow = false;
        castlingLeftPosition = null;
        castlingRightPosition = null;

        int leftBorder = column - 1;
        int rightBorder = column + 1;
        int topBorder = row - 1;
        int bottomBorder = row + 1;

        // check if king is at the edge and handling overbound index problem
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
        ArrayList[][] movesBoard = null;
        if(color == chessboard.getColorToMove()) {
            movesBoard = chessboard.getMoveGenerator().generateMoves(enemyColor);

            //castling
            if (!hasMoved && !chessboard.isCheck()) {

                rookRight = chessboard.getPiece(row, Main.columns - 1);
                rookLeft = chessboard.getPiece(row, 0);

                //short
                if (rookRight instanceof Rook && ((Rook) rookRight).canCastle()) {
                    boolean isCastlingPossible = true;
                    for (int i = 1; i <= 2; i++) {
                        if (!(chessboard.getPiece(row, column + i) == null && movesBoard[row][column + i] == null)) {
                            isCastlingPossible = false;
                            break;
                        }
                    }
                    if (isCastlingPossible) {
                        castlingRightPosition = new Position(row, column + 2);
                        possibleMoves.add(new Move(this, this.getPosition(), castlingRightPosition));
                        canCastleNow = true;
                    }
                }

                //long
                if (rookLeft instanceof Rook && ((Rook) rookLeft).canCastle()) {
                    boolean isCastlingPossible = true;
                    for (int i = 1; i <= 3; i++) {
                        if (!(chessboard.getPiece(row, column - i) == null && movesBoard[row][column - i] == null)) {
                            isCastlingPossible = false;
                            break;
                        }
                    }
                    if (isCastlingPossible) {
                        castlingLeftPosition = new Position(row, column - 2);
                        possibleMoves.add(new Move(this, this.getPosition(),castlingLeftPosition));
                        canCastleNow = true;
                    }
                }
            }
        }

        for(int row = topBorder; row <= bottomBorder; row++){
            for(int column = leftBorder; column <= rightBorder; column++){
                Piece piece = chessboard.getPiece(row, column);
                if(MoveHandler.isValid(this, piece) && (movesBoard == null ||
                        (movesBoard[row][column] == null))){

                    possibleMoves.add(new Move(this, getPosition(), new Position(row, column)));
                }
            }
        }
        return possibleMoves;
    }

    @Override
    public void move(int row, int column) {
        if(canCastleNow) {
            if(castlingRightPosition!= null && castlingRightPosition.getColumn()==column && castlingRightPosition.getRow()==row) ((Rook)rookRight).castle();
            else if(castlingLeftPosition.getColumn() == column && castlingLeftPosition.getRow() == row) ((Rook)rookLeft).castle();
        }
        super.move(row, column);

        hasMoved = true;
    }
}
