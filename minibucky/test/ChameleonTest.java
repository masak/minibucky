/* ChameleonTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the different movements of the chameleon piece.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class ChameleonTest extends Test {
    public String name() {
        return "Chameleon";
    }

    public int plan() {
        return 5;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "....j...",
                "........",
                "....C...",
                "........",
                "........",
                "........");

        ok(!b.moveIsLegal("e4-d5"));
        ok(!b.moveIsLegal("e4-g6"));
        ok(!b.moveIsLegal("e4-e3"));
        ok(!b.moveIsLegal("e4xe6"));
        ok(!b.moveIsLegal("e4ye6"));
    }
}
