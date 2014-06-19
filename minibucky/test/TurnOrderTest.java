/* TurnOrderTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests that white begins the game, and that the players take turns
 * making moves.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class TurnOrderTest extends Test {
    public String name() {
        return "TurnOrder";
    }

    public int plan() {
        return 3;
    }
    
    public void test() {
        Board b = new Board();

        is(b.nextPlayer(), Player.WHITE);

        b = b.makeMove("e2-e4");
        is(b.nextPlayer(), Player.BLACK);

        b = b.makeMove("e7-e5");
        is(b.nextPlayer(), Player.WHITE);
    }
}
