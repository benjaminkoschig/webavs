package ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier;

import globaz.pyxis.db.tiers.TITiers;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.AbstractPegasusBuilder;
import ch.globaz.pyxis.business.service.AdresseService;

public abstract class SingleTransfertPCAbstractBuilder extends AbstractPegasusBuilder {

    public static final ArrayList<String> listOrderAdresseTiers = new ArrayList<String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            this.add(AdresseService.CS_TYPE_COURRIER);
            this.add(AdresseService.CS_TYPE_DOMICILE);
        }
    };

    public SingleTransfertPCAbstractBuilder() {
        super();
    }

    protected List<TITiers> getTiersCopies(List<String> idTiersCopies) throws Exception {
        List<TITiers> result = new ArrayList<TITiers>();
        for (String idTiers : idTiersCopies) {
            result.add(loadTiers(idTiers));
        }
        return result;
    }

    private TITiers loadTiers(String idTiers) throws Exception {

        if (idTiers == null) {
            throw new CommonTechnicalException("the idTiers can't be null");
        }

        TITiers tiersToReturn = new TITiers();
        tiersToReturn.setId(idTiers);
        tiersToReturn.setSession(getSession());
        tiersToReturn.retrieve();

        return tiersToReturn;
    }

}