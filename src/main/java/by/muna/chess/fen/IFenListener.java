package by.muna.chess.fen;

import by.muna.chess.ChessPiece;
import by.muna.chess.ChessPosition;

public interface IFenListener<T> {
    void onPiece(int x, int y, ChessPiece piece);

    T finish(boolean isWhiteTurn, ChessPosition enPassant,
             boolean whiteKingCastling, boolean whiteQueenCastling,
             boolean blackKingCastling, boolean blackQueenCastling,
             int halfMoves, int fullMoves);
}
