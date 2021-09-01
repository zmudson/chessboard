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
    private boolean hasMoved = false;
    private Position castlingRightPosition;
    private Position castlingLeftPosition;
    private Piece rookRight;
    private Piece rookLeft;
    private boolean canCastleNow;

    public King(int row, int column, Piece.Colors color, ChessboardGenerator chessboardGenerator) {
        super(row, column, name, filename, power, color, chessboardGenerator);
    }

    public List<Move> getPossibleMoves(){
        /* TODO */
        // castling
        canCastleNow = false;
        castlingLeftPosition = null;
        castlingRightPosition = null;
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
        ArrayList[][] movesBoard = null;
        if(color == chessboardGenerator.getColorToMove()) {
            movesBoard = chessboardGenerator.getMoveGenerator().generateMoves(enemyColor);

            //castling
            if (!hasMoved && !chessboardGenerator.isCheck()) {
                rookRight = chessboardGenerator.getPiece(row, Main.columns - 1);
                rookLeft = chessboardGenerator.getPiece(row, 0);
                //short
                //not null condition because intelliJ said so
                if (rookRight instanceof Rook && ((Rook) rookRight).canCastle()) {
                    boolean isCastlingPossible = true;
                    for (int i = 1; i <= 2; i++) {
                        if (!(chessboardGenerator.getPiece(row, column + i) == null && movesBoard[row][column + i] == null)) {
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
                        if (!(chessboardGenerator.getPiece(row, column - i) == null && movesBoard[row][column - i] == null)) {
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
                Piece piece = chessboardGenerator.getPiece(row, column);
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
