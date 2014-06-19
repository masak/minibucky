/* ChameleonJediTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the different movements of chameleons standing next to jedi pieces.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class ChameleonJediTest extends Test {
    public String name() {
        return "ChameleonJedi";
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
                "....JC.C",
                "..CJ....",
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
        ok(!b.moveIsLegal("h3yg4"));
    }
}
