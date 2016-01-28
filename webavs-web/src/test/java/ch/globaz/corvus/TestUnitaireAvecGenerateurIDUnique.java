package ch.globaz.corvus;

/**
 * A �tendre pour vos classe de test unitaire, ajouter une fonction {@link #genererUnIdUnique()} qui vous retournera �
 * chaque fois un long diff�rent
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
