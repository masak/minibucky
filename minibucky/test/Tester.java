/* Tester.java
 *
 * (C) 2005 Carl Masak
 *
 * Central location for running tests.
 *
 */
package minibucky.test;

public class Tester {
    static Test[] test_suites = new Test[] { new TurnOrderTest(),
                                             new InitialPositionTest(),
                                             new JediTest(),
                                             new WagonTest(),
                                             new ShamanTest(),
                                             new ChameleonTest(),
                                             new DudeTest(),
                                             new MadamTest(),
                                             new PromotionTest(),
                                             new TeleportationTest(),
                                             new EnPassantTest(),
                                             new BuckTest(),
                                             new HypnosisTest(),
                                             new DoubleHypnosisTest(),
                                             new RevengeTest(),
                                             new ChameleonJediTest(),
                                             new ChameleonWagonTest(),
                                             new ChameleonShamanTest(),
                                             new ChameleonChameleonTest(),
                                             new ChameleonDudeTest(),
                                             new ChameleonMadamTest(),
                                             new ChameleonPromotionTest(),
                                             new ChameleonTeleportationTest(),
                                             new ChameleonEnPassantTest(),
                                             new ChameleonHypnosisTest(),
                                             new ChameleonDoubleHypnosisTest(),
                                             new ChameleonRevengeTest(),
                                             new JediBlockingTest(),
                                           };

    public static void main(String[] arg) {
        for (Test t : test_suites) {
            System.out.println(t.name());
            try {
                t.test();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.print("\nResults:\n\n");
        
        for (Test t : test_suites) {
            if (t.tests_made != t.plan()) {
                System.out.println(t.name() + ": " +
                        t.plan() + " tests planned, " +
                        t.tests_made + " made");
            }
            else if (t.tests_succ < t.tests_made) {
                System.out.println(t.name() + ": " +
                        (t.tests_made - t.tests_succ) +
                        " test" + 
                        (t.tests_made - t.tests_succ == 1 ? "" : "s") +
                        " failed");
            }
        }
    }
}
