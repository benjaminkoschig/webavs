package globaz.phenix.process.documentsItext;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.phenix.documentsItext.CPImpressionCommunication_Doc;

/**
 * D�valida une d�cision - Enl�ve l'�tat validation et remet l'idPassage � blanc Ne peut se faire que si la d�cision
 * n'est pas en �tat "factur�" ou "reprise" Date de cr�ation : (25.02.2002 13:41:13)
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
