package utils;

//simple class having only position of piece

public class Position {

    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public boolean equals(Position position){
        return row == position.row && column == position.column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

}
