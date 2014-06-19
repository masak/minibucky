/* ComputerPlayer.java
 *
 * Minimalistic bucky frontend, human player against computer.
 */

package minibucky.cli;

import minibucky.state.Board;
import minibucky.state.Player;
import minibucky.ai.TreeSearch;
import java.util.Scanner;

public class ComputerPlayer {
    public static void main(String[] arg) {
        Board b = new Board();
        String player = "white";

        Scanner keyb = new Scanner(System.in);

        while (true) {
            System.out.println(b);
            if (b.dudeIsThreatened(Player.WHITE)) {
                System.out.println("Buck.");
            }

            while (true) {
                System.out.print(player);
                System.out.print("> ");

                String command = keyb.nextLine();
                if ("quit".equals(command)) {
                    System.exit(0);
                }
                if ("moves".equals(command)) {
                    for (String move : b.possibleMoves()) {
                        System.out.println(move);
                    }
                    continue;
                }
                if (b.moveIsLegal(command)) {
                    b = b.makeMove(command);
                    if (b.dudeIsThreatened(Player.BLACK)) {
                        System.out.println("Buck.");
                    }
                    break;
                }
                System.out.println(b.whyIsMoveIllegal(command));
            }

            TreeSearch t = new TreeSearch();
            String[] p = b.possibleMoves();

            double bestScore = Double.MAX_VALUE;
            String bestMove = null;
            int i = 0;
            for (String m : p) {
                i++;
                double score = t.lookahead(b.makeMove(m), 1);

                System.out.print("\rThinking..." +
                        "\\|/-".charAt(i % 4));

                if (bestScore > score) {
                    bestScore = score;
                    bestMove = m;
                }
            }
            System.out.println();

            if (null == bestMove) {
                if (b.dudeIsThreatened(Player.WHITE)) {
                    System.out.println("White wins.");
                }
                else {
                    System.out.println("Stalemate.");
                }
                System.exit(0);
            }

            System.out.println("black: " + bestMove);
            System.out.println();

            b = b.makeMove(bestMove);
        }
    }
}
