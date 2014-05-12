package by.muna.chess;

public interface IChessBoard {
    ChessPiece getPieceAt(int x, int y);
    void setPieceAt(int x, int y, ChessPiece piece);
}
