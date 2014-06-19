/* ChameleonMadamTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the different movements of a chameleon standing next to a madam
 * piece.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class ChameleonMadamTest extends Test {
    public String name() {
        return "ChameleonMadam";
    }

    public int plan() {
        return 8;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "....M...",
                "...C....",
                "........",
                "........",
                "...s....",
                "........");

        ok(b.moveIsLegal("d5-a5"));
        ok(b.moveIsLegal("d5-a8"));
        ok(b.moveIsLegal("d5-d3"));
        ok(b.moveIsLegal("d5-b5"));
        ok(!b.moveIsLegal("d5-d1"));

        ok(!b.moveIsLegal("d5-h2"));

        ok(!b.moveIsLegal("d5yd2"));
        ok(b.moveIsLegal("d5xd2"));
    }
}
