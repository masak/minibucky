/*
 * Role.java
 *
 * (C) 2005 Carl Masak
 *
 * Enum class containing the different roles.
 */

package minibucky.state;

public class Role {
    public static final Role WHITE_VALIANT   = new Role("white valiant");
    public static final Role BLACK_VALIANT   = new Role("black valiant");
    public static final Role WHITE_ENTHUSIASTIC
                                             = new Role("white enthusiastic");
    public static final Role BLACK_ENTHUSIASTIC
                                             = new Role("black enthusiastic");

    public static final Role ZEALOUS         = new Role("zealous");
    public static final Role MODEST          = new Role("modest");
    public static final Role MESMERIZING     = new Role("mesmerizing");
    public static final Role AGHAST          = new Role("aghast");

    public static final Role MIMICKING       = new Role("mimicking");
    public static final Role DIRECT          = new Role("direct");
    public static final Role LADYLIKE        = new Role("ladylike");
    public static final Role VENGEFUL        = new Role("vengeful");

    public static final Role WHITE_POSITIVE  = new Role("white positive");
    public static final Role WHITE_NEGATIVE  = new Role("white negative");
    public static final Role BLACK_POSITIVE  = new Role("black positive");
    public static final Role BLACK_NEGATIVE  = new Role("black negative");
    
    private String name;
    
    private Role(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
