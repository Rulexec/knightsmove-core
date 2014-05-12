package by.muna.chess.tasks;

import by.muna.chess.IChessField;

public interface IChessTask {
    IChessField getField();
    IChessTaskCriteria getCriteria();
}
