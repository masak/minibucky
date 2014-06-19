/* Score.java
 *
 * Algorithms for computing a score for a given board situation.
 */

package minibucky.ai;

import minibucky.state.Board;
import minibucky.state.Player;
import minibucky.state.Piece;

public class Score {
    private static double JEDI_VALUE      = 1.0;
    private static double WAGON_VALUE     = 3.5;
    private static double SHAMAN_VALUE    = 4.0;
    private static double CHAMELEON_VALUE = 3.0;
    private static double MADAM_VALUE     = 8.0;

    public static double pieceValue(Piece p) {
        if (p == Piece.WHITE_JEDI)      return +JEDI_VALUE;
        if (p == Piece.WHITE_WAGON)     return +WAGON_VALUE;
        if (p == Piece.WHITE_SHAMAN)    return +SHAMAN_VALUE;
        if (p == Piece.WHITE_CHAMELEON) return +CHAMELEON_VALUE;
        if (p == Piece.WHITE_MADAM)     return +MADAM_VALUE;

        if (p == Piece.BLACK_JEDI)      return -JEDI_VALUE;
        if (p == Piece.BLACK_WAGON)     return -WAGON_VALUE;
        if (p == Piece.BLACK_SHAMAN)    return -SHAMAN_VALUE;
        if (p == Piece.BLACK_CHAMELEON) return -CHAMELEON_VALUE;
        if (p == Piece.BLACK_MADAM)     return -MADAM_VALUE;

        return 0;
    }
    
    public static double compute(Board b) {
        double score = 0.0;
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                score += pieceValue( b.pieceAt(row, col) );

                if ( b.isHypnotized(row, col) ) {
                    score -= pieceValue( b.pieceAt(row, col) ) / 2;

                    if ( b.pieceAt(row, col) == Piece.WHITE_DUDE ) {
                        score -= 25;
                    }
                    else if ( b.pieceAt(row, col) == Piece.BLACK_DUDE ) {
                        score -= -25;
                    }
                }
            }
        }

        if (b.dudeIsThreatened(b.nextPlayer())) {
            score += (b.nextPlayer() == Player.WHITE ? -50 : 50);
        }

        return score;
    }
}
