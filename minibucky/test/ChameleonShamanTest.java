/* ChameleonShamanTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the different movements of chameleons standing next to a shaman.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class ChameleonShamanTest extends Test {
    public String name() {
        return "ChameleonShaman";
    }

    public int plan() {
        return 2;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "........",
                "....C...",
                "....S...",
                "........",
                "........",
                "........");

        ok(b.moveIsLegal("e5-d5"));
        ok(b.moveIsLegal("e5-f6"));
    }
}
