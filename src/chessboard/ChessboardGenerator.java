package chessboard;

import chessboard.pieces.*;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import utils.ImageGenerator;
import utils.MoveHandler;
import utils.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ChessboardGenerator {

    // fields colors
    public static Color CHESSBOARD_WHITE_COLOR = Color.web("#F0D9B5");
    public static Color CHESSBOARD_BLACK_COLOR = Color.web("#B58863");

    private final double width;
    private final double height;
    private final int rows;
    private final int columns;

    public Parent getRoot() {
        return root;
    }

    private Parent root;
    private Node[][] rectangles;
    private ArrayList<Piece> pieces;
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;

    private Piece.Colors colorToMove = Piece.Colors.WHITE;
    protected List<Position> fieldsToBlockCheck = new ArrayList<>();

    //for en passant
    private static Pawn pawnForEnPassant = null;

    private King whiteKing = null;
    private King blackKing = null;

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

    public void handleImageDraw(Piece piece){
        double widthPercentage = 1.0 / 6;
        double heightPercentage = 1.0 / 2;
        int factor = 0;
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

    // set pieces on their position on chessboard
    public void fillChessboard(){
        for(Piece piece : pieces){
            handleImageDraw(piece);
        }
    }

    // initialize all pieces and add them to vector
    public void initChessboard(){
        pieces = new ArrayList<>();

        // init black pieces
        blackPieces = new ArrayList<>();

        pieces.add(new Rook(0, 0, Piece.Colors.BLACK));
        pieces.add(new Knight(0, 1, Piece.Colors.BLACK));
        pieces.add(new Bishop(0, 2, Piece.Colors.BLACK));
        pieces.add(new Queen(0, 3, Piece.Colors.BLACK));
        blackKing = new King(0, 4, Piece.Colors.BLACK);
        pieces.add(blackKing);
        pieces.add(new Bishop(0, 5, Piece.Colors.BLACK));
        pieces.add(new Knight(0, 6, Piece.Colors.BLACK));
        pieces.add(new Rook(0, 7, Piece.Colors.BLACK));

        for(int i = 0; i < columns; i++){
            pieces.add(new Pawn(1, i, Piece.Colors.BLACK));
        }

        // init white pieces
        whitePieces = new ArrayList<>();

        pieces.add(new Rook(rows - 1, 0, Piece.Colors.WHITE));
        pieces.add(new Knight(rows - 1, 1, Piece.Colors.WHITE));
        pieces.add(new Bishop(rows - 1, 2, Piece.Colors.WHITE));
        pieces.add(new Queen(rows - 1, 3, Piece.Colors.WHITE));
        whiteKing = new King(rows - 1, 4, Piece.Colors.WHITE);
        pieces.add(whiteKing);
        pieces.add(new Bishop(rows - 1, 5, Piece.Colors.WHITE));
        pieces.add(new Knight(rows - 1, 6, Piece.Colors.WHITE));
        pieces.add(new Rook(rows - 1, 7, Piece.Colors.WHITE));

        for(int i = 0; i < columns; i++){
            pieces.add(new Pawn(rows - 2, i, Piece.Colors.WHITE));
        }

        for(Piece piece : pieces){
            if(piece.getColor() == Piece.Colors.BLACK)
                blackPieces.add(piece);
            else if (piece.getColor() == Piece.Colors.WHITE)
                whitePieces.add(piece);
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

    public List<Piece> getPieces() {
        return pieces;
    }

    // return piece on provided position if it is on it, otherwise return null
    /* consider add this method to utils */
    public static Piece getPiece(int row, int column, List<Piece> pieces) {
        for (Piece piece : pieces) {
            if (piece.getRow() == row && piece.getColumn() == column) {
                return piece;
            }
        }
        return null;
    }

    public static boolean isFieldEmpty(int row, int column, ArrayList<Piece> pieces){
        return getPiece(row, column, pieces) == null;
    }

    public static void setPawnAbleToBeCapturedByEnPassant(Pawn pawn) {
        pawnForEnPassant = pawn;
    }

    public static Pawn getPawnAbleToBeCapturedByEnPassant() {
        return pawnForEnPassant;
    }

    public List<Piece> getWhitePieces() {
        return whitePieces;
    }

    public void setWhitePieces(ArrayList<Piece> whitePieces) {
        this.whitePieces = whitePieces;
    }

    public ArrayList<Piece> getBlackPieces() {
        return blackPieces;
    }

    public void setBlackPieces(ArrayList<Piece> blackPieces) {
        this.blackPieces = blackPieces;
    }

    public void checkAllPinsAndChecks() {
        //which king is going to be checked
        King king = null;
        ArrayList<Knight> knights = new ArrayList<>();

        fieldsToBlockCheck.clear();
        // looking for the king and knights
        /*for(Piece piece : pieces) {
            if(piece instanceof King && piece.getColor() == colorToMove) {
                king = (King) piece;
            }
            if(piece instanceof Knight && piece.getColor() != colorToMove) {
                knights.add((Knight) piece);
            }
            if(king!=null && knights.size()==2) break;
        }*/
        List<Piece> ourColorPieces = null;
        List<Piece> enemyColorPieces = null;
        if(colorToMove == Piece.Colors.WHITE) {
            king = whiteKing;
            ourColorPieces = whitePieces;
            enemyColorPieces = blackPieces;
        }
        else {
            king = blackKing;
            ourColorPieces = blackPieces;
            enemyColorPieces = whitePieces;
        }
        //list with all checks
        List<Piece> checks = new ArrayList<>();
        //the king's position
        final int kingColumn = king.getColumn();
        final int kingRow = king.getRow();
        //loop over every direction
        for(int dy=-1; dy<=1; dy++) {
            for(int dx=-1; dx<=1; dx++) {
                if(dx==0&&dy==0) continue; //there is no such a direction
                int y = kingColumn;
                int x = kingRow;
                Piece possiblePin = null;
                //loop through the direction
                while(x>=0 && x<Main.columns && y>=0 && y<=Main.rows) {
                    x+=dx;
                    y+=dy;
                    //System.out.println("Sprawdzenie "+x+" "+y);
                    Piece piece = getPiece(x,y,pieces);
                    if(piece==null) continue; //skip to the next position
                    //check for pin. If there was no pin before, this piece could be a pin. If there was a pin before,
                    // we can end looking for pins and checks (obvious reasons).
                    if(piece.getColor()==colorToMove) {
                        if(possiblePin==null) {
                            possiblePin = piece;
                            continue;
                        }
                        else break;
                    }
                     // if dx + dy==0 | 2 => diagonal, ==1 => horizontal
                    boolean diagonal = Math.abs(dx + dy) != 1;
                    //check if enemy pieces could attack the king
                    if ( (diagonal && piece instanceof Bishop) ||
                            (!diagonal && piece instanceof Rook) ||
                            (piece instanceof Queen) || //TODO dodać warunek króla
                            (diagonal && piece instanceof Pawn && (y==kingColumn+1 || y==kingColumn-1) &&dx==-((Pawn)piece).getDirection() )) {
                        //if there was our color piece before, it must be a pin
                        if(possiblePin!=null) {
                            possiblePin.setPinned(true);

                            //calculate which direction is going to be blocked and
                            //set appropriate flags for blocking moves (because its being pinned)

                            possiblePin.blockDirections(getThisDirection(dx, dy));

                            System.out.println(possiblePin.getName()+"["+possiblePin.getRow()+","+possiblePin.getColumn()+"] jest pinem!");
                        }
                        // if there weren't any pins it must be a check
                        else {
                            //only in this case we will create blocking fields to be blocked by our pieces
                            if(checks.size()==0) {
                                int yToBlock = kingColumn;
                                int xToBlock = kingRow;
                                while(xToBlock>=0 && xToBlock<Main.columns && yToBlock>=0 && yToBlock<=Main.rows) {
                                    xToBlock+=dx;
                                    yToBlock+=dy;
                                    fieldsToBlockCheck.add(new Position(xToBlock, yToBlock));
                                    if(xToBlock==piece.getRow() && yToBlock==piece.getColumn()) {
                                        System.out.println("Wyznaczono pola do blokowania");
                                        for(Position field : fieldsToBlockCheck) {
                                            System.out.println(field.getRow()+", "+field.getColumn());
                                        }
                                        System.out.println("-------------");
                                        break;
                                    }
                                }
                            }

                            checks.add(piece);
                            System.out.println(piece.getName()+" szachuje!");
                        }
                    }
                    break; //if it was a black piece, anything behind it won't be checking
                }
            }
            if(checks.size()==2) break; //if there are 2 checks only king can make move
        }
        if(checks.size()<2) {
            //we must also check knights
                for (Piece piece : enemyColorPieces)
                    if (piece instanceof Knight) knights.add((Knight) piece);


                for (Knight knight : knights) {
                    for (int i = 0, rowMove = -2, columnMove; i < 8; i++) {
                        // set column move
                        columnMove = (1 + Math.abs(rowMove % 2)) * (i % 2 == 0 ? -1 : 1);
                        // change current position
                        int row = knight.getRow() + rowMove;
                        int column = knight.getColumn() + columnMove;
                        // add move to an array if is legal
                        if (row >= 0 && row <= Main.rows - 1 && column >= 0 && column <= Main.columns - 1)
                            //HERE WE CHECK FOR CHECK
                            if (kingColumn == column && kingRow == row) checks.add(knight);

                        // set row move
                        if (i % 2 != 0) {
                            rowMove++;
                            if (rowMove == 0)
                                rowMove++;
                        }
                    }
                }
        }
        if(checks.size()==2) {
            System.out.println("Król musi sie ruszyć");
                for(Piece piece: ourColorPieces) {
                    if(piece==king) continue;
                    piece.setCanMove(false);
                }

        }
        //DEBUG
        if(checks.size()>0) System.out.println("Checks:");
        for(Piece check: checks) {
            System.out.println(check.getName());
        }
    }

    public void unpinAndResetAllDirections() {
        for(Piece piece: pieces) {
            piece.setPinned(false);
            piece.unblockDirections();
            piece.setCanMove(true);
        }
    }

    private Piece.BlockedDirections getThisDirection(int dx, int dy) {
        int diff = Math.abs(dx - dy);
        if(diff!=1) { // diagonal
            if(diff==0) return (Piece.BlockedDirections.DOWN_UP_DIAGONAL);
            else return (Piece.BlockedDirections.UP_DOWN_DIAGONAL);
        }
        //vertical/horizontal
        else {
            if(dx==0) return (Piece.BlockedDirections.HORIZONTAL);
            else return (Piece.BlockedDirections.VERTICAL);
        }
    }

}
