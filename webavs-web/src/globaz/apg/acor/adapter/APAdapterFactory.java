package globaz.apg.acor.adapter;

import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.globall.db.BSession;
import globaz.prestation.acor.PRACORAdapter;
import globaz.prestation.acor.PRACORException;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Factory pour les classes de generation des fichier acor.
 * </p>
 * 
 * <p>
 * Le but de cette factory est de créer des instances d'adapteurs permettant de générer des fichiers de configuration de
 * ACOR qui s'adaptent à la version de ACOR utilisée par le client.
 * </p>
 * 
 * @author vre
 * @see #getInstance(BSession)
 */
public abstract class APAdapterFactory {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static APAdapterFactory FACTORY;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * retourne une instance de factory.
     * 
     * <p>
     * recherche une propriété de l'application portant le nom {@link APApplication#PROPERTY_ACOR_FACTORY
     * APApplication#PROPERTY_ACOR_FACTORY} et retourne une instance de cette classe.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut instance
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public static final synchronized APAdapterFactory getInstance(BSession session) throws PRACORException {
        if (FACTORY == null) {
            try {
                FACTORY = (APAdapterFactory) Class.forName(
                        session.getApplication().getProperty(APApplication.PROPERTY_ACOR_FACTORY)).newInstance();
            } catch (Exception e) {
                throw new PRACORException("impossible d'instancier la factory des adapteurs", e);
            }
        }

        return FACTORY;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APAdapterFactory.
     */
    protected APAdapterFactory() {
    }

    /**
     * Cree une instance d'un adapteur pour fichiers ACOR pour le droit donne.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param droit
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public abstract PRACORAdapter createAdapter(BSession session, APDroitLAPG droit) throws PRACORException;
}
