package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import globaz.jade.client.util.JadeDateUtil;
import ch.gdk_cds.xmlns.pv_5201_000201._3.Message;
import ch.gdk_cds.xmlns.pv_common._3.DecreeType;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexReceptionOnlyException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;

/**
 * Gestionnaire de réception pour les annonces 'Interruption'
 * 
 * @author cbu
 * @version XSD _1
 */
public class AnnonceStopHandler extends AnnonceHandlerAbstract {
    private Message stop;

    public AnnonceStopHandler(Message stop) {
        this.stop = stop;
    }

    @Override
    public SimpleAnnonceSedex execute() throws AnnonceSedexReceptionOnlyException {
        throw new AnnonceSedexReceptionOnlyException("AnnonceStopHandler isn't a reception class !");
    }

    @Override
    public StringBuffer getDetailsAnnonce() {
        StringBuffer stringBufferInfos = new StringBuffer();

        DecreeType decreeType = stop.getContent().getStop().getDecree();
        stringBufferInfos.append("<strong>Informations interruption : </strong></br>");
        String stopMonthDate = AMSedexRPUtil.getDateXMLToString(stop.getContent().getStop().getStopMonth());
        String startMonthDate = AMSedexRPUtil.getDateXMLToString(decreeType.getBeginMonth());
        String type = "écourtement";
        if (JadeDateUtil.isDateMonthYearBefore(stopMonthDate, startMonthDate)) {
            type = "annulation";
        }
        stringBufferInfos.append("Dernier mois : " + stopMonthDate + " (" + type + ")</br>");
        stringBufferInfos.append("</br>");
        stringBufferInfos.append("***********************************************</br>");

        stringBufferInfos.append(getDecreeTypeInfos(decreeType));

        return stringBufferInfos;
    }

}
