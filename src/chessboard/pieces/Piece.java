package chessboard.pieces;

import chessboard.Chessboard;
import chessboard.Main;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import utils.Move;
import utils.MoveHandler;
import utils.Position;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {

    public enum Colors{
        WHITE,
        BLACK
    };

    public enum BlockedDirections {
        VERTICAL,
        HORIZONTAL,
        UP_DOWN_DIAGONAL, //we look at it from left to right
        DOWN_UP_DIAGONAL
    }

    protected int row;
    protected int column;
    private String name;
    private String filename;
    private double power;
    protected Colors color;
    private ImageView image;

    protected boolean isPinned;
    protected boolean canMove = true;
    protected List<BlockedDirections> blockedDirections = new ArrayList<>();

    protected Chessboard chessboard;

    public Piece(int row, int column, String name, String filename, double power, Colors color, Chessboard chessboard) {
        this.row = row;
        this.column = column;
        this.name = name;
        this.filename = filename;
        this.power = power;
        this.color = color;
        this.chessboard = chessboard;
    }

    protected void removeIllegalMoves(List<Move> possibleMoves){
        if(chessboard.isCheck() && color == chessboard.getColorToMove()){
            possibleMoves.removeIf(move -> {
                boolean remove = true;
                for(Position position : chessboard.getFieldsToBlockCheck()){
                    if(move.getEndPosition().equals(position)){
                        remove = false;
                        break;
                    }
                }
                return remove;
            });
        }
    }

    // get all pseudolegal moves
    public abstract List<Move> getPossibleMoves();

    // capture handling
    protected void capture(Piece piece){
        if(piece != null){
            ((Group) chessboard.getRoot()).getChildren().remove(piece.getImage());
            chessboard.getPieces().remove(piece);
            if(piece.getColor() == Colors.WHITE)
                chessboard.getWhitePieces().remove(piece);
            else if(piece.getColor() == Colors.BLACK)
                chessboard.getBlackPieces().remove(piece);

            // draw handling
            chessboard.checkDraw();
        }
    }

    protected void changePosition(int row, int column){
        this.row = row;
        this.column = column;
        int x = (int)(image.getFitWidth() * column);
        int y = (int)(image.getFitHeight() * row);
        image.setX(x);
        image.setY(y);
    }

    protected void unsetEnPassant(){
        Pawn pawn = Chessboard.getPawnAbleToBeCapturedByEnPassant();
        if(pawn != null){
            pawn.setIsPossibleToBeCapturedByEnPassant(false);
            Chessboard.setPawnAbleToBeCapturedByEnPassant(null);
        }
    }

    // move piece on different field and update his image position
    public void move(int row, int column){

        // handle en passant
        unsetEnPassant();

        // capture handling
        Piece piece = chessboard.getPiece(row, column);
        if(piece != null)
            capture(piece);

        // handle change of piece and his image
        changePosition(row, column);
    }

    // add move to possible moves array and return true if next position is available or false otherwise
    protected boolean getPossibleMove(int row, int column, List<Move> possibleMoves, Piece piece){
        boolean isNext = false;
        if(MoveHandler.isValid(this, piece)){
            possibleMoves.add(new Move(this, getPosition(), new Position(row, column)));
            if(piece == null || (color != chessboard.getColorToMove() && piece instanceof King && color != piece.getColor()))
                isNext = true;
        }
        return isNext;
    }

    protected List<Move> getStraightMoves(){

        List<Move> possibleMoves = new ArrayList<>();

        //if horizontal are possible
        if(! blockedDirections.contains(BlockedDirections.HORIZONTAL)) {
            // get possible moves from current position to left border
            for(int column = this.column - 1; column >= 0; column--){
                Piece piece = chessboard.getPiece(this.row, column);
                if(!getPossibleMove(this.row, column, possibleMoves, piece))
                    break;
            }

            // get possible moves from current position to right border
            for(int column = this.column + 1; column <= Main.columns - 1; column++){
                Piece piece = chessboard.getPiece(this.row, column);
                if(!getPossibleMove(this.row, column, possibleMoves, piece))
                    break;
            }
        }
        //if vertical are possible
        if(! blockedDirections.contains(BlockedDirections.VERTICAL)) {
            // get possible moves from current position to top border
            for(int row = this.row - 1; row >= 0; row--){
                Piece piece = chessboard.getPiece(row, this.column);
                if(!getPossibleMove(row, this.column, possibleMoves, piece))
                    break;
            }

            // get possible moves from current position to bottom border
            for(int row = this.row + 1; row <= Main.rows - 1; row++){
                Piece piece = chessboard.getPiece(row, this.column);
                if(!getPossibleMove(row, this.column, possibleMoves, piece))
                    break;
            }
        }

        return possibleMoves;
    }

    protected List<Move> getDiagonalMoves(){
        List<Move> possibleMoves = new ArrayList<>();

        //if udd are possible
        if(! blockedDirections.contains(BlockedDirections.DOWN_UP_DIAGONAL)) {

            // get possible moves from current position to the top of first diagonal \
            for (int column = this.column - 1, row = this.row - 1; column >= 0 && row >= 0; column--, row--) {
                Piece piece = chessboard.getPiece(row, column);
                if (!getPossibleMove(row, column, possibleMoves, piece))
                    break;
            }

            // get possible moves from current position to the bottom of first diagonal \
            for (int column = this.column + 1, row = this.row + 1; column <= Main.columns - 1 && row <= Main.columns - 1; column++, row++) {
                Piece piece = chessboard.getPiece(row, column);
                if (!getPossibleMove(row, column, possibleMoves, piece))
                    break;
            }
        }
        //if dud are possible
        if(! blockedDirections.contains(BlockedDirections.UP_DOWN_DIAGONAL)) {

            // get possible moves from current position to the top of second diagonal /
            for (int column = this.column + 1, row = this.row - 1; column <= Main.columns - 1 && row >= 0; column++, row--) {
                Piece piece = chessboard.getPiece(row, column);
                if (!getPossibleMove(row, column, possibleMoves, piece))
                    break;
            }

            // get possible moves from current position to the bottom of second diagonal /
            for (int column = this.column - 1, row = this.row + 1; column >= 0 && row <= Main.rows - 1; column--, row++) {
                Piece piece = chessboard.getPiece(row, column);
                if (!getPossibleMove(row, column, possibleMoves, piece))
                    break;
            }
        }

        return possibleMoves;
    }

    public void blockDirections(BlockedDirections direction) {
        blockedDirections.add(BlockedDirections.DOWN_UP_DIAGONAL);
        blockedDirections.add(BlockedDirections.UP_DOWN_DIAGONAL);
        blockedDirections.add(BlockedDirections.VERTICAL);
        blockedDirections.add(BlockedDirections.HORIZONTAL);
        blockedDirections.remove(direction);
    }

    public static boolean isSignificantFigure(Piece piece){
        return piece instanceof Pawn || piece instanceof Rook || piece instanceof Queen;
    }

    public void unblockDirections() {
        blockedDirections.clear();
    }


    public Position getPosition(){
        return new Position(row, column);
    }

    public int getRow() {
        return row;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public int getColumn() {
        return column;
    }

    public String getName() {
        return name;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public Chessboard getChessboard(){
        return chessboard;
    }

    public void setRow(int row){
        this.row = row;
    }

}
