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

    public Pawn(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
        //if color is white direction is to go up
        direction = color == Colors.WHITE ? -1 : 1;
    }

    @Override
    public void move(int row, int column, ChessboardGenerator chessboardGenerator) {
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

        Piece piece = ChessboardGenerator.getPiece(row, column, pieces);

        if (piece != null)
            capture(piece, chessboardGenerator);
        else if(this.column != column){
            Piece pawn = ChessboardGenerator.getPiece(row - direction, column, pieces);
            capture(pawn, chessboardGenerator);
        }
        changePosition(row, column);

        // handle promotion
        if(row == 0 || row == Main.rows - 1){
            // remove pawn from chessboard
            capture(this, chessboardGenerator);

            // add queen to chessboard
            Piece queen = new Queen(row, column, color);
            pieces.add(queen);
            if(color == Colors.WHITE)
                chessboardGenerator.getWhitePieces().add(queen);
            else if(color == Colors.BLACK)
                chessboardGenerator.getBlackPieces().add(queen);
            chessboardGenerator.handleImageDraw(queen);
        }
    }

    public List<Move> getPossibleMoves(List<Piece> pieces) {

        List<Move> possiblePositions = new ArrayList<>();

        //check positions
        //possible moves for pawn:
        // up, capture and en passant

        Piece frontPiece = ChessboardGenerator.getPiece(row + direction, column, pieces);

        //even if position is out of the board pieces will be null
        Piece rightFrontPiece = ChessboardGenerator.getPiece(row + direction, column + 1, pieces);
        Piece leftFrontPiece = ChessboardGenerator.getPiece(row + direction, column - 1, pieces);

        //move forward, check if forward move is out of the board
        if(frontPiece == null) {
            possiblePositions.add(new Move(this, getPosition(), new Position(row + direction, column)));

            // double move
            if(!moved) {
                Piece doubleForwardPiece = ChessboardGenerator.getPiece(row + 2 * direction, column, pieces);
                if(doubleForwardPiece == null) {
                    possiblePositions.add(new Move(this, getPosition(), new Position(row + 2 * direction, column)));
                }
            }
        }

        // normal capture
        if(rightFrontPiece != null && MoveHandler.isValid(this, rightFrontPiece)) {
            possiblePositions.add(new Move(this, getPosition(), new Position(row + direction, column + 1)));
        }

        if(leftFrontPiece != null && MoveHandler.isValid(this, leftFrontPiece)) {
            possiblePositions.add(new Move(this, getPosition(), new Position(row + direction, column - 1)));
        }

        // check if en passant is possible
        if(row == enPassantLineNumber || row == Main.rows - 1 - enPassantLineNumber){

            //en passant
            Piece leftPiece = ChessboardGenerator.getPiece(row,column - 1, pieces);
            Piece rightPiece = ChessboardGenerator.getPiece(row,column + 1, pieces);

            //left
            if(leftPiece instanceof Pawn && ((Pawn) leftPiece).isPossibleToBeCapturedByEnPassant()) {
                possiblePositions.add(new Move(this, getPosition(), new Position(row + direction, column - 1)));
            }

            //right
            if(rightPiece instanceof Pawn && ((Pawn) rightPiece).isPossibleToBeCapturedByEnPassant()) {
                possiblePositions.add(new Move(this, getPosition(), new Position(row + direction, column + 1)));
            }
        }

        return possiblePositions;
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
