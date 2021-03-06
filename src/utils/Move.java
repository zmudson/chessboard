package utils;

import chessboard.pieces.Piece;

public class Move {

    private Piece piece;
    private Position startPosition;
    private Position endPosition;

    public Move(Piece piece, Position startPosition, Position endPosition) {
        this.piece = piece;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Position getEndPosition() {
        return endPosition;
    }

}
