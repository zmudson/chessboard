package chessboard;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.EventHandler;

public class Main extends Application {

    public static final double width = 600;
    public static final double height = 600;
    public static final String title = "chess.iek";
    public static final String iconFilename = "assets/images/piece.png";
    public static final String moveSoundEffectFilename = "src/assets/sounds/sound.wav";
    public static final int rows = 8;
    public static final int columns = 8;

    private Stage stage;
    private Parent root;
    private Scene scene;

    private EventHandler eventHandler;
    private Chessboard chessboard;

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

        chessboard = new Chessboard(width,height,rows,columns,root);

        initialize();
        stage.show();
        // init handler events
        initHandlerEvents();
    }

    // initialize chessboard
    public void initialize(){
        this.scene = new Scene(root, width, height);
        stage.setScene(scene);
        chessboard.initChessboard();
        chessboard.generateChessboard();
        chessboard.fillChessboard();
    }

    private void initHandlerEvents() {
        eventHandler = new EventHandler(columns, rows, chessboard);
        eventHandler.setUpRectangleEvents(chessboard.getRectangles());
    }
}
