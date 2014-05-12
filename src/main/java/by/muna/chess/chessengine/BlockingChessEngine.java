package by.muna.chess.chessengine;

import by.muna.chess.ChessMove;
import by.muna.chess.IChessField;
import by.muna.chess.chessengine.exceptions.ChessEngineException;

import java.util.concurrent.CountDownLatch;

public class BlockingChessEngine {
    private ChessEngineAsyncProvider asyncEngine;

    public BlockingChessEngine(ChessEngineAsyncProvider asyncEngine) {
        this.asyncEngine = asyncEngine;
    }

    public ChessMove calc(IChessField field, int plies) throws ChessEngineException {
        class EngineResult {
            public ChessMove move = null;
            public boolean fail = false;
        }

        final CountDownLatch latch = new CountDownLatch(1);
        final EngineResult result = new EngineResult();

        this.asyncEngine.calc(field, plies, new ChessEngineCallback() {
            @Override
            public void onSuccess(ChessMove move) {
                result.move = move;
                latch.countDown();
            }

            @Override
            public void onFail() {
                result.fail = true;
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException ex) {}

        if (!result.fail) {
            return result.move;
        } else {
            throw new ChessEngineException();
        }
    }
}
