package by.muna.chess.rules.classic;

import by.muna.chess.ChessPosition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Pawn {
    public static Iterator<ChessPosition> beatingCells(ChessPosition pos, boolean forWhite) {
        int x1 = pos.getX() - 1;
        int x2 = pos.getX() + 1;
        int y = pos.getY() + (forWhite ? 1 : -1);

        List<ChessPosition> positions = new ArrayList<>(2);

        if (x1 >= 0 && x1 < 8) positions.add(new ChessPosition(x1, y));
        if (x2 >= 0 && x2 < 8) positions.add(new ChessPosition(x2, y));

        return positions.iterator();
    }
}
