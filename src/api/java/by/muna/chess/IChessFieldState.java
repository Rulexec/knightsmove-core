package by.muna.chess;

public interface IChessFieldState {
    boolean isWhiteTurn();

    boolean isCastlingAvailable(boolean forWhite, boolean kingSided);
    ChessPosition getEnPassant();

    int getHalfMoves();
    int getFullMoves();

    void setTurn(boolean white);
    void setCastling(boolean available, boolean forWhite, boolean kingSided);
    void setEnPassant(ChessPosition pos);

    void setHalfMoves(int halfMoves);
    void setFullMoves(int fullMoves);

    default void resetHalfMoves() { this.setHalfMoves(0); }
    default void incrementHalfMoves() { this.setHalfMoves(this.getHalfMoves() + 1); }
    default void incrementFullMoves() { this.setFullMoves(this.getFullMoves() + 1); }
}
