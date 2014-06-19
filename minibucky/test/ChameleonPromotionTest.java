/* ChameleonPromotionTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the various properties of promotion among chameleons.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class ChameleonPromotionTest extends Test {
    public String name() {
        return "ChameleonPromotion";
    }

    public int plan() {
        return 15;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "....JC..",
                "........",
                "........",
                "........",
                "........",
                "..CJ....",
                "........");

        ok(!b.moveIsLegal("c2-b3=M"));
        ok(!b.moveIsLegal("c2-a4=M"));
        ok(!b.moveIsLegal("c2-c8=M"));
        ok(!b.moveIsLegal("f7-f8"));

        ok(b.moveIsLegal("f7-f8=J"));
        ok(b.moveIsLegal("f7-f8=W"));
        ok(b.moveIsLegal("f7-f8=S"));
        ok(!b.moveIsLegal("f7-f8=C"));
        ok(!b.moveIsLegal("f7-f8=D"));
        ok(b.moveIsLegal("f7-f8=M"));

        b = b.makeMove("f7-f8=W");
        is(b.pieceAt("f8"), Piece.WHITE_WAGON);

        b = BoardGenerator.generate(Player.WHITE,
                "........",
                "....JC..",
                "........",
                "........",
                "........",
                ".w......",
                "..CJ....",
                "........");

        ok(!b.moveIsLegal("c2-c8"));

        b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "..W.j...",
                "...C....",
                "........",
                "........",
                "........",
                "........");

        ok(!b.moveIsLegal("d5-d1"));
        ok(b.moveIsLegal("d5-d1=M"));

        b = b.makeMove("d5-d1=M");
        ok(b.pieceAt("d1") == Piece.WHITE_MADAM);
    }
}
