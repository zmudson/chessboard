package utils;

import chessboard.pieces.King;
import chessboard.pieces.Piece;

public class MoveHandler {
    public static boolean isValid(Piece movingPiece, Piece attackedPiece) {
        if(attackedPiece == null){
            return true;
        }else if(movingPiece.getColor() != movingPiece.getChessboard().getColorToMove()){
            if(attackedPiece instanceof King){
                return movingPiece.getColor() != attackedPiece.getColor();
            }else
                return true;
        }else {
            return movingPiece.getColor() != attackedPiece.getColor() && !(attackedPiece instanceof King);
        }
    }
}























//       if (attackedPiece == null){
//               return true;
//               } else if(attackedPiece != null && movingPiece.getColor() != attackedPiece.getColor() && !(attackedPiece instanceof King)){
//               return true;
//               }else if(movingPiece.getColor() != movingPiece.getChessboardGenerator().getColorToMove() && attackedPiece instanceof King){
//               return true;
//               }else if(movingPiece.getColor() != movingPiece.getChessboardGenerator().getColorToMove()
//               && movingPiece.getColor() == attackedPiece.getColor()){
//               return true;
//               }
//               else{
//               return false;
//               }