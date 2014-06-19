/* Test.java
 *
 * (C) 2005 Carl Masak
 *
 * Base class for other test classes.
 *
 */
package minibucky.test;

public abstract class Test {
    int tests_made = 0;
    int tests_succ = 0;

    private void nextTest() {
        System.out.print(++tests_made);
        System.out.print(" - ");
    }

    public void ok(boolean b) {
        nextTest();

        if (b) {
            ++tests_succ;
        }
        else {
            System.out.print("not ");
        }

        System.out.println("ok");
    }
    
    public void is(Object a, Object b) {
        nextTest();

        if (null == a && null == b || null != a && a.equals(b)) {
            ++tests_succ;
        }
        else {
            System.out.print("not ");
        }

        System.out.println("ok");
    }
    
    public abstract String name();
    public abstract int plan();
    
    public abstract void test();
}
