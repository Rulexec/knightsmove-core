package by.muna.chess.misc;

import by.muna.chess.ChessPiece;
import by.muna.chess.IChessField;

import java.util.HashMap;
import java.util.Map;

public class ChessBoardFormatter {
    private static Map<Character, Character> pieceCharaterMap = new HashMap<>();
    static {
        ChessBoardFormatter.pieceCharaterMap.put('n', '♘');
        ChessBoardFormatter.pieceCharaterMap.put('b', '♗');
        ChessBoardFormatter.pieceCharaterMap.put('r', '♖');
        ChessBoardFormatter.pieceCharaterMap.put('q', '♕');
        ChessBoardFormatter.pieceCharaterMap.put('k', '♔');
        ChessBoardFormatter.pieceCharaterMap.put('P', '♟');
        ChessBoardFormatter.pieceCharaterMap.put('N', '♞');
        ChessBoardFormatter.pieceCharaterMap.put('B', '♝');
        ChessBoardFormatter.pieceCharaterMap.put('R', '♜');
        ChessBoardFormatter.pieceCharaterMap.put('Q', '♛');
        ChessBoardFormatter.pieceCharaterMap.put('K', '♚');
        ChessBoardFormatter.pieceCharaterMap.put('p', '♙');
    }

    public static String boardToString(IChessField field) {
        StringBuilder sb = new StringBuilder();

        sb.append("╔═══╤═══╤═══╤═══╤═══╤═══╤═══╤═══╗\n");

        for (int y = 7; y >= 0; y--) {
            sb.append("║ ");

            for (int x = 0; x < 8; x++) {
                ChessPiece piece = field.getPieceAt(x, y);

                if (piece != null) {
                    char c;

                    if (piece.isWhite()) {
                        c = Character.toUpperCase(piece.getPieceType().getLetter());
                    } else {
                        c = piece.getPieceType().getLetter();
                    }

                    sb.append(ChessBoardFormatter.pieceCharaterMap.get(c));
                } else {
                    sb.append(' ');
                }

                if (x != 7) sb.append(" | ");
            }

            sb.append(" ║ " + (y + 1) + "\n");

            if (y != 0) sb.append("╟───┼───┼───┼───┼───┼───┼───┼───╢\n");
        }

        sb.append("╚═══╧═══╧═══╧═══╧═══╧═══╧═══╧═══╝\n");
        sb.append("  a   b   c   d   e   f   g   h  \n");

        return sb.toString();
    }
}
