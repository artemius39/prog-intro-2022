package game;

public class MnkPosition implements Position {

    private final MnkBoard board;

    public MnkPosition(MnkBoard board) {
        this.board = board;
    }

    @Override
    public boolean isValid(Move move) {
        return board.isValid(move);
    }

    @Override
    public Cell getCell(int r, int c) {
        return board.getCell(r, c);
    }

    @Override
    public String toString() {
        return board.toString();
    }
}
