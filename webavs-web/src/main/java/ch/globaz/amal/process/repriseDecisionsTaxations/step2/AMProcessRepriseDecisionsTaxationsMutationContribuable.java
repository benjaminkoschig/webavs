package ch.globaz.amal.process.repriseDecisionsTaxations.step2;

import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.Map;
import ch.globaz.amal.business.exceptions.models.parametreannuel.ParametreAnnuelException;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.utils.parametres.ContainerParametres;
import ch.globaz.amal.businessimpl.utils.parametres.ParametresAnnuelsProvider;
import ch.globaz.amal.process.repriseDecisionsTaxations.AMAbstractProcessJAXBReprise;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;

public class AMProcessRepriseDecisionsTaxationsMutationContribuable extends AMAbstractProcessJAXBReprise implements
        JadeProcessStepInterface, JadeProcessStepBeforable, JadeProcessStepAfterable {
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

        if (AMProcessRepriseDecisionsTaxationsEntityHandler.isRepriseAdresses) {
            String message = "";

            message += "********* Aucune modification *********\n";
            message += "Courrier : O / Domicile : O : "
                    + AMProcessRepriseDecisionsTaxationsEntityHandler._courrierOuiDomicileOui + "\n";
            message += "Courrier : O / Domicile : N : "
                    + AMProcessRepriseDecisionsTaxationsEntityHandler._courrierOuiDomicileNon + "\n";
            message += "***************************************\n\n";
            message += "********* Adresse Courrier crées *********\n";
            message += "Courrier : N / Domicile : O / CP PA : "
                    + AMProcessRepriseDecisionsTaxationsEntityHandler._courrierNonDomicileOuiCPPA + "\n";
            message += "Courrier : N / Domicile : N / CP PA : "
                    + AMProcessRepriseDecisionsTaxationsEntityHandler._courrierNonDomicileNonCPPA + "\n";
            message += "***************************************\n\n";
            message += "********* Adresse Domicile mise a jour *********\n";
            message += "Courrier : N / Domicile : O : "
                    + AMProcessRepriseDecisionsTaxationsEntityHandler._courrierNonDomicileOui + "\n";
            message += "Adresses déjà à jour : " + AMProcessRepriseDecisionsTaxationsEntityHandler._adresseUpToDate
                    + "\n";
            message += "***************************************\n\n";
            message += "********* Adresse Domicile crées *********\n";
            message += "Courrier : N / Domicile : N : "
                    + AMProcessRepriseDecisionsTaxationsEntityHandler._courrierNonDomicileNon + "\n";
            message += "***************************************\n\n";

            try {
                JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(),
                        "Stats reprise adresses", message, null);
            } catch (Exception e) {
                // Etant donné que l'envoi de cet email n'a qu'un but informatif, si l'envoi ne fonctionne pas, ce n'est
                // pas grave
            }
        }
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
