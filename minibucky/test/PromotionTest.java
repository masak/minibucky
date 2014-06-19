/* PromotionTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the various properties of promotion.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class PromotionTest extends Test {
    public String name() {
        return "Promotion";
    }

    public int plan() {
        return 12;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                ".....J..",
                "........",
                "........",
                "........",
                "........",
                "..J.....",
                "........");

        ok(!b.moveIsLegal("c2-b3=M"));
        ok(!b.moveIsLegal("c2-a4=M"));
        ok(!b.moveIsLegal("f7-f8"));

        ok(!b.moveIsLegal("f7-f8=J"));
        ok(b.moveIsLegal("f7-f8=W"));
        ok(b.moveIsLegal("f7-f8=S"));
        ok(b.moveIsLegal("f7-f8=C"));
        ok(!b.moveIsLegal("f7-f8=D"));
        ok(b.moveIsLegal("f7-f8=M"));

        b = b.makeMove("f7-f8=W");
        is(b.pieceAt("f8"), Piece.WHITE_WAGON);

        b = BoardGenerator.generate(Player.WHITE,
                ".....s..",
                ".....J..",
                "........",
                "........",
                "........",
                "........",
                "..J.....",
                "........");

        ok(b.moveIsLegal("f7yf8"));
        ok(!b.moveIsLegal("f7yf8=W"));
    }
}
