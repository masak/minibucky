/* ChameleonChameleonTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the different movements of a chameleon piece standing next to
 * another.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class ChameleonChameleonTest extends Test {
    public String name() {
        return "ChameleonChameleon";
    }

    public int plan() {
        return 6;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "....j...",
                "........",
                "....CC..",
                "........",
                "........",
                "........");

        ok(!b.moveIsLegal("e4-d5"));
        ok(!b.moveIsLegal("e4-g6"));
        ok(!b.moveIsLegal("e4-e3"));
        ok(!b.moveIsLegal("e4xe6"));
        ok(!b.moveIsLegal("e4ye6"));

        b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "....j...",
                ".....C..",
                "....C...",
                "........",
                "........",
                "........");

        ok(b.moveIsLegal("e4-e3"));
    }
}
