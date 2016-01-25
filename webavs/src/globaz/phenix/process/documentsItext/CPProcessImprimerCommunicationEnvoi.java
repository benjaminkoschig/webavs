package globaz.phenix.process.documentsItext;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.phenix.documentsItext.CPImpressionCommunication_Doc;

/**
 * Dévalida une décision - Enlève l'état validation et remet l'idPassage à blanc Ne peut se faire que si la décision
 * n'est pas en état "facturé" ou "reprise" Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public class CPProcessImprimerCommunicationEnvoi extends CPImpressionCommunication_Doc {

    private static final long serialVersionUID = -7629958420368651529L;

    /**
     * Constructor for CPProcessImprimerCommunicationEnvoi.
     * 
     * @throws Exception
     */
    public CPProcessImprimerCommunicationEnvoi() throws Exception {
        super();
    }

    /**
     * Constructor for CPProcessImprimerCommunicationEnvoi.
     * 
     * @param parent
     * @throws FWIException
     */
    public CPProcessImprimerCommunicationEnvoi(BProcess parent) throws FWIException {
        super(parent);
    }

    /**
     * Constructor for CPProcessImprimerCommunicationEnvoi.
     * 
     * @param session
     * @throws FWIException
     */
    public CPProcessImprimerCommunicationEnvoi(BSession session) throws FWIException {
        super(session);
    }

}
