package chessboard;

import chessboard.pieces.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.EventHandler;

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

    private EventHandler eventHandler;
    private ChessboardGenerator chessboardGenerator;

    public static void main(String[] args) {
        launch(args);
    }

    // handling javafx setup and initialize chessboard and eventHandler
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle(title);
        stage.setResizable(false);
        stage.getIcons().add(new Image(iconFilename));
        this.root = new Group();

        chessboardGenerator = new ChessboardGenerator(width,height,rows,columns,root, pieces);

        initialize();
        stage.show();
        // init handler events
        initHandlerEvents();
    }

    // initialize chessboard
    public void initialize(){
        this.scene = new Scene(root, width, height);
        stage.setScene(scene);
        chessboardGenerator.initChessboard();
        chessboardGenerator.generateChessboard();
        chessboardGenerator.fillChessboard();
    }

    private void initHandlerEvents() {
        eventHandler = new EventHandler(columns, rows, chessboardGenerator);
        eventHandler.setUpRectangleEvents(chessboardGenerator.getRectangles(), pieces);
    }
}
