package ch.globaz.perseus.business.services.models.situationfamille;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;

public interface EnfantFamilleService extends JadeApplicationService {
    public int count(EnfantFamilleSearchModel search) throws JadePersistenceException, SituationFamilleException;

    public EnfantFamille create(EnfantFamille enfantFamille) throws JadePersistenceException, SituationFamilleException;

    /**
     * Ajout d'enfant dans la situation familiale pour les rentes-ponts (sans les check) (Bricolage)
     * 
     * @param enfantFamille
     * @return
     * @throws JadePersistenceException
     * @throws SituationFamilleException
     */
    public EnfantFamille createForRP(EnfantFamille enfantFamille) throws JadePersistenceException,
            SituationFamilleException;

    public EnfantFamilleAddCheckMessage checkEnfantSituationFamillialeCoherence(Demande idDemande)
            throws SituationFamilleException, JadePersistenceException, DemandeException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError;

    public EnfantFamille delete(EnfantFamille enfantFamille, String idDemande) throws JadePersistenceException,
            SituationFamilleException, Exception;

    /**
     * Check avant appel ajax d'ajout d'enfants.
     * Retourne un objet json contenant le message (la cas échéant) et le type du message
     * 
     * @param enfantFamille l'objet à persister en db
     * @return une instance de <code>EnfantFamilleAddCheckMessage</code> contenant le messgae et sont type
     * @throws Exception
     */
    public EnfantFamilleAddCheckMessage checkForAjaxAdd(EnfantFamille enfantFamille) throws Exception;

    /**
     * Méthode add pour l'appel ajax spécifique
     * 
     * @param enfantFamille
     * @return
     * @throws JadePersistenceException
     * @throws SituationFamilleException
     * @throws Exception
     */
    public EnfantFamille createAjax(EnfantFamille enfantFamille) throws JadePersistenceException,
            SituationFamilleException, Exception;

    /**
     * Suppression d'un enfant dans la situation familiaile pour les rentes ponts
     * 
     * @param enfantFamille
     * @param idDemande
     * @return
     * @throws JadePersistenceException
     * @throws SituationFamilleException
     */
    public EnfantFamille deleteForRP(EnfantFamille enfantFamille) throws JadePersistenceException,
            SituationFamilleException;

    public int deleteForSituationFamiliale(String idSituationFamiliale) throws JadePersistenceException,
            SituationFamilleException;

    public EnfantFamille read(String idEnfantFamille) throws JadePersistenceException, SituationFamilleException;

    public EnfantFamilleSearchModel search(EnfantFamilleSearchModel search) throws JadePersistenceException,
            SituationFamilleException;

    public EnfantFamille update(EnfantFamille enfantFamille) throws JadePersistenceException,
            SituationFamilleException, Exception;

    public EnfantFamilleAddCheckMessage updateAjax(EnfantFamille enfantFamille) throws JadePersistenceException,
            SituationFamilleException, Exception;

    List<EnfantFamille> findEnfantByIdSF(String idSf) throws JadePersistenceException, SituationFamilleException;

    // /**
    // * Service appelé en ajax lors d ela création d'un enfant. Permettant de rmeonter des informations de warnnig si
    // * l'enfant existe déjà dans une situation familliale
    // * en garde exclusive.
    // *
    // * @param simpleEnfantFamille
    // * @return
    // * @throws SituationFamilleException
    // * @throws DemandeException
    // * @throws JadeApplicationServiceNotAvailableException
    // * @throws JadePersistenceException
    // * @throws JadeNoBusinessLogSessionError
    // * @throws Exception
    // */
    // public SimpleEnfantFamilleWarningWrapper checkEnfantNotInAntherFamille(EnfantFamille enfantFamille)
    // throws SituationFamilleException, DemandeException, JadeApplicationServiceNotAvailableException,
    // JadePersistenceException, JadeNoBusinessLogSessionError, Exception;

}
