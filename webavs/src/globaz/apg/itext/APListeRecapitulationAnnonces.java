package globaz.apg.itext;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;

/**
 * Générateur de liste récapitulative des annonces RAPG (après septembre 2012)
 * 
 * @author VRE
 * @author PBA
 */
public class APListeRecapitulationAnnonces extends APAbstractListeRecapitulationAnnonces {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public APListeRecapitulationAnnonces() {
        super();
    }

    public APListeRecapitulationAnnonces(BProcess parent) throws FWIException {
        super(parent);
    }

    public APListeRecapitulationAnnonces(BSession session) throws FWIException {
        super(session);
    }

    @Override
    protected APAbstractRecapitulationAnnonceAdapter getAdapter(BSession session, String forMoisAnneeCommptable) {
        return new APRecapitulationAnnonceAdapter(session, forMoisAnneeCommptable);
    }
}
