/* ChameleonDudeTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the different movements of a chameleon standing next to a dude piece.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class ChameleonDudeTest extends Test {
    public String name() {
        return "ChameleonDude";
    }

    public int plan() {
        return 5;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "....D...",
                "....C...",
                "...m....",
                "........",
                "........",
                "........");

        ok(b.moveIsLegal("e5-d5"));
        ok(b.moveIsLegal("e5-f6"));
        ok(b.moveIsLegal("e5xd4"));

        ok(!b.moveIsLegal("e5-d4"));
        ok(!b.moveIsLegal("e5yd4"));
    }
}
