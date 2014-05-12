package by.muna.chess;

import by.muna.chess.exceptions.ChessBadFENException;
import by.muna.chess.fen.ChessFen;
import by.muna.chess.fen.IFenListener;

public class ChessField implements IChessField {
    private ChessPiece[][] board;
    private IChessFieldState fieldState;

    private ChessField(ChessPiece[][] board, IChessFieldState fieldState) {
        this.board = board;
        this.fieldState = fieldState;
    }
    public ChessField(IChessFieldState fieldState) {
        this(new ChessPiece[8][8], fieldState);
    }
    public static ChessField fromFen(String fen) throws ChessBadFENException {
        return ChessFen.readFen(fen, new IFenListener<ChessField>() {
            private ChessPiece[][] board = new ChessPiece[8][8];

            @Override
            public void onPiece(int x, int y, ChessPiece piece) {
                this.board[y][x] = piece;
            }

            @Override
            public ChessField finish(boolean isWhiteTurn, ChessPosition enPassant,
                                     boolean whiteKingCastling, boolean whiteQueenCastling,
                                     boolean blackKingCastling, boolean blackQueenCastling,
                                     int halfMoves, int fullMoves)
            {
                return new ChessField(
                    this.board,
                    new ChessFieldState(
                        isWhiteTurn, enPassant,
                        whiteKingCastling, whiteQueenCastling,
                        blackKingCastling, blackQueenCastling,
                        halfMoves, fullMoves
                    )
                );
            }
        });
    }
    public static ChessField fromField(IChessField field) {
        ChessPiece[][] board = new ChessPiece[8][8];

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                board[y][x] = field.getPieceAt(x, y);
            }
        }

        return new ChessField(board, ChessFieldState.fromFieldState(field.getFieldState()));
    }

    @Override
    public ChessPiece getPieceAt(int x, int y) {
        return this.board[y][x];
    }

    @Override
    public void setPieceAt(int x, int y, ChessPiece piece) {
        this.board[y][x] = piece;
    }

    @Override
    public IChessFieldState getFieldState() {
        return this.fieldState;
    }
}
