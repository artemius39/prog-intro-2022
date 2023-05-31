package game;

import java.util.Scanner;

public class Main {
    private static TournamentPlayer getPlayer(String type, int m, int n) {
        return switch (type) {
            case "Human" -> new ConsoleHumanPlayer();
            case "Random" -> new RandomPlayer(m, n);
            case "Sequential" -> new SequentialPlayer(m, n);
            default -> throw new IllegalStateException("Invalid player type");
        };
    }

    public static void main(String[] args) {
        try {
            ConsoleInputReader in = new ConsoleInputReader();
            String[] gameModes = new String[]{"Normal", "With Diagonals Prohibited"};
            String[] playerTypes = new String[]{"Human", "Random", "Sequential"};

            boolean basicGame;
            while (true) {
                int x = in.readInt("Would you like to play a tournament (0) or just a game (1)?");
                if (x == 0 || x == 1) {
                    basicGame = x == 1;
                    break;
                }
                System.out.println("You must enter number 0 or 1");
            }

            StringBuilder gameModeNames = new StringBuilder();
            for (int i = 0; i < gameModes.length; i++) {
                gameModeNames.append(String.format("%s (%d), ", gameModes[i], i + 1));
            }
            gameModeNames.delete(gameModeNames.length() - 2, gameModeNames.length());

            int gameMode;
            while(true) {
                gameMode = in.readInt("Choose board type (enter number):\n" + gameModeNames);
                if (0 < gameMode && gameMode <= gameModes.length) {
                    break;
                }
                System.out.println("Game mode number " + gameMode + " not supported");
            }

            int m, n;
            if (gameModes[gameMode - 1].equals("Normal")) {
                while (true) {
                    m = in.readInt("Enter row count:");
                    n = in.readInt("Enter column count:");
                    if (m > 0 && n > 0) {
                        break;
                    }
                    System.out.println("m & n must be positive");
                }
            } else {
                while (true) {
                    n = in.readInt("Enter board size:");
                    if (n > 0) {
                        m = n;
                        break;
                    }
                    System.out.println("Board side size must be positive and odd");
                }
            }

            int k;
            while (true) {
                k = in.readInt("Enter k:");
                if (0 < k && k <= m && k <= n) {
                    break;
                }
                System.out.println("k must be positive and not exceed m or n");
            }

            StringBuilder playerNames = new StringBuilder();
            for (int i = 0; i < playerTypes.length; i++) {
                playerNames.append(String.format("%s (%d), ", playerTypes[i], i + 1));
            }
            playerNames.delete(playerNames.length() - 2, playerNames.length());

            if (basicGame) {
                Board board;
                if (gameModes[gameMode - 1].equals("Normal")) {
                    board = new MnkBoard(m, n, k);
                } else {
                    board = new ExampleBoard(n, k);
                }
                Player player1, player2;
                int playerType;
                while (true) {
                    playerType = in.readInt("Choose player 1 type (enter its number):\n" + playerNames);
                    if (0 < playerType && playerType <= playerTypes.length) {
                        player1 = getPlayer(playerTypes[playerType - 1], m, n);
                        break;
                    }
                    System.out.println("Player type no. " + playerType + " not supported");
                }
                while (true) {
                    playerType = in.readInt("Choose player 2 type (enter its number):\n" + playerNames);
                    if (0 < playerType && playerType <= playerTypes.length) {
                        player2 = getPlayer(playerTypes[playerType - 1], m, n);
                        break;
                    }
                    System.out.println("Player type no. " + playerType + " not supported");
                }
                Game game = new Game(true, player1, player2);
                game.play(board);
            } else {
                int participantCount;
                while (true) {
                    participantCount = in.readInt("Enter player count: ");
                    if (participantCount >= 2) {
                        break;
                    }
                    System.out.println("Player count must be at least 2");
                }

                for (int i = 0; i < playerTypes.length; i++) {
                    playerNames.append(String.format(", Make this and all remaining %s (%d)", playerTypes[i], -i - 1));
                }

                TournamentPlayer[] players = new TournamentPlayer[participantCount];
                outer:
                for (int i = 0; i < participantCount; i++) {
                    int playerType;
                    while (true) {
                        playerType = in.readInt("Choose player %d type (enter its number):\n%s".formatted(i + 1, playerNames));
                        if (0 < playerType && playerType <= playerTypes.length) {
                            players[i] = getPlayer(playerTypes[playerType - 1], m, n);
                            break;
                        } else if (-playerTypes.length <= playerType && playerType < 0) {
                            for (int j = i; j < participantCount; j++) {
                                players[j] = getPlayer(playerTypes[-playerType - 1], m, n);
                            }
                            break outer;
                        }
                        System.out.println("Player type no. " + playerType + " not supported");
                    }
                }

                TournamentBoard board;
                if (gameModes[gameMode - 1].equals("Normal")) {
                    board = new MnkBoard(m, n, k);
                } else {
                    board = new ExampleBoard(n, k);
                }

                Tournament tournament = new Tournament(board, participantCount, players);
                tournament.play();
                System.out.println("\nTournament leaderboard:");
                tournament.displayStats(System.out);
            }
        } catch (Exception e) {
            System.out.println("Could not play game: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
