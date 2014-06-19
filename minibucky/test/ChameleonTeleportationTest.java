/* ChameleonTeleportationTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests teleportation among chameleons.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class ChameleonTeleportationTest extends Test {
    public String name() {
        return "ChameleonTeleportation";
    }

    public int plan() {
        return 5;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                ".j.....m",
                "........",
                "...MC...",
                ".d......",
                ".c......",
                ".....D..",
                "........");

        ok(b.moveIsLegal("e5-f2"));
        ok(!b.moveIsLegal("e5-b4"));

        b = b.makeMove("e5-f2");
        ok(b.moveIsLegal("b3-h7"));

        b = b.makeMove("b7-b6");
        ok(!b.moveIsLegal("d5-f2"));

        b = new Board();
        ok(b.moveIsLegal("c1-f1"));
    }
}
