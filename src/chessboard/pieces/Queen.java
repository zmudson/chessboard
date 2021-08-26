package chessboard.pieces;

public class Queen extends Piece {
    public static final double power = 900;
    public static final String name = "queen";
    private static final String filename = "";

    public Queen(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
    }
}
