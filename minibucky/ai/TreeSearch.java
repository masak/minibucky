/* TreeSearch.java
 *
 * Facilities for computing the best scores in a game tree.
 */

package minibucky.ai;

import minibucky.state.Board;
import minibucky.state.Player;
import java.util.Hashtable;

public class TreeSearch {
    private static int searchedPositions = 0;

    public static int searchedPositions() {
        int temp = searchedPositions;
        searchedPositions = 0;

        return temp;
    }
    
    public double lookahead(Board b, int level) {
        if (b.nextPlayer() == Player.WHITE) {
            return lookahead(b, level, Double.MIN_VALUE);
        }
        else {
            return lookahead(b, level, Double.MAX_VALUE);
        }
    }

    public double lookahead(Board b, int level, double threshold) {
        searchedPositions++;
        
        if (level == 0) {
            return Score.compute(b);
        }
        
        String[] moves = b.possibleMoves();

        Player nextPlayer = b.nextPlayer();
        double bestScore = nextPlayer == Player.WHITE ? Double.MIN_VALUE
                         :                              Double.MAX_VALUE;

        for (String move : moves) {
            double score = lookahead(b.makeMove(move), level - 1, bestScore);

            if (nextPlayer == Player.WHITE) {
                // if (score > threshold) {
                //     return score;
                // }

                if (bestScore < score) {
                    bestScore = score;
                }
            }
            else if (nextPlayer == Player.BLACK) {
                // if (score < threshold) {
                //     return score;
                // }
                
                if (bestScore > score) {
                    bestScore = score;
                }
            }
        }

        return bestScore;
    }
}
