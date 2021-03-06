/* DudeTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the different movements of the dude piece.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class DudeTest extends Test {
    public String name() {
        return "Dude";
    }

    public int plan() {
        return 5;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "........",
                "....D...",
                "....j...",
                "........",
                "..S.....",
                "........");

        ok(b.moveIsLegal("e5-d5"));
        ok(b.moveIsLegal("e5-f6"));
        ok(b.moveIsLegal("e5xe4"));

        ok(!b.moveIsLegal("e5-e4"));
        ok(!b.moveIsLegal("e5ye4"));
    }
}
