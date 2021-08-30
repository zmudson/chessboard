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

    public ArrayList<Move>[][] generateMoves(Piece.Colors color){
        ArrayList<Piece> pieces = null;

        if(color == Piece.Colors.WHITE)
            pieces = whitePieces;
        else if(color == Piece.Colors.BLACK)
            pieces = blackPieces;

        Object[][] movesBoard = new Object[Main.rows][Main.columns];

        if(pieces != null){
            for (Piece piece : pieces){
                List<Move> possibleMoves = piece.getPossibleMoves(pieces);
                for(Move move : possibleMoves){
                    Position position = move.getEndPosition();
                    ArrayList<Move> movesList = (ArrayList<Move>) movesBoard[position.getRow()][position.getColumn()];
                    if(movesList == null){
                        movesBoard[piece.getRow()][piece.getColumn()] = new ArrayList<Move>();
                        movesList = (ArrayList<Move>) movesBoard[piece.getRow()][piece.getColumn()];
                    }
                    movesList.add(move);
                }
            }
        }

        return (ArrayList<Move>[][])movesBoard;
    }
}
