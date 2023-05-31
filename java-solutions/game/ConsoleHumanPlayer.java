package game;

public class ConsoleHumanPlayer implements TournamentPlayer {

    @Override
    public Move move(final Position position, final Cell cell) {
        ConsoleInputReader userInput = new ConsoleInputReader();
        int row, column;
        String prompt =
            "Position\n" +
                position + "\n" +
                cell + "'s move\n" +
                "Enter row and column";
        while (true) {
            int[] rowAndColumn = userInput.readInts(2, prompt);
            row = rowAndColumn[0];
            column = rowAndColumn[1];
            final Move move = new Move(row - 1, column - 1, cell);
            if (position.isValid(move)) {
                return move;
            }
            System.out.println("The move " + move + " is invalid");
        }
    }

    @Override
    public void reset() {
    }
}
