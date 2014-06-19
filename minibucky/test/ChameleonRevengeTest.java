/* ChameleonRevengeTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the properties of the vengeful role, mimicked by a chameleon.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class ChameleonRevengeTest extends Test {
    public String name() {
        return "ChameleonRevenge";
    }

    public int plan() {
        return 3;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "...M....",
                "...C....",
                "........",
                ".....s..",
                "...w....",
                "........");

        ok(!b.moveIsLegal("d5xd2"));
        ok(b.moveIsLegal("d5xf3"));

        b = BoardGenerator.generate(Player.WHITE,
                "........",
                ".S......",
                "...M....",
                "...C...S",
                "........",
                ".....s..",
                "........",
                "........");

        ok(b.moveIsLegal("d5-d4"));
    }
}
