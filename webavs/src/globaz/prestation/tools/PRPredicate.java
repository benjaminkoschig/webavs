package globaz.prestation.tools;

/**
 * Une interface qui permet de définir un prédicat.
 * 
 * <p>
 * C'est-a-dire un test qui retourne vrai ou faux suivant qu'un objet vérifie une condition.
 * </p>
 * 
 * @author vre
 */
public interface PRPredicate {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Evaluer le prédicat sur l'objet transmis en paramètre.
     * 
     * @param object
     *            un objet à tester
     * 
     * @return vrai ou faux suivant le résultat du test.
     */
    public boolean evaluer(Object object);
}
