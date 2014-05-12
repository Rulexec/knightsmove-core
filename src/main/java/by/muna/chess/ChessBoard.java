package by.muna.chess;

public class ChessBoard implements IChessBoard {
    private ChessPiece[][] board;

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }
    public ChessBoard(ChessPiece[][] board) {
        this.board = board;
    }

    @Override
    public ChessPiece getPieceAt(int x, int y) {
        return this.board[y][x];
    }

    @Override
    public void setPieceAt(int x, int y, ChessPiece piece) {
        this.board[y][x] = piece;
    }
}
