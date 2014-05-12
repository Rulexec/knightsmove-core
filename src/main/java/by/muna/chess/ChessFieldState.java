package by.muna.chess;

public class ChessFieldState implements IChessFieldState {
    private boolean whiteTurn;
    private ChessPosition enPassant;
    boolean[] castlings = new boolean[4];
    private int halfMoves;
    private int fullMoves;

    public ChessFieldState(boolean isWhiteTurn, ChessPosition enPassant,
                           boolean whiteKingCastling, boolean whiteQueenCastling,
                           boolean blackKingCastling, boolean blackQueenCastling,
                           int halfMoves, int fullMoves)
    {
        this.whiteTurn = isWhiteTurn;
        this.enPassant = enPassant;

        this.castlings[0] = whiteKingCastling;
        this.castlings[1] = whiteQueenCastling;
        this.castlings[2] = blackKingCastling;
        this.castlings[3] = blackQueenCastling;

        this.halfMoves = halfMoves;
        this.fullMoves = fullMoves;
    }
    public static ChessFieldState fromFieldState(IChessFieldState fieldState) {
        return new ChessFieldState(
            fieldState.isWhiteTurn(), fieldState.getEnPassant(),
            fieldState.isCastlingAvailable(true, true),
            fieldState.isCastlingAvailable(true, false),
            fieldState.isCastlingAvailable(false, true),
            fieldState.isCastlingAvailable(false, false),
            fieldState.getHalfMoves(), fieldState.getFullMoves()
        );
    }

    @Override
    public boolean isWhiteTurn() {
        return this.whiteTurn;
    }

    @Override
    public void setTurn(boolean white) {
        this.whiteTurn = white;
    }

    @Override
    public boolean isCastlingAvailable(boolean forWhite, boolean kingSided) {
        return this.castlings[(forWhite ? 0 : 2) + (kingSided ? 0 : 1)];
    }

    @Override
    public void setCastling(boolean available, boolean forWhite, boolean kingSided) {
        this.castlings[(forWhite ? 0 : 2) + (kingSided ? 0 : 1)] = available;
    }

    @Override
    public ChessPosition getEnPassant() {
        return this.enPassant;
    }

    @Override
    public void setEnPassant(ChessPosition pos) {
        this.enPassant = pos;
    }

    @Override
    public int getHalfMoves() {
        return this.halfMoves;
    }

    @Override
    public void setHalfMoves(int halfMoves) {
        this.halfMoves = halfMoves;
    }

    @Override
    public int getFullMoves() {
        return this.fullMoves;
    }

    @Override
    public void setFullMoves(int fullMoves) {
        this.fullMoves = fullMoves;
    }
}
