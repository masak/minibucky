/* DoubleHypnosisTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests the effects of double hypnosis.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class DoubleHypnosisTest extends Test {
    public String name() {
        return "DoubleHypnosis";
    }

    public int plan() {
        return 7;
    }
    
    public void test() {
        Board b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "...j....",
                "....w...",
                ".S......",
                "...m...S",
                ".S......",
                "........");

        b = b.makeMove("h3-g3");
        is(b.pieceAt("e5"), null);
        is(b.pieceAt("d6"), null);
        
        ok(!b.moveIsLegal("d3-c3"));

        b = BoardGenerator.generate(Player.WHITE,
                "........",
                "........",
                "........",
                "........",
                "....D...",
                ".s......",
                ".......s",
                "........");

        ok(!b.moveIsLegal("e4-e5"));

        b = BoardGenerator.generate(Player.BLACK,
                "........",
                "..s.....",
                ".....S..",
                "....M...",
                "...j...s",
                "..s.....",
                ".w.S....",
                "S.......");

        b = b.makeMove("d4-d3");
        is(b.pieceAt("b2"), null);

        b = BoardGenerator.generate(Player.BLACK,
                "........",
                "........",
                "..S.....",
                "........",
                "...M...w",
                "........",
                "..S.....",
                "........");

       ok(!b.moveIsLegal("h4yd4"));
       
       b = BoardGenerator.generate(Player.BLACK,
                "wscmdc.w",
                "j.j....j",
                "..j..j..",
                ".j.....J",
                ".J...sJ.",
                "J...J...",
                "JSCJJ.CS",
                "W..MD..W");

       b = b.makeMove("c7-b6");
       is(b.pieceAt("h2"), Piece.WHITE_SHAMAN);
    }
}
