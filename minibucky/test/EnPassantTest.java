/* EnPassantTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the different en passant movements.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class EnPassantTest extends Test {
    public String name() {
        return "EnPassant";
    }

    public int plan() {
        return 6;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "........",
                "........",
                ".j......",
                "...j....",
                "..J.....",
                "........");

        ok(!b.moveIsLegal("c2yc3"));

        Board b2 = b.makeMove("c2-c4");
        ok(b2.moveIsLegal("b4yb3"));
        ok(b2.moveIsLegal("b4yc3"));

        b2 = b.makeMove("c2-d4");
        ok(!b2.moveIsLegal("b4yb3"));
        ok(b2.moveIsLegal("b4yc3"));

        ok(!b2.moveIsLegal("d3yc2"));
    }
}
