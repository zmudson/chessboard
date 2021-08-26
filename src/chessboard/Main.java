package chessboard;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import utils.ImageGenerator;

import java.util.Vector;

public class Main extends Application {

    public static final double width = 600;
    public static final double height = 600;
    public static final String title = "chess.iek";
    public static final int rows = 8;
    public static final int columns = 8;

    private Stage stage;
    private Parent root;
    private Scene scene;
    private Vector<Figure> figures;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle(title);
        stage.setResizable(false);
        initChessboard();
        draw();
        stage.show();
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
            }
        }
    }

    public void fillChessboard(){
        double widthPercentage = 1.0 / 6;
        double heightPercentage = 1.0 / 2;
        int factor = 0;
        for(Figure figure : figures){
            double xPositionPercentage = 1.0 / 6;
            double yPositionPercentage = 0;
            if(figure.getColor() == Figure.Colors.BLACK){
                yPositionPercentage = 1.0 / 2;
            }
            if(figure instanceof King){
                factor = 0;
            }else if(figure instanceof Queen){
                factor = 1;
            }else if(figure instanceof Bishop){
                factor = 2;
            }else if(figure instanceof Knight){
                factor = 3;
            }else if(figure instanceof Rook){
                factor = 4;
            }else if(figure instanceof Pawn){
                factor = 5;
            }
            xPositionPercentage *= factor;
            ImageView imageView = ImageGenerator.getImagePart("assets/images/pieces.svg.png", xPositionPercentage,
                    yPositionPercentage, widthPercentage, heightPercentage);
            double figureWidth = width / columns;
            double figureHeight = height / rows;
            imageView.setFitWidth(figureWidth);
            imageView.setFitHeight(figureHeight);
            imageView.setX(figure.getColumn() * figureWidth);
            imageView.setY(figure.getRow() * figureHeight);
            ((Group) root).getChildren().add(imageView);
        }
    }

    public void initChessboard(){
        figures = new Vector<>();
        figures.add(new Rook(0, 0, Figure.Colors.BLACK));
        figures.add(new Knight(0, 1, Figure.Colors.BLACK));
        figures.add(new Bishop(0, 2, Figure.Colors.BLACK));
        figures.add(new Queen(0, 3, Figure.Colors.BLACK));
        figures.add(new King(0, 4, Figure.Colors.BLACK));
        figures.add(new Bishop(0, 5, Figure.Colors.BLACK));
        figures.add(new Knight(0, 6, Figure.Colors.BLACK));
        figures.add(new Rook(0, 7, Figure.Colors.BLACK));

        for(int i = 0; i < columns; i++){
            figures.add(new Pawn(1, i, Figure.Colors.BLACK));
        }

        figures.add(new Rook(rows - 1, 0, Figure.Colors.WHITE));
        figures.add(new Knight(rows - 1, 1, Figure.Colors.WHITE));
        figures.add(new Bishop(rows - 1, 2, Figure.Colors.WHITE));
        figures.add(new Queen(rows - 1, 3, Figure.Colors.WHITE));
        figures.add(new King(rows - 1, 4, Figure.Colors.WHITE));
        figures.add(new Bishop(rows - 1, 5, Figure.Colors.WHITE));
        figures.add(new Knight(rows - 1, 6, Figure.Colors.WHITE));
        figures.add(new Rook(rows - 1, 7, Figure.Colors.WHITE));

        for(int i = 0; i < columns; i++){
            figures.add(new Pawn(rows - 2, i, Figure.Colors.WHITE));
        }
    }

}
