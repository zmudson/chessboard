package utils;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class EventHandler {

    public static Node clickedRectangle;

    private int columns, rows;

    public EventHandler(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public void setUpRectangleEvents(Node[][] rectangles) {
        for(int i=0; i<columns; i++) {
            for(int j=0; j<rows; j++) {
                final int row = i;
                final int column = j;
                rectangles[j][i].setOnMousePressed(new javafx.event.EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        clickedRectangle = rectangles[row][column];

                        System.out.println("Kliknięto: ["+row+"]["+column+"]");
                    }
                });
            }
        }
    }
}
