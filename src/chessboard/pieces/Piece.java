package chessboard.pieces;

import javafx.scene.image.ImageView;

public abstract class Piece {

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
    private ImageView image;


    public Piece(int row, int column, String name, String filename, double power, Colors color) {
        this.row = row;
        this.column = column;
        this.name = name;
        this.filename = filename;
        this.power = power;
        this.color = color;
    }

    public void move(int row, int column){
        this.row = row;
        this.column = column;
        int x = (int)(image.getFitWidth() * column);
        int y = (int)(image.getFitHeight() * row);
        image.setX(x);
        image.setY(y);
    }

    public int getRow() {
        return row;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
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
