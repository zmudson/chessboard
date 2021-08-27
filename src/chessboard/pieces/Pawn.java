package chessboard.pieces;

import chessboard.ChessboardGenerator;
import utils.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Pawn extends Piece {
    public static final double power = 100;
    public static final String name = "pawn";
    private static final String filename = "";

    private int direction;

    public Pawn(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
        //if color is white direction is to go up
        direction = color == Colors.WHITE ? 1 : -1;
    }

    public List<Position> getPossibleMoves(Vector<Piece> pieces) {
        List<Position> positions = new ArrayList<>();
        //check positions
        //possible moves for pawn:
        // up, beating and en passant

        //TODO dokończyć

        Piece frontPiece = ChessboardGenerator.getPiece(row, column+direction, pieces);
        Piece leftPiece = ChessboardGenerator.getPiece(row-1, column, pieces);
        Piece rightPiece = ChessboardGenerator.getPiece(row+1, column, pieces);
        Piece rightFrontPiece = ChessboardGenerator.getPiece(row+1, column+direction, pieces);
        Piece leftFrontPiece = ChessboardGenerator.getPiece(row-1, column+direction, pieces);

        if(frontPiece==null)
        {
            positions.add(new Position(row, column+direction));
        }
        if(leftPiece!=null && leftFrontPiece==null) {
            if (leftPiece.getColor()!=color) positions.add(new Position(row-1, column+direction));
        }
        if(rightPiece!=null && rightFrontPiece==null) {
            if (rightPiece.getColor()!=color) positions.add(new Position(row+1, column+direction));
        }
        //if(rightFrontPiece!=null)

        return positions;
    }
}
