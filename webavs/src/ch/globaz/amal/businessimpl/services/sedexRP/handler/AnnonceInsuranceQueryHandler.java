package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import ch.gdk_cds.xmlns.pv_5202_000401._3.Message;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexReceptionOnlyException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;

/**
 * Gestionnaire de réception pour les annonces 'Demande du rapport d'assurance'
 * 
 * @author cbu
 * @version XSD _1
 */
public class AnnonceInsuranceQueryHandler extends AnnonceHandlerAbstract {
    Message insuranceQuery;

    public AnnonceInsuranceQueryHandler(Message insuranceQuery) {
        this.insuranceQuery = insuranceQuery;
    }

    @Override
    public SimpleAnnonceSedex execute() throws AnnonceSedexReceptionOnlyException {
        throw new AnnonceSedexReceptionOnlyException("AnnonceInsuranceQueryHandler isn't a reception class !");
    }

    @Override
    public StringBuffer getDetailsAnnonce() {
        StringBuffer stringBufferInfos = new StringBuffer();
        stringBufferInfos.append("<strong>Données de la demande du rapport d'assurance :</strong></br>");
        stringBufferInfos.append("<br>");
        stringBufferInfos.append("Mois de début : "
                + AMSedexRPUtil.getDateXMLToString(insuranceQuery.getContent().getInsuranceQuery().getBeginMonth())
                + "<br>");
        stringBufferInfos.append("Mois de fin : "
                + AMSedexRPUtil.getDateXMLToString(insuranceQuery.getContent().getInsuranceQuery().getEndMonth())
                + "<br>");
        stringBufferInfos.append("<br>");
        stringBufferInfos.append("***********************************************</br>");

        return stringBufferInfos;
    }
}
