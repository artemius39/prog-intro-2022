package game;

import java.util.Arrays;
import java.util.Map;

public class MnkBoard implements TournamentBoard, Position {
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '.',
            Cell.P, '#'
    );

    protected final Cell[][] cells;
    private Cell turn;
    protected final int rowCount;
    private final int columnCount;
    private final int stonesToWin;
    protected int vacantCells;

    public MnkBoard(int m, int n, int k) {
        rowCount = m;
        columnCount = n;
        stonesToWin = k;
        vacantCells = m * n;
        this.cells = new Cell[m][n];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
    }

    @Override
    public Position getPosition() {
        return new MnkPosition(this);
    }

    @Override
    public Cell getCell() {
        return turn;
    }

    private int count(Move move, int dr, int dc) {
        int t = 1;
        while ( t < stonesToWin &&
                0 <= move.getRow() + dr * t && move.getRow() + dr * t < rowCount &&
                0 <= move.getColumn() + dc * t && move.getColumn() + dc * t < columnCount &&
                cells[move.getRow() + dr * t][move.getColumn() + dc * t] == move.getValue()
        ) {
            t++;
        }
        return t;
    }

    @Override
    public Result makeMove(final Move move) {
        if (!isValid(move)) {
            return Result.LOSE;
        }

        cells[move.getRow()][move.getColumn()] = move.getValue();
        vacantCells--;

        if (    // counting vertically
                count(move, 1, 0) + count(move, -1, 0) - 1 >= stonesToWin ||
                // counting horizontally
                count(move, 0, 1) + count(move, 0, -1) - 1 >= stonesToWin ||
                // counting diagonally
                count(move, 1, 1) + count(move, -1, -1) - 1 >= stonesToWin ||
                count(move, 1, -1) + count(move, -1, 1) - 1 >= stonesToWin
        ) {
            return Result.WIN;
        }

        if (vacantCells == 0) {
            return Result.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;
        return Result.UNKNOWN;
    }

    @Override
    public boolean isValid(final Move move) {
        return  move != null &&
                0 <= move.getRow() && move.getRow() < rowCount &&
                0 <= move.getColumn() && move.getColumn() < columnCount &&
                cells[move.getRow()][move.getColumn()] == Cell.E &&
                turn == getCell();
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public String toString() {
        int horizontalOffset = String.valueOf(rowCount).length();
        int verticalOffset = String.valueOf(columnCount).length();
        String[] columnNumbers = new String[columnCount + 1];
        for (int i = 1; i < columnCount + 1; i++) {
            columnNumbers[i] = String.format("%" + verticalOffset + "d", i);
        }

        final StringBuilder sb = new StringBuilder();
        for (int j = 0; j < verticalOffset; j++) {
            sb.append(" ".repeat(horizontalOffset + 1));
            for (int i = 1; i < columnCount + 1; i++) {
                sb.append(columnNumbers[i].charAt(j));
                sb.append(" ");
            }
            sb.append('\n');
        }

        for (int r = 0; r < rowCount; r++) {
            sb.append(String.format("%" + horizontalOffset + "d", r + 1));
            sb.append(" ");
            for (int c = 0; c < columnCount; c++) {
                sb.append(SYMBOLS.get(cells[r][c]));
                sb.append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public void reset() {
        vacantCells = rowCount * columnCount;
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
    }
}
