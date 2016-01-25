/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.situationfamille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.situationfamille.Enfant;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.business.models.situationfamille.SimpleEnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.SimpleEnfantFamilleSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleAddCheckMessage;
import ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleService;
import ch.globaz.perseus.businessimpl.checkers.situationfamille.SimpleEnfantFamilleChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import com.google.common.collect.Lists;

/**
 * @author DDE
 * 
 */
public class EnfantFamilleServiceImpl extends PerseusAbstractServiceImpl implements EnfantFamilleService {

    @Override
    public int count(EnfantFamilleSearchModel search) throws JadePersistenceException, SituationFamilleException {
        if (search == null) {
            throw new SituationFamilleException("Unable de count enfantFamille, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public EnfantFamilleAddCheckMessage checkForAjaxAdd(EnfantFamille enfantFamille) throws Exception {

        try {
            create(enfantFamille);
        } catch (Exception e) {
            JadeThread.rollbackSession();
            return EnfantFamilleAddCheckMessage.exception(e.getStackTrace().toString());
        }
        // on rollback la session dans tous les cas
        JadeThread.rollbackSession();

        if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            return EnfantFamilleAddCheckMessage.error();

        } else if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            return EnfantFamilleAddCheckMessage.warn();
        } else {
            return EnfantFamilleAddCheckMessage.ok();
        }

    }

    @Override
    public EnfantFamille createAjax(EnfantFamille enfantFamille) throws Exception {
        if (enfantFamille == null) {
            throw new SituationFamilleException("Unable de create enfantFamille, the model passed is null!");
        }

        try {
            Enfant enfant = enfantFamille.getEnfant();
            enfantFamille.setEnfant(PerseusServiceLocator.getEnfantService().create(enfant));
            enfantFamille.getSimpleEnfantFamille().setIdEnfant(enfant.getId());

            SimpleEnfantFamille simpleEnfantFamille = enfantFamille.getSimpleEnfantFamille();
            enfantFamille.setSimpleEnfantFamille(PerseusImplServiceLocator.getSimpleEnfantFamilleService().create(
                    simpleEnfantFamille));

            List<JadeBusinessMessage> messagesTemp = new ArrayList<JadeBusinessMessage>();

            if (null != JadeThread.logMessages()) {
                // on vide le smessages de type warn, déjà traités par le premier appel ajax de check
                for (JadeBusinessMessage msg : JadeThread.logMessages()) {
                    if (msg.getLevel() != JadeBusinessMessageLevels.WARN) {
                        messagesTemp.add(msg);
                    }
                }

                JadeThread.logClear();

                // on rajoute le smessages d'erreurs dans le JadeThread
                for (JadeBusinessMessage msg : messagesTemp) {
                    JadeThread.logError(msg.getSource(), msg.getMessageId());
                }

            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not avaiable - " + e.getMessage());
        }

        // Update de la demande
        updateDemandeCalculable(enfantFamille);

        return enfantFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleService#create(ch.globaz.perseus.business
     * .models.situationfamille.EnfantFamille)
     */
    @Override
    public EnfantFamille create(EnfantFamille enfantFamille) throws SituationFamilleException, JadePersistenceException {
        if (enfantFamille == null) {
            throw new SituationFamilleException("Unable de create enfantFamille, the model passed is null!");
        }

        try {
            Enfant enfant = enfantFamille.getEnfant();
            enfantFamille.setEnfant(PerseusServiceLocator.getEnfantService().create(enfant));
            enfantFamille.getSimpleEnfantFamille().setIdEnfant(enfant.getId());

            SimpleEnfantFamille simpleEnfantFamille = enfantFamille.getSimpleEnfantFamille();
            enfantFamille.setSimpleEnfantFamille(PerseusImplServiceLocator.getSimpleEnfantFamilleService().create(
                    simpleEnfantFamille));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not avaiable - " + e.getMessage());
        }

        // Update de la demande
        updateDemandeCalculable(enfantFamille);

        return enfantFamille;
    }

    // @Override
    // public SimpleEnfantFamilleWarningWrapper checkEnfantNotInAntherFamille(EnfantFamille enfantFamille)
    // throws JadeNoBusinessLogSessionError, Exception {
    //
    // // Le bloc suivant va faire potentiellemt deux insertions db
    // Enfant enfant = enfantFamille.getEnfant();
    // enfantFamille.setEnfant(PerseusServiceLocator.getEnfantService().create(enfant));
    // enfantFamille.getSimpleEnfantFamille().setIdEnfant(enfant.getId());
    // // fin des potentiels insertions
    //
    // SimpleEnfantFamille simpleEnfantFamille = enfantFamille.getSimpleEnfantFamille();
    //
    // // on check que l'enfant ne soit pas en garde (partagee, exclu ou les deux)
    // SimpleEnfantFamilleChecker.checkIntegrityForEnfantFamilleNotInAnotherFamille(simpleEnfantFamille);
    //
    // JadeBusinessMessage msg = getMessageFromEnfantGardeProblems();
    //
    // // on ne traite pas de message d'erreurs, uniquement les warn
    // if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR) && msg != null) {
    //
    // String messageWarn = JadeThread.getMessage(msg.getMessageId());
    //
    // JadeThread.rollbackSession();
    //
    // return new SimpleEnfantFamilleWarningWrapper(messageWarn);
    //
    // } else {
    //
    // return new SimpleEnfantFamilleWarningWrapper();
    // }
    //
    // }

    // private JadeBusinessMessage getMessageFromEnfantGardeProblems() {
    //
    // if (null == JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
    // return null;
    // }
    //
    // for (JadeBusinessMessage msg : JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
    // if (msg.getMessageId().equals(SimpleEnfantFamilleChecker.ENFANT_GARDE_EXCLUSIVE_WARN_KEY)
    // || msg.getMessageId().equals(SimpleEnfantFamilleChecker.ENFANT_GARDE_PARTAGEE_WARN_KEY)
    // || msg.getMessageId().equals(SimpleEnfantFamilleChecker.ENFANT_GARDE_PARTAGEE_ET_EXCLU_WARN_KEY)) {
    // return msg;
    //
    // }
    // }
    //
    // return null;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleService#createForRP(ch.globaz.perseus
     * .business.models.situationfamille.EnfantFamille)
     */
    @Override
    public EnfantFamille createForRP(EnfantFamille enfantFamille) throws JadePersistenceException,
            SituationFamilleException {
        if (enfantFamille == null) {
            throw new SituationFamilleException("Unable de create enfantFamille, the model passed is null!");
        }

        try {
            Enfant enfant = enfantFamille.getEnfant();
            enfantFamille.setEnfant(PerseusServiceLocator.getEnfantService().create(enfant));
            enfantFamille.getSimpleEnfantFamille().setIdEnfant(enfant.getId());

            SimpleEnfantFamille simpleEnfantFamille = enfantFamille.getSimpleEnfantFamille();
            enfantFamille.setSimpleEnfantFamille(PerseusImplServiceLocator.getSimpleEnfantFamilleService().createForRP(
                    simpleEnfantFamille));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not avaiable - " + e.getMessage());
        }

        return enfantFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleService#delete(ch.globaz.perseus.business
     * .models.situationfamille.EnfantFamille)
     */
    @Override
    public EnfantFamille delete(EnfantFamille enfantFamille, String idDemande) throws Exception {
        if (enfantFamille == null) {
            throw new SituationFamilleException("Unable to delete enfantFamille, the model passed is null!");
        }
        if (JadeStringUtil.isEmpty(idDemande)) {
            throw new SituationFamilleException("Unable to delete enfantFamille, idDemande is empty !");
        }

        try {
            // Supprimer les données financières de l'enfant
            PerseusServiceLocator.getDonneeFinanciereService().deleteForDemandeAndMembreFamille(idDemande,
                    enfantFamille.getEnfant().getMembreFamille().getId());
            enfantFamille.setSimpleEnfantFamille(PerseusImplServiceLocator.getSimpleEnfantFamilleService().delete(
                    enfantFamille.getSimpleEnfantFamille()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not avaiable - " + e.getMessage());
        } catch (DonneesFinancieresException e) {
            throw new SituationFamilleException("DonneeFinanciereException during enfantFamille delete : "
                    + e.getMessage(), e);
        }

        // Update de la demande
        updateDemandeCalculable(enfantFamille);

        return enfantFamille;
    }

    // @Override
    // public SimpleEnfantFamilleWarningWrapper checkEnfantNotInAntherFamille(SimpleEnfantFamille simpleEnfantFamille)
    // throws SituationFamilleException, DemandeException, JadeApplicationServiceNotAvailableException,
    // JadePersistenceException, JadeNoBusinessLogSessionError {
    //
    // SimpleEnfantFamilleChecker.checkIntegrityForEnfantFamilleNotInAnotherFamille(simpleEnfantFamille);
    //
    // if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
    //
    // String messageWarn = JadeThread.getMessage(SimpleEnfantFamilleChecker.ENFANT_GARDE_EXCLUSIVE_WARN_KEY);
    //
    // if (null != messageWarn) {
    // return new SimpleEnfantFamilleWarningWrapper(
    // JadeThread.getMessage(SimpleEnfantFamilleChecker.ENFANT_GARDE_EXCLUSIVE_WARN_KEY));
    // }
    //
    // }
    //
    // return new SimpleEnfantFamilleWarningWrapper();
    //
    // }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleService#deleteForRP(ch.globaz.perseus
     * .business.models.situationfamille.EnfantFamille)
     */
    @Override
    public EnfantFamille deleteForRP(EnfantFamille enfantFamille) throws JadePersistenceException,
            SituationFamilleException {
        if (enfantFamille == null) {
            throw new SituationFamilleException("Unable to delete enfantFamille, the model passed is null!");
        }

        try {
            enfantFamille.setSimpleEnfantFamille(PerseusImplServiceLocator.getSimpleEnfantFamilleService().delete(
                    enfantFamille.getSimpleEnfantFamille()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not avaiable - " + e.getMessage());
        }

        return enfantFamille;
    }

    @Override
    public int deleteForSituationFamiliale(String idSituationFamiliale) throws JadePersistenceException,
            SituationFamilleException {
        if (JadeStringUtil.isEmpty(idSituationFamiliale)) {
            throw new SituationFamilleException(
                    "Unable to delete simpleEnfantFamille for SiutationFamiliale, the id passed is empty!");
        }
        SimpleEnfantFamilleSearchModel simpleEnfantFamilleSearchModel = new SimpleEnfantFamilleSearchModel();
        simpleEnfantFamilleSearchModel.setForIdSituationFamiliale(idSituationFamiliale);

        try {
            return PerseusImplServiceLocator.getSimpleEnfantFamilleService().delete(simpleEnfantFamilleSearchModel);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not avaiable - " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleService#read(java.lang.String)
     */
    @Override
    public EnfantFamille read(String idEnfantFamille) throws JadePersistenceException, SituationFamilleException {
        if (JadeStringUtil.isEmpty(idEnfantFamille)) {
            throw new SituationFamilleException("Unable to read enfantFamille, the id passed is null!");
        }
        EnfantFamille enfantFamille = new EnfantFamille();
        enfantFamille.setId(idEnfantFamille);

        return (EnfantFamille) JadePersistenceManager.read(enfantFamille);
    }

    @Override
    public EnfantFamilleSearchModel search(EnfantFamilleSearchModel search) throws JadePersistenceException,
            SituationFamilleException {
        if (search == null) {
            throw new SituationFamilleException("Unable de search enfantFamille, the model passed is null!");
        }

        return (EnfantFamilleSearchModel) JadePersistenceManager.search(search);
    }

    @Override
    public List<EnfantFamille> findEnfantByIdSF(String idSf) throws JadePersistenceException, SituationFamilleException {

        EnfantFamilleSearchModel search = new EnfantFamilleSearchModel();
        search.setForIdSituationFamiliale(idSf);
        search = search(search);

        List<EnfantFamille> listeRetour = Lists.newArrayList();

        for (JadeAbstractModel enfant : search.getSearchResults()) {
            listeRetour.add((EnfantFamille) enfant);
        }

        return listeRetour;
    }

    @Override
    public EnfantFamilleAddCheckMessage checkEnfantSituationFamillialeCoherence(Demande demande)
            throws SituationFamilleException, JadePersistenceException, DemandeException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError {

        List<EnfantFamille> enfantsToCheck = findEnfantByIdSF(demande.getSituationFamiliale()
                .getSimpleSituationFamiliale().getIdSituationFamilliale());

        for (EnfantFamille enfant : enfantsToCheck) {
            SimpleEnfantFamilleChecker.checkInegrityForCopie(enfant.getSimpleEnfantFamille(), demande);
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            return EnfantFamilleAddCheckMessage.warnForCopie();
        } else {
            return EnfantFamilleAddCheckMessage.ok();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleService#update(ch.globaz.perseus.business
     * .models.situationfamille.EnfantFamille)
     */
    @Override
    public EnfantFamille update(EnfantFamille enfantFamille) throws Exception {
        if (enfantFamille == null) {
            throw new SituationFamilleException("Unable to update enfantFamille, the model passed is null!");
        }

        try {
            // Update du membre famille puisque le cas de l'enfant à l'ai pourrait changer
            enfantFamille
                    .getEnfant()
                    .getMembreFamille()
                    .setSimpleMembreFamille(
                            PerseusImplServiceLocator.getSimpleMembreFamilleService().update(
                                    enfantFamille.getEnfant().getMembreFamille().getSimpleMembreFamille()));
            // Update de l'enfant famille
            enfantFamille.setSimpleEnfantFamille(PerseusImplServiceLocator.getSimpleEnfantFamilleService().update(
                    enfantFamille.getSimpleEnfantFamille()));

            // Update de la demande
            updateDemandeCalculable(enfantFamille);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not avaiable - " + e.getMessage());
        }

        return enfantFamille;
    }

    @Override
    public EnfantFamilleAddCheckMessage updateAjax(EnfantFamille enfantFamille) throws Exception {
        if (enfantFamille == null) {
            throw new SituationFamilleException("Unable to update enfantFamille, the model passed is null!");
        }

        try {
            // Update du membre famille puisque le cas de l'enfant à l'ai pourrait changer
            enfantFamille
                    .getEnfant()
                    .getMembreFamille()
                    .setSimpleMembreFamille(
                            PerseusImplServiceLocator.getSimpleMembreFamilleService().update(
                                    enfantFamille.getEnfant().getMembreFamille().getSimpleMembreFamille()));
            // Update de l'enfant famille
            enfantFamille.setSimpleEnfantFamille(PerseusImplServiceLocator.getSimpleEnfantFamilleService().update(
                    enfantFamille.getSimpleEnfantFamille()));

            // Update de la demande
            updateDemandeCalculable(enfantFamille);
        } catch (Exception e) {
            JadeThread.rollbackSession();
            return EnfantFamilleAddCheckMessage.exception(e.getStackTrace().toString());
        }
        // on rollback la session dans tous les cas
        // JadeThread.rollbackSession();

        if (JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
            return EnfantFamilleAddCheckMessage.error();

        } else if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            return EnfantFamilleAddCheckMessage.warn();
        } else {
            return EnfantFamilleAddCheckMessage.ok();
        }

    }

    /**
     * Fait un update sur la demande afin de modifier l'état calculable de la demande
     * 
     * @throws JadePersistenceException
     * @throws SituationFamilleException
     * 
     * @throws Exception
     */
    private void updateDemandeCalculable(EnfantFamille enfantFamille) throws JadePersistenceException,
            SituationFamilleException {
        try {
            DemandeSearchModel searchModel = new DemandeSearchModel();
            searchModel.setForIdSituationFamiliale(enfantFamille.getSimpleEnfantFamille().getIdSituationFamiliale());
            searchModel = PerseusServiceLocator.getDemandeService().search(searchModel);
            if (searchModel.getSize() == 1) {
                Demande demande = (Demande) searchModel.getSearchResults()[0];
                // Un check pour le champ calculable est fait à l'update
                // Cela permet aussi de nettoyer la demande
                demande = PerseusServiceLocator.getDemandeService().updateAndClean(demande, false);
            } else {
                // Il s'agit d'une rente pont on ne fait rien
                // throw new SituationFamilleException(
                // "Unable to update Demande for calculable check, demande cannot be found");
            }
        } catch (DemandeException e) {
            throw new SituationFamilleException("Demande exception during calculable check : " + e.getMessage(), e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SituationFamilleException("Service not available : " + e.getMessage(), e);
        }

    }

}
