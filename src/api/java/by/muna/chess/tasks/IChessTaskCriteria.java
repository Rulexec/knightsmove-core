package by.muna.chess.tasks;

import by.muna.chess.IChessField;

public interface IChessTaskCriteria {
    ChessTaskCriteriaType getCriteriaType();

    Object init(IChessTask task);

    ChessTaskState check(Object o, IChessField field);
}
