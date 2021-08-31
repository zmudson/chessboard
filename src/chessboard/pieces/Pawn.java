package chessboard.pieces;

import chessboard.ChessboardGenerator;
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
    private boolean moved = false;

    // en passant
    private boolean isPossibleToBeCapturedByEnPassant = false;
    private static final int enPassantLineNumber = 3;

    public Pawn(int row, int column, Piece.Colors color, ChessboardGenerator chessboardGenerator) {
        super(row, column, name, filename, power, color, chessboardGenerator);
        //if color is white direction is to go up
        direction = color == Colors.WHITE ? -1 : 1;
    }

    @Override
    public void move(int row, int column) {
        // handle en passant
        unsetEnPassant();

        // check if did move before and handle it
        if(!moved){
            moved = true;

            // check if it is double move
            if(Math.abs(this.row-row) == 2) {
                isPossibleToBeCapturedByEnPassant = true;
                ChessboardGenerator.setPawnAbleToBeCapturedByEnPassant(this);
            }
        }

        List<Piece> pieces = chessboardGenerator.getPieces();

        Piece piece = chessboardGenerator.getPiece(row, column);

        if (piece != null)
            capture(piece);
        else if(this.column != column){
            Piece pawn = chessboardGenerator.getPiece(row - direction, column);
            capture(pawn);
        }
        changePosition(row, column);

        // handle promotion
        if(row == 0 || row == Main.rows - 1){
            // remove pawn from chessboard
            capture(this);

            // add queen to chessboard
            Piece queen = new Queen(row, column, color, chessboardGenerator);
            pieces.add(queen);
            if(color == Colors.WHITE)
                chessboardGenerator.getWhitePieces().add(queen);
            else if(color == Colors.BLACK)
                chessboardGenerator.getBlackPieces().add(queen);
            chessboardGenerator.handleImageDraw(queen);
        }

        chessboardGenerator.unpinAndResetAllDirections();
        chessboardGenerator.checkAllPinsAndChecks();
    }

    public List<Move> getPossibleMoves(List<Piece> pieces) {

        List<Move> possibleMoves = new ArrayList<>();

        if(!canMove()) return possibleMoves;

        //check positions
        //possible moves for pawn:
        // up, capture and en passant

        Piece frontPiece = chessboardGenerator.getPiece(row + direction, column);

        //even if position is out of the board pieces will be null
        Piece rightFrontPiece = chessboardGenerator.getPiece(row + direction, column + 1);
        Piece leftFrontPiece = chessboardGenerator.getPiece(row + direction, column - 1);

        //move forward, check if forward move is out of the board
        if(frontPiece == null&&(! blockedDirections.contains(BlockedDirections.VERTICAL))) {
            possibleMoves.add(new Move(this, getPosition(), new Position(row + direction, column)));

            // double move
            if(!moved) {
                Piece doubleForwardPiece = chessboardGenerator.getPiece(row + 2 * direction, column);
                if(doubleForwardPiece == null) {
                    possibleMoves.add(new Move(this, getPosition(), new Position(row + 2 * direction, column)));
                }
            }
        }

        // normal capture
        if(rightFrontPiece != null && MoveHandler.isValid(this, rightFrontPiece) && ! blockedDirections.contains(BlockedDirections.DOWN_UP_DIAGONAL)) {
            possibleMoves.add(new Move(this, getPosition(), new Position(row + direction, column + 1)));
        }

        if(leftFrontPiece != null && MoveHandler.isValid(this, leftFrontPiece) &&! blockedDirections.contains(BlockedDirections.UP_DOWN_DIAGONAL)) {
            possibleMoves.add(new Move(this, getPosition(), new Position(row + direction, column - 1)));
        }

        // check if en passant is possible
        if(row == enPassantLineNumber || row == Main.rows - 1 - enPassantLineNumber){

            //en passant
            Piece leftPiece = chessboardGenerator.getPiece(row,column - 1);
            Piece rightPiece = chessboardGenerator.getPiece(row,column + 1);

//            we have to check if doing en passant creates a check
//            boolean presentKing = false;
//            boolean possibleChecker = false;
//            boolean pawnBetweenTwo = false;
//            for(int i=0; i<Main.columns; i++) {
//                Piece piece = ChessboardGenerator.getPiece(row, i, pieces);
//                if(piece instanceof King) {
//                    presentKing = true;
//                }
//                else if(piece instanceof Queen || piece instanceof Rook) {
//                    possibleChecker = true;
//                }
//                else if(piece==this&&((presentKing&&!possibleChecker) || (!presentKing&&possibleChecker))) {
//                    pawnBetweenTwo = true;
//                }
//            }

            //left
            if(leftPiece instanceof Pawn && ((Pawn) leftPiece).isPossibleToBeCapturedByEnPassant()) {
                boolean canBePinned = false;
                boolean add = true;
                for(int column = this.column + 1; column <= Main.columns - 1; column++){
                    if(chessboardGenerator.getPiece(row, column) instanceof King){
                        canBePinned = true;
                        break;
                    }
                }
                if(canBePinned){
                    for(int column = this.column - 1; column >= 0; column--){
                        Piece piece = chessboardGenerator.getPiece(row, column);
                        if(piece instanceof Rook || piece instanceof Queen){
                            add = false;
                            break;
                        }
                    }
                }
                if(add)
                    possibleMoves.add(new Move(this, getPosition(), new Position(row + direction, column - 1)));
            }

            //right
            else if(rightPiece instanceof Pawn && ((Pawn) rightPiece).isPossibleToBeCapturedByEnPassant()) {
                boolean canBePinned = false;
                boolean add = true;
                for(int column = this.column - 1; column >= 0; column--){
                    if(chessboardGenerator.getPiece(row, column) instanceof King){
                        canBePinned = true;
                        break;
                    }
                }
                if(canBePinned){
                    for(int column = this.column + 1; column <= Main.columns - 1; column++){
                        Piece piece = chessboardGenerator.getPiece(row, column);
                        if(piece instanceof Rook || piece instanceof Queen){
                            add = false;
                            break;
                        }
                    }
                }
                if(add)
                    possibleMoves.add(new Move(this, getPosition(), new Position(row + direction, column + 1)));
            }
        }
        removeIllegalMoves(possibleMoves);
        return possibleMoves;
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
