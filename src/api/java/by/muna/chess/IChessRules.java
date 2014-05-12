package by.muna.chess;

import by.muna.chess.exceptions.ChessIllegalMoveException;

public interface IChessRules {
    ChessMateState getMateState(IChessField field);

    IChessTurnApplicator prepareTurn(IChessField field, ChessMove move) throws ChessIllegalMoveException;

    default void makeMove(IChessField field, ChessMove move) throws ChessIllegalMoveException {
        this.prepareTurn(field, move).applyToField(field);
    }
}
