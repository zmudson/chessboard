package utils;

import chessboard.ChessboardGenerator;
import chessboard.Main;
import chessboard.pieces.Pawn;
import chessboard.pieces.Piece;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {

    // focused field color
    public static Color CLICKED_COLOR = Color.web("#f7d64f");
    public static Color AVAILABLE_MOVE_COLOR = Color.web("#b4d64f");
    public static Color MOVED_FROM_COLOR = Color.web("#f08330");
    public static Color MOVED_TO_COLOR = Color.web("#5c95f7");

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
    private List<Position> focusedPiecePositions = new ArrayList<>();
    private Rectangle lastMovedFromField = null;
    private Rectangle lastMovedToField = null;


    private final ChessboardGenerator chessboardGenerator;

    public EventHandler(int columns, int rows, ChessboardGenerator chessboardGenerator) {
        this.columns = columns;
        this.rows = rows;
        this.chessboardGenerator = chessboardGenerator;
    }

    // setup fields events
    public void setUpRectangleEvents(Node[][] rectangles) {

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                final int row = i;
                final int column = j;
                rectangles[i][j].setOnMousePressed(event -> {
                    clickedField = rectangles[row][column];

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
                            focusedPiecePositions = positions;
                            for(Position pos : positions) {
                                Rectangle rect = (Rectangle) rectangles[pos.getRow()][pos.getColumn()];
                                rect.setFill(AVAILABLE_MOVE_COLOR);
                            }

                            //uncolor last piece
                            if (lastClicked != null) chessboardGenerator.colorField(lastClicked, lastColor);
                            lastClicked = (Rectangle) clickedField;
                            lastColor = (Color) lastClicked.getFill();
                            chessboardGenerator.colorField(row, column, CLICKED_COLOR);
                        }
                    }
                    // isFocused == true
                    else {
                        // handling piece move
                        boolean legalMove = false;

                        for(Position pos : focusedPiecePositions) {
                            //if move is (pseudo)legal
                            if(pos.getColumn()==column&&pos.getRow()==row) {
                                legalMove = true;
                                break;
                            }
                        }
                        // uncolor
                        chessboardGenerator.colorField(lastClicked, lastColor);
                        for(Position pos : focusedPiecePositions) {
                            Color color = getFieldColor(pos.getRow(),pos.getColumn());
                            chessboardGenerator.colorField(pos.getRow(), pos.getColumn(), color);
                        }

                        //if move is possible then make move and color it
                        if(legalMove) {

                            currentPiece.move(row, column, chessboardGenerator);

                            //uncolor last moved fields
                            if(lastMovedToField!=null) {
                                lastMovedFromField.setFill(getFieldColor((int)(lastMovedFromField.getX()*8/Main.width), (int)(lastMovedFromField.getY()*8/Main.width)));
                                lastMovedToField.setFill(getFieldColor((int)(lastMovedToField.getX()*8/Main.width), (int)(lastMovedToField.getY()*8/Main.width)));
                            }
                            //color this move
                            chessboardGenerator.colorField(lastClicked, MOVED_FROM_COLOR);
                            chessboardGenerator.colorField(row, column, MOVED_TO_COLOR);
                            //remember which fields has been colored
                            lastMovedFromField = lastClicked;
                            lastMovedToField = (Rectangle) rectangles[row][column];
                            lastClicked = null;
                        }
                        //unfocus
                        focusedOnPiece = false;
                        currentPiece = null;
                        //lastClicked = (Rectangle)clickedField;
                    }
                });
            }
        }
    }

    private Color getFieldColor(int row, int column) {
        return  (row + column) % 2 == 0 ? ChessboardGenerator.CHESSBOARD_WHITE_COLOR : ChessboardGenerator.CHESSBOARD_BLACK_COLOR;
    }


}
