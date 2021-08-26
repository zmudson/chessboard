package chessboard;

import chessboard.pieces.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import utils.EventHandler;
import utils.ImageGenerator;

import java.util.Vector;

public class Main extends Application {

    public static final double width = 600;
    public static final double height = 600;
    public static final String title = "chess.iek";
    public static final String iconFilename = "assets/images/piece.png";
    public static final int rows = 8;
    public static final int columns = 8;

    private Stage stage;
    private Parent root;
    private Scene scene;
    private Vector<Piece> pieces;
    private Node[][] rectangles;
    private EventHandler eventHandler;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle(title);
        stage.setResizable(false);
        stage.getIcons().add(new Image(iconFilename));
        initChessboard();

        rectangles = new Node[rows][columns];

        draw();
        stage.show();
        // init handler events
        initHandlerEvents();

    }

    public void draw(){
        this.root = new Group();
        this.scene = new Scene(root, width, height);
        stage.setScene(scene);
        generateChessboard();
        fillChessboard();

    }

    public void generateChessboard(){
        double rectangleWidth = width / columns;
        double rectangleHeight = height / rows;
        for(int row = 0; row < rows; row++){
            for(int column = 0; column < columns; column++){
                Color color = (row + column) % 2 == 0 ? Color.web("#F0D9B5") : Color.web("#B58863");
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
            xPositionPercentage *= factor;
            ImageView imageView = ImageGenerator.getImagePart("assets/images/pieces.svg.png", xPositionPercentage,
                    yPositionPercentage, widthPercentage, heightPercentage);
            double figureWidth = width / columns;
            double figureHeight = height / rows;
            imageView.setFitWidth(figureWidth);
            imageView.setFitHeight(figureHeight);
            imageView.setX(piece.getColumn() * figureWidth);
            imageView.setY(piece.getRow() * figureHeight);
            ((Group) root).getChildren().add(imageView);
        }
    }

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

    private void initHandlerEvents() {

        eventHandler = new EventHandler(columns, rows);
        eventHandler.setUpRectangleEvents(rectangles);
    }

}
