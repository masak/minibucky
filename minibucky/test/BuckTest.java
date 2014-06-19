/* BuckTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests different bucking situations.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class BuckTest extends Test {
    public String name() {
        return "Buck";
    }

    public int plan() {
        return 4;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "w.......",
                "....D...",
                "........",
                "...J...J",
                ".m......",
                "........");

        ok(!b.moveIsLegal("h3-h4"));
        ok(!b.moveIsLegal("e5-e6"));
        ok(b.moveIsLegal("e5-e4"));
        ok(b.moveIsLegal("d3-d4"));
    }
}
