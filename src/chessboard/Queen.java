package chessboard;

public class Queen extends Figure{
    public static final double power = 900;
    public static final String name = "queen";
    private static final String filename = "";

    public Queen(int row, int column, Figure.Colors color) {
        super(row, column, name, filename, power, color);
    }
}
