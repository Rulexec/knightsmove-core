package by.muna.chess.chessengine;

import by.muna.chess.ChessMove;

public interface ChessEngineCallback {
    void onSuccess(ChessMove move);
    void onFail();
}
