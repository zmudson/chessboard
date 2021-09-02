package utils;

import chessboard.Chessboard;
import chessboard.Main;
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
    private List<Move> focusedPieceMoves = new ArrayList<>();
    private Rectangle lastMovedFromField = null;
    private Rectangle lastMovedToField = null;


    private final Chessboard chessboard;

    public EventHandler(int columns, int rows, Chessboard chessboard) {
        this.columns = columns;
        this.rows = rows;
        this.chessboard = chessboard;
    }

    // setup fields events
    public void setUpRectangleEvents(Node[][] rectangles) {

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                final int row = i;
                final int column = j;
                rectangles[i][j].setOnMousePressed(event -> {
                    clickedField = rectangles[row][column];
                    if(!chessboard.isRunning()) return;

                    if(!focusedOnPiece) {
                        Piece piece;
                        piece = chessboard.getPiece(row, column);

                        // can't move if it is not this piece turn
                        if(piece != null && piece.getColor() != chessboard.getColorToMove())
                            return;

                        //check if it is possile to color rectangle
                        //color only rectangles with pieces on it

                        if (piece != null) {
                            // focus on piece
                            focusedOnPiece = true;
                            currentPiece = piece;

                            //uncolor last piece
                            if (lastClicked != null) chessboard.colorField(lastClicked, lastColor);
                            lastClicked = (Rectangle) clickedField;
                            lastColor = (Color) lastClicked.getFill();
                            chessboard.colorField(row, column, CLICKED_COLOR);

                            //get and show available moves
                            List<Move> moves = piece.getPossibleMoves();
                            focusedPieceMoves = moves;
                            for(Move move : moves) {
                                Position position = move.getEndPosition();
                                Rectangle rect = (Rectangle) rectangles[position.getRow()][position.getColumn()];
                                rect.setFill(AVAILABLE_MOVE_COLOR);
                            }


                        }
                    }
                    // isFocused == true
                    else {
                        // handling piece move
                        boolean legalMove = false;

                        for(Move move : focusedPieceMoves) {
                            //if move is (pseudo)legal
                            Position position = move.getEndPosition();
                            if(position.getColumn() == column && position.getRow() == row) {
                                legalMove = true;
                                break;
                            }
                        }
                        // uncolor
                        chessboard.colorField(lastClicked, lastColor);
                        for(Move move : focusedPieceMoves) {
                            Position position = move.getEndPosition();
                            Color color = getFieldColor(position.getRow(),position.getColumn());
                            chessboard.colorField(position.getRow(), position.getColumn(), color);
                        }
                        //make sure that last colors are showed
                        if(lastMovedToField!=null) {
                            lastMovedToField.setFill(MOVED_TO_COLOR);
                            lastMovedFromField.setFill(MOVED_FROM_COLOR);
                        }

                        //if move is possible then make move and color it
                        if(legalMove) {

                            // make turn
                            chessboard.play(row, column, currentPiece);

                            //uncolor last moved fields
                            if(lastMovedToField!=null) {
                                lastMovedFromField.setFill(getFieldColor((int)(lastMovedFromField.getX()*8/Main.width), (int)(lastMovedFromField.getY()*8/Main.width)));
                                lastMovedToField.setFill(getFieldColor((int)(lastMovedToField.getX()*8/Main.width), (int)(lastMovedToField.getY()*8/Main.width)));
                            }
                            //color this move
                            chessboard.colorField(lastClicked, MOVED_FROM_COLOR);
                            chessboard.colorField(row, column, MOVED_TO_COLOR);
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
        return  (row + column) % 2 == 0 ? Chessboard.CHESSBOARD_WHITE_COLOR : Chessboard.CHESSBOARD_BLACK_COLOR;
    }


}
