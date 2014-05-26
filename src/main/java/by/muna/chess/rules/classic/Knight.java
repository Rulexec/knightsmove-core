package by.muna.chess.rules.classic;

import by.muna.chess.ChessPosition;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Knight {
    private static class Offset {
        public int x, y;
        public Offset(int x, int y) { this.x = x; this.y = y; }
    }
    private static final List<Offset> offsets = Arrays.asList(
        new Offset(-2, -1), new Offset(-1, -2), new Offset(1, -2), new Offset(2, -1),
        new Offset(-2, 1), new Offset(-1, 2), new Offset(1, 2), new Offset(2, 1)
    );

    public static Iterator<ChessPosition> beatingCells(final ChessPosition pos) {
        return new Iterator<ChessPosition>() {
            private Iterator<Offset> offsetIterator = Knight.offsets.iterator();

            private ChessPosition nextPosition = null;

            private void prepareNext() {
                this.nextPosition = null;

                while (this.offsetIterator.hasNext()) {
                    Offset offset = this.offsetIterator.next();

                    int x = pos.getX() + offset.x;
                    int y = pos.getY() + offset.y;

                    if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                        this.nextPosition = new ChessPosition(x, y);
                        break;
                    }
                }
            }

            @Override
            public boolean hasNext() {
                if (this.nextPosition == null) {
                    if (!this.offsetIterator.hasNext()) return false;

                    this.prepareNext();

                    return this.nextPosition != null;
                } else {
                    return true;
                }
            }

            @Override
            public ChessPosition next() {
                if (this.nextPosition == null) {
                    this.prepareNext();
                }

                ChessPosition result = this.nextPosition;
                this.nextPosition = null;

                return result;
            }
        };
    }
}
