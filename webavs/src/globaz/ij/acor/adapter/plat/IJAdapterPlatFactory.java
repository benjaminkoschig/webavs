package globaz.ij.acor.adapter.plat;

import globaz.globall.db.BSession;
import globaz.ij.acor.adapter.IJAdapterFactory;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prononces.IJPrononce;
import globaz.prestation.acor.PRACORAdapter;
import globaz.prestation.acor.PRACORException;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJAdapterPlatFactory extends IJAdapterFactory {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJAdapterPlatFactory.
     */
    public IJAdapterPlatFactory() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
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
    @Override
    public PRACORAdapter createAdapter(BSession session, IJBaseIndemnisation base, IJIJCalculee ijCalculee)
            throws PRACORException {
        try {
            return new IJACORBaseIndemnisationAdapter(session, base.loadPrononce(null), base, ijCalculee);
        } catch (Exception e) {
            throw new PRACORException("impossible de charger le prononce", e);
        }
    }

    /**
     * @param session
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public PRACORAdapter createAdapter(BSession session, IJPrononce prononce) {
        return new IJACORPrononceAdapter(session, prononce);
    }
}
