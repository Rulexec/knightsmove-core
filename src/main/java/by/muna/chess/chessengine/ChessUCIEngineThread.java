package by.muna.chess.chessengine;

import by.muna.chess.ChessMove;
import by.muna.chess.IChessField;
import by.muna.chess.fen.ChessFen;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ChessUCIEngineThread implements ChessEngineAsyncProvider {
    private static class Task {
        public IChessField field;
        public ChessEngineCallback callback;
        public int plies;

        public Task(IChessField field, ChessEngineCallback callback, int plies) {
            this.field = field;
            this.callback = callback;
            this.plies = plies;
        }
    }

    private Thread thread;

    private BlockingQueue<Task> tasks = new LinkedBlockingQueue<>();

    public ChessUCIEngineThread(String path) {
        this.thread = new Thread(() -> this.run(path));
        this.thread.start();
    }

    private void run(String path) {
        try {
            String lastQuery = null;

            try {
                ProcessBuilder builder = new ProcessBuilder("stockfish");
                Process process = builder.start();

                Scanner sc = new Scanner(process.getInputStream());
                Writer wr = new OutputStreamWriter(process.getOutputStream());

                while (true) {
                    Task task = this.tasks.take();

                    lastQuery = "ucinewgame\nposition fen " +
                                ChessFen.fieldToFen(task.field) +
                                "\ngo depth " + Integer.toString(task.plies) + "\n";

                    wr.append(lastQuery);

                    wr.flush();

                    while (true) {
                        String line = sc.nextLine();

                        if (line.startsWith("bestmove ")) {
                            int sliceTo = line.indexOf(" ", 9);

                            String moveString = line.substring(9, sliceTo);

                            if (!moveString.equals("(none)")) {
                                task.callback.onSuccess(ChessMove.fromString(moveString));
                            } else {
                                task.callback.onSuccess(null);
                            }

                            break;
                        }
                    }
                }
            } catch (Throwable ex) {
                System.err.println("Crash! last uci query: " + lastQuery);
                ex.printStackTrace();
            }

            while (true) {
                this.tasks.take().callback.onFail();
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void calc(IChessField field, int plies, ChessEngineCallback callback) {
        this.tasks.add(new Task(field, callback, plies));
    }
}
