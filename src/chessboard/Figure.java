package chessboard;

public abstract class Figure {

    public enum Colors{
        WHITE,
        BLACK
    };

    private int row;
    private int column;
    private String name;
    private String filename;
    private double power;
    private Colors color;


    public Figure(int row, int column, String name, String filename, double power, Colors color) {
        this.row = row;
        this.column = column;
        this.name = name;
        this.filename = filename;
        this.power = power;
        this.color = color;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }
}
