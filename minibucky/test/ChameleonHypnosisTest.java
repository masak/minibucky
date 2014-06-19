/* ChameleonHypnosisTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the effects of mimicked hypnosis (on both the sending and receiving
 * ends).
 *
 */
package minibucky.test;
import minibucky.state.*;

public class ChameleonHypnosisTest extends Test {
    public String name() {
        return "ChameleonHypnosis";
    }

    public int plan() {
        return 5;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "..W.....",
                "........",
                "....cs..",
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
                "....C...",
                "....S...",
                "........",
                ".m.....s",
                "........");

        ok(b.moveIsLegal("c7-b7"));
        ok(!b.moveIsLegal("e5-e4"));

        b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "........",
                "........",
                "....s...",
                "........",
                "..J.....",
                "...C....");

        ok(!b.moveIsLegal("d1-d2"));
    }
}
