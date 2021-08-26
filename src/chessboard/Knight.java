package chessboard;

public class Knight extends Figure{
    public static final double power = 300;
    public static final String name = "knight";
    private static final String filename = "";

    public Knight(int row, int column, Figure.Colors color) {
        super(row, column, name, filename, power, color);
    }
}
