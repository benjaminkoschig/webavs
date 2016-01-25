package ch.globaz.corvus;

/**
 * A étendre pour vos classe de test unitaire, ajouter une fonction {@link #genererUnIdUnique()} qui vous retournera à
 * chaque fois un long différent
 */
public abstract class TestUnitaireAvecGenerateurIDUnique {

    private long idUnique = 0l;

    protected final long genererUnIdUnique() {
        return ++idUnique;
    }

    protected final String genererUnIdUniqueToString() {
        return "" + genererUnIdUnique();
    }
}
