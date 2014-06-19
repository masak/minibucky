/* ChameleonDoubleHypnosisTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the effects of double hypnosis on chameleons.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class ChameleonDoubleHypnosisTest extends Test {
    public String name() {
        return "ChameleonDoubleHypnosis";
    }

    public int plan() {
        return 4;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "........",
                "....j...",
                "........",
                "..S.....",
                ".....CS.",
                "........");

        b = b.makeMove("f2-g3");
        is(b.pieceAt("e5"), null);

        b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "...D....",
                "...C....",
                "........",
                "....s...",
                "........",
                "........");

        ok(!b.moveIsLegal("d5-d4"));

        b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "........",
                "....cc..",
                ".......S",
                "..S.....",
                "........",
                "........");

        b = b.makeMove("h4-h3");
        is(b.pieceAt("e5"), null);
        is(b.pieceAt("f5"), null);
    }
}
