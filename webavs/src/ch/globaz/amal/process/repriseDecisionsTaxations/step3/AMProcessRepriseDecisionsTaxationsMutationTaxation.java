package ch.globaz.amal.process.repriseDecisionsTaxations.step3;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Map;
import ch.globaz.amal.business.exceptions.models.parametreannuel.ParametreAnnuelException;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.utils.parametres.ContainerParametres;
import ch.globaz.amal.businessimpl.utils.parametres.ParametresAnnuelsProvider;
import ch.globaz.amal.process.repriseDecisionsTaxations.AMAbstractProcessJAXBReprise;
import ch.globaz.amal.process.repriseDecisionsTaxations.AMProcessRepriseDecisionsTaxationsEnum;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepEnable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;

public class AMProcessRepriseDecisionsTaxationsMutationTaxation extends AMAbstractProcessJAXBReprise implements
        JadeProcessStepInterface, JadeProcessStepBeforable, JadeProcessStepAfterable, JadeProcessStepEnable {
    private ContainerParametres containerParametres = null;
    private String idJob = null;

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        containerParametres = null;
        unmarshaller = null;
        AMAbstractProcessJAXBReprise.context = null;
        clearProps();
        JadeThread.logClear();
    }

    @Override
    public void before(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        loadParameterContainer();
        idJob = step.getIdExecutionProcess();
        initJaxb();
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new AMProcessRepriseDecisionsTaxationsEntityHandler(containerParametres, unmarshaller, idJob);
    }

    @Override
    public Boolean isEnabled(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        if (!map.containsKey(AMProcessRepriseDecisionsTaxationsEnum.IS_REPRISE_ADRESSE)) {
            return true;
        } else {
            return false;
        }
    }

    private void loadParameterContainer() throws ParametreAnnuelException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        containerParametres = new ContainerParametres();
        SimpleParametreAnnuelSearch simpleParametreAnnuelSearch = new SimpleParametreAnnuelSearch();
        simpleParametreAnnuelSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        simpleParametreAnnuelSearch = AmalServiceLocator.getParametreAnnuelService()
                .search(simpleParametreAnnuelSearch);
        containerParametres.setParametresAnnuelsProvider(new ParametresAnnuelsProvider(simpleParametreAnnuelSearch));

    }

}
