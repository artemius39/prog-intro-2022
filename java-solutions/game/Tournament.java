package game;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;

public class Tournament {
    private final int participantCount;
    private final Participant[] participants;
    private final TournamentBoard board;

    static class Participant {
        public final int no;
        public int score;
        public final TournamentPlayer player;

        public Participant(int no, int score, TournamentPlayer player) {
            this.no = no;
            this.score = score;
            this.player = player;
        }

        public void addPoints(int points) {
            score += points;
        }
    }

    static class SortByScore implements Comparator<Participant> {
        @Override
        public int compare(Participant p1, Participant p2) {
            return p2.score - p1.score;
        }
    }

    public Tournament(TournamentBoard board, int participantCount, TournamentPlayer[] players) {
        this.participantCount = participantCount;
        this.participants = new Participant[participantCount];
        this.board = board;
        for (int no = 0; no < participantCount; no++) {
            participants[no] = new Participant(no, 0, players[no]);
        }
    }

    public void play() {
        for (Participant participant1 : participants) {
            for (Participant participant2 : participants) {
                if (participant2.no != participant1.no) {
                    System.out.println("\nPlayer 1: contestant no. " + (participant1.no + 1));
                    System.out.println("Player 2: contestant no. " + (participant2.no + 1) + "\n");
                    final Game game = new Game(true, participant1.player, participant2.player);
                    int result = game.play(board);
                    board.reset();
                    participant1.player.reset();
                    participant2.player.reset();
                    if (result == 1) {
                        participant1.addPoints(3);
                    } else if (result == 2) {
                        participant2.addPoints(3);
                    } else {
                        participant1.addPoints(1);
                        participant2.addPoints(1);
                    }
                }
            }
        }
    }

    public void displayStats(PrintStream out) {
        Arrays.sort(participants, new SortByScore());
        int numberLength = String.valueOf(participantCount).length();
        int scoreLength = String.valueOf(participants[0].score).length();
        for (Participant participant : participants) {
            out.printf("Player %" + numberLength + "d: %" + scoreLength + "d\n", participant.no + 1, participant.score);
        }
    }
}
