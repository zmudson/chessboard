package chessboard;

import chessboard.pieces.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import utils.ImageGenerator;

import java.util.Vector;

public class ChessboardGenerator {

    // fields colors
    public static Color CHESSBOARD_WHITE_COLOR = Color.web("#F0D9B5");
    public static Color CHESSBOARD_BLACK_COLOR = Color.web("#B58863");

    private final double width;
    private final double height;
    private final int rows;
    private final int columns;
    private Parent root;
    private Node[][] rectangles;
    private Vector<Piece> pieces;

    public ChessboardGenerator(double width, double height, int rows, int columns, Parent root) {
        this.width = width;
        this.height = height;
        this.rows = rows;
        this.columns = columns;
        this.root = root;
       // this.pieces = pieces;

        rectangles = new Node[rows][columns];
    }

    // create chessboard fields and color them
    public void generateChessboard(){
        double rectangleWidth = width / columns;
        double rectangleHeight = height / rows;
        for(int row = 0; row < rows; row++){
            for(int column = 0; column < columns; column++){
                Color color = (row + column) % 2 == 0 ? CHESSBOARD_WHITE_COLOR : CHESSBOARD_BLACK_COLOR;
                double x = rectangleWidth * column;
                double y = rectangleHeight * row;
                Rectangle rectangle = new Rectangle(rectangleWidth, rectangleHeight, color);
                rectangle.setX(x);
                rectangle.setY(y);
                ((Group) root).getChildren().add(rectangle);
                //add rectangles to array
                rectangles[row][column] = rectangle;
            }
        }
    }

    // set pieces on their position on chessboard
    public void fillChessboard(){
        double widthPercentage = 1.0 / 6;
        double heightPercentage = 1.0 / 2;
        int factor = 0;
        for(Piece piece : pieces){
            double xPositionPercentage = 1.0 / 6;
            double yPositionPercentage = 0;
            if(piece.getColor() == Piece.Colors.BLACK){
                yPositionPercentage = 1.0 / 2;
            }
            if(piece instanceof King){
                factor = 0;
            }else if(piece instanceof Queen){
                factor = 1;
            }else if(piece instanceof Bishop){
                factor = 2;
            }else if(piece instanceof Knight){
                factor = 3;
            }else if(piece instanceof Rook){
                factor = 4;
            }else if(piece instanceof Pawn){
                factor = 5;
            }

            // handling imageView initialization
            xPositionPercentage *= factor;
            ImageView imageView = ImageGenerator.getImagePart("assets/images/pieces.svg.png", xPositionPercentage,
                    yPositionPercentage, widthPercentage, heightPercentage);
            double figureWidth = width / columns;
            double figureHeight = height / rows;
            imageView.setFitWidth(figureWidth);
            imageView.setFitHeight(figureHeight);
            imageView.setX(piece.getColumn() * figureWidth);
            imageView.setY(piece.getRow() * figureHeight);
            piece.setImage(imageView);

            //for eventhandler
            imageView.setMouseTransparent(true);

            // add imageView to root
            ((Group) root).getChildren().add(imageView);
        }
    }

    // initialize all pieces and add them to vector
    public void initChessboard(){
        pieces = new Vector<>();
        pieces.add(new Rook(0, 0, Piece.Colors.BLACK));
        pieces.add(new Knight(0, 1, Piece.Colors.BLACK));
        pieces.add(new Bishop(0, 2, Piece.Colors.BLACK));
        pieces.add(new Queen(0, 3, Piece.Colors.BLACK));
        pieces.add(new King(0, 4, Piece.Colors.BLACK));
        pieces.add(new Bishop(0, 5, Piece.Colors.BLACK));
        pieces.add(new Knight(0, 6, Piece.Colors.BLACK));
        pieces.add(new Rook(0, 7, Piece.Colors.BLACK));

        for(int i = 0; i < columns; i++){
            pieces.add(new Pawn(1, i, Piece.Colors.BLACK));
        }

        pieces.add(new Rook(rows - 1, 0, Piece.Colors.WHITE));
        pieces.add(new Knight(rows - 1, 1, Piece.Colors.WHITE));
        pieces.add(new Bishop(rows - 1, 2, Piece.Colors.WHITE));
        pieces.add(new Queen(rows - 1, 3, Piece.Colors.WHITE));
        pieces.add(new King(rows - 1, 4, Piece.Colors.WHITE));
        pieces.add(new Bishop(rows - 1, 5, Piece.Colors.WHITE));
        pieces.add(new Knight(rows - 1, 6, Piece.Colors.WHITE));
        pieces.add(new Rook(rows - 1, 7, Piece.Colors.WHITE));

        for(int i = 0; i < columns; i++){
            pieces.add(new Pawn(rows - 2, i, Piece.Colors.WHITE));
        }
    }

    public Node[][] getRectangles() {
        return rectangles;
    }

    public void colorField(int row, int column, Color color) {
        Rectangle rect = (Rectangle) rectangles[row][column];
        rect.setFill(color);
    }

    public void colorField(Node rectangle, Color color) {
        Rectangle rect = (Rectangle) rectangle;
        rect.setFill(color);
    }

    public Vector<Piece> getPieces() {
        return pieces;
    }

    // return piece on provided position if it is on it, otherwise return null
    public static Piece getPiece(int row, int column, Vector<Piece> pieces) {
        for (Piece piece : pieces) {
            if (piece.getRow() == row && piece.getColumn() == column) {
                return piece;
            }
        }
        return null;
    }
}
