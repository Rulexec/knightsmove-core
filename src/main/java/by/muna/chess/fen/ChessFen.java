package by.muna.chess.fen;

import by.muna.chess.ChessPiece;
import by.muna.chess.ChessPieceType;
import by.muna.chess.ChessPosition;
import by.muna.chess.IChessField;
import by.muna.chess.IChessFieldState;
import by.muna.chess.exceptions.ChessBadFENException;

public class ChessFen {
    public static String fieldToFen(IChessField field) {
        StringBuilder sb = new StringBuilder(64);

        sb.append(ChessFen.boardOnlyToFen(field));

        sb.append(' ');

        sb.append(ChessFen.fieldStateToFen(field.getFieldState()));

        return sb.toString();
    }

    public static String boardOnlyToFen(IChessField field) {
        StringBuilder sb = new StringBuilder(64);

        for (int y = 7; y >= 0; y--) {
            if (y != 7) {
                sb.append('/');
            }

            int space = 0;

            for (int x = 0; x < 8; x++) {
                ChessPiece piece = field.getPieceAt(x, y);

                if (piece != null) {
                    if (space > 0) {
                        sb.append(Integer.toString(space));
                    }

                    char letter = piece.getPieceType().getLetter();
                    if (piece.isWhite()) {
                        letter = Character.toUpperCase(letter);
                    }

                    sb.append(letter);

                    space = 0;
                } else {
                    space++;
                }
            }

            if (space > 0) {
                sb.append(Integer.toString(space));
            }
        }

        return sb.toString();
    }

    public static String fieldStateToFen(IChessFieldState fieldState) {
        // w KQkq - 0 1
        StringBuilder sb = new StringBuilder(16);

        sb.append(fieldState.isWhiteTurn() ? 'w' : 'b').append(' ');

        int noCastlings = 0;

        if (fieldState.isCastlingAvailable(true, true)) sb.append('K'); else noCastlings++;
        if (fieldState.isCastlingAvailable(true, false)) sb.append('Q'); else noCastlings++;
        if (fieldState.isCastlingAvailable(false, true)) sb.append('k'); else noCastlings++;
        if (fieldState.isCastlingAvailable(false, false)) sb.append('q'); else noCastlings++;

        if (noCastlings == 4) sb.append('-');

        sb.append(' ');

        ChessPosition enPassant = fieldState.getEnPassant();

        if (enPassant != null) sb.append(enPassant.toString());
        else sb.append('-');

        sb.append(' ');

        sb.append(Integer.toString(fieldState.getHalfMoves()));
        sb.append(' ');
        sb.append(Integer.toString(fieldState.getFullMoves()));

        return sb.toString();
    }

    public static <T> T readFen(String fen, IFenListener<T> listener) throws ChessBadFENException {
        // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1

        String[] parts = fen.split(" ");
        if (parts.length != 6) {
            throw new ChessBadFENException();
        }

        String board[] = parts[0].split("/");
        if (board.length != 8) throw new ChessBadFENException();

        boolean isWhiteTurn = parts[1].charAt(0) == 'w';
        String castling = parts[2];
        String enPassantPos = parts[3];
        int halfMoves, fullMoves;

        try {
            halfMoves = Integer.parseInt(parts[4]);
            fullMoves = Integer.parseInt(parts[5]);
        } catch (NumberFormatException ex) {
            throw new ChessBadFENException();
        }

        if (halfMoves < 0 || fullMoves < 1) throw new ChessBadFENException();

        // parse board
        int y = 8;
        for (String line : board) {
            y--;
            int x = 0;
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);

                if (c >= '0' && c <= '9') {
                    x += c - '0';
                } else {
                    boolean isWhite = Character.isUpperCase(c);
                    c = Character.toLowerCase(c);

                    ChessPiece piece;

                    switch (c) {
                    case 'p': piece = new ChessPiece(ChessPieceType.PAWN, isWhite); break;
                    case 'n': piece = new ChessPiece(ChessPieceType.KNIGHT, isWhite); break;
                    case 'b': piece = new ChessPiece(ChessPieceType.BISHOP, isWhite); break;
                    case 'r': piece = new ChessPiece(ChessPieceType.ROOK, isWhite); break;
                    case 'q': piece = new ChessPiece(ChessPieceType.QUEEN, isWhite); break;
                    case 'k': piece = new ChessPiece(ChessPieceType.KING, isWhite); break;
                    default: throw new ChessBadFENException();
                    }

                    //field.setPieceAt(x, y, piece);
                    listener.onPiece(x, y, piece);

                    x++;
                }
            }
        }

        boolean whiteKingCastling = false,
            whiteQueenCastling = false,
            blackKingCastling = false,
            blackQueenCastling = false;

        // castling
        if (castling.charAt(0) != '-') {
            for (int i = 0; i < castling.length(); i++) {
                char c = castling.charAt(i);
                boolean isWhite = Character.isUpperCase(c);
                c = Character.toLowerCase(c);

                switch (c) {
                case 'k':
                    if (isWhite) {
                        whiteKingCastling = true;
                    } else {
                        blackKingCastling = true;
                    }
                    break;
                case 'q':
                    if (isWhite) {
                        whiteQueenCastling = true;
                    } else {
                        blackQueenCastling = true;
                    }
                    break;
                default: throw new ChessBadFENException();
                }
            }
        }

        ChessPosition enPassant = null;
        if (enPassantPos.charAt(0) != '-') {
            int x = enPassantPos.charAt(0) - 'a';
            y = enPassantPos.charAt(1) - '1';

            if (x < 0 || x > 7 || y < 0 || y > 7) throw new ChessBadFENException();

            enPassant = new ChessPosition(x, y);
        }

        return listener.finish(
            isWhiteTurn, enPassant,
            whiteKingCastling, whiteQueenCastling,
            blackKingCastling, blackQueenCastling,
            halfMoves, fullMoves
        );
    }
}
