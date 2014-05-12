package by.muna.chess.chessengine.exceptions;

public class ChessEngineException extends Exception {
    public ChessEngineException() {
        super();
    }

    public ChessEngineException(String message) {
        super(message);
    }

    public ChessEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChessEngineException(Throwable cause) {
        super(cause);
    }
}
