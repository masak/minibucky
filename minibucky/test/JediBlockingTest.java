/* JediBlockingTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the situations where a jedi cannot move two steps forward because
 * the intermediate location is blocked.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class JediBlockingTest extends Test {
    public String name() {
        return "JediBlocking";
    }

    public int plan() {
        return 3;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "...s....",
                "........",
                "........",
                ".w......",
                "..J..J..",
                "........");

        ok(!b.moveIsLegal("c2-a4"));
        ok(!b.moveIsLegal("f2-h4"));

        b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "...s....",
                ".......s",
                "........",
                "....J...",
                ".....CJ.",
                "........");

        ok(!b.moveIsLegal("f2-f4"));
    }
}
