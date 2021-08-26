package chessboard;

public class Pawn extends Figure{
    public static final double power = 100;
    public static final String name = "pawn";
    private static final String filename = "";

    public Pawn(int row, int column, Figure.Colors color) {
        super(row, column, name, filename, power, color);
    }
}
