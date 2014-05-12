package by.muna.chess.rules.classic;

import by.muna.chess.ChessPosition;

import java.util.Arrays;
import java.util.Iterator;

public class Straight {
    public static Iterator<ChessPosition> line(ChessPosition pos, int dx, int dy) {
        return Straight.line(pos, dx, dy, 7);
    }
    public static Iterator<ChessPosition> line(ChessPosition pos, int dx, int dy, int distance) {
        return new Iterator<ChessPosition>() {
            private int x = pos.getX() + dx, y = pos.getY() + dy;
            private int d = distance;

            @Override
            public boolean hasNext() {
                return this.x >= 0 && this.x < 8 && this.y >= 0 && this.y < 8 && this.d > 0;
            }

            @Override
            public ChessPosition next() {
                ChessPosition result = new ChessPosition(this.x, this.y);

                this.x += dx;
                this.y += dy;
                this.d--;

                return result;
            }
        };
    }

    public static Iterator<Iterator<ChessPosition>> bishopBeatingBeams(ChessPosition pos) {
        return Arrays.asList(
            Straight.line(pos, -1, -1), Straight.line(pos, -1, 1),
            Straight.line(pos, 1, -1), Straight.line(pos, 1, 1)
        ).iterator();
    }
    public static Iterator<Iterator<ChessPosition>> rookBeatingBeams(ChessPosition pos) {
        return Arrays.asList(
            Straight.line(pos, 0, -1), Straight.line(pos, 0, 1),
            Straight.line(pos, -1, 0), Straight.line(pos, 1, 0)
        ).iterator();
    }
    public static Iterator<Iterator<ChessPosition>> queenBeatingBeams(ChessPosition pos) {
        return Arrays.asList(
            Straight.line(pos, -1, -1), Straight.line(pos, -1, 1),
            Straight.line(pos, 1, -1), Straight.line(pos, 1, 1),
            Straight.line(pos, 0, -1), Straight.line(pos, 0, 1),
            Straight.line(pos, -1, 0), Straight.line(pos, 1, 0)
        ).iterator();
    }
}
