package ch.globaz.perseus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;

/**
 * 
 * @author MBO
 * 
 */

public interface DecisionService extends JadeApplicationService {

    public int count(DecisionSearchModel search) throws DecisionException, JadePersistenceException;

    public Decision create(Decision decision) throws JadePersistenceException, DecisionException;

    public Decision createNewDecisionAfterProjetValidation(Decision decision) throws Exception;

    public String definitTypeDecision(Decision decision, boolean isNewProject, boolean isNewSuppressionVolontaire)
            throws JadePersistenceException, PCFAccordeeException, JadeApplicationServiceNotAvailableException;

    public Decision delete(Decision decision) throws JadePersistenceException, DecisionException;

    public Demande getDemandePrecedenteValideDecisionOCtroiPrecedanteForNumOFS(Demande demande)
            throws DemandeException, JadePersistenceException;

    public String getNumeroDemandeCalculee(String annee) throws JadePersistenceException;

    public Decision read(String idDecision) throws JadePersistenceException, DecisionException,
            JadeApplicationServiceNotAvailableException;

    public DecisionSearchModel search(DecisionSearchModel searchModel) throws JadePersistenceException,
            DecisionException;

    public Decision update(Decision decision) throws JadePersistenceException, DecisionException;

    /**
     * Permet de valider une d�cision et d'y faire toutes les actions qui en d�coulent :
     * <ul>
     * <li>Cr�ation d'un lot retroactif si il n'y en a pas d'ouvert</li>
     * <li>Calcul du retroactif</li>
     * <li>Cr�ation de la prestation pour le retroactif</li>
     * <li>Controler que le retroactif a bien �t� r�parti sur les cr�anciers</li>
     * <li>Cr�er les ordres de versement de la prestation aux cr�anciers</li>
     * <li>Cr�er l'ordre de versement au requ�rant</li>
     * <li>Mise � jour des �tats de la demande, de la pcfaccordee et de la d�cision</li>
     * <li>Cr�ation des QD</li>
     * </ul>
     * 
     * @return
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public Decision valider(Decision decision, String user) throws JadePersistenceException, DecisionException;

    /**
     * Permet de valider une d�cision et d'y faire toutes les actions qui en d�coulent :
     * <ul>
     * <li>Cr�ation d'un lot retroactif si il n'y en a pas d'ouvert</li>
     * <li>Calcul du retroactif</li>
     * <li>Cr�ation de la prestation pour le retroactif</li>
     * <li>Controler que le retroactif a bien �t� r�parti sur les cr�anciers</li>
     * <li>Cr�er les ordres de versement de la prestation aux cr�anciers</li>
     * <li>Cr�er l'ordre de versement au requ�rant</li>
     * <li>Mise � jour des �tats de la demande, de la pcfaccordee et de la d�cision</li>
     * <li>Cr�ation des QD</li>
     * </ul>
     * 
     * @param decision
     *            La d�cision � valider
     * @param user
     *            Le nom d'utilisateur de la personne qui valide
     * @param emptyAnnoncesChangement
     *            True pour vider les annonces de changement
     * @return
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public Decision valider(Decision decision, String user, Boolean emptyAnnoncesChangement)
            throws JadePersistenceException, DecisionException;

    /**
     * Permet d'afficher le type de d�cision pour une date de facture en temps r�el lors de l'ajout d'une facture.
     * 
     * @param idDossier
     *            Id du dossier contenant la facture
     * @param dateFacture
     *            date de la facture
     * @return
     * @throws Exception
     */
    public List<String> getInformationSurLaDecision(String idDossier, String dateFacture) throws Exception;
}
