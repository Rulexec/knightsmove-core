package by.muna.chess.chessengine;

import by.muna.chess.IChessField;

public interface ChessEngineAsyncProvider {
    void calc(IChessField field, int plies, ChessEngineCallback callback);
}
