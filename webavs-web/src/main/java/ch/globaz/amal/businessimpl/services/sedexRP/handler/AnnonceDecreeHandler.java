package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import ch.gdk_cds.xmlns.pv_5201_000101._3.Message;
import ch.gdk_cds.xmlns.pv_common._3.DecreeType;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexReceptionOnlyException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;

/**
 * Gestionnaire de réception pour les annonces 'Nouvelle décision'
 * 
 * @author cbu
 * @version XSD _1
 */
public class AnnonceDecreeHandler extends AnnonceHandlerAbstract {
    private Message decree;

    public AnnonceDecreeHandler(Message decree) {
        this.decree = decree;
    }

    @Override
    public SimpleAnnonceSedex execute() throws AnnonceSedexReceptionOnlyException {
        throw new AnnonceSedexReceptionOnlyException("AnnonceDecreeHandler isn't a reception class !");
    }

    @Override
    public StringBuffer getDetailsAnnonce() {
        StringBuffer stringBufferInfos = new StringBuffer();
        stringBufferInfos.append("Annonce : 'Nouvelle décision'</br></br>");
        DecreeType decreeType = decree.getContent().getDecree();
        stringBufferInfos.append(getDecreeTypeInfos(decreeType));

        return stringBufferInfos;
    }

}
