package utils;

import chessboard.ChessboardGenerator;
import chessboard.Main;
import chessboard.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    private final List<Piece> whitePieces;
    private final List<Piece> blackPieces;

    private ArrayList[][] cachedMovesBoard;

    private int cachedMovesNumber;

    private final ChessboardGenerator chessboardGenerator;

    public MoveGenerator(ChessboardGenerator chessboardGenerator) {
        this.whitePieces = chessboardGenerator.getWhitePieces();
        this.blackPieces = chessboardGenerator.getBlackPieces();
        this.chessboardGenerator = chessboardGenerator;
        cachedMovesBoard = null;
        cachedMovesNumber = 0;
    }

    public ArrayList[][] generateMoves(Piece.Colors color){

        List<Piece> pieces = color == Piece.Colors.WHITE ? whitePieces : blackPieces;
        ArrayList[][] movesBoard = new ArrayList[Main.rows][Main.columns];
        boolean isCurrentColorTurn = color == chessboardGenerator.getColorToMove();
        if(isCurrentColorTurn)
            cachedMovesNumber = 0;

        for (Piece piece : pieces){
            List<Move> possibleMoves = piece.getPossibleMoves();
            for(Move move : possibleMoves){
                if(isCurrentColorTurn)
                    cachedMovesNumber++;
                Position position = move.getEndPosition();
                ArrayList<Move> movesList = movesBoard[position.getRow()][position.getColumn()];
                if(movesList == null){
                    movesList = new ArrayList<>();
                    movesBoard[position.getRow()][position.getColumn()] = movesList;
                }
                movesList.add(move);
            }
        }
        if(isCurrentColorTurn)
            cachedMovesBoard = movesBoard;

        return movesBoard;
    }

    public int getCachedMovesNumber() {
        return cachedMovesNumber;
    }

}
