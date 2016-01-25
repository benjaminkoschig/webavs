package globaz.corvus.acor.adapter.plat;

import globaz.corvus.acor.adapter.REAdapterFactory;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.db.BSession;
import globaz.prestation.acor.PRACORAdapter;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public class REAdapterPlatFactory extends REAdapterFactory {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REAdapterPlatFactory.
     */
    public REAdapterPlatFactory() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param session
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public PRACORAdapter createAdapter(BSession session, REDemandeRente demande) {
        return new REACORDemandeAdapter(session, demande);
    }
}
