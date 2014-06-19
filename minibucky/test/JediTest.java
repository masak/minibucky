/* JediTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the different movements of the jedi piece.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class JediTest extends Test {
    public String name() {
        return "Jedi";
    }

    public int plan() {
        return 18;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "........",
                "........",
                "......j.",
                ".....J..",
                "..J.....",
                "........");

        ok(b.moveIsLegal("c2-b3"));
        ok(b.moveIsLegal("c2-c3"));
        ok(b.moveIsLegal("c2-d3"));
        ok(b.moveIsLegal("c2-a4"));
        ok(b.moveIsLegal("c2-b4"));
        ok(b.moveIsLegal("c2-c4"));
        ok(b.moveIsLegal("c2-d4"));
        ok(b.moveIsLegal("c2-e4"));

        ok(b.moveIsLegal("f3-e4"));
        ok(b.moveIsLegal("f3-f4"));
        ok(!b.moveIsLegal("f3-g4"));
        ok(!b.moveIsLegal("f3-d5"));
        ok(!b.moveIsLegal("f3-e5"));
        ok(!b.moveIsLegal("f3-f5"));
        ok(!b.moveIsLegal("f3-g5"));
        ok(!b.moveIsLegal("f3-h5"));

        ok(b.moveIsLegal("f3yg4"));

        b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "........",
                "........",
                "....J...",
                ".....J..",
                "........",
                "........");

        ok(!b.moveIsLegal("f3ye4"));
    }
}
