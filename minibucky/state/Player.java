/*
 * Player.java
 *
 * (C) 2005 Carl Masak
 *
 * Enum class containing the black and white players.
 */

package minibucky.state;

public class Player {
    public static final Player WHITE = new Player("white");
    public static final Player BLACK = new Player("black");

    private String name;
    
    private Player(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
