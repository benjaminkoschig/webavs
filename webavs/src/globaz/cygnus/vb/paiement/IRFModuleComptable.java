package globaz.cygnus.vb.paiement;

import globaz.cygnus.services.comptabilite.RFComptabiliserDecisionService;
import globaz.cygnus.services.comptabilite.RFPrestationData;
import globaz.cygnus.utils.RFLogToDB;
import globaz.globall.api.BIMessageLog;
import java.util.List;

/**
 * @author fha
 * @author vch
 */
public interface IRFModuleComptable {

    public BIMessageLog doTraitement(RFComptabiliserDecisionService process,
            List<RFPrestationData> prestationsMemeAdresseDePaiementList, RFLogToDB rfmLogger, boolean isLotAVASAD)
            throws Exception;

    public int getPriority();

    @Override
    public String toString();

}