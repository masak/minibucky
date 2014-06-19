/*
 * MoveType.java
 *
 * (C) 2005 Carl Masak
 *
 * Simple enum class to distinguish the five main types of move.
 */

package minibucky.state;

public class MoveType {
    public static final MoveType ROLL       = new MoveType("roll");
    public static final MoveType CAPTURE    = new MoveType("capture");
    public static final MoveType SMASH      = new MoveType("smash");
    public static final MoveType TELEPORTATION
                                            = new MoveType("teleportation");
    public static final MoveType EN_PASSANT = new MoveType("en passant");

    private String name;
    
    private MoveType(String name) {
        this.name = name;
    }
}
