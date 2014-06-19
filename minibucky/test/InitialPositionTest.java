/* InitialPositionTest.java
 *
 * (C) 2005 Carl Masak
 *
 * Tests that on a board created in the standard way has pieces in the
 * expected places.
 *
 */
package minibucky.test;
import minibucky.state.*;

public class InitialPositionTest extends Test {
    public String name() {
        return "InitialPosition";
    }

    public int plan() {
        return 33;
    }
    
    public void test() {
        Board b = new Board();

        is(b.pieceAt("a1"), Piece.WHITE_WAGON);
        is(b.pieceAt("b1"), Piece.WHITE_SHAMAN);
        is(b.pieceAt("c1"), Piece.WHITE_CHAMELEON);
        is(b.pieceAt("d1"), Piece.WHITE_MADAM);
        is(b.pieceAt("e1"), Piece.WHITE_DUDE);
        is(b.pieceAt("f1"), Piece.WHITE_CHAMELEON);
        is(b.pieceAt("g1"), Piece.WHITE_SHAMAN);
        is(b.pieceAt("h1"), Piece.WHITE_WAGON);
        for (int file = 0; file < 8; file++) {
            is(b.pieceAt(1, file), Piece.WHITE_JEDI);
        }

        is(b.pieceAt("a8"), Piece.BLACK_WAGON);
        is(b.pieceAt("b8"), Piece.BLACK_SHAMAN);
        is(b.pieceAt("c8"), Piece.BLACK_CHAMELEON);
        is(b.pieceAt("d8"), Piece.BLACK_MADAM);
        is(b.pieceAt("e8"), Piece.BLACK_DUDE);
        is(b.pieceAt("f8"), Piece.BLACK_CHAMELEON);
        is(b.pieceAt("g8"), Piece.BLACK_SHAMAN);
        is(b.pieceAt("h8"), Piece.BLACK_WAGON);
        for (int file = 0; file < 8; file++) {
            is(b.pieceAt(6, file), Piece.BLACK_JEDI);
        }

        boolean allEmpty = true;
        for (int rank = 2; rank < 6; rank++) {
            for (int file = 0; file < 8; file++) {
                if (b.pieceAt(rank, file) != null) {
                    allEmpty = false;
                }
            }
        }
        ok(allEmpty);
        
    }
}
