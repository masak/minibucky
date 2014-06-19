/*
 * Board.java
 *
 * (C) 2005 Carl Masak
 *
 * Contains a snapshot of the entire game state, including the positions
 * of all pieces, who has teleported, things like en passant and whose
 * turn it is to move.
 */

package minibucky.state;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Board {
    private Player nextPlayer = Player.WHITE;

    private Piece[][] contents;

    private boolean whiteHasTeleported = false;
    private boolean blackHasTeleported = false;

    private String[] vulnerablePositions = new String[0];

    private boolean[][] isRemoved = new boolean[8][8];

    private String cachedMove;
    private Board cachedBoard;

    public static final Pattern MOVE_SYNTAX =
        Pattern.compile("^[a-h][1-8][-xy][a-h][1-8](=[jwscm])?$",
                Pattern.CASE_INSENSITIVE);
    
    public Board() {
        contents = new Piece[8][8];

        contents[0][0] = Piece.WHITE_WAGON;
        contents[0][1] = Piece.WHITE_SHAMAN;
        contents[0][2] = Piece.WHITE_CHAMELEON;
        contents[0][3] = Piece.WHITE_MADAM;
        contents[0][4] = Piece.WHITE_DUDE;
        contents[0][5] = Piece.WHITE_CHAMELEON;
        contents[0][6] = Piece.WHITE_SHAMAN;
        contents[0][7] = Piece.WHITE_WAGON;

        for (int col = 0; col < 8; col++) {
            contents[1][col] = Piece.WHITE_JEDI;
            contents[6][col] = Piece.BLACK_JEDI;
        }

        contents[7][0] = Piece.BLACK_WAGON;
        contents[7][1] = Piece.BLACK_SHAMAN;
        contents[7][2] = Piece.BLACK_CHAMELEON;
        contents[7][3] = Piece.BLACK_MADAM;
        contents[7][4] = Piece.BLACK_DUDE;
        contents[7][5] = Piece.BLACK_CHAMELEON;
        contents[7][6] = Piece.BLACK_SHAMAN;
        contents[7][7] = Piece.BLACK_WAGON;
    }

    Board(Player nextPlayer, Piece[][] contents) {
        this.nextPlayer = nextPlayer;
        this.contents = contents;
    }
    
    public Player nextPlayer() {
        return nextPlayer;
    }

    public Piece pieceAt(int row, int col) {
        return contents[row][col];
    }

    public Piece pieceAt(String position) {
        return pieceAt(position.charAt(1) - '1',
                       position.toLowerCase().charAt(0) - 'a');
    }

    private boolean hasRole(int row, int col, Role r) {
        Piece p = contents[row][col];
        if (null == p) {
            return false;
        }

        if (Piece.WHITE_JEDI == p) {
            return Role.WHITE_VALIANT == r
                || Role.WHITE_ENTHUSIASTIC == r && row == 1;
        }
        if (Piece.BLACK_JEDI == p) {
            return Role.BLACK_VALIANT == r
                || Role.BLACK_ENTHUSIASTIC == r && row == 6;
        }

        if (Piece.WHITE_WAGON == p || Piece.BLACK_WAGON == p) {
            return Role.ZEALOUS == r;
        }

        if (Piece.WHITE_SHAMAN == p || Piece.BLACK_SHAMAN == p) {
            return Role.MODEST == r
                || Role.MESMERIZING == r;
        }

        if (Role.WHITE_POSITIVE == r && Piece.WHITE_MADAM == p) {
            return true;
        }
        if (Role.BLACK_POSITIVE == r && Piece.BLACK_MADAM == p) {
            return true;
        }
        
        if (Role.WHITE_NEGATIVE == r && Piece.WHITE_DUDE == p) {
            return true;
        }
        if (Role.BLACK_NEGATIVE == r && Piece.BLACK_DUDE == p) {
            return true;
        }
        
        if (Piece.WHITE_DUDE == p || Piece.BLACK_DUDE == p) {
            return Role.MODEST == r
                || Role.DIRECT == r;
        }

        if (Piece.WHITE_MADAM == p || Piece.BLACK_MADAM == p) {
            return Role.LADYLIKE == r
                || Role.VENGEFUL == r;
        }

        if (Piece.WHITE_CHAMELEON == p || Piece.BLACK_CHAMELEON == p) {
            ArrayList<String> placesToLook = new ArrayList<String>();
            
            boolean[][] traversedPieces = new boolean[8][8];

            placesToLook.add("" + (char)('a' + col) + (char)('1' + row));

            while (placesToLook.size() > 0) {
                String current_pos = placesToLook.remove(0);
                int current_row = current_pos.charAt(1) - '1',
                    current_col = current_pos.charAt(0) - 'a';
                traversedPieces[current_row][current_col] = true;

                Piece current_piece = contents[current_row][current_col];
                if (Piece.WHITE_CHAMELEON == current_piece
                        || Piece.BLACK_CHAMELEON == current_piece) {
                    for (int t_row = current_row - 1;
                         t_row <= current_row + 1;
                         t_row++) {
                        if (t_row >= 0 && t_row <= 7) {
                            for (int t_col = current_col - 1;
                                     t_col <= current_col + 1;
                                     t_col++) {
                                if (t_col >= 0 && t_col <= 7 &&
                                        contents[t_row][t_col] != null &&
                                        !traversedPieces[t_row][t_col]) {
                                    placesToLook.add("" +
                                            (char)('a' + t_col) +
                                            (char)('1' + t_row));
                                }
                            }
                        }
                    }
                }
                else {
                    if (hasRole(current_row, current_col, r)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    private MoveType moveType(String move) {
        int to_row = move.charAt(4) - '1';
        int to_col = move.toLowerCase().charAt(3) - 'a';

        MoveType t = null;
        switch (move.charAt(2)) {
            case '-': t = MoveType.ROLL;    break;
            case 'x': t = MoveType.CAPTURE; break;
            case 'y': t = MoveType.SMASH;   break;
        }
        
        if (MoveType.ROLL == t && null != contents[to_row][to_col]) {
            t = MoveType.TELEPORTATION;
        }
        if (MoveType.SMASH == t && null == contents[to_row][to_col]) {
            t = MoveType.EN_PASSANT;
        }

        return t;
    }

    private String[] intermediatePositions(int from_row, int from_col,
                                           int to_row,   int to_col) {
        int middle_row = (from_row + to_row) / 2;
        ArrayList<String> candidates = new ArrayList<String>();

        int start_col = Math.max(from_col, to_col) - 1,
            end_col   = Math.min(from_col, to_col) + 1;

        if (start_col < 0) {
            start_col = 0;
        }

        if (end_col > 7) {
            end_col = 7;
        }

        for (int col = start_col; col <= end_col; col++) {
            candidates.add("" + (char)('a' + col) +
                                (char)('1' + middle_row));
        }

        ArrayList<String> eligiblePositions = new ArrayList<String>();

        for(String candidate : candidates) {
            int row = candidate.charAt(1) - '1',
                col = candidate.charAt(0) - 'a';

            if (contents[row][col] != null) {
                continue;
            }

            Piece[][] new_contents = new Piece[8][8];
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    new_contents[r][c] = contents[r][c];
                }
            }

            Board sandbox = new Board(null, new_contents);
            sandbox.contents[row][col] = contents[from_row][from_col];
            sandbox.contents[from_row][from_col] = null;
        
            if (sandbox.isHypnotized(row, col)) {
                continue;
            }

            eligiblePositions.add(candidate);
        }
        
        return (String[])eligiblePositions.toArray(new String[0]);
    }

    public boolean isHypnotized(int row, int col) {
        if (contents[row][col] == null) {
            return false;
        }
        Player player = contents[row][col].owner();

        ArrayList<String> placesToLook = new ArrayList<String>();
        placesToLook.add("" + (char)('a' + col) + (char)('1' + row));
            
        boolean[][] traversedPieces = new boolean[8][8];

        while (placesToLook.size() > 0) {
            String current_pos = placesToLook.remove(0);
            int current_row = current_pos.charAt(1) - '1',
                current_col = current_pos.charAt(0) - 'a';
            traversedPieces[current_row][current_col] = true;

            Piece current_piece = contents[current_row][current_col];
            Player opponent = current_piece.owner() ==
                 Player.WHITE ? Player.BLACK : Player.WHITE;

            for (int delta_row = -1; delta_row <= 1; delta_row += 2) {
                for (int delta_col = -1; delta_col <= 1; delta_col += 2) {
                    for (int t_row = current_row + delta_row,
                             t_col = current_col + delta_col;
                         t_row >= 0 && t_row < 8 && t_col >= 0 && t_col < 8;) {

                        if (hasRole(t_row, t_col, Role.MESMERIZING) &&
                            contents[t_row][t_col].owner() == opponent) {

                            return true;
                        }

                        if (contents[t_row][t_col] != null &&
                                 !isRemoved[t_row][t_col]) {
                            break;
                        }

                        t_row += delta_row;
                        t_col += delta_col;
                    }
                }
            }

            if (Piece.WHITE_CHAMELEON == current_piece
             || Piece.BLACK_CHAMELEON == current_piece) {

                for (int t_row = current_row - 1;
                     t_row <= current_row + 1;
                     t_row++) {

                    if (t_row >= 0 && t_row <= 7) {
                        for (int t_col = current_col - 1;
                                 t_col <= current_col + 1;
                                 t_col++) {

                            if (t_col >= 0 && t_col <= 7 &&
                                    contents[t_row][t_col] != null &&
                                    !traversedPieces[t_row][t_col]) {
                                placesToLook.add("" +
                                        (char)('a' + t_col) +
                                        (char)('1' + t_row));
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }

    private boolean isDoublyHypnotized(int row, int col) {
        if (contents[row][col] == null) {
            return false;
        }
        Player player = contents[row][col].owner();

        ArrayList<String> placesToLook = new ArrayList<String>();
        placesToLook.add("" + (char)('a' + col) + (char)('1' + row));
            
        boolean[][] traversedPieces = new boolean[8][8];

        String alreadyHypnotizedBy = null;

        while (placesToLook.size() > 0) {
            String current_pos = placesToLook.remove(0);
            int current_row = current_pos.charAt(1) - '1',
                current_col = current_pos.charAt(0) - 'a';
            traversedPieces[current_row][current_col] = true;

            Piece current_piece = contents[current_row][current_col];
            Player opponent = current_piece.owner() ==
                 Player.WHITE ? Player.BLACK : Player.WHITE;

            for (int delta_row = -1; delta_row <= 1; delta_row += 2) {
                for (int delta_col = -1; delta_col <= 1; delta_col += 2) {
                    for (int t_row = current_row + delta_row,
                             t_col = current_col + delta_col;
                         t_row >= 0 && t_row < 8 && t_col >= 0 && t_col < 8;) {

                        if (hasRole(t_row, t_col, Role.MESMERIZING) &&
                            contents[t_row][t_col].owner() == opponent) {

                            String thisHypnosis = "" + t_row + t_col
                                + delta_row + delta_col;
                            if (null == alreadyHypnotizedBy) {
                                alreadyHypnotizedBy = thisHypnosis;
                            }
                            else if
                                (!thisHypnosis.equals(alreadyHypnotizedBy)) {
                                return true;
                            }
                        }

                        if (contents[t_row][t_col] != null &&
                                 !isRemoved[t_row][t_col]) {
                            break;
                        }

                        t_row += delta_row;
                        t_col += delta_col;
                    }
                }
            }

            if (Piece.WHITE_CHAMELEON == current_piece
             || Piece.BLACK_CHAMELEON == current_piece) {

                for (int t_row = current_row - 1;
                     t_row <= current_row + 1;
                     t_row++) {

                    if (t_row >= 0 && t_row <= 7) {
                        for (int t_col = current_col - 1;
                                 t_col <= current_col + 1;
                                 t_col++) {

                            if (t_col >= 0 && t_col <= 7 &&
                                    contents[t_row][t_col] != null &&
                                    !traversedPieces[t_row][t_col]) {
                                placesToLook.add("" +
                                        (char)('a' + t_col) +
                                        (char)('1' + t_row));
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }

    private String[] doublyHypnotizedPieces() {
        ArrayList<String> dh_pieces = new ArrayList<String>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (isDoublyHypnotized(row, col) &&
                        !isRemoved[row][col]) {
                    dh_pieces.add("" + (char)('a' + col)
                                     + (char)('1' + row));
                }
            }
        }

        return (String[])dh_pieces.toArray(new String[0]);
    }

    private boolean movementIsPromoting(String move) {
        int from_row = move.charAt(1) - '1',
            from_col = move.charAt(0) - 'a',
            to_row   = move.charAt(4) - '1',
            to_col   = move.charAt(3) - 'a';
        
        if (moveType(move) == MoveType.SMASH) {
            return false;
        }
        if (hasRole(from_row, from_col, Role.WHITE_VALIANT)
                && from_row != 7 && to_row == 7) {
            return true;
        }
        if (hasRole(from_row, from_col, Role.BLACK_VALIANT)
                && from_row != 0 && to_row == 0) {
            return true;
        }
        return false;
    }

    private Board performCoreMove(String move) {
        Player next = this.nextPlayer == Player.WHITE ? Player.BLACK
                    :                                   Player.WHITE;

        Piece[][] new_contents = new Piece[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                new_contents[row][col] = contents[row][col];
            }
        }
        Board new_board = new Board(next, new_contents);
        new_board.whiteHasTeleported = whiteHasTeleported;
        new_board.blackHasTeleported = blackHasTeleported;

        MoveType moveType = moveType(move);
        int from_row = move.charAt(1) - '1';
        int from_col = move.toLowerCase().charAt(0) - 'a';

        int to_row = move.charAt(4) - '1';
        int to_col = move.toLowerCase().charAt(3) - 'a';

        if (MoveType.ROLL == moveType &&
                to_row - from_row == 2 &&
                Math.abs(to_col - from_col) <= 2 &&
                hasRole(from_row, from_col, Role.WHITE_ENTHUSIASTIC)) {

            new_board.vulnerablePositions =
                intermediatePositions(from_row, from_col, to_row, to_col);
        }
        if (MoveType.ROLL == moveType &&
                to_row - from_row == -2 &&
                Math.abs(to_col - from_col) <= 2 &&
                hasRole(from_row, from_col, Role.BLACK_ENTHUSIASTIC)) {

            new_board.vulnerablePositions =
                intermediatePositions(from_row, from_col, to_row, to_col);
        }
        
        if (MoveType.ROLL == moveType) {
            new_board.contents[to_row][to_col] =
                new_board.contents[from_row][from_col];
            new_board.contents[from_row][from_col] = null;
        }
        else if (MoveType.CAPTURE == moveType) {
            new_board.contents[to_row][to_col] =
                new_board.contents[from_row][from_col];
            new_board.contents[from_row][from_col] = null;
        }
        else if (MoveType.SMASH == moveType) {
            int row_distance = Math.abs(from_row - to_row);
            int col_distance = Math.abs(from_col - to_col);
            int row_direction = row_distance == 0 ? 0
                              : (to_row - from_row) / row_distance;
            int col_direction = col_distance == 0 ? 0
                              : (to_col - from_col) / col_distance;

            new_board.contents[to_row][to_col] = null;
            if (to_row-row_direction != from_row ||
                    to_col-col_direction != from_col) {

                new_board.contents[to_row - row_direction]
                                  [to_col - col_direction] =
                    new_board.contents[from_row][from_col];
                new_board.contents[from_row][from_col] = null;
            }
        }
        else if (MoveType.TELEPORTATION == moveType) {
            if (hasRole(from_row, from_col, Role.WHITE_NEGATIVE) &&
                    hasRole(to_row, to_col, Role.WHITE_POSITIVE) ||
                    hasRole(from_row, from_col, Role.WHITE_POSITIVE) &&
                    hasRole(to_row, to_col, Role.WHITE_NEGATIVE)) {

                new_board.whiteHasTeleported = true;
            }
            if (hasRole(from_row, from_col, Role.BLACK_NEGATIVE) &&
                    hasRole(to_row, to_col, Role.BLACK_POSITIVE) ||
                    hasRole(from_row, from_col, Role.BLACK_POSITIVE) &&
                    hasRole(to_row, to_col, Role.BLACK_NEGATIVE)) {

                new_board.blackHasTeleported = true;
            }
            
            Piece temp = new_board.contents[from_row][from_col];
            new_board.contents[from_row][from_col] =
                new_board.contents[to_row][to_col];
            new_board.contents[to_row][to_col] = temp;
        }

        if (movementIsPromoting(move)) {
            char promoteTo = move.toLowerCase().charAt(6);

            if (this.nextPlayer == Player.BLACK) {
                switch (promoteTo) {
                    case 'j': new_board.contents[to_row][to_col] =
                                  Piece.BLACK_JEDI;
                              break;
                    case 'w': new_board.contents[to_row][to_col] =
                                  Piece.BLACK_WAGON;
                              break;
                    case 's': new_board.contents[to_row][to_col] =
                                  Piece.BLACK_SHAMAN;
                              break;
                    case 'c': new_board.contents[to_row][to_col] =
                                  Piece.BLACK_CHAMELEON;
                              break;
                    case 'm': new_board.contents[to_row][to_col] =
                                  Piece.BLACK_MADAM;
                              break;
                }
            }
            else {
                switch (promoteTo) {
                    case 'j': new_board.contents[to_row][to_col] =
                                  Piece.WHITE_JEDI;
                              break;
                    case 'w': new_board.contents[to_row][to_col] =
                                  Piece.WHITE_WAGON;
                              break;
                    case 's': new_board.contents[to_row][to_col] =
                                  Piece.WHITE_SHAMAN;
                              break;
                    case 'c': new_board.contents[to_row][to_col] =
                                  Piece.WHITE_CHAMELEON;
                              break;
                    case 'm': new_board.contents[to_row][to_col] =
                                  Piece.WHITE_MADAM;
                              break;
                }
            }
        }

        return new_board;
    }

    private String[] possibleTakingMoves(int from_row, int from_col) {
        if (isHypnotized(from_row, from_col)) {
            return new String[0];
        }
        
        String from_pos = "" + (char)('a' + from_col) + (char)('1' + from_row);
        ArrayList<String> moves = new ArrayList<String>();
        
        if (hasRole(from_row, from_col, Role.WHITE_VALIANT)) {
            if (from_row < 7) {
                if (from_col > 0) {
                    moves.add(from_pos + "y" + (char)('a' + from_col - 1)
                                             + (char)('1' + from_row + 1));
                }
                moves.add(from_pos + "y" + (char)('a' + from_col    )
                                         + (char)('1' + from_row + 1));
                if (from_col < 7) {
                    moves.add(from_pos + "y" + (char)('a' + from_col + 1)
                                             + (char)('1' + from_row + 1));
                }
            }
        }

        if (hasRole(from_row, from_col, Role.BLACK_VALIANT)) {
            if (from_row > 0) {
                if (from_col > 0) {
                    moves.add(from_pos + "y" + (char)('a' + from_col - 1)
                                             + (char)('1' + from_row - 1));
                }
                moves.add(from_pos + "y" + (char)('a' + from_col    )
                                         + (char)('1' + from_row - 1));
                if (from_col < 7) {
                    moves.add(from_pos + "y" + (char)('a' + from_col + 1)
                                             + (char)('1' + from_row - 1));
                }
            }
        }

        if (hasRole(from_row, from_col, Role.ZEALOUS)) {
            Player movingPlayer = contents[from_row][from_col].owner();
            Player opponent = movingPlayer == Player.WHITE ? Player.BLACK
                            :                                Player.WHITE;
            
            int[] dir_col = new int[] { 1, 1, 0, -1, -1, -1,  0,  1 };
            int[] dir_row = new int[] { 0, 1, 1,  1,  0, -1, -1, -1 };
            for (int direction = 0; direction < 8; direction++) {
                int delta_col = dir_col[direction];
                int delta_row = dir_row[direction];

                for (int row = from_row + delta_row,
                         col = from_col + delta_col;
                     row >= 0 && row < 8 && col >= 0 && col < 8;) {

                    if (contents[row][col] != null) {
                        if (contents[row][col].owner() == opponent) {
                            moves.add(from_pos + "y" + (char)('a' + col)
                                                     + (char)('1' + row));
                        }
                        break;
                    }
                    row += delta_row;
                    col += delta_col;
                }
            }
        }

        if (hasRole(from_row, from_col, Role.LADYLIKE)) {
            Player movingPlayer = contents[from_row][from_col].owner();
            Player opponent = movingPlayer == Player.WHITE ? Player.BLACK
                            :                                Player.WHITE;
            
            int[] dir_col = new int[] { 1, 1, 0, -1, -1, -1,  0,  1 };
            int[] dir_row = new int[] { 0, 1, 1,  1,  0, -1, -1, -1 };
            for (int direction = 0; direction < 8; direction++) {
                int delta_col = dir_col[direction];
                int delta_row = dir_row[direction];

                for (int row = from_row + delta_row,
                         col = from_col + delta_col;
                     row >= 0 && row < 8 && col >= 0 && col < 8;) {

                    if (contents[row][col] != null) {
                        if (contents[row][col].owner() == opponent) {
                            moves.add(from_pos + "x" + (char)('a' + col)
                                                     + (char)('1' + row));
                        }
                        break;
                    }
                    row += delta_row;
                    col += delta_col;
                }
            }
        }
                                                   
        if (hasRole(from_row, from_col, Role.DIRECT)) {
            Player movingPlayer = contents[from_row][from_col].owner();
            Player opponent = movingPlayer == Player.WHITE ? Player.BLACK
                            :                                Player.WHITE;
            
            int[] dir_col = new int[] { 1, 1, 0, -1, -1, -1,  0,  1 };
            int[] dir_row = new int[] { 0, 1, 1,  1,  0, -1, -1, -1 };
            for (int direction = 0; direction < 8; direction++) {
                int delta_col = dir_col[direction];
                int delta_row = dir_row[direction];

                int row = from_row + delta_row,
                    col = from_col + delta_col;

                if (row >= 0 && row <= 7 && col >=0 && col <= 7 &&
                        contents[row][col] != null &&
                        contents[row][col].owner() == opponent) {
                    
                    moves.add(from_pos + "x" + (char)('a' + col)
                                             + (char)('1' + row));
                }
            }
        }

        if (hasRole(from_row, from_col, Role.MESMERIZING)) {
            Player movingPlayer = contents[from_row][from_col].owner();
            Player opponent = movingPlayer == Player.WHITE ? Player.BLACK
                            :                                Player.WHITE;
            
            int[] dir_col = new int[] { 1, 1, 0, -1, -1, -1,  0,  1 };
            int[] dir_row = new int[] { 0, 1, 1,  1,  0, -1, -1, -1 };
            for (int direction = 0; direction < 8; direction++) {
                int delta_col = dir_col[direction];
                int delta_row = dir_row[direction];

                int to_row = from_row + delta_row,
                    to_col = from_col + delta_col;

                if (to_row >= 0 && to_row <= 7 &&
                        to_col >=0 && to_col <= 7 &&
                        contents[to_row][to_col] == null) {

                    String move = from_pos + "-"
                                           + (char)('a' + to_col)
                                           + (char)('1' + to_row);
                    Board new_board = performCoreMove(move);

                    if (new_board.doublyHypnotizedPieces().length > 0) {
                        moves.add(move);
                    }
                }
            }
        }

        return (String[])moves.toArray(new String[0]);
    }

    public boolean dudeIsThreatened(Player player) {
        Player opponent = player == Player.WHITE ? Player.BLACK
                        :                          Player.WHITE;
        
        String dudePosition = null;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((contents[row][col] == Piece.WHITE_DUDE ||
                     contents[row][col] == Piece.BLACK_DUDE) &&
                        contents[row][col].owner() == player) {

                    dudePosition = "" + (char)('a' + col) + (char)('1' + row);
                }
            }
        }
        if (dudePosition == null) {
            return false;
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (null != contents[row][col] &&
                        contents[row][col].owner() == opponent) {

                    String[] possibleMoves =
                        possibleTakingMoves(row, col);

                    for (String move : possibleMoves) {
                        if (dudePosition.equals(move.substring(3, 5))) {
                            return true;
                        }
                        if (move.charAt(2) == '-') {
                            Board new_board = performCoreMove(move);

                            while (true) {
                                String[] markedPieces =
                                    new_board.doublyHypnotizedPieces();
                                if (markedPieces.length == 0) {
                                    break;
                                }

                                for (String marked_pos : markedPieces) {
                                    int marked_row = marked_pos.charAt(1) - '1',
                                        marked_col = marked_pos.charAt(0) - 'a';

                                    new_board.isRemoved[marked_row][marked_col]
                                        = true;
                                    if (dudePosition.equals(marked_pos)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }

    public boolean moveIsLegal(String move) {
        if (!MOVE_SYNTAX.matcher(move).matches()) {
            return false;
        }
        
        int from_row = move.charAt(1) - '1';
        int from_col = move.toLowerCase().charAt(0) - 'a';

        int to_row = move.charAt(4) - '1';
        int to_col = move.toLowerCase().charAt(3) - 'a';

        if (null == contents[from_row][from_col]) {
            return false;
        }

        Piece movingPiece = contents[from_row][from_col];
        if (movingPiece.owner() != nextPlayer) {
            return false;
        }
        
        boolean tryingForRevenge = false;
        if (isHypnotized(from_row, from_col) &&
                hasRole(from_row, from_col, Role.VENGEFUL)) {

            tryingForRevenge = true;
        }
        else if (isHypnotized(from_row, from_col)) {
            return false;
        }
        
        MoveType moveType = moveType(move);
        if (MoveType.CAPTURE == moveType &&
                null == contents[to_row][to_col]) {

            return false;
        }
        if ((MoveType.CAPTURE == moveType || MoveType.SMASH == moveType) &&
                null != contents[to_row][to_col] &&
                contents[to_row][to_col].owner() == nextPlayer) {
            return false;
        }

        boolean opponentPiecesAreRemoved = false;
        if (MoveType.CAPTURE == moveType || MoveType.SMASH == moveType) {
            opponentPiecesAreRemoved = true;
        }

        boolean motionIsAccepted = false;
        
        if (hasRole(from_row, from_col, Role.WHITE_VALIANT)) {
            int row_delta = to_row - from_row;
            int col_distance = Math.abs(to_col - from_col);

            if (row_delta == 1 && col_distance <= 1 &&
                (MoveType.SMASH == moveType || MoveType.ROLL == moveType)) {

                motionIsAccepted = true;
            }

            if (MoveType.EN_PASSANT == moveType &&
                    vulnerablePositions != null) {

                String toPos = move.substring(3, 5);
                boolean toVulnerable = false;
                for (String v : vulnerablePositions) {
                    if (toPos.equals(v)) {
                        toVulnerable = true;
                    }
                }
                if (row_delta == 1 && col_distance <= 1 &&
                        toVulnerable) {

                    motionIsAccepted = true;
                }
            }
                    
            if (row_delta == 2 && col_distance <= 2 &&
                hasRole(from_row, from_col, Role.WHITE_ENTHUSIASTIC) &&
                moveType.ROLL == moveType &&
                intermediatePositions(from_row, from_col,
                                      to_row, to_col).length > 0) {

                motionIsAccepted = true;
            }
        }

        if (hasRole(from_row, from_col, Role.BLACK_VALIANT)) {
            int row_delta = to_row - from_row;
            int col_distance = Math.abs(to_col - from_col);

            if (row_delta == -1 && col_distance <= 1 &&
                (MoveType.SMASH == moveType || MoveType.ROLL == moveType)) {

                motionIsAccepted = true;
            }

            if (MoveType.EN_PASSANT == moveType &&
                    vulnerablePositions != null) {

                String toPos = move.substring(3, 5);
                boolean toVulnerable = false;
                for (String v : vulnerablePositions) {
                    if (toPos.equals(v)) {
                        toVulnerable = true;
                    }
                }
                if (row_delta == -1 && col_distance <= 1 &&
                        toVulnerable) {

                    motionIsAccepted = true;
                }
            }
                    
            if (row_delta == -2 && col_distance <= 2 &&
                hasRole(from_row, from_col, Role.BLACK_ENTHUSIASTIC) &&
                moveType.ROLL == moveType &&
                intermediatePositions(from_row, from_col,
                                      to_row, to_col).length > 0) {

                motionIsAccepted = true;
            }
        }

        if (hasRole(from_row, from_col, Role.ZEALOUS) &&
                (MoveType.ROLL == moveType || MoveType.SMASH == moveType)) {
            int row_distance = Math.abs(from_row - to_row);
            int col_distance = Math.abs(from_col - to_col);

            if (row_distance == 0 && col_distance != 0
             || row_distance != 0 && col_distance == 0
             || row_distance == col_distance) {
                
                int distance = Math.max(row_distance, col_distance);
                if (MoveType.SMASH == moveType) {
                    distance--;
                }
                int row_direction = row_distance == 0 ? 0
                                  : (to_row - from_row) / row_distance;
                int col_direction = col_distance == 0 ? 0
                                  : (to_col - from_col) / col_distance;
                
                int current_row = from_row,
                    current_col = from_col;
                boolean all_clear = true;
                
                for (int i = 1; i <= distance; i++) {
                    current_row += row_direction;
                    current_col += col_direction;

                    if (null != contents[current_row][current_col]) {
                        all_clear = false;
                    }
                }

                if (all_clear) {
                    current_row += row_direction;
                    current_col += col_direction;
                    
                    if (current_row < 0 || current_row > 7
                        || current_col < 0 || current_col > 7
                        || null != contents[current_row][current_col]) {
                        
                        motionIsAccepted = true;
                    }
                }
            }
        }
                
        if (hasRole(from_row, from_col, Role.MODEST)
                && MoveType.ROLL == moveType) {
            int row_distance = Math.abs(to_row - from_row);
            int col_distance = Math.abs(to_col - from_col);

            if (row_distance <= 1 && col_distance <= 1) {
                motionIsAccepted = true;
            }
        }

        if (hasRole(from_row, from_col, Role.DIRECT)
                && MoveType.CAPTURE == moveType) {
            int row_distance = Math.abs(to_row - from_row);
            int col_distance = Math.abs(to_col - from_col);

            if (row_distance <= 1 && col_distance <= 1) {
                motionIsAccepted = true;
            }
        }

        if (hasRole(from_row, from_col, Role.LADYLIKE) &&
                (MoveType.ROLL == moveType
                 || MoveType.CAPTURE == moveType)) {
            int row_distance = Math.abs(from_row - to_row);
            int col_distance = Math.abs(from_col - to_col);

            if (row_distance == 0 && col_distance != 0
             || row_distance != 0 && col_distance == 0
             || row_distance == col_distance) {
                
                int distance = Math.max(row_distance, col_distance);
                if (MoveType.CAPTURE == moveType) {
                    distance--;
                }
                int row_direction = row_distance == 0 ? 0
                                  : (to_row - from_row) / row_distance;
                int col_direction = col_distance == 0 ? 0
                                  : (to_col - from_col) / col_distance;
                
                int current_row = from_row,
                    current_col = from_col;
                boolean all_clear = true;
                
                for (int i = 1; i <= distance; i++) {
                    current_row += row_direction;
                    current_col += col_direction;

                    if (null != contents[current_row][current_col]) {
                        all_clear = false;
                    }
                }

                if (all_clear) {
                    motionIsAccepted = true;
                }
            }
        }

        if (hasRole(from_row, from_col, Role.WHITE_NEGATIVE) &&
                hasRole(to_row, to_col, Role.WHITE_POSITIVE) &&
                !whiteHasTeleported                          &&
                MoveType.TELEPORTATION == moveType) {

            motionIsAccepted = true;
        }
        if (hasRole(from_row, from_col, Role.WHITE_POSITIVE) &&
                hasRole(to_row, to_col, Role.WHITE_NEGATIVE) &&
                !whiteHasTeleported                          &&
                MoveType.TELEPORTATION == moveType) {
            
            motionIsAccepted = true;
        }

        if (hasRole(from_row, from_col, Role.BLACK_NEGATIVE) &&
                hasRole(to_row, to_col, Role.BLACK_POSITIVE) &&
                !blackHasTeleported                          &&
                MoveType.TELEPORTATION == moveType) {

            motionIsAccepted = true;
        }
        if (hasRole(from_row, from_col, Role.BLACK_POSITIVE) &&
                hasRole(to_row, to_col, Role.BLACK_NEGATIVE) &&
                !blackHasTeleported                          &&
                MoveType.TELEPORTATION == moveType) {
            
            motionIsAccepted = true;
        }

        if (!motionIsAccepted) {
            return false;
        }

        boolean moveHasPromotionPart = move.length() == 7
            && move.charAt(5) == '=';

        if (moveHasPromotionPart != movementIsPromoting(move)) {
            return false;
        }

        if (moveHasPromotionPart) {
            char promoteTo = move.toLowerCase().charAt(6);
            if (promoteTo == movingPiece.toString().toLowerCase().charAt(0)) {
                return false;
            }
        }

        Board sandbox = performCoreMove(move);

        String target_pos = move.substring(3, 5);
        if (MoveType.SMASH == moveType) {
            int row_distance = Math.abs(from_row - to_row);
            int col_distance = Math.abs(from_col - to_col);
            int row_direction = row_distance == 0 ? 0
                              : (to_row - from_row) / row_distance;
            int col_direction = col_distance == 0 ? 0
                              : (to_col - from_col) / col_distance;

            target_pos = "" + (char)('a' + to_col - col_direction)
                            + (char)('1' + to_row - row_direction);
        }

        while (true) {
            String[] markedPieces = sandbox.doublyHypnotizedPieces();
            if (markedPieces.length == 0) {
                break;
            }

            for (String marked_pos : markedPieces) {
                int marked_row = marked_pos.charAt(1) - '1',
                    marked_col = marked_pos.charAt(0) - 'a';

                sandbox.isRemoved[marked_row][marked_col] = true;
                if (target_pos.equals(marked_pos)) {
                    return false;
                }
                if (sandbox.contents[marked_row][marked_col].owner() ==
                        sandbox.nextPlayer) {
                    opponentPiecesAreRemoved = true;
                }
            }
        }
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (sandbox.isRemoved[row][col]) {
                    sandbox.contents[row][col] = null;
                }
                sandbox.isRemoved[row][col] = false;
            }
        }

        if (tryingForRevenge) {
            if (!opponentPiecesAreRemoved) {
                return false;
            }
            
            Piece temp = sandbox.contents[from_row][from_col];
            sandbox.contents[from_row][from_col] =
                   contents[from_row][from_col];
            if (sandbox.isHypnotized(from_row, from_col)) {
                return false;
            }
            sandbox.contents[from_row][from_col] = temp;
        }

        if (sandbox.dudeIsThreatened(this.nextPlayer)) {
            return false;
        }
        
        cachedMove = move;
        cachedBoard = sandbox;
        return true;
    }
    
    public String whyIsMoveIllegal(String move) {
        if (!MOVE_SYNTAX.matcher(move).matches()) {
            return "Syntax error. A move must conform to the regular " +
                "expression /" + MOVE_SYNTAX + "/.";
        }
        
        int from_row = move.charAt(1) - '1';
        int from_col = move.toLowerCase().charAt(0) - 'a';

        int to_row = move.charAt(4) - '1';
        int to_col = move.toLowerCase().charAt(3) - 'a';

        if (null == contents[from_row][from_col]) {
            return "There's no piece at " + move.substring(0, 2) + ".";
        }

        Piece movingPiece = contents[from_row][from_col];
        if (movingPiece.owner() != nextPlayer) {
            return "The piece at " + move.substring(0, 2) +
                " does not belong to " + nextPlayer + ".";
        }
        
        boolean tryingForRevenge = false;
        if (isHypnotized(from_row, from_col) &&
                hasRole(from_row, from_col, Role.VENGEFUL)) {

            tryingForRevenge = true;
        }
        else if (isHypnotized(from_row, from_col)) {
            return "The piece at " + move.substring(0, 2) + " is hypnotized.";
        }
        
        MoveType moveType = moveType(move);
        if (MoveType.CAPTURE == moveType &&
                null == contents[to_row][to_col]) {

            return "There's no piece to capture at " + move.substring(3, 5) +
                ". Did you mean " + move.substring(0, 2) + "-" +
                move.substring(3) + "?";
        }
        if ((MoveType.CAPTURE == moveType || MoveType.SMASH == moveType) &&
                null != contents[to_row][to_col] &&
                contents[to_row][to_col].owner() == nextPlayer) {
            return "The piece at " + move.substring(3, 5) + " is your own.";
        }

        boolean opponentPiecesAreRemoved = false;
        if (MoveType.CAPTURE == moveType || MoveType.SMASH == moveType) {
            opponentPiecesAreRemoved = true;
        }

        boolean motionIsAccepted = false;
        
        if (hasRole(from_row, from_col, Role.WHITE_VALIANT)) {
            int row_delta = to_row - from_row;
            int col_distance = Math.abs(to_col - from_col);

            if (row_delta == 1 && col_distance <= 1 &&
                (MoveType.SMASH == moveType || MoveType.ROLL == moveType)) {

                motionIsAccepted = true;
            }

            if (MoveType.EN_PASSANT == moveType &&
                    vulnerablePositions != null) {

                String toPos = move.substring(3, 5);
                boolean toVulnerable = false;
                for (String v : vulnerablePositions) {
                    if (toPos.equals(v)) {
                        toVulnerable = true;
                    }
                }
                if (row_delta == 1 && col_distance <= 1 &&
                        toVulnerable) {

                    motionIsAccepted = true;
                }
            }
                    
            if (row_delta == 2 && col_distance <= 2 &&
                hasRole(from_row, from_col, Role.WHITE_ENTHUSIASTIC) &&
                moveType.ROLL == moveType &&
                intermediatePositions(from_row, from_col,
                                      to_row, to_col).length > 0) {

                motionIsAccepted = true;
            }
        }

        if (hasRole(from_row, from_col, Role.BLACK_VALIANT)) {
            int row_delta = to_row - from_row;
            int col_distance = Math.abs(to_col - from_col);

            if (row_delta == -1 && col_distance <= 1 &&
                (MoveType.SMASH == moveType || MoveType.ROLL == moveType)) {

                motionIsAccepted = true;
            }

            if (MoveType.EN_PASSANT == moveType &&
                    vulnerablePositions != null) {

                String toPos = move.substring(3, 5);
                boolean toVulnerable = false;
                for (String v : vulnerablePositions) {
                    if (toPos.equals(v)) {
                        toVulnerable = true;
                    }
                }
                if (row_delta == -1 && col_distance <= 1 &&
                        toVulnerable) {

                    motionIsAccepted = true;
                }
            }
                    
            if (row_delta == -2 && col_distance <= 2 &&
                hasRole(from_row, from_col, Role.BLACK_ENTHUSIASTIC) &&
                moveType.ROLL == moveType &&
                intermediatePositions(from_row, from_col,
                                      to_row, to_col).length > 0) {

                motionIsAccepted = true;
            }
        }

        if (hasRole(from_row, from_col, Role.ZEALOUS) &&
                (MoveType.ROLL == moveType || MoveType.SMASH == moveType)) {
            int row_distance = Math.abs(from_row - to_row);
            int col_distance = Math.abs(from_col - to_col);

            if (row_distance == 0 && col_distance != 0
             || row_distance != 0 && col_distance == 0
             || row_distance == col_distance) {
                
                int distance = Math.max(row_distance, col_distance);
                if (MoveType.SMASH == moveType) {
                    distance--;
                }
                int row_direction = row_distance == 0 ? 0
                                  : (to_row - from_row) / row_distance;
                int col_direction = col_distance == 0 ? 0
                                  : (to_col - from_col) / col_distance;
                
                int current_row = from_row,
                    current_col = from_col;
                boolean all_clear = true;
                
                for (int i = 1; i <= distance; i++) {
                    current_row += row_direction;
                    current_col += col_direction;

                    if (null != contents[current_row][current_col]) {
                        all_clear = false;
                    }
                }

                if (all_clear) {
                    current_row += row_direction;
                    current_col += col_direction;
                    
                    if (current_row < 0 || current_row > 7
                        || current_col < 0 || current_col > 7
                        || null != contents[current_row][current_col]) {
                        
                        motionIsAccepted = true;
                    }
                }
            }
        }
                
        if (hasRole(from_row, from_col, Role.MODEST)
                && MoveType.ROLL == moveType) {
            int row_distance = Math.abs(to_row - from_row);
            int col_distance = Math.abs(to_col - from_col);

            if (row_distance <= 1 && col_distance <= 1) {
                motionIsAccepted = true;
            }
        }

        if (hasRole(from_row, from_col, Role.DIRECT)
                && MoveType.CAPTURE == moveType) {
            int row_distance = Math.abs(to_row - from_row);
            int col_distance = Math.abs(to_col - from_col);

            if (row_distance <= 1 && col_distance <= 1) {
                motionIsAccepted = true;
            }
        }

        if (hasRole(from_row, from_col, Role.LADYLIKE) &&
                (MoveType.ROLL == moveType
                 || MoveType.CAPTURE == moveType)) {
            int row_distance = Math.abs(from_row - to_row);
            int col_distance = Math.abs(from_col - to_col);

            if (row_distance == 0 && col_distance != 0
             || row_distance != 0 && col_distance == 0
             || row_distance == col_distance) {
                
                int distance = Math.max(row_distance, col_distance);
                if (MoveType.CAPTURE == moveType) {
                    distance--;
                }
                int row_direction = row_distance == 0 ? 0
                                  : (to_row - from_row) / row_distance;
                int col_direction = col_distance == 0 ? 0
                                  : (to_col - from_col) / col_distance;
                
                int current_row = from_row,
                    current_col = from_col;
                boolean all_clear = true;
                
                for (int i = 1; i <= distance; i++) {
                    current_row += row_direction;
                    current_col += col_direction;

                    if (null != contents[current_row][current_col]) {
                        all_clear = false;
                    }
                }

                if (all_clear) {
                    motionIsAccepted = true;
                }
            }
        }

        if (hasRole(from_row, from_col, Role.WHITE_NEGATIVE) &&
                hasRole(to_row, to_col, Role.WHITE_POSITIVE) &&
                !whiteHasTeleported                          &&
                MoveType.TELEPORTATION == moveType) {

            motionIsAccepted = true;
        }
        if (hasRole(from_row, from_col, Role.WHITE_POSITIVE) &&
                hasRole(to_row, to_col, Role.WHITE_NEGATIVE) &&
                !whiteHasTeleported                          &&
                MoveType.TELEPORTATION == moveType) {
            
            motionIsAccepted = true;
        }

        if (hasRole(from_row, from_col, Role.BLACK_NEGATIVE) &&
                hasRole(to_row, to_col, Role.BLACK_POSITIVE) &&
                !blackHasTeleported                          &&
                MoveType.TELEPORTATION == moveType) {

            motionIsAccepted = true;
        }
        if (hasRole(from_row, from_col, Role.BLACK_POSITIVE) &&
                hasRole(to_row, to_col, Role.BLACK_NEGATIVE) &&
                !blackHasTeleported                          &&
                MoveType.TELEPORTATION == moveType) {
            
            motionIsAccepted = true;
        }

        if (!motionIsAccepted) {
            return "The piece at " + move.substring(0, 2) + " cannot move" +
                " in that way accoring to the rules.";
        }

        boolean moveHasPromotionPart = move.length() == 7
            && move.charAt(5) == '=';

        if (moveHasPromotionPart && !movementIsPromoting(move)) {
            return "You are not allowed to promote with that movement.";
        }
        else if (!moveHasPromotionPart && movementIsPromoting(move)) {
            return "You have to promote if you move to " +
                move.substring(3, 5) + ".";
        }

        if (moveHasPromotionPart) {
            char promoteTo = move.toLowerCase().charAt(6);
            if (promoteTo == movingPiece.toString().toLowerCase().charAt(0)) {
                return "You must promote to a different piece.";
            }
        }

        Board sandbox = performCoreMove(move);

        String target_pos = move.substring(3, 5);
        if (MoveType.SMASH == moveType) {
            int row_distance = Math.abs(from_row - to_row);
            int col_distance = Math.abs(from_col - to_col);
            int row_direction = row_distance == 0 ? 0
                              : (to_row - from_row) / row_distance;
            int col_direction = col_distance == 0 ? 0
                              : (to_col - from_col) / col_distance;

            target_pos = "" + (char)('a' + to_col - col_direction)
                            + (char)('1' + to_row - row_direction);
        }

        while (true) {
            String[] markedPieces = sandbox.doublyHypnotizedPieces();
            if (markedPieces.length == 0) {
                break;
            }

            for (String marked_pos : markedPieces) {
                int marked_row = marked_pos.charAt(1) - '1',
                    marked_col = marked_pos.charAt(0) - 'a';

                sandbox.isRemoved[marked_row][marked_col] = true;
                if (target_pos.equals(marked_pos)) {
                    return "The piece becomes double hypnotized when it " +
                        "moves there.";
                }
                if (sandbox.contents[marked_row][marked_col].owner() ==
                        sandbox.nextPlayer) {
                    opponentPiecesAreRemoved = true;
                }
            }
        }
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (sandbox.isRemoved[row][col]) {
                    sandbox.contents[row][col] = null;
                }
                sandbox.isRemoved[row][col] = false;
            }
        }

        if (tryingForRevenge) {
            if (!opponentPiecesAreRemoved) {
                return "The piece must take an opponent piece in order " +
                    "to be able to use its vengefulness.";
            }
            
            Piece temp = sandbox.contents[from_row][from_col];
            sandbox.contents[from_row][from_col] =
                   contents[from_row][from_col];
            if (sandbox.isHypnotized(from_row, from_col)) {
                return "The piece must make the position " +
                    move.substring(0, 2) + " unhypnotized in order to " +
                    "be able to use its vengefulness.";
            }
            sandbox.contents[from_row][from_col] = temp;
        }

        if (sandbox.dudeIsThreatened(this.nextPlayer)) {
            if (this.dudeIsThreatened(this.nextPlayer)) {
                return "The dude is still threatened after that move.";
            }

            return "The dude becomes threatened by that move.";
        }
        
        throw new IllegalArgumentException("The move " + move + " is legal.");
    }
    
    public Board makeMove(String move) {
        if (move.equals(cachedMove)) {
            return cachedBoard;
        }
        
        if (!moveIsLegal(move)) {
            throw new IllegalArgumentException("Illegal move: " + move);
        }

        return cachedBoard;
    }

    public String[] possibleMoves() {
        ArrayList<String> moves = new ArrayList<String>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (contents[row][col] == null ||
                        contents[row][col].owner() != nextPlayer) {
                    continue;
                }
                if (isHypnotized(row, col) &&
                        !hasRole(row, col, Role.VENGEFUL)) {
                    continue;
                }
                String from_pos = "" + (char)('a' + col) + (char)('1' + row);

                if (hasRole(row, col, Role.WHITE_VALIANT) && row < 7) {
                    if (col > 0) {
                        moves.add(from_pos + "-" + (char)('a' + col - 1)
                                                 + (char)('1' + row + 1));
                        moves.add(from_pos + "y" + (char)('a' + col - 1)
                                                 + (char)('1' + row + 1));
                    }
                    moves.add(from_pos + "-" + (char)('a' + col)
                                             + (char)('1' + row + 1));
                    moves.add(from_pos + "y" + (char)('a' + col)
                                             + (char)('1' + row + 1));
                    if (col < 7) {
                        moves.add(from_pos + "-" + (char)('a' + col + 1)
                                                 + (char)('1' + row + 1));
                        moves.add(from_pos + "y" + (char)('a' + col + 1)
                                                 + (char)('1' + row + 1));
                    }

                    if (hasRole(row, col, Role.WHITE_ENTHUSIASTIC) && row < 6) {
                        if (col > 1) {
                            moves.add(from_pos + "-" + (char)('a' + col - 2)
                                                     + (char)('1' + row + 2));
                        }
                        if (col > 0) {
                            moves.add(from_pos + "-" + (char)('a' + col - 1)
                                                     + (char)('1' + row + 2));
                        }
                        moves.add(from_pos + "-" + (char)('a' + col)
                                                 + (char)('1' + row + 2));
                        if (col < 7) {
                            moves.add(from_pos + "-" + (char)('a' + col + 1)
                                                     + (char)('1' + row + 2));
                        }
                        if (col < 6) {
                            moves.add(from_pos + "-" + (char)('a' + col + 2)
                                                     + (char)('1' + row + 2));
                        }
                    }
                }
                
                if (hasRole(row, col, Role.BLACK_VALIANT) && row > 0) {
                    if (col > 0) {
                        moves.add(from_pos + "-" + (char)('a' + col - 1)
                                                 + (char)('1' + row - 1));
                        moves.add(from_pos + "y" + (char)('a' + col - 1)
                                                 + (char)('1' + row - 1));
                    }
                    moves.add(from_pos + "-" + (char)('a' + col)
                                             + (char)('1' + row - 1));
                    moves.add(from_pos + "y" + (char)('a' + col)
                                             + (char)('1' + row - 1));
                    if (col < 7) {
                        moves.add(from_pos + "-" + (char)('a' + col + 1)
                                                 + (char)('1' + row - 1));
                        moves.add(from_pos + "y" + (char)('a' + col + 1)
                                                 + (char)('1' + row - 1));
                    }

                    if (hasRole(row, col, Role.BLACK_ENTHUSIASTIC) && row > 1) {
                        if (col > 1) {
                            moves.add(from_pos + "-" + (char)('a' + col - 2)
                                                     + (char)('1' + row - 2));
                        }
                        if (col > 0) {
                            moves.add(from_pos + "-" + (char)('a' + col - 1)
                                                     + (char)('1' + row - 2));
                        }
                        moves.add(from_pos + "-" + (char)('a' + col)
                                                 + (char)('1' + row - 2));
                        if (col < 7) {
                            moves.add(from_pos + "-" + (char)('a' + col + 1)
                                                     + (char)('1' + row - 2));
                        }
                        if (col < 6) {
                            moves.add(from_pos + "-" + (char)('a' + col + 2)
                                                     + (char)('1' + row - 2));
                        }
                    }
                }
                
                if (hasRole(row, col, Role.LADYLIKE) ||
                        hasRole(row, col, Role.ZEALOUS)) {
                    Player movingPlayer = contents[row][col].owner();
                    Player opponent =
                        movingPlayer == Player.WHITE ? Player.BLACK
                                                     : Player.WHITE;
            
                    int[] dir_col = new int[] { 1, 1, 0, -1, -1, -1,  0,  1 };
                    int[] dir_row = new int[] { 0, 1, 1,  1,  0, -1, -1, -1 };
                    for (int direction = 0; direction < 8; direction++) {
                        int delta_col = dir_col[direction];
                        int delta_row = dir_row[direction];

                        for (int t_row = row + delta_row,
                                 t_col = col + delta_col;
                                 t_row >= 0 && t_row < 8 &&
                                 t_col >= 0 && t_col < 8;) {

                            if (contents[t_row][t_col] != null) {
                                if (contents[t_row][t_col].owner()
                                        == opponent) {
                                    if (hasRole(row, col, Role.LADYLIKE)) {
                                        moves.add(from_pos + "x"
                                                + (char)('a' + t_col)
                                                + (char)('1' + t_row));
                                    }
                                    if (hasRole(row, col, Role.ZEALOUS)) {
                                        moves.add(from_pos + "y"
                                                + (char)('a' + t_col)
                                                + (char)('1' + t_row));
                                    }
                                }
                                break;
                            }
                            moves.add(from_pos + "-" + (char)('a' + t_col)
                                                     + (char)('1' + t_row));
                            t_row += delta_row;
                            t_col += delta_col;
                        }
                    }
                }

                if (hasRole(row, col, Role.MODEST)) {
                    Player movingPlayer = contents[row][col].owner();
                    Player opponent = movingPlayer ==
                        Player.WHITE ? Player.BLACK : Player.WHITE;
            
                    int[] dir_col = new int[] { 1, 1, 0, -1, -1, -1,  0,  1 };
                    int[] dir_row = new int[] { 0, 1, 1,  1,  0, -1, -1, -1 };
                    for (int direction = 0; direction < 8; direction++) {
                        int delta_col = dir_col[direction];
                        int delta_row = dir_row[direction];

                        int t_row = row + delta_row,
                            t_col = col + delta_col;

                        if (t_row >= 0 && t_row <= 7 &&
                                t_col >=0 && t_col <= 7) {
                            if (contents[t_row][t_col] == null) {
                                moves.add(from_pos + "-" + (char)('a' + t_col)
                                                         + (char)('1' + t_row));
                            }
                            else if (contents[t_row][t_col].owner() == opponent
                                      && hasRole(row, col, Role.DIRECT)) {
                                moves.add(from_pos + "x"
                                        + (char)('a' + t_col)
                                        + (char)('1' + t_row));
                            }
                        }
                    }
                }

                Role[] abilities = new Role[] {
                    Role.WHITE_POSITIVE, Role.WHITE_NEGATIVE,
                    Role.BLACK_POSITIVE, Role.BLACK_NEGATIVE };

                for (int i = 0; i < 4; i++) {
                    Role ability       = abilities[i],
                         other_ability = abilities[(i+1)%2 + 2*i/2];
                    
                    if (hasRole(row, col, ability)) {
                        for (int t_row = 0; t_row < 8; t_row++) {
                            for (int t_col = 0; t_col < 8; t_col++) {
                                if (hasRole(row, col, other_ability)) {
                                    moves.add(from_pos + "-"
                                            + (char)('a' + t_col)
                                            + (char)('1' + t_row));
                                }
                            }
                        }
                    }
                }
            }
        }

        ArrayList<String> promotionMoves = new ArrayList<String>();
        for (String move : moves) {
            if (move.charAt(2) == '-' || move.charAt(2) == 'x') {
                int from_row = move.charAt(1) - '1';
                int from_col = move.charAt(0) - 'a';
                int to_row   = move.charAt(4) - '1';

                if (hasRole(from_row, from_col, Role.WHITE_VALIANT) &&
                        to_row == 7 ||
                    hasRole(from_row, from_col, Role.BLACK_VALIANT) &&
                        to_row == 0) {

                    if (contents[from_row][from_col].owner()
                            == Player.WHITE) {

                        promotionMoves.add(move + "=J");
                        promotionMoves.add(move + "=W");
                        promotionMoves.add(move + "=S");
                        promotionMoves.add(move + "=C");
                        promotionMoves.add(move + "=M");
                    }
                    else {
                        promotionMoves.add(move + "=J");
                        promotionMoves.add(move + "=W");
                        promotionMoves.add(move + "=S");
                        promotionMoves.add(move + "=C");
                        promotionMoves.add(move + "=M");
                    }
                }
            }
        }
        for (String m : promotionMoves) {
            moves.add(m);
        }

        ArrayList<String> legalMoves = new ArrayList<String>();
        for (String move : moves) {
            if (!legalMoves.contains(move) &&
                    moveIsLegal(move)) {
                legalMoves.add(move);
            }
        }

        return legalMoves.toArray(new String[0]);
    }

    public String toString() {
        StringBuffer s = new StringBuffer();

        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                Piece cur_piece = contents[row][col];
                
                if (cur_piece == null) {
                    s.append('.');
                }
                else {
                    s.append(cur_piece.toString());
                }
            }
            s.append('\n');
        }

        return s.toString();
    }
}
