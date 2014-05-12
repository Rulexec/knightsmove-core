package by.muna.chess;

import by.muna.chess.exceptions.ChessBadPieceTypeStringException;

public enum ChessPieceType {
    PAWN('p'), KNIGHT('n'), BISHOP('b'), ROOK('r'), QUEEN('q'), KING('k');

    private char letter;

    private ChessPieceType(char letter) {
        this.letter = letter;
    }
    public static ChessPieceType fromChar(char c) throws ChessBadPieceTypeStringException {
        switch (c) {
        case 'p': return PAWN;
        case 'n': return KNIGHT;
        case 'b': return BISHOP;
        case 'r': return ROOK;
        case 'q': return QUEEN;
        case 'k': return KING;
        default: throw new ChessBadPieceTypeStringException();
        }
    }

    public char getLetter() {
        return this.letter;
    }
}