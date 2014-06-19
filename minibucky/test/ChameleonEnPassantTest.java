/* ChameleonEnPassantTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests chameleons who remove by, and are removed by, en passant.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class ChameleonEnPassantTest extends Test {
    public String name() {
        return "ChameleonEnPassant";
    }

    public int plan() {
        return 8;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "........",
                "........",
                ".j......",
                "...j....",
                "..CJ....",
                "........");

        Board b2 = b.makeMove("c2-c4");
        ok(b2.moveIsLegal("b4yb3"));
        ok(b2.moveIsLegal("b4yc3"));

        b2 = b.makeMove("c2-d4");
        ok(!b2.moveIsLegal("b4yb3"));
        ok(b2.moveIsLegal("b4yc3"));

        b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "........",
                ".j......",
                ".c......",
                "...j....",
                "..J.....",
                "........");

        b2 = b.makeMove("c2-c4");
        ok(b2.moveIsLegal("b4yb3"));
        ok(b2.moveIsLegal("b4yc3"));

        b2 = b.makeMove("c2-d4");
        ok(!b2.moveIsLegal("b4yb3"));
        ok(b2.moveIsLegal("b4yc3"));
    }
}
