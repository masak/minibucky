/* TeleportationTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests different movements involving teleportation.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class TeleportationTest extends Test {
    public String name() {
        return "Teleportation";
    }

    public int plan() {
        return 4;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                ".j....m.",
                "........",
                "...M....",
                ".d......",
                "........",
                ".....D..",
                "........");

        ok(b.moveIsLegal("d5-f2"));
        ok(!b.moveIsLegal("d5-g7"));

        b = b.makeMove("d5-f2");
        ok(b.moveIsLegal("b4-g7"));

        b = b.makeMove("b7-b6");
        ok(!b.moveIsLegal("d5-f2"));
    }
}
