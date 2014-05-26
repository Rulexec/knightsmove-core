package by.muna.chess.tasks;

import by.muna.chess.IChessField;

public interface IChessTaskCriteria {
    ChessTaskCriteriaType getCriteriaType();

    IChessTaskCriteriaState init(IChessTask task);

    ChessTaskState check(IChessTaskCriteriaState o, IChessField field);
}
