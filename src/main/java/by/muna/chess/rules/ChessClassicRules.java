package by.muna.chess.rules;

import by.muna.chess.ChessBoard;
import by.muna.chess.ChessMateState;
import by.muna.chess.ChessMove;
import by.muna.chess.ChessPiece;
import by.muna.chess.ChessPieceType;
import by.muna.chess.ChessPosition;
import by.muna.chess.IChessBoard;
import by.muna.chess.IChessField;
import by.muna.chess.IChessFieldState;
import by.muna.chess.IChessRules;
import by.muna.chess.IChessTurnApplicator;
import by.muna.chess.exceptions.ChessIllegalMoveException;
import by.muna.chess.rules.classic.King;
import by.muna.chess.rules.classic.Knight;
import by.muna.chess.rules.classic.Pawn;
import by.muna.chess.rules.classic.Straight;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChessClassicRules implements IChessRules {
    public static final IChessRules RULES = new ChessClassicRules();

    @Override
    public ChessMateState getMateState(IChessField field) {
        // FIXME: крайне не эффективно, переписать полностью

        List<ChessPosition> piecePositions = new ArrayList<>(16);

        boolean forWhite = field.getFieldState().isWhiteTurn();
        ChessPosition whiteKingPos = null, blackKingPos = null;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                ChessPiece piece = field.getPieceAt(x, y);
                if (piece != null) {
                    if (piece.isWhite() == forWhite) {
                        piecePositions.add(new ChessPosition(x, y));
                    }

                    if (piece.getPieceType() == ChessPieceType.KING) {
                        if (piece.isWhite()) {
                            whiteKingPos = new ChessPosition(x, y);
                        } else {
                            blackKingPos = new ChessPosition(x, y);
                        }
                    }
                }
            }
        }

        boolean canMove = false;

        loop: for (ChessPosition pos : piecePositions) {
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    try {
                        this.prepareTurn(
                            field,
                            new ChessMove(pos, new ChessPosition(x, y), ChessPieceType.QUEEN)
                        );

                        // if can move, exception will not thrown
                        canMove = true;
                        break loop;
                    } catch (ChessIllegalMoveException ex) {}
                }
            }
        }

        ChessPosition kingPosition = forWhite ? whiteKingPos : blackKingPos;
        boolean isCheck = ChessClassicRules.isUnderCheck(
            field, kingPosition, forWhite
        );

        if (isCheck) {
            return canMove ? ChessMateState.CHECK : ChessMateState.CHECKMATE;
        } else {
            return canMove ? ChessMateState.NORMAL : ChessMateState.STALEMATE;
        }
    }

    @Override
    public IChessTurnApplicator prepareTurn(IChessField field, ChessMove move)
        throws ChessIllegalMoveException
    {
        ChessPiece piece = field.getPieceAt(move.getFromX(), move.getFromY());
        ChessPiece targetPiece = field.getPieceAt(move.getToX(), move.getToY());

        boolean isWhiteTurn = field.getFieldState().isWhiteTurn();

        // Есть ли фигура на том поле и того ли она цвета
        if (piece == null) throw new ChessIllegalMoveException("empty cell");
        if (piece.isWhite() != isWhiteTurn) throw new ChessIllegalMoveException("not your piece");

        if (targetPiece != null && targetPiece.isWhite() == isWhiteTurn) {
            throw new ChessIllegalMoveException("cannot beat own piece");
        }

        final ChessPiece[][] board = new ChessPiece[8][8];

        ChessPosition whiteKingPos = null, blackKingPos = null;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                ChessPiece p = field.getPieceAt(x, y);

                board[y][x] = p;

                if (p != null && p.getPieceType() == ChessPieceType.KING) {
                    if (p.isWhite()) {
                        whiteKingPos = new ChessPosition(x, y);
                    } else {
                        blackKingPos = new ChessPosition(x, y);
                    }
                }
            }
        }

        ChessPosition newEnPassant = null;
        boolean resetHalfMoves =
            (targetPiece != null && targetPiece.isWhite() != isWhiteTurn) ||
                piece.getPieceType() == ChessPieceType.PAWN;
        boolean kingSidedCastlingBreak = false,
                queenSidedCastlingBreak = false;

        ChessPosition ourKingPosition = (isWhiteTurn ? whiteKingPos : blackKingPos);

        piecesSwitch: switch (piece.getPieceType()) {
        case PAWN: {
            int dx = move.getToX() - move.getFromX();
            int dy = move.getToY() - move.getFromY();

            if (dx == 0) { // Ход вперёд
                int ady = Math.abs(dy);
                if (ady > 2) throw new ChessIllegalMoveException();

                // Проверяем, не занята ли клетка назначения
                if (board[move.getToY()][move.getToX()] != null) {
                    throw new ChessIllegalMoveException();
                }

                if (dy > 0 != isWhiteTurn) {
                    throw new ChessIllegalMoveException("pawn inversed move");
                }

                if (ady == 2) {
                    // Если это ход на две клетки, проверяем, точно ли это исходный ряд пешек
                    // и не занята ли проходная.
                    if (move.getFromY() != (isWhiteTurn ? 1 : 6)) throw new ChessIllegalMoveException();

                    ChessPosition enPassant =
                        new ChessPosition(move.getFromX(), move.getFromY() + (isWhiteTurn ? 1 : -1));

                    newEnPassant = enPassant;

                    if (board[enPassant.getY()][enPassant.getX()] != null) {
                        throw new ChessIllegalMoveException();
                    }
                } // else ady == 1
            } else { // Возможно сбитие
                int adx = Math.abs(dx);

                if (adx > 1 || dy != (isWhiteTurn ? 1 : -1)) {
                    // Не сбитие
                    throw new ChessIllegalMoveException();
                }

                ChessPiece enemyPiece = board[move.getToY()][move.getToX()];

                if (enemyPiece != null) {
                    if (enemyPiece.isWhite() == isWhiteTurn) throw new ChessIllegalMoveException();
                } else {
                    // Там, где пытаемся сбить нет фигуры,
                    // это или взятие на проходе, или фейл.
                    ChessPosition enPassant = field.getFieldState().getEnPassant();
                    if (enPassant == null || !move.getTo().equals(enPassant)) {
                        throw new ChessIllegalMoveException();
                    }

                    board[enPassant.getY() + (isWhiteTurn ? -1 : 1)][enPassant.getX()] = null;
                }
            }

            // Проверка на поставку фигуры
            if (move.getToY() != (isWhiteTurn ? 7 : 0)) {
                board[move.getToY()][move.getToX()] = piece;
            } else {
                if (move.getSpawn() != null) {
                    board[move.getToY()][move.getToX()] = new ChessPiece(move.getSpawn(), isWhiteTurn);
                } else {
                    throw new ChessIllegalMoveException("No spawn specified");
                }
            }
            board[move.getFromY()][move.getFromX()] = null;

        } break;
        case KNIGHT: {
            int adx = Math.abs(move.getToX() - move.getFromX());
            int ady = Math.abs(move.getToY() - move.getFromY());

            // Не ход конём
            if (!((adx == 1 && ady == 2) || (adx == 2 && ady == 1))) {
                throw new ChessIllegalMoveException();
            }

            board[move.getToY()][move.getToX()] = piece;
            board[move.getFromY()][move.getFromX()] = null;

        } break;
        case BISHOP: case ROOK: case QUEEN: case KING: {
            int dx = move.getToX() - move.getFromX();
            int dy = move.getToY() - move.getFromY();
            int mdx = (int) Math.signum(dx);
            int mdy = (int) Math.signum(dy);
            int amdx = Math.abs(mdx);
            int amdy = Math.abs(mdy);

            switch (piece.getPieceType()) {
            case BISHOP:
                if (!(amdx == 1 && amdy == 1)) {
                    throw new ChessIllegalMoveException();
                }
                break;
            case ROOK:
                if (!((amdx == 1 && amdy == 0) || (amdx == 0 && amdy == 1))) {
                    throw new ChessIllegalMoveException();
                }

                if (move.getFromX() == 0 && move.getFromY() == (isWhiteTurn ? 0 : 7)) {
                    queenSidedCastlingBreak = true;
                } else if (move.getFromX() == 7 && move.getFromY() == (isWhiteTurn ? 0 : 7)) {
                    kingSidedCastlingBreak = true;
                }
                break;
            case KING: {
                int adx = Math.abs(dx);
                int ady = Math.abs(dy);

                if (adx == 2 && ady == 0) {
                    if (!field.getFieldState().isCastlingAvailable(isWhiteTurn, dx > 0)) {
                        throw new ChessIllegalMoveException("castling impossible: castling not available");
                    }

                    boolean isPassFieldUnderCheck = ChessClassicRules.isUnderCheck(
                        new ChessBoard(board),
                        new ChessPosition(ourKingPosition.getX() + mdx, ourKingPosition.getY()),
                        isWhiteTurn
                    );

                    if (isPassFieldUnderCheck) {
                        throw new ChessIllegalMoveException("castling impossible: under check");
                    }

                    board[move.getToY()][move.getToX()] = piece;
                    ChessPiece rook = board[isWhiteTurn ? 0 : 7][amdx < 0 ? 0 : 7];
                    board[isWhiteTurn ? 0 : 7][amdx < 0 ? 0 : 7] = null;
                    board[move.getToY()][move.getToX() - mdx] = rook;
                    board[move.getFromY()][move.getFromX()] = null;

                    kingSidedCastlingBreak = true;
                    queenSidedCastlingBreak = true;

                    ourKingPosition = move.getTo();

                    break piecesSwitch;
                } else {
                    if (!(adx <= 1 && ady <= 1)) {
                        throw new ChessIllegalMoveException();
                    }

                    ourKingPosition = move.getTo();
                }

                kingSidedCastlingBreak = true;
                queenSidedCastlingBreak = true;

            } break;
            }

            int x = move.getFromX();
            int y = move.getFromY();

            while (true) {
                x += mdx;
                y += mdy;

                if (x == move.getToX() && y == move.getToY()) break;

                if (x < 0 || x > 7 || y < 0 || y > 7 || board[y][x] != null) {
                    throw new ChessIllegalMoveException();
                }
            }

            board[move.getToY()][move.getToX()] = piece;
            board[move.getFromY()][move.getFromX()] = null;

        } break;
        default: throw new RuntimeException("impossible");
        }

        // Проверяем на шах королю
        if (ChessClassicRules.isUnderCheck(new ChessBoard(board), ourKingPosition, isWhiteTurn)) {
            throw new ChessIllegalMoveException("king is under check");
        }

        final ChessPosition ourKingPositionFinal = ourKingPosition;
        final ChessPosition newEnPassantFinal = newEnPassant;
        final boolean kingSidedCastlingBreakFinal = kingSidedCastlingBreak;
        final boolean queenSidedCastlingBreakFinal = queenSidedCastlingBreak;

        return modifiableField -> {
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    modifiableField.setPieceAt(x, y, board[y][x]);
                }
            }

            IChessFieldState fieldState = modifiableField.getFieldState();

            fieldState.setEnPassant(newEnPassantFinal);

            if (resetHalfMoves) fieldState.resetHalfMoves();
            else fieldState.incrementHalfMoves();

            if (!isWhiteTurn) fieldState.incrementFullMoves();

            if (kingSidedCastlingBreakFinal) fieldState.setCastling(false, isWhiteTurn, true);
            if (queenSidedCastlingBreakFinal) fieldState.setCastling(false, isWhiteTurn, false);

            fieldState.setTurn(!isWhiteTurn);
        };
    }

    private static boolean isUnderCheck(IChessBoard board, ChessPosition pos, boolean forWhite) {
        return ChessClassicRules.testAllCells(
            board, Knight.beatingCells(pos), ChessPieceType.KNIGHT, forWhite
        ) || ChessClassicRules.testAllCells(
            board, Pawn.beatingCells(pos, forWhite), ChessPieceType.PAWN, forWhite
        ) || ChessClassicRules.testDirectedCells(
            board, Straight.bishopBeatingBeams(pos), ChessPieceType.BISHOP, forWhite
        ) || ChessClassicRules.testDirectedCells(
            board, Straight.rookBeatingBeams(pos), ChessPieceType.ROOK, forWhite
        ) || ChessClassicRules.testDirectedCells(
            board, Straight.queenBeatingBeams(pos), ChessPieceType.QUEEN, forWhite
        ) || ChessClassicRules.testAllCells(
            board, King.beatingCells(pos), ChessPieceType.KING, forWhite
        );
    }

    private static boolean testAllCells(IChessBoard board, Iterator<ChessPosition> it,
                                        ChessPieceType pieceType, boolean forWhite)
    {
        while (it.hasNext()) {
            ChessPosition p = it.next();

            ChessPiece piece = board.getPieceAt(p.getX(), p.getY());
            if (piece != null && piece.isWhite() != forWhite &&
                piece.getPieceType() == pieceType)
            {
                return true;
            }
        }

        return false;
    }

    private static boolean testDirectedCells(
        IChessBoard board, Iterator<Iterator<ChessPosition>> beams,
        ChessPieceType pieceType, boolean forWhite)
    {
        while (beams.hasNext()) {
            Iterator<ChessPosition> it = beams.next();

            while (it.hasNext()) {
                ChessPosition p = it.next();

                ChessPiece piece = board.getPieceAt(p.getX(), p.getY());
                if (piece != null) {
                    if (piece.isWhite() != forWhite && piece.getPieceType() == pieceType) {
                        return true;
                    } else {
                        break;
                    }
                }
            }
        }

        return false;
    }
}
