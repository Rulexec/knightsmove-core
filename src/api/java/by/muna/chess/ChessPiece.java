package by.muna.chess;

public class ChessPiece {
    private ChessPieceType pieceType;
    private boolean isWhite;

    public ChessPiece(ChessPieceType pieceType, boolean isWhite) {
        this.pieceType = pieceType;
        this.isWhite = isWhite;
    }

    public ChessPieceType getPieceType() {
        return this.pieceType;
    }

    public boolean isWhite() {
        return this.isWhite;
    }
}
