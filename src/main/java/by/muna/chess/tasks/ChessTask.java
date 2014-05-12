package by.muna.chess.tasks;

import by.muna.chess.IChessField;

public class ChessTask implements IChessTask {
    private IChessField field;
    private IChessTaskCriteria criteria;

    public ChessTask(IChessField field, IChessTaskCriteria criteria) {
        this.field = field;
        this.criteria = criteria;
    }

    @Override
    public IChessField getField() {
        return this.field;
    }

    @Override
    public IChessTaskCriteria getCriteria() {
        return this.criteria;
    }
}
