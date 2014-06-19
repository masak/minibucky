/*
 * BoardGenerator.java
 *
 * (C) 2005 Carl Masak
 *
 * Utility class for creating a board out of a set of strings.
 */

package minibucky.state;

public class BoardGenerator {
    public static Board generate(Player nextPlayer,
                           String r7, String r6, String r5, String r4,
                           String r3, String r2, String r1, String r0) {
        Piece[][] contents = new Piece[8][8];

        String[] rows = new String[] { r0, r1, r2, r3, r4, r5, r6, r7 };

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                switch ( rows[row].charAt(col) ) {
                    case 'J':
                        contents[row][col] = Piece.WHITE_JEDI;
                        break;
                    case 'W':
                        contents[row][col] = Piece.WHITE_WAGON;
                        break;
                    case 'S':
                        contents[row][col] = Piece.WHITE_SHAMAN;
                        break;
                    case 'C':
                        contents[row][col] = Piece.WHITE_CHAMELEON;
                        break;
                    case 'D':
                        contents[row][col] = Piece.WHITE_DUDE;
                        break;
                    case 'M':
                        contents[row][col] = Piece.WHITE_MADAM;
                        break;

                    case 'j':
                        contents[row][col] = Piece.BLACK_JEDI;
                        break;
                    case 'w':
                        contents[row][col] = Piece.BLACK_WAGON;
                        break;
                    case 's':
                        contents[row][col] = Piece.BLACK_SHAMAN;
                        break;
                    case 'c':
                        contents[row][col] = Piece.BLACK_CHAMELEON;
                        break;
                    case 'd':
                        contents[row][col] = Piece.BLACK_DUDE;
                        break;
                    case 'm':
                        contents[row][col] = Piece.BLACK_MADAM;
                        break;
                }
            }
        }

        return new Board(nextPlayer, contents);
    }
}
