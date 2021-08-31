package chessboard.pieces;

import chessboard.ChessboardGenerator;
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

    private boolean isPinned;
    private boolean canMove = true;
    protected List<BlockedDirections> blockedDirections = new ArrayList<>();

    protected ChessboardGenerator chessboardGenerator;

    public Piece(int row, int column, String name, String filename, double power, Colors color, ChessboardGenerator chessboardGenerator) {
        this.row = row;
        this.column = column;
        this.name = name;
        this.filename = filename;
        this.power = power;
        this.color = color;
        this.chessboardGenerator = chessboardGenerator;
    }

    protected void removeIllegalMoves(List<Move> possibleMoves){
        if(chessboardGenerator.isCheck()){
            for(Move move : possibleMoves){
                boolean remove = true;
                for(Position position : chessboardGenerator.getFieldsToBlockCheck()){
                    if(move.getEndPosition().equals(position)){
                        remove = false;
                        break;
                    }
                }
                if(remove)
                    possibleMoves.remove(move);
            }
        }
    }

    // get all pseudolegal moves
    public abstract List<Move> getPossibleMoves(List<Piece> pieces);

    // capture handling
    protected void capture(Piece piece){
        if(piece != null){
            ((Group)chessboardGenerator.getRoot()).getChildren().remove(piece.getImage());
            chessboardGenerator.getPieces().remove(piece);
            if(piece.getColor() == Colors.WHITE)
                chessboardGenerator.getWhitePieces().remove(piece);
            else if(piece.getColor() == Colors.BLACK)
                chessboardGenerator.getBlackPieces().remove(piece);
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
        Pawn pawn = ChessboardGenerator.getPawnAbleToBeCapturedByEnPassant();
        if(pawn != null){
            pawn.setIsPossibleToBeCapturedByEnPassant(false);
            ChessboardGenerator.setPawnAbleToBeCapturedByEnPassant(null);
        }
    }

    // move piece on different field and update his image position
    public void move(int row, int column){

        // handle en passant
        unsetEnPassant();

        // capture handling
        Piece piece = chessboardGenerator.getPiece(row, column);
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
            if(piece == null)
                isNext = true;
        }
        return isNext;
    }

    protected List<Move> getStraightMoves(List<Piece> pieces){
        List<Move> possibleMoves = new ArrayList<>();

        //if horizontal are possible
        if(! blockedDirections.contains(BlockedDirections.HORIZONTAL)) {
            // get possible moves from current position to left border
            for(int column = this.column - 1; column >= 0; column--){
                Piece piece = chessboardGenerator.getPiece(this.row, column);
                if(!getPossibleMove(this.row, column, possibleMoves, piece))
                    break;
            }

            // get possible moves from current position to right border
            for(int column = this.column + 1; column <= Main.columns - 1; column++){
                Piece piece = chessboardGenerator.getPiece(this.row, column);
                if(!getPossibleMove(this.row, column, possibleMoves, piece))
                    break;
            }
        }
        //if vertical are possible
        if(! blockedDirections.contains(BlockedDirections.VERTICAL)) {
            // get possible moves from current position to top border
            for(int row = this.row - 1; row >= 0; row--){
                Piece piece = chessboardGenerator.getPiece(row, this.column);
                if(!getPossibleMove(row, this.column, possibleMoves, piece))
                    break;
            }

            // get possible moves from current position to bottom border
            for(int row = this.row + 1; row <= Main.rows - 1; row++){
                Piece piece = chessboardGenerator.getPiece(row, this.column);
                if(!getPossibleMove(row, this.column, possibleMoves, piece))
                    break;
            }
        }

        return possibleMoves;
    }

    protected List<Move> getDiagonalMoves(List<Piece> pieces){
        List<Move> possibleMoves = new ArrayList<>();



        //if udd are possible
        if(! blockedDirections.contains(BlockedDirections.DOWN_UP_DIAGONAL)) {

            // get possible moves from current position to the top of first diagonal \
            for (int column = this.column - 1, row = this.row - 1; column >= 0 && row >= 0; column--, row--) {
                Piece piece = chessboardGenerator.getPiece(row, column);
                if (!getPossibleMove(row, column, possibleMoves, piece))
                    break;
            }

            // get possible moves from current position to the bottom of first diagonal \
            for (int column = this.column + 1, row = this.row + 1; column <= Main.columns - 1 && row <= Main.columns - 1; column++, row++) {
                Piece piece = chessboardGenerator.getPiece(row, column);
                if (!getPossibleMove(row, column, possibleMoves, piece))
                    break;
            }
        }
        //if dud are possible
        if(! blockedDirections.contains(BlockedDirections.UP_DOWN_DIAGONAL)) {

            // get possible moves from current position to the top of second diagonal /
            for (int column = this.column + 1, row = this.row - 1; column <= Main.columns - 1 && row >= 0; column++, row--) {
                Piece piece = chessboardGenerator.getPiece(row, column);
                if (!getPossibleMove(row, column, possibleMoves, piece))
                    break;
            }

            // get possible moves from current position to the bottom of second diagonal /
            for (int column = this.column - 1, row = this.row + 1; column >= 0 && row <= Main.rows - 1; column--, row++) {
                Piece piece = chessboardGenerator.getPiece(row, column);
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

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }
    public boolean canMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

}
