package chessboard.pieces;

public class Rook extends Piece {
    public static final double power = 500;
    public static final String name = "rook";
    private static final String filename = "";

    public Rook(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
    }
}
