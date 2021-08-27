package chessboard.pieces;

import chessboard.ChessboardGenerator;
import chessboard.Main;
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
        direction = color == Colors.WHITE ? -1 : 1;
    }

    public List<Position> getPossibleMoves(Vector<Piece> pieces) {
        List<Position> positions = new ArrayList<>();
        //check positions
        //possible moves for pawn:
        // up, beating and en passant

        //TODO dokończyć bicie w przelocie oraz pokomentować

        Piece frontPiece = ChessboardGenerator.getPiece(row+direction, column, pieces);

        //Piece leftPiece = ChessboardGenerator.getPiece(row, column-1, pieces);
        //Piece rightPiece = ChessboardGenerator.getPiece(row, column+1, pieces);

        //even if position is out of the board pieces will be null
        Piece rightFrontPiece = ChessboardGenerator.getPiece(row+direction, column+1, pieces);
        Piece leftFrontPiece = ChessboardGenerator.getPiece(row+direction, column-1, pieces);

        //move forward
        if(frontPiece==null) {
            //check if forward move is out of the board
            if(row+direction!=Main.columns&&row+direction!=-1) positions.add(new Position(row+direction, column));
        }
        //normal beating
        if(rightFrontPiece!=null) {
            if(rightFrontPiece.getColor()!=color) {
                positions.add(new Position(row+direction, column+1));
            }
        }
        if(leftFrontPiece!=null) {
            if(leftFrontPiece.getColor()!=color) {
                positions.add(new Position(row+direction, column-1));
            }
        }


        //if(rightFrontPiece!=null)
        System.out.println("Obliczono "+positions.size()+" pozycji");
        return positions;
    }
}
