package by.muna.chess;

import by.muna.chess.exceptions.ChessBadMoveStringException;
import by.muna.chess.exceptions.ChessBadPieceTypeStringException;
import by.muna.chess.exceptions.ChessBadPosStringException;

public class ChessMove {
    private ChessPosition from, to;
    private ChessPieceType spawn;

    public ChessMove(ChessPosition from, ChessPosition to) {
        this(from, to, null);
    }
    public ChessMove(ChessPosition from, ChessPosition to, ChessPieceType spawn) {
        this.from = from;
        this.to = to;
        this.spawn = spawn;
    }
    public static ChessMove fromString(String move) throws ChessBadMoveStringException {
        try {
            if (!(move.length() == 4 || move.length() == 5)) throw new ChessBadMoveStringException();

            ChessPosition from, to;
            ChessPieceType spawn = null;

            from = ChessPosition.fromString(move.substring(0, 2));
            to = ChessPosition.fromString(move.substring(2, 4));

            if (from.equals(to)) {
                throw new ChessBadMoveStringException();
            }

            if (move.length() == 5) {
                spawn = ChessPieceType.fromChar(move.charAt(4));
            }

            return new ChessMove(from, to, spawn);
        } catch (ChessBadPosStringException | ChessBadPieceTypeStringException ex) {
            throw new ChessBadMoveStringException();
        }
    }

    public int getFromX() { return this.from.getX(); }
    public int getFromY() { return this.from.getY(); }
    public int getToX() { return this.to.getX(); }
    public int getToY() { return this.to.getY(); }

    public ChessPosition getTo() {
        return this.to;
    }

    public ChessPieceType getSpawn() { return this.spawn; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(5);

        sb.append(this.from.toString());
        sb.append(this.to.toString());

        if (this.spawn != null) {
            sb.append(this.spawn.getLetter());
        }

        return sb.toString();
    }
}
