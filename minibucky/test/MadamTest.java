/* MadamTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the different movements of the madam piece.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class MadamTest extends Test {
    public String name() {
        return "Madam";
    }

    public int plan() {
        return 8;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "........",
                "...M....",
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
