/*
 * Piece.java
 *
 * (C) 2005 Carl Masak
 *
 * Enum class containing the piece types.
 */

package minibucky.state;

public class Piece {
    public static final Piece WHITE_JEDI      = new Piece("white jedi");
    public static final Piece WHITE_WAGON     = new Piece("white wagon");
    public static final Piece WHITE_CHAMELEON = new Piece("white chameleon");
    public static final Piece WHITE_SHAMAN    = new Piece("white shaman");
    public static final Piece WHITE_DUDE      = new Piece("white dude");
    public static final Piece WHITE_MADAM     = new Piece("white madam");

    public static final Piece BLACK_JEDI      = new Piece("black jedi");
    public static final Piece BLACK_WAGON     = new Piece("black wagon");
    public static final Piece BLACK_CHAMELEON = new Piece("black chameleon");
    public static final Piece BLACK_SHAMAN    = new Piece("black shaman");
    public static final Piece BLACK_DUDE      = new Piece("black dude");
    public static final Piece BLACK_MADAM     = new Piece("black madam");

    private String name;
    
    private Piece(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
    
    public String toString() {
        String oneLetterDescription = "" + name.charAt(6);
        if (owner() == Player.WHITE) {
            oneLetterDescription = oneLetterDescription.toUpperCase();
        }
        return oneLetterDescription;
    }

    public Player owner() {
        return name.charAt(0) == 'w' ? Player.WHITE : Player.BLACK;
    }
}
