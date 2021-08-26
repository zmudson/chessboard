package chessboard;

public class Bishop extends Figure{
    public static final double power = 300;
    public static final String name = "bishop";
    private static final String filename = "";
    public Bishop(int row, int column, Figure.Colors color) {
        super(row, column, name, filename, power, color);
    }
}
