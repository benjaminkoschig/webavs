package globaz.prestation.tools;

/**
 * Une interface qui permet de d�finir un pr�dicat.
 * 
 * <p>
 * C'est-a-dire un test qui retourne vrai ou faux suivant qu'un objet v�rifie une condition.
 * </p>
 * 
 * @author vre
 */
public interface PRPredicate {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Evaluer le pr�dicat sur l'objet transmis en param�tre.
     * 
     * @param object
     *            un objet � tester
     * 
     * @return vrai ou faux suivant le r�sultat du test.
     */
    public boolean evaluer(Object object);
}
