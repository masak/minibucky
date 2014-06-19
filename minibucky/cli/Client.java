/* Client.java
 *
 * Minimalistic bucky frontend, for one and two player games.
 */

package minibucky.cli;

import minibucky.state.Board;
import java.util.Scanner;

public class Client {
    public static void main(String[] arg) {
        Board b = new Board();
        String player = "white";

        Scanner keyb = new Scanner(System.in);

        while (true) {
            System.out.println(b);

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
                    break;
                }
                System.out.println(b.whyIsMoveIllegal(command));
            }

            player = "white".equals(player) ? "black" : "white";
        }
    }
}
