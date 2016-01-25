package ch.globaz.pegasus.process.adaptation.stepPSARente;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepEnable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.models.process.adaptation.AdaptationPsal;
import ch.globaz.pegasus.business.models.process.adaptation.AdaptationPsalSearch;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;

public class PCProcessAdaptationStep implements JadeProcessStepInterface, JadeProcessStepEnable,
        JadeProcessStepBeforable {

    Map<String, List<AdaptationPsal>> psal = null;

    @Override
    public void before(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        psal = findPsalToUpdateAndGroupByIdeDemande(map.get(PCProcessAdapationEnum.PSAL_MONTANT_ANCIEN));
    }

    private Map<String, List<AdaptationPsal>> findPsalToUpdateAndGroupByIdeDemande(String ancienMontant)
            throws JadePersistenceException {
        AdaptationPsalSearch search = new AdaptationPsalSearch();

        search.setWhereKey("forAdaptationVersioned");
        // search.setForNumeroVersion(droitACalculer.getSimpleVersionDroit().getNoVersion());
        search.setForMontantCotisationsAnnuelle(ancienMontant);
        search.setForCsEtat(IPCDemandes.CS_OCTROYE);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        List<AdaptationPsal> listPsal = PersistenceUtil.search(search, search.whichModelClass());
        Map<String, List<AdaptationPsal>> mapPsal = JadeListUtil.groupBy(listPsal, new Key<AdaptationPsal>() {
            @Override
            public String exec(AdaptationPsal e) {
                return e.getIdDemande();
            }
        });
        return mapPsal;
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PCProcessAdaptationPSALRenteEntityHandler(psal);
    }

    @Override
    public Boolean isEnabled(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        if ("true".equalsIgnoreCase(map.get(PCProcessAdapationEnum.HAS_ADAPTATION_DES_PSAL))) {
            return true;
        } else {
            return false;
        }
    }
}
