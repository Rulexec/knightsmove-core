package by.muna.chess.exceptions;

public class ChessIllegalMoveException extends Exception {
    public ChessIllegalMoveException() {
        super();
    }
    public ChessIllegalMoveException(String message) {
        super(message);
    }
}
