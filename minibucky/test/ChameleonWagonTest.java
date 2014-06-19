/* ChameleonWagonTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the different movements of chameleons standing next to a wagon
 * piece.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class ChameleonWagonTest extends Test {
    public String name() {
        return "ChameleonWagon";
    }

    public int plan() {
        return 7;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "........",
                "...Cw...",
                "........",
                "........",
                "...s....",
                "........");

        ok(b.moveIsLegal("d5-a5"));
        ok(b.moveIsLegal("d5-a8"));
        ok(b.moveIsLegal("d5-d3"));

        ok(!b.moveIsLegal("d5-b5"));
        ok(!b.moveIsLegal("d5-d1"));
        ok(!b.moveIsLegal("d5-h2"));

        ok(b.moveIsLegal("d5yd2"));
    }
}
