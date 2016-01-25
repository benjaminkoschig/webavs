package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import ch.gdk_cds.xmlns.pv_5203_000501._3.Message;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexReceptionOnlyException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;

/**
 * Gestionnaire de r�ception pour les annonces 'Etat des d�cisions'
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

        stringBufferInfos.append("<strong>Donn�es de l'annonce 'Etat des d�cisions' :</strong></br>");
        stringBufferInfos.append("<br>");
        stringBufferInfos.append("Mois de r�f�rence : "
                + AMSedexRPUtil
                        .getDateXMLToString(decreeInventory.getContent().getDecreeInventory().getInventoryDate())
                + "<br>");
        stringBufferInfos.append("Mois de d�but : "
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
