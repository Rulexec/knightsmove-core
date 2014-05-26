package by.muna.chess.tasks.criterias;

import by.muna.chess.ChessMateState;
import by.muna.chess.IChessField;
import by.muna.chess.IChessFieldState;
import by.muna.chess.rules.ChessClassicRules;
import by.muna.chess.tasks.ChessTaskCriteriaType;
import by.muna.chess.tasks.ChessTaskState;
import by.muna.chess.tasks.IChessTask;
import by.muna.chess.tasks.IChessTaskCriteria;
import by.muna.chess.tasks.IChessTaskCriteriaState;

public class ChessTaskCheckmateCriteria implements IChessTaskCriteria {
    private static class CriteriaState implements IChessTaskCriteriaState {
        public boolean playerIsWhite;
        public int startTurn;

        public CriteriaState(boolean playerIsWhite, int startTurn) {
            this.playerIsWhite = playerIsWhite;
            this.startTurn = startTurn;
        }

        @Override
        public IChessTaskCriteriaState clone() {
            return new CriteriaState(this.playerIsWhite, this.startTurn);
        }
    }

    private int turns;

    public ChessTaskCheckmateCriteria(int turns) {
        this.turns = turns;
    }

    public int getTurns() {
        return this.turns;
    }

    @Override
    public IChessTaskCriteriaState init(IChessTask task) {
        IChessFieldState fieldState = task.getField().getFieldState();

        return new CriteriaState(fieldState.isWhiteTurn(), fieldState.getFullMoves());
    }

    @Override
    public ChessTaskState check(IChessTaskCriteriaState o, IChessField field) {
        CriteriaState criteriaState = (CriteriaState) o;

        boolean playerMoved = criteriaState.playerIsWhite != field.getFieldState().isWhiteTurn();

        int movesDiff = field.getFieldState().getFullMoves() - criteriaState.startTurn;

        ChessMateState mateState = ChessClassicRules.RULES.getMateState(field);

        if (playerMoved) {
            switch (mateState) {
            case CHECKMATE: return ChessTaskState.WIN;
            case STALEMATE: return ChessTaskState.LOOSE;
            default: return movesDiff < this.turns ? ChessTaskState.NORMAL : ChessTaskState.LOOSE;
            }
        } else {
            switch (mateState) {
            case CHECKMATE: case STALEMATE: return ChessTaskState.LOOSE;
            default: return movesDiff < this.turns ? ChessTaskState.NORMAL : ChessTaskState.LOOSE;
            }
        }
    }

    @Override
    public ChessTaskCriteriaType getCriteriaType() {
        return ChessTaskCriteriaType.CHECKMATE;
    }
}
