package globaz.apg.itext;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;

/**
 * Générateur de liste récapitulative des anciennes annonces APG (avant septembre 2012)
 * 
 * @author LGA
 * @author PBA
 */
public class APListeRecapitulationAnnoncesHermes extends APAbstractListeRecapitulationAnnonces {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public APListeRecapitulationAnnoncesHermes() {
        super();
    }

    public APListeRecapitulationAnnoncesHermes(BProcess parent) throws FWIException {
        super(parent);
    }

    public APListeRecapitulationAnnoncesHermes(BSession session) throws FWIException {
        super(session);
    }

    @Override
    protected APAbstractRecapitulationAnnonceAdapter getAdapter(BSession session, String forMoisAnneeCommptable) {
        return new APRecapitulationAnnonceAdapterHermes(session, forMoisAnneeCommptable);
    }
}
