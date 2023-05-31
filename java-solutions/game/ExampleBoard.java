package game;

public class ExampleBoard extends MnkBoard {
    public ExampleBoard(int n, int k) {
        super(n, n, k);
        for (int i = 0; i < n; i++) {
            cells[i][i] = Cell.P;
            cells[i][n - i - 1] = Cell.P;
        }
        vacantCells = n * n - n - n + n % 2;
    }

    @Override
    public void reset() {
        super.reset();
        for (int i = 0; i < rowCount; i++) {
            cells[i][i] = Cell.P;
            cells[i][rowCount - i - 1] = Cell.P;
        }
        vacantCells = rowCount * rowCount - rowCount - rowCount + rowCount % 2;
    }
}
