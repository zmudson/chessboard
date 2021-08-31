package utils;

import chessboard.pieces.King;
import chessboard.pieces.Piece;

public class MoveHandler {
    public static boolean isValid(Piece movingPiece, Piece attackedPiece) {
        if(attackedPiece == null)
            return true;
        else{
            Piece.Colors turnColor = movingPiece.getChessboardGenerator().getColorToMove();
            return (movingPiece.getColor() != attackedPiece.getColor() && !(attackedPiece instanceof King))
                    || (movingPiece.getColor() != turnColor && attackedPiece.getColor() == turnColor && attackedPiece instanceof King);

        }
    }
//    Jeżeli attackedPiece jest nullem:
//    Zwróc true
//    Jeżeli nie jest nullem to:
//    Jeżeli( movingPiece ma różny kolor od attackedPiece i attackedPiece nie jest krolem)  lub (kolor movingPiece jest różny od koloru tury i attackedPiece jest krolem koloru tury):
//    Zwróć true
//    Else:
//    Zwróć false

}
