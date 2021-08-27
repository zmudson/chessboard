package utils;

import chessboard.pieces.King;
import chessboard.pieces.Piece;

public class MoveHandler {
    public static boolean isValid(Piece movingPiece, Piece attackedPiece){
        return !(attackedPiece != null && (attackedPiece.getColor() == movingPiece.getColor() || attackedPiece instanceof King));
    }
}
