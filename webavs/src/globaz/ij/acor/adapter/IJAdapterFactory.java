package globaz.ij.acor.adapter;

import globaz.globall.db.BSession;
import globaz.ij.application.IJApplication;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prononces.IJPrononce;
import globaz.prestation.acor.PRACORAdapter;
import globaz.prestation.acor.PRACORException;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Cette classe se base {@link globaz.ij.application.IJApplication#PROPERTY_ACOR_FACTORY une propriété systeme} pour
 * déterminer quelle factory instancier.
 * </p>
 * 
 * @author vre
 */
public abstract class IJAdapterFactory {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static IJAdapterFactory FACTORY;

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
    public static final synchronized IJAdapterFactory getInstance(BSession session) throws PRACORException {
        if (FACTORY == null) {
            try {
                FACTORY = (IJAdapterFactory) Class.forName(
                        session.getApplication().getProperty(IJApplication.PROPERTY_ACOR_FACTORY)).newInstance();
            } catch (Exception e) {
                throw new PRACORException("impossible de creer une instance de la factory des adapteurs", e);
            }
        }

        return FACTORY;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJAdapterFactory.
     */
    protected IJAdapterFactory() {
    }

    /**
     * Cree une instance d'un adapteur pour la base d'indemnisation donnée.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param base
     *            DOCUMENT ME!
     * @param ijCalculee
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public abstract PRACORAdapter createAdapter(BSession session, IJBaseIndemnisation base, IJIJCalculee ijCalculee)
            throws PRACORException;

    /**
     * Cree une instance d'un adapteur pour le prononce donne.
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
    public abstract PRACORAdapter createAdapter(BSession session, IJPrononce prononce) throws PRACORException;
}
