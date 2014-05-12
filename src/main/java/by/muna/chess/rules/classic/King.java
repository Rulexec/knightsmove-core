package by.muna.chess.rules.classic;

import by.muna.chess.ChessPosition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class King {
    public static Iterator<ChessPosition> beatingCells(ChessPosition pos) {
        List<ChessPosition> cells = new ArrayList<>(8);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int x = pos.getX() + dx;
                int y = pos.getY() + dy;

                if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                    cells.add(new ChessPosition(x, y));
                }
            }
        }

        return cells.iterator();
    }
}
