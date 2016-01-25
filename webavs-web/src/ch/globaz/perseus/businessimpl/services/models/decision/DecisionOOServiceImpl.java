package ch.globaz.perseus.businessimpl.services.models.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.perseus.business.exceptions.models.creancier.CreancierException;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.creancier.Creancier;
import ch.globaz.perseus.business.models.creancier.CreancierSearchModel;
import ch.globaz.perseus.business.models.decision.AnnexeDecision;
import ch.globaz.perseus.business.models.decision.AnnexeDecisionSearchModel;
import ch.globaz.perseus.business.models.decision.CopieDecision;
import ch.globaz.perseus.business.models.decision.CopieDecisionSearchModel;
import ch.globaz.perseus.business.models.decision.DecisionOO;
import ch.globaz.perseus.business.models.decision.DecisionOOSearchModel;
import ch.globaz.perseus.business.models.situationfamille.Enfant;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.decision.DecisionOOService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class DecisionOOServiceImpl extends PerseusAbstractServiceImpl implements DecisionOOService {

    @Override
    public int count(DecisionOOSearchModel search) throws DecisionException, JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to count DecisionOO, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public DecisionOO create(DecisionOO decisionOO) throws JadePersistenceException, DecisionException {
        if (decisionOO == null) {
            throw new DecisionException("Unable to create decisionOO, the given model is null!");
        }

        return decisionOO;
    }

    @Override
    public DecisionOO delete(DecisionOO decisionOO) throws JadePersistenceException, DecisionException {
        if (decisionOO == null) {
            throw new DecisionException("Unable to delete a decisionOO, the model passed is null!");
        }
        return null;
    }

    private DecisionOO loadAnnexeDecision(DecisionOO decisionOO) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        AnnexeDecisionSearchModel searchModel = new AnnexeDecisionSearchModel();
        searchModel.setForIdDecision(decisionOO.getSimpleDecision().getId());
        searchModel = PerseusServiceLocator.getAnnexeDecisionService().search(searchModel);
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            AnnexeDecision annexeDecision = (AnnexeDecision) model;
            decisionOO.getListeAnnexe().add(annexeDecision);
        }
        return decisionOO;
    }

    private DecisionOO loadCopieDecision(DecisionOO decisionOO) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        CopieDecisionSearchModel searchModel = new CopieDecisionSearchModel();
        searchModel.setForIdDecision(decisionOO.getSimpleDecision().getId());
        searchModel = PerseusServiceLocator.getCopieDecisionService().search(searchModel);
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            CopieDecision copieDecision = (CopieDecision) model;
            decisionOO.getListeCopie().add(copieDecision);
        }
        return decisionOO;
    }

    private DecisionOO loadCreancier(DecisionOO decisionOO) throws CreancierException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        CreancierSearchModel searchModel = new CreancierSearchModel();
        searchModel.setForIdDemande(decisionOO.getDemande().getId());
        searchModel = PerseusServiceLocator.getCreancierService().search(searchModel);
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            Creancier creancier = (Creancier) model;
            decisionOO.getCreancier().add(creancier);
        }

        return decisionOO;
    }

    private DecisionOO loadEnfants(DecisionOO decisionOO) throws SituationFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        ArrayList<Enfant> listEnfants = new ArrayList<Enfant>();
        EnfantFamilleSearchModel enfantFamilleSearchModel = new EnfantFamilleSearchModel();
        enfantFamilleSearchModel.setForIdSituationFamiliale(decisionOO.getDemande().getSituationFamiliale().getId());
        enfantFamilleSearchModel = PerseusImplServiceLocator.getEnfantFamilleService().search(enfantFamilleSearchModel);

        for (JadeAbstractModel abstractModel : enfantFamilleSearchModel.getSearchResults()) {
            EnfantFamille enfantfamille = (EnfantFamille) abstractModel;
            decisionOO.getListeEnfants().add(enfantfamille.getEnfant());
        }
        return decisionOO;
    }

    @Override
    public DecisionOO read(String idDecision) throws JadePersistenceException, DecisionException,
            SituationFamilleException, JadeApplicationServiceNotAvailableException, CreancierException {
        if (JadeStringUtil.isEmpty(idDecision)) {
            throw new DecisionException("Unable to read a decisionOO, the id passed is null!");
        }

        DecisionOO decisionOO = new DecisionOO();
        decisionOO.setId(idDecision);
        decisionOO = (DecisionOO) JadePersistenceManager.read(decisionOO);

        decisionOO = loadEnfants(decisionOO);
        decisionOO = loadCreancier(decisionOO);
        decisionOO = loadCopieDecision(decisionOO);
        decisionOO = loadAnnexeDecision(decisionOO);

        return decisionOO;
    }

    @Override
    public DecisionOOSearchModel search(DecisionOOSearchModel searchModel) throws JadePersistenceException,
            DecisionException {
        if (searchModel == null) {
            throw new DecisionException("Unable to search a decisionOO, the search model passed is null!");
        }

        return (DecisionOOSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public DecisionOO update(DecisionOO decisionOO) throws JadePersistenceException, DecisionException {
        if (decisionOO == null) {
            throw new DecisionException("Unable to update decisionOO, the given model is null!");
        }
        return null;
    }

}
