/* ShamanTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the different movements of the shaman piece.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class ShamanTest extends Test {
    public String name() {
        return "Shaman";
    }

    public int plan() {
        return 5;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "........",
                "....S...",
                "....j...",
                "........",
                "........",
                "........");

        ok(b.moveIsLegal("e5-d5"));
        ok(b.moveIsLegal("e5-f6"));

        ok(!b.moveIsLegal("e5-e4"));
        ok(!b.moveIsLegal("e5xe4"));
        ok(!b.moveIsLegal("e5ye4"));
    }
}
