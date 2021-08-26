package chessboard;

public class Rook extends Figure{
    public static final double power = 500;
    public static final String name = "rook";
    private static final String filename = "";

    public Rook(int row, int column, Figure.Colors color) {
        super(row, column, name, filename, power, color);
    }
}
