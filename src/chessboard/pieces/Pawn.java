package chessboard.pieces;

import chessboard.ChessboardGenerator;
import chessboard.Main;
import utils.EventHandler;
import utils.MoveHandler;
import utils.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Pawn extends Piece {
    public static final double power = 100;
    public static final String name = "pawn";
    private static final String filename = "";

    private final int direction;
    //to check if pawn can perform double field move
    private boolean moved = false;

    // en passant
    private boolean possibleToBeBeatenEnPassant = false;
    private int enPassantLineNumber = 3;

    public Pawn(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
        //if color is white direction is to go up
        direction = color == Colors.WHITE ? -1 : 1;
    }

    @Override
    public void move(int row, int column, ChessboardGenerator chessboardGenerator) {
        if(Math.abs(this.row-row)==2) {
            possibleToBeBeatenEnPassant = true;
            ChessboardGenerator.setPawnAbleToBeBeatenEnPessant(this);
        }
        super.move(row,column, chessboardGenerator);
        moved = true;
    }

    public List<Position> getPossibleMoves(Vector<Piece> pieces) {

        List<Position> possiblePositions = new ArrayList<>();

        //check positions
        //possible moves for pawn:
        // up, capture and en passant

        Piece frontPiece = ChessboardGenerator.getPiece(row + direction, column, pieces);

        //even if position is out of the board pieces will be null
        Piece rightFrontPiece = ChessboardGenerator.getPiece(row + direction, column + 1, pieces);
        Piece leftFrontPiece = ChessboardGenerator.getPiece(row + direction, column - 1, pieces);

        //move forward, check if forward move is out of the board
        if(frontPiece == null && (row + direction >= 0 && row + direction <= Main.rows - 1)) {
            possiblePositions.add(new Position(row + direction, column));

            // double move
            if(!moved) {
                Piece doubleForwardPiece = ChessboardGenerator.getPiece(row + 2 * direction, column, pieces);
                if(doubleForwardPiece == null) {
                    possiblePositions.add(new Position(row + 2 * direction, column));
                }
            }
        }

        // normal capture
        if(rightFrontPiece != null && MoveHandler.isValid(this, rightFrontPiece)) {
            possiblePositions.add(new Position(row + direction, column + 1));
        }

        if(leftFrontPiece != null && MoveHandler.isValid(this, leftFrontPiece)) {
            possiblePositions.add(new Position(row + direction, column - 1));
        }

        // check if en passant is possible
        if(row == enPassantLineNumber || row == Main.rows - 1 - enPassantLineNumber){
            //en pessant
            Piece leftPiece = ChessboardGenerator.getPiece(row,column - 1, pieces);
            Piece rightPiece = ChessboardGenerator.getPiece(row,column + 1, pieces);
            //left
            if(leftPiece != null) {
                if(leftPiece instanceof Pawn) {
                    if(((Pawn) leftPiece).isPossibleToBeBeatenEnPassant()) {
                        possiblePositions.add(new Position(row + direction, column - 1));
                    }
                }
            }
            //right
            if(rightPiece != null) {
                if(rightPiece instanceof Pawn) {
                    if(((Pawn) rightPiece).isPossibleToBeBeatenEnPassant()) {
                        possiblePositions.add(new Position(row + direction, column + 1));
                    }
                }
            }
        }

        return possiblePositions;
    }

    public boolean isPossibleToBeBeatenEnPassant() {
        return possibleToBeBeatenEnPassant;
    }

    public void setPossibleToBeBeatenEnPassant(boolean possibleToBeBeatenEnPassant) {
        this.possibleToBeBeatenEnPassant = possibleToBeBeatenEnPassant;
    }
}
