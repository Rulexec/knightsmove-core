package by.muna.chess;

import by.muna.chess.exceptions.ChessBadPosStringException;

public class ChessPosition {
    private int x, y;

    public ChessPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public static ChessPosition fromString(String pos) throws ChessBadPosStringException {
        int x = pos.charAt(0) - 'a';
        int y = pos.charAt(1) - '1';

        if (x < 0 || x > 7 || y < 0 || y > 7) throw new ChessBadPosStringException();

        return new ChessPosition(x, y);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject instanceof ChessPosition) {
            ChessPosition other = (ChessPosition) otherObject;
            return this.x == other.x && this.y == other.y;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return new String(new char[] {(char) ('a' + this.x), (char) ('1' + this.y)});
    }
}
