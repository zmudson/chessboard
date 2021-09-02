package chessboard.pieces;

import chessboard.Chessboard;
import chessboard.Main;
import utils.Move;
import utils.MoveHandler;
import utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public static final double power = 100;
    public static final String name = "pawn";
    private static final String filename = "";

    private final int direction;
    //to check if pawn can perform double field move
    private boolean hasMoved = false;

    // en passant
    private boolean isPossibleToBeCapturedByEnPassant = false;
    private static final int enPassantLineNumber = 3;
    private enum Directions{
        LEFT,
        RIGHT
    }

    public Pawn(int row, int column, Piece.Colors color, Chessboard chessboard) {
        super(row, column, name, filename, power, color, chessboard);
        //if color is white direction is to go up
        direction = color == Colors.WHITE ? -1 : 1;
    }

    @Override
    public void move(int row, int column) {
        // handle en passant
        unsetEnPassant();

        // check if did move before and handle it
        if(!hasMoved){
            hasMoved = true;

            // check if it is double move
            if(Math.abs(this.row-row) == 2) {
                isPossibleToBeCapturedByEnPassant = true;
                Chessboard.setPawnAbleToBeCapturedByEnPassant(this);
            }
        }

        List<Piece> pieces = chessboard.getPieces();

        Piece piece = chessboard.getPiece(row, column);

        if (piece != null)
            capture(piece);
        else if(this.column != column){
            Piece pawn = chessboard.getPiece(row - direction, column);
            capture(pawn);
        }
        changePosition(row, column);

        // handle promotion
        if(row == 0 || row == Main.rows - 1){
            // remove pawn from chessboard
            capture(this);

            // add queen to chessboard
            Piece queen = new Queen(row, column, color, chessboard);
            pieces.add(queen);
            if(color == Colors.WHITE)
                chessboard.getWhitePieces().add(queen);
            else if(color == Colors.BLACK)
                chessboard.getBlackPieces().add(queen);
            chessboard.handleImageDraw(queen);
        }
    }

    public List<Move> getPossibleMoves() {

        List<Move> possibleMoves = new ArrayList<>();

        if(!canMove) return possibleMoves;

        if(color == chessboard.getColorToMove()){
            Piece frontPiece = chessboard.getPiece(row + direction, column);

            //move forward, check if forward move is out of the board
            if(frontPiece == null&&(! blockedDirections.contains(BlockedDirections.VERTICAL))) {
                possibleMoves.add(new Move(this, getPosition(), new Position(row + direction, column)));

                // double move
                if(!hasMoved) {
                    Piece doubleForwardPiece = chessboard.getPiece(row + 2 * direction, column);
                    if(doubleForwardPiece == null) {
                        possibleMoves.add(new Move(this, getPosition(), new Position(row + 2 * direction, column)));
                    }
                }
            }

            // check if en passant is possible
            if(row == enPassantLineNumber || row == Main.rows - 1 - enPassantLineNumber){

                //en passant
                Piece leftPiece = chessboard.getPiece(row,column - 1);
                Piece rightPiece = chessboard.getPiece(row,column + 1);

                //left
                if(leftPiece instanceof Pawn && ((Pawn) leftPiece).isPossibleToBeCapturedByEnPassant()) {
                    if(isEnPassantPinned(Directions.LEFT))
                        possibleMoves.add(new Move(this, getPosition(), new Position(row + direction, column - 1)));
                }
                //right
                else if(rightPiece instanceof Pawn && ((Pawn) rightPiece).isPossibleToBeCapturedByEnPassant()) {
                    if(isEnPassantPinned(Directions.RIGHT))
                        possibleMoves.add(new Move(this, getPosition(), new Position(row + direction, column + 1)));
                }
            }
            //even if position is out of the board pieces will be null
            Piece rightFrontPiece = chessboard.getPiece(row + direction, column + 1);
            Piece leftFrontPiece = chessboard.getPiece(row + direction, column - 1);

            // normal capture
            if(rightFrontPiece != null && MoveHandler.isValid(this, rightFrontPiece) && ! blockedDirections.contains(BlockedDirections.DOWN_UP_DIAGONAL)) {
                possibleMoves.add(new Move(this, getPosition(), new Position(row + direction, column + 1)));
            }

            if(leftFrontPiece != null && MoveHandler.isValid(this, leftFrontPiece) &&! blockedDirections.contains(BlockedDirections.UP_DOWN_DIAGONAL)) {
                possibleMoves.add(new Move(this, getPosition(), new Position(row + direction, column - 1)));
            }
        }else{
            if(column - 1 >= 0)
                possibleMoves.add(new Move(this, getPosition(), new Position(row + direction, column - 1)));
            if(column + 1 <= Main.rows - 1)
                possibleMoves.add(new Move(this, getPosition(), new Position(row + direction, column + 1)));
        }

        removeIllegalMoves(possibleMoves);
        return possibleMoves;
    }

    private boolean isEnPassantPinned(Directions direction){
        boolean add = false;
        Piece king = null;
        Piece enemy = null;

        int dx;

        dx = direction == Directions.LEFT ? -2 : -1;

        for(int column = this.column + dx; column >= 0; column--){
            Piece currentPiece = chessboard.getPiece(row, column);
            if(currentPiece != null){
                if(currentPiece instanceof King && currentPiece.getColor() == color)
                    king = currentPiece;
                else if(currentPiece instanceof Queen || currentPiece instanceof Rook)
                    enemy = currentPiece;
                else
                    add = true;
                break;
            }
        }
        if(king != null || enemy != null){

            dx = direction == Directions.LEFT ? 1 : 2;

            for(int column = this.column + dx; column <= Main.columns - 1; column++){
                Piece currentPiece = chessboard.getPiece(row, column);
                if(currentPiece != null){
                    if(king != null && (currentPiece instanceof Queen || currentPiece instanceof Rook))
                        add = false;
                    else add = enemy == null || !(currentPiece instanceof King);
                    break;
                }
            }
        }
        return add;
    }

    public boolean isPossibleToBeCapturedByEnPassant() {
        return isPossibleToBeCapturedByEnPassant;
    }

    public void setIsPossibleToBeCapturedByEnPassant(boolean isPossibleToBeCapturedByEnPassant) {
        this.isPossibleToBeCapturedByEnPassant = isPossibleToBeCapturedByEnPassant;
    }

    public int getDirection() {
        return direction;
    }
}
