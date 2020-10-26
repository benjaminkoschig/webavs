package ch.globaz.pegasus.businessimpl.utils.topazbuilder.decisions;

import java.util.ArrayList;
import org.apache.commons.lang.NotImplementedException;
import ch.globaz.pegasus.business.constantes.IPCCatalogueTextes;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.DecisionRefus;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.decision.DecisionBuilder;
import ch.globaz.pegasus.businessimpl.services.models.decision.DACPublishHandler;
import ch.globaz.topaz.datajuicer.DocumentData;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class DecisionRefusBuilder extends AbstractDecisionBuilder implements DecisionBuilder {

    private DecisionRefus decisionRefus = null;
    private JadeUser gestionnaire = null;

    public JadePrintDocumentContainer build(ArrayList<String> decisionsId, String mailGest, String dateDoc,
            String persref, Boolean forftp, Boolean allowCopie, Boolean forGed) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {
        throw new NotImplementedException();
    }

    @Override
    public void build(DACPublishHandler handler) throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, Exception {
        throw new NotImplementedException();
    }

    public DocumentData build(String idDecision) throws Exception, DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Chargement décsision
        decisionRefus = PegasusServiceLocator.getDecisionRefusService().read(idDecision);
        // Chargement jadeUser
        gestionnaire = getSession()
                .getApplication()
                ._getSecurityManager()
                .getUserForVisa(getSession(),
                        decisionRefus.getDecisionHeader().getSimpleDecisionHeader().getPreparationPar());
        // Donnees document
        DocumentData data = new DocumentData();
        data = buildHeader(data);
        // liaison avec templarte dans topaz-config.xml
        data.addData(IPCCatalogueTextes.STR_ID_PROCESS, IPCCatalogueTextes.PROCESS_DECISION_REFUS);

        data.addData("DATE_ET_LIEU", "Le Noirmont, le 11.02.2011");
        // data.addData("NOM_COLLABORATEUR", "FUCKING BASTARD");
        // data.addData("NOM_COLLABORATEUR", document.getTextes(1).getTexte(1).getDescription());
        // this.data = new DocumentData();

        return data;
    }

    @Override
    public void buildDecisionsForFtp(DACPublishHandler handler) throws Exception, DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        throw new NotImplementedException();
    }

    @Override
    public void buildForFtpValidation(DACPublishHandler handler) throws Exception, DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        throw new UnsupportedOperationException("operation not implemented for decision de refus");

    }
    @Override
    public void buildDecisionForAdaptation(DACPublishHandler handler) throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, Exception {
        throw new NotImplementedException();
    }
    private DocumentData buildHeader(DocumentData data) {

        // Prépartion des données
        String nomCollabo = gestionnaire.getFirstname() + " " + gestionnaire.getLastname();
        String tel = gestionnaire.getPhone();// ).this.decisionRefus.getDecisionHeader().getSimpleDecisionHeader().getPreparationPar());
        data.addData("NOM_COLLABORATEUR", nomCollabo);
        data.addData("TEL_COLLABORATEUR", tel);
        data.addData("SERVICE_COLLABORATEUR", gestionnaire.getDepartment());
        data.addData("EMAIL_COLLABORATEUR", gestionnaire.getEmail());
        data.addData("DATE_ET_LIEU", "");

        data.addData("LIBELLE", "");
        data.addData("VALEUR", "");

        data.addData("IS_COPIE", "");
        data.addData("HEADER_RECOMMANDE", "");
        data.addData("HEADER_CONFIDENTIEL", "");
        data.addData("ADRESSE", "");

        return data;
    }

}
