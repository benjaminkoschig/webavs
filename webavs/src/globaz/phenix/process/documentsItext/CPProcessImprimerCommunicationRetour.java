package globaz.phenix.process.documentsItext;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.phenix.documentsItext.CPImpressionCommunicationRetour_Doc;

/**
 * Dévalida une décision - Enlève l'état validation et remet l'idPassage à blanc Ne peut se faire que si la décision
 * n'est pas en état "facturé" ou "reprise" Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public class CPProcessImprimerCommunicationRetour extends CPImpressionCommunicationRetour_Doc {

    private static final long serialVersionUID = 1122113247609333084L;

    /**
     * Constructor for CPProcessImprimerCommunicationRetour.
     * 
     * @throws Exception
     */
    public CPProcessImprimerCommunicationRetour() throws Exception {
        super();
    }

    /**
     * Constructor for CPProcessImprimerCommunicationRetour.
     * 
     * @param parent
     * @throws FWIException
     */
    public CPProcessImprimerCommunicationRetour(BProcess parent) throws FWIException {
        super(parent);
    }

    /**
     * Constructor for CPProcessImprimerCommunicationRetour.
     * 
     * @param session
     * @throws FWIException
     */
    public CPProcessImprimerCommunicationRetour(BSession session) throws FWIException {
        super(session);
    }

}
