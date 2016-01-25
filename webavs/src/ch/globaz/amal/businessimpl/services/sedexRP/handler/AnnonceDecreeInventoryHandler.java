package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import ch.gdk_cds.xmlns.pv_5203_000501._3.Message;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexReceptionOnlyException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;

/**
 * Gestionnaire de réception pour les annonces 'Etat des décisions'
 * 
 * @author cbu
 * @version XSD _1
 */
public class AnnonceDecreeInventoryHandler extends AnnonceHandlerAbstract {
    private Message decreeInventory;

    public AnnonceDecreeInventoryHandler(Message decreeInventory) {
        this.decreeInventory = decreeInventory;
    }

    @Override
    public SimpleAnnonceSedex execute() throws AnnonceSedexReceptionOnlyException {
        throw new AnnonceSedexReceptionOnlyException("AnnonceDecreeInventoryHandler isn't a reception class !");
    }

    @Override
    public StringBuffer getDetailsAnnonce() {
        StringBuffer stringBufferInfos = new StringBuffer();

        stringBufferInfos.append("<strong>Données de l'annonce 'Etat des décisions' :</strong></br>");
        stringBufferInfos.append("<br>");
        stringBufferInfos.append("Mois de référence : "
                + AMSedexRPUtil
                        .getDateXMLToString(decreeInventory.getContent().getDecreeInventory().getInventoryDate())
                + "<br>");
        stringBufferInfos.append("Mois de début : "
                + AMSedexRPUtil.getDateXMLToString(decreeInventory.getContent().getDecreeInventory().getBeginDate())
                + "<br>");
        stringBufferInfos.append("Mois de fin : "
                + AMSedexRPUtil.getDateXMLToString(decreeInventory.getContent().getDecreeInventory().getEndDate())
                + "<br>");
        stringBufferInfos.append("<br>");
        stringBufferInfos.append("***********************************************</br>");

        return stringBufferInfos;
    }

}
