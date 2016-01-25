package ch.globaz.amal.process.repriseDecisionsTaxations.step1;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Map;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.utils.parametres.ContainerParametres;
import ch.globaz.amal.businessimpl.utils.parametres.ParametresAnnuelsProvider;
import ch.globaz.amal.process.repriseDecisionsTaxations.AMAbstractProcessJAXBReprise;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;

public class AMProcessRepriseValidation extends AMAbstractProcessJAXBReprise implements JadeProcessStepInterface,
        JadeProcessStepBeforable {
    private ContainerParametres containerParametres = null;
    private String idJob = null;

    @Override
    public void before(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        containerParametres = new ContainerParametres();
        SimpleParametreAnnuelSearch simpleParametreAnnuelSearch = new SimpleParametreAnnuelSearch();
        simpleParametreAnnuelSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        simpleParametreAnnuelSearch = AmalServiceLocator.getParametreAnnuelService()
                .search(simpleParametreAnnuelSearch);
        containerParametres.setParametresAnnuelsProvider(new ParametresAnnuelsProvider(simpleParametreAnnuelSearch));
        idJob = step.getIdExecutionProcess();
        initJaxb();
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new AMProcessRepriseDecisionsTaxationsEntityHandler(unmarshaller, idJob);
    }

}
