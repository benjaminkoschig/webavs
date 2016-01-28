package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import ch.gdk_cds.xmlns.pv_5201_000101._3.Message;
import ch.gdk_cds.xmlns.pv_common._3.DecreeType;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexReceptionOnlyException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;

/**
 * Gestionnaire de r�ception pour les annonces 'Nouvelle d�cision'
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
        stringBufferInfos.append("Annonce : 'Nouvelle d�cision'</br></br>");
        DecreeType decreeType = decree.getContent().getDecree();
        stringBufferInfos.append(getDecreeTypeInfos(decreeType));

        return stringBufferInfos;
    }

}
