package globaz.naos.util;

public class AFVerrouAffiliation {
    private static AFVerrouAffiliation instance = null;

    // bug lié à l'affiliation de 2 cas en même temps
    public static synchronized AFVerrouAffiliation getInstance() {
        if (AFVerrouAffiliation.instance == null) {
            AFVerrouAffiliation.instance = new AFVerrouAffiliation();
        }
        return AFVerrouAffiliation.instance;
    }

    private AFVerrouAffiliation() {
        super();
    }
}
