package utils;

import chessboard.ChessboardGenerator;
import chessboard.pieces.Piece;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class EventHandler {

    // focused field color
    public static Color CLICKED_COLOR = Color.web("#f7d64f");
    public static Color AVAILABLE_MOVE_COLOR = Color.web("#b4d64f");

    public static Node clickedField;

    /*
    *
    may be change to rectangles.length and rectangles[i].length
    *
    */
    private final int columns;
    private final int rows;

    // rectangle click handling
    private Rectangle lastClicked;
    private Color lastColor;

    // piece focus handling
    private boolean focusedOnPiece = false;
    private Piece currentPiece = null;

    private final ChessboardGenerator chessboardGenerator;

    public EventHandler(int columns, int rows, ChessboardGenerator chessboardGenerator) {
        this.columns = columns;
        this.rows = rows;
        this.chessboardGenerator = chessboardGenerator;
    }

    // setup fields events
    public void setUpRectangleEvents(Node[][] rectangles) {
        for(int i = 0; i < columns; i++) {
            for(int j = 0; j < rows; j++) {
                final int row = i;
                final int column = j;
                rectangles[j][i].setOnMousePressed(new javafx.event.EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        clickedField = rectangles[column][row];

                        if(!focusedOnPiece) {
                            Piece piece;
                            piece = ChessboardGenerator.getPiece(row, column, chessboardGenerator.getPieces());

                            //check if it is possile to color rectangle
                            //color only rectangles with pieces on it

                            if (piece != null) {
                                System.out.println(piece.getName());

                                // focus on piece
                                focusedOnPiece = true;
                                currentPiece = piece;

                                //get and show available moves
                                List<Position> positions = piece.getPossibleMoves(chessboardGenerator.getPieces());
                                for(Position pos : positions) {
                                    Rectangle rect = (Rectangle) rectangles[pos.getRow()][pos.getColumn()];
                                    rect.setFill(AVAILABLE_MOVE_COLOR);
                                    System.out.println(pos.getColumn());
                                }


                                //uncolor last piece
                                if (lastClicked != null) chessboardGenerator.colorField(lastClicked, lastColor);
                                lastClicked = (Rectangle) clickedField;
                                lastColor = (Color) lastClicked.getFill();
                                chessboardGenerator.colorField(column, row, CLICKED_COLOR);
                            }
                        }else{
                            // handling piece move
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
