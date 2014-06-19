/* RevengeTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the properties of the vengeful role.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class RevengeTest extends Test {
    public String name() {
        return "Revenge";
    }

    public int plan() {
        return 3;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "........",
                "...M....",
                "........",
                ".....s..",
                "...w....",
                "........");

        ok(!b.moveIsLegal("d5xd2"));
        ok(b.moveIsLegal("d5xf3"));

        b = BoardGenerator.generate(Player.WHITE,
                "........",
                ".S......",
                "........",
                "...M...S",
                "........",
                ".....s..",
                "........",
                "........");

        ok(b.moveIsLegal("d5-d4"));
    }
}
