package utils;

import chessboard.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    private ArrayList<Piece> pieces;
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;

    public MoveGenerator(ArrayList<Piece> pieces, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        this.pieces = pieces;
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
    }
}
