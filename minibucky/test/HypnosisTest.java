/* HypnosisTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the effects of hypnosis.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class HypnosisTest extends Test {
    public String name() {
        return "Hypnosis";
    }

    public int plan() {
        return 5;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "..W.....",
                "........",
                "....s...",
                "........",
                "........",
                ".J......",
                "........");

        ok(!b.moveIsLegal("b2-b3"));
        ok(!b.moveIsLegal("c7ye5"));

        b = BoardGenerator.generate(Player.WHITE,
                "........",
                "..D.....",
                "........",
                "....S...",
                "........",
                "........",
                ".m.....s",
                "........");

        ok(b.moveIsLegal("c7-b7"));
        ok(!b.moveIsLegal("e5-e4"));

        b = BoardGenerator.generate(Player.WHITE,
                "........",
                ".D......",
                "........",
                "........",
                "....S...",
                "........",
                ".m......",
                "........");

        ok(b.moveIsLegal("e4-e5"));
    }
}
