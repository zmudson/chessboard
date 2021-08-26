package chessboard.pieces;

public class King extends Piece {
    public static final double power = 1000;
    public static final String name = "king";
    private static final String filename = "";

    public King(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
    }
}
