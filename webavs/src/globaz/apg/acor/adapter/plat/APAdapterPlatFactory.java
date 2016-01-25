package globaz.apg.acor.adapter.plat;

import globaz.apg.acor.adapter.APAdapterFactory;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.globall.db.BSession;
import globaz.prestation.acor.PRACORAdapter;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APAdapterPlatFactory extends APAdapterFactory {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APAdapterPlatFactory.
     */
    public APAdapterPlatFactory() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne une instance d'un adapteur correct pour un droit APG ou mat.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param droit
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public PRACORAdapter createAdapter(BSession session, APDroitLAPG droit) {
        if (droit instanceof APDroitMaternite) {
            return new APACORDroitMatAdapter(session, (APDroitMaternite) droit);
        } else {
            return new APACORDroitAPGAdapter(session, (APDroitAPG) droit);
        }
    }
}
