package utils;

import chessboard.Main;
import chessboard.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;

    private ArrayList<Move>[][] movesBoard;

    public MoveGenerator(ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
        movesBoard = new ArrayList[Main.rows][Main.columns];
    }

    public ArrayList<Move>[][] generateMoves(Piece.Colors color){

        ArrayList<Piece> pieces = color == Piece.Colors.WHITE ? whitePieces : blackPieces;

        clearBoard();

        for (Piece piece : pieces){
            List<Move> possibleMoves = piece.getPossibleMoves();
            for(Move move : possibleMoves){
                Position position = move.getEndPosition();
                ArrayList<Move> movesList = movesBoard[position.getRow()][position.getColumn()];
                if(movesList == null){
                    movesList = new ArrayList<>();
                    movesBoard[position.getRow()][position.getColumn()] = movesList;
                }
                movesList.add(move);
            }
        }

        return movesBoard;
    }

    private void clearBoard(){
        for(int row = 0; row < Main.rows; row++){
            for(int column = 0; column < Main.columns; column++){
                movesBoard[row][column] = null;
            }
        }
    }
}
