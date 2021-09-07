package chessboard;

import chessboard.pieces.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import utils.EventHandler;
import utils.ImageGenerator;
import utils.MoveGenerator;
import utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Chessboard {

    // fields colors
    public static Color CHESSBOARD_WHITE_COLOR = Color.web("#F0D9B5");
    public static Color CHESSBOARD_BLACK_COLOR = Color.web("#B58863");

    private final double width;
    private final double height;
    private final double fieldWidth;
    private final double fieldHeight;
    private final int rows;
    private final int columns;

    private Group root;
    private EventHandler eventHandler;
    private Node[][] rectangles;
    private Node[][] circles;
    private ArrayList<Piece> pieces;
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;

    private MoveGenerator moveGenerator;

    private Piece.Colors colorToMove = Piece.Colors.WHITE;

    private List<Position> fieldsToBlockCheck = new ArrayList<>();

    private boolean isCheck;
    private boolean isRunning = true;

    //for en passant
    private static Pawn pawnForEnPassant = null;

    private King whiteKing = null;
    private King blackKing = null;

    public Chessboard(double width, double height, int rows, int columns, Group root) {
        this.width = width;
        this.height = height;
        this.rows = rows;
        this.columns = columns;
        this.fieldWidth = width / columns;
        this.fieldHeight = height / rows;
        this.root = root;
       // this.pieces = pieces;

        rectangles = new Node[rows][columns];
        circles = new Node[rows][columns];
    }

    public void play(int row, int column, Piece piece){
        if(isRunning && colorToMove == piece.getColor()){
            piece.move(row, column);
            colorToMove = colorToMove == Piece.Colors.WHITE ? Piece.Colors.BLACK : Piece.Colors.WHITE;
            unpinAndResetAllDirections();
            checkAllPinsAndChecks();
        }
    }

    // handling draw check
    public void checkDraw(){
        if(whitePieces.size() < 3 && blackPieces.size() < 3){
            int length = Math.max(whitePieces.size(), blackPieces.size());
            boolean isDraw = true;
            for(int i = 0; i < length; i++){
                if (whitePieces.size() > i) {
                    if (Piece.isSignificantFigure(whitePieces.get(i))) {
                        isDraw = false;
                        break;
                    }
                }else{
                    if (Piece.isSignificantFigure(blackPieces.get(i))) {
                        isDraw = false;
                        break;
                    }
                }
            }
            if(isDraw){
                isRunning = false;
                System.out.println("draw");
            }
        }
    }

    // create chessboard fields and color them
    public void generateChessboard(){
        for(int row = 0; row < rows; row++){
            for(int column = 0; column < columns; column++){
                // init rectangle
                Color color = (row + column) % 2 == 0 ? CHESSBOARD_WHITE_COLOR : CHESSBOARD_BLACK_COLOR;
                double rectangleX = fieldWidth * column;
                double rectangleY = fieldHeight * row;
                Rectangle rectangle = new Rectangle(fieldWidth, fieldHeight, color);
                rectangle.setX(rectangleX);
                rectangle.setY(rectangleY);

                // init circle
                double circleX = rectangleX + fieldWidth / 2;
                double circleY = rectangleY + fieldHeight / 2;
                Circle circle = new Circle(circleX,circleY, Math.min(fieldWidth, fieldHeight) / 8, EventHandler.AVAILABLE_MOVE_COLOR);
//                circle.setOpacity(0.75);
                circle.setVisible(false);
                circle.setMouseTransparent(true);

                // add nodes to group
                ((Group) root).getChildren().add(rectangle);
                ((Group) root).getChildren().add(circle);
                //add rectangles to array
                rectangles[row][column] = rectangle;
                circles[row][column] = circle;
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
        root.getChildren().add(imageView);
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

        pieces.add(new Rook(0, 0, Piece.Colors.BLACK, this));
        pieces.add(new Knight(0, 1, Piece.Colors.BLACK, this));
        pieces.add(new Bishop(0, 2, Piece.Colors.BLACK, this));
        pieces.add(new Queen(0, 3, Piece.Colors.BLACK, this));
        blackKing = new King(0, 4, Piece.Colors.BLACK, this);
        pieces.add(blackKing);
        pieces.add(new Bishop(0, 5, Piece.Colors.BLACK, this));
        pieces.add(new Knight(0, 6, Piece.Colors.BLACK, this));
        pieces.add(new Rook(0, 7, Piece.Colors.BLACK, this));

        for(int i = 0; i < columns; i++){
            pieces.add(new Pawn(1, i, Piece.Colors.BLACK, this));
        }

        // init white pieces
        whitePieces = new ArrayList<>();

        pieces.add(new Rook(rows - 1, 0, Piece.Colors.WHITE, this));
        pieces.add(new Knight(rows - 1, 1, Piece.Colors.WHITE, this));
        pieces.add(new Bishop(rows - 1, 2, Piece.Colors.WHITE, this));
        pieces.add(new Queen(rows - 1, 3, Piece.Colors.WHITE, this));
        whiteKing = new King(rows - 1, 4, Piece.Colors.WHITE, this);
        pieces.add(whiteKing);
        pieces.add(new Bishop(rows - 1, 5, Piece.Colors.WHITE, this));
        pieces.add(new Knight(rows - 1, 6, Piece.Colors.WHITE, this));
        pieces.add(new Rook(rows - 1, 7, Piece.Colors.WHITE, this));

        for(int i = 0; i < columns; i++){
            pieces.add(new Pawn(rows - 2, i, Piece.Colors.WHITE, this));
        }

        for(Piece piece : pieces){
            if(piece.getColor() == Piece.Colors.BLACK)
                blackPieces.add(piece);
            else if (piece.getColor() == Piece.Colors.WHITE)
                whitePieces.add(piece);
        }

        moveGenerator = new MoveGenerator(this);
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
    public Piece getPiece(int row, int column) {
        for (Piece piece : pieces) {
            if (piece.getRow() == row && piece.getColumn() == column) {
                return piece;
            }
        }
        return null;
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

    public ArrayList<Piece> getBlackPieces() {
        return blackPieces;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void checkAllPinsAndChecks() {
        //which king is going to be checked
        King king = colorToMove == Piece.Colors.WHITE ? whiteKing : blackKing;
        ArrayList<Knight> knights = new ArrayList<>();
        isCheck = false;

        fieldsToBlockCheck.clear();

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
                    Piece piece = getPiece(x,y);
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
                            (piece instanceof Queen) ||
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
            ArrayList<Piece> pieces = colorToMove == Piece.Colors.WHITE ? blackPieces : whitePieces;
            for (Piece piece : pieces) {
                if (piece instanceof Knight) knights.add((Knight) piece);
                //if(knights.size()==2) break;
            }

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
                        if (kingColumn == column && kingRow == row) {
                            checks.add(knight);
                            //and add possibility to beat the knight
                            fieldsToBlockCheck.add(new Position(knight.getRow(), knight.getColumn()));
                        }

                    // set row move
                    if (i % 2 != 0) {
                        rowMove++;
                        if (rowMove == 0)
                            rowMove++;
                    }
                }
            }
        }
        else if(checks.size()==2) {
            ArrayList<Piece> pieces = colorToMove == Piece.Colors.WHITE ? whitePieces : blackPieces;
            for (Piece piece : pieces) {
                if (piece != king)
                    piece.setCanMove(false);
            }

        }

        // set check before moves generating
        if(checks.size() > 0)
            isCheck = true;

        // generate moves and count them
        moveGenerator.generateMoves(colorToMove);
        int movesNumber = moveGenerator.getCachedMovesNumber();

        String color = colorToMove == Piece.Colors.WHITE ? "white" : "black";
        System.out.println(color + ":" + movesNumber);

        if(movesNumber == 0){
            isRunning = false;
            if (isCheck)
                System.out.println("checkmate");
            else
                System.out.println("stalemate");
        }

        //DEBUG
        if(checks.size()>0){
            System.out.println("Checks:");
        }
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

    public void handlePawnPromotion(int row, int column, Piece.Colors color){
        // only promotion popup can be clicked
        disableFieldsEvents();

        // create and init popup and his child nodes
        FlowPane popup = new FlowPane();
        popup.setMaxWidth(fieldWidth);
        root.getChildren().add(popup);

        Rectangle queenRectangle = new Rectangle();
        queenRectangle.setId("queen");
        Rectangle rookRectangle = new Rectangle();
        rookRectangle.setId("rook");
        Rectangle knightRectangle = new Rectangle();
        knightRectangle.setId("knight");
        Rectangle bishopRectangle = new Rectangle();
        bishopRectangle.setId("bishop");
        Rectangle[] rectangles = {queenRectangle, rookRectangle, knightRectangle, bishopRectangle};
        ArrayList<Piece> promotionPieces = new ArrayList<>(rectangles.length);
        for(Rectangle rectangle : rectangles){
            rectangle.setWidth(fieldWidth);
            rectangle.setHeight(fieldHeight);
            rectangle.setFill(Color.web("#939393"));
            popup.getChildren().add(rectangle);
            Piece piece = switch (rectangle.getId()) {
                case "queen" -> new Queen(row, column, color, this);
                case "rook" -> new Rook(row + 1, column, color, this);
                case "knight" -> new Knight(row + 2, column, color, this);
                case "bishop" -> new Bishop(row + 3, column, color, this);
                default -> null;
            };
            promotionPieces.add(piece);
            if(piece != null){
                handleImageDraw(piece);
                rectangle.setOnMouseClicked(event ->{
                    pieces.add(piece);
                    if(color == Piece.Colors.WHITE)
                        whitePieces.add(piece);
                    else
                        blackPieces.add(piece);
                    for(Piece promotionPiece : promotionPieces){
                        root.getChildren().remove(promotionPiece.getImage());
                    }
                    piece.setRow(row);
                    handleImageDraw(piece);

                    unpinAndResetAllDirections();
                    checkAllPinsAndChecks();
                    
                    // handle check mark
                    if(isCheck){
                        King king = colorToMove == Piece.Colors.WHITE ? whiteKing : blackKing;
                        eventHandler.handleCheckMark(king.getPosition());
                    }
                    enableFieldsEvents();
                    eventHandler.sound();
                    root.getChildren().remove(popup);
                });
            }
        }
        popup.setLayoutX(column * fieldWidth);
        popup.setLayoutY(row == 0 ? 0 : height - fieldHeight * rectangles.length);
    }

    private void enableFieldsEvents(){
        for(int row = 0; row < rows; row++){
            for(int column = 0; column < columns; column++){
                rectangles[row][column].setMouseTransparent(false);
            }
        }
    }

    private void disableFieldsEvents(){
        for(int row = 0; row < rows; row++){
            for(int column = 0; column < columns; column++){
                rectangles[row][column].setMouseTransparent(true);
            }
        }
    }

    public MoveGenerator getMoveGenerator() {
        return moveGenerator;
    }

    public List<Position> getFieldsToBlockCheck() {
        return fieldsToBlockCheck;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public Piece.Colors getColorToMove() {
        return colorToMove;
    }

    public Node[][] getCircles(){
        return circles;
    }

    public double getFieldWidth() {
        return fieldWidth;
    }

    public double getFieldHeight() {
        return fieldHeight;
    }

    public Group getRoot() {
        return root;
    }

    public King getWhiteKing() {
        return whiteKing;
    }

    public King getBlackKing(){
        return blackKing;
    }

    public void setEventHandler(EventHandler eventHandler){
        this.eventHandler = eventHandler;
    }
}

