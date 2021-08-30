package utils;

import chessboard.Main;
import chessboard.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    private ArrayList<Piece> pieces;
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;

    public MoveGenerator(ArrayList<Piece> pieces, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        this.pieces = pieces;
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
    }

    public ArrayList<Move>[][] generatorMoves(Piece.Colors color){
        ArrayList<Piece> pieces = null;

        if(color == Piece.Colors.WHITE)
            pieces = whitePieces;
        else if(color == Piece.Colors.BLACK)
            pieces = blackPieces;

        Object[][] movesBoard = new Object[Main.rows][Main.columns];

        if(pieces != null){
            for (Piece piece : pieces){
                List<Move> possibleMoves = piece.getPossibleMoves(pieces);
                Object movesList = movesBoard[piece.getRow()][piece.getColumn()];
                if(movesList == null)
                    movesList = new ArrayList<Move>();
//                else
//                    ((ArrayList<Move>) movesList).add()
            }
        }

        return (ArrayList<Move>[][])movesBoard;
    }
}
