package utils;

import chessboard.ChessboardGenerator;
import chessboard.pieces.Piece;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EventHandler {

    public static Color CLICKED_COLOR = Color.web("#f7d64f");

    public static Node clickedField;

    // can change to rectangles.length and rectangles[i].length
    private final int columns;
    private final int rows;

    //which rectangle was clicked last time
    private Rectangle lastClicked;
    private Color lastColor;

    private boolean focusedOnPiece = false;
    private Piece currentPiece = null;

    private ChessboardGenerator chessboardGenerator;

    public EventHandler(int columns, int rows, ChessboardGenerator chessboardGenerator) {
        this.columns = columns;
        this.rows = rows;
        this.chessboardGenerator = chessboardGenerator;
    }

    public void setUpRectangleEvents(Node[][] rectangles) {
        for(int i = 0; i < columns; i++) {
            for(int j = 0; j < rows; j++) {
                final int row = i;
                final int column = j;
                rectangles[j][i].setOnMousePressed(new javafx.event.EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        clickedField = rectangles[column][row];

                        //check if it is possile to color rectangle
                        //color only rectangles with pieces on it
                        if(!focusedOnPiece) {
                            Piece piece;
                            piece = chessboardGenerator.getPiece(row, column);
                            if (piece != null) {
                                System.out.println(piece.getName());

                                // focus on piece
                                focusedOnPiece = true;
                                currentPiece = piece;

                                //uncolor last piece
                                if (lastClicked != null) chessboardGenerator.colorField(lastClicked, lastColor);
                                lastClicked = (Rectangle) clickedField;
                                lastColor = (Color) lastClicked.getFill();
                                chessboardGenerator.colorField(column, row, CLICKED_COLOR);
                            }
                        }else{
                            currentPiece.move(column, row);
                            currentPiece = null;
                            focusedOnPiece = false;
                        }
                    }
                });
            }
        }
    }
}
