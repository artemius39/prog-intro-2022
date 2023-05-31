package game;

import java.util.Random;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class RandomPlayer implements TournamentPlayer {
    private Random random;
    private final int rowCount;
    private final int columnCount;

    public RandomPlayer(final Random random, int rowCount, int columnCount) {
        this.random = random;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    public RandomPlayer(int rowCount, int columnCount) {
        this(new Random(), rowCount, columnCount);
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            int r = random.nextInt(rowCount);
            int c = random.nextInt(columnCount);
            final Move move = new Move(r, c, cell);
            if (position.isValid(move)) {
                return move;
            }
        }
    }

    @Override
    public void reset() {
        this.random = new Random();
    }
}
