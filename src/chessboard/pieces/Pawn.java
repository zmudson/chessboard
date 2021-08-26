package chessboard.pieces;

public class Pawn extends Piece {
    public static final double power = 100;
    public static final String name = "pawn";
    private static final String filename = "";

    public Pawn(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
    }
}
