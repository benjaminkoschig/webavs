package globaz.corvus.acor.adapter;

import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.db.BSession;
import globaz.prestation.acor.PRACORAdapter;
import globaz.prestation.acor.PRACORException;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Cette classe se base {@link globaz.re.application.REApplication#PROPERTY_ACOR_FACTORY une propriété systeme} pour
 * déterminer quelle factory instancier.
 * </p>
 * 
 * @author scr
 */
public abstract class REAdapterFactory {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static REAdapterFactory FACTORY;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut instance.
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut instance
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public static final synchronized REAdapterFactory getInstance(BSession session) throws PRACORException {
        if (FACTORY == null) {
            try {
                FACTORY = (REAdapterFactory) Class.forName(
                        session.getApplication().getProperty(REApplication.PROPERTY_ACOR_FACTORY)).newInstance();
            } catch (Exception e) {
                throw new PRACORException("impossible de creer une instance de la factory des adapteurs", e);
            }
        }

        return FACTORY;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REAdapterFactory.
     */
    protected REAdapterFactory() {
    }

    /**
     * Cree une instance d'un adapteur pour la demande donne.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public abstract PRACORAdapter createAdapter(BSession session, REDemandeRente demande) throws PRACORException;
}
