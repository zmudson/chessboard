package chessboard.pieces;

public class Knight extends Piece {
    public static final double power = 300;
    public static final String name = "knight";
    private static final String filename = "";

    public Knight(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
    }
}
