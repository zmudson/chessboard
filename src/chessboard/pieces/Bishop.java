package chessboard.pieces;

public class Bishop extends Piece {
    public static final double power = 300;
    public static final String name = "bishop";
    private static final String filename = "";

    public Bishop(int row, int column, Piece.Colors color) {
        super(row, column, name, filename, power, color);
    }
}
