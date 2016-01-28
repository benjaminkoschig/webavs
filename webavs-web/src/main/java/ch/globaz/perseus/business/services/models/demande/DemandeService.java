package ch.globaz.perseus.business.services.models.demande;

import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.dossier.DossierException;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.situationfamille.Enfant;

/**
 * @author DDE
 * 
 */
public interface DemandeService extends JadeApplicationService {

    /**
     * Permet d'annuler une demande qui n'a jamais �t� pay�e. Il s'agit en fait d'une demande qui d�marre dans le mois
     * en cours.
     * 
     * @param demande
     * @return
     * @throws JadePersistenceException
     * @throws DemandeException
     */
    public Demande annuler(Demande demande) throws JadePersistenceException, DemandeException;

    public Float calculerMontantVerseImpotSource(Demande demande) throws DemandeException, JadePersistenceException;

    public Float calculerMontantVerseImpotSource(Demande demande, String date) throws DemandeException,
            JadePersistenceException;

    /**
     * Permet de calculer le montant r�troactif d� � la validation d'une demande
     * 
     * @param demande
     * @return
     * @throws JadePersistenceException
     * @throws DemandeException
     */
    public Float calculerRetro(Demande demande) throws JadePersistenceException, DemandeException;

    public Float calculerRetro(Demande demande, String date) throws JadePersistenceException, DemandeException;

    /**
     * Permet de calculer le montant r�troactif d� � la validation d'une demande pour la mesure de coaching
     * 
     * @param demande
     * @return
     * @throws JadePersistenceException
     * @throws DemandeException
     */
    public Float calculerRetroMesureCoaching(Demande demande) throws JadePersistenceException, DemandeException;

    public Float calculerRetroMesureCoaching(Demande demande, String date) throws JadePersistenceException,
            DemandeException;

    /**
     * Permet de calculer le montant disponbile pour les cr�anciers d'une demande. (montant dispo � la r�partition) Le
     * mois de validation est d� � l'assur� (pour les d�cisions prises en fin de mois)
     * 
     * @param demande
     * @return
     * @throws JadePersistenceException
     * @throws DemandeException
     */
    public Float calculerRetroPourCreanciers(Demande demande) throws JadePersistenceException, DemandeException;

    /**
     * Permet de de d�terminer si la demande est calculable
     * 
     * @param demande
     * @return
     * @throws JadePersistenceException
     * @throws DemandeException
     */
    public boolean checkCalculable(Demande demande) throws DemandeException, JadePersistenceException;

    /**
     * Cr�er une nouvelle demande avec des donn�es identiques � celle pass�e en param�tre
     * 
     * @return
     * @throws JadePersistenceException
     * @throws DemandeException
     * @throws Exception
     */
    public Demande copier(Demande demande) throws JadePersistenceException, DemandeException, JadeCloneModelException,
            Exception;

    public boolean iSConjointDossier(Demande demande) throws DossierException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    public Demande copier(Demande demande, Demande newDemande) throws JadePersistenceException, DemandeException,
            JadeCloneModelException;

    public int count(DemandeSearchModel search) throws DemandeException, JadePersistenceException;

    public Demande create(Demande demande) throws JadePersistenceException, DemandeException;

    public Demande delete(Demande demande) throws JadePersistenceException, DemandeException;

    /**
     * Permet de retrouver la derni�re demande active avant la nouvelle demande Pour une demande purement retroactive,
     * retourne la demande active le dernier mois de la demande Pour demande qui sera active, retourne la demande active
     * le mois du dernier paiement mensuel
     * 
     * @param demande
     * @return
     * @throws DemandeException
     * @throws JadePersistenceException
     */
    public Demande getDemandePrecedante(Demande demande) throws DemandeException, JadePersistenceException;

    /**
     * Retourne la derni�re demande en cours ou qui a �t� en cours pour un dossier
     * 
     * @param idDossier
     * @return
     * @throws DemandeException
     * @throws JadePersistenceException
     */
    public Demande getDerniereDemande(String idDossier) throws DemandeException, JadePersistenceException;

    /**
     * Permet de retrouver la liste des enfants compris dans une demande
     * 
     * @param demande
     * @return
     * @throws JadePersistenceException
     * @throws DemandeException
     */
    public List<Enfant> getListEnfants(Demande demande) throws JadePersistenceException, DemandeException;

    public String getNumeroOFSCalculee() throws JadePersistenceException;

    public String getNumeroOFSCalculeeForDemande(Demande demande) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    public Boolean hasCreanciers(String idDemande) throws JadePersistenceException, DemandeException;

    /**
     * Renvoie le boolean si la demande est calculable sous forme de String pour arriver dans le widget de la JSP TODO
     * Modifier le comportement du widget pour qu'il utilise directement un boolean
     * 
     * @param idDemande
     * @return 'true' or 'false'
     * @throws JadePersistenceException
     * @throws DemandeException
     */
    public String isCalculable(String idDemande) throws JadePersistenceException, DemandeException;

    public Demande read(String idDemande) throws JadePersistenceException, DemandeException;

    public DemandeSearchModel search(DemandeSearchModel searchModel) throws JadePersistenceException, DemandeException;

    public Demande update(Demande demande) throws JadePersistenceException, DemandeException, SituationFamilleException;

    /**
     * A utiliser pour modifier la date de fin d'une demande apr�s une d�cision de renonciation
     * 
     * @param demande
     * @param renonciation
     * @return
     * @throws JadePersistenceException
     * @throws DemandeException
     * @throws SituationFamilleException
     * @throws s
     */

    /**
     * Cette m�thode doit �tre appell�e pour toute modification concernant la demande (situation familiale, donn�es
     * financi�res, ...) qui doivent avoir un impact sur la pcfaccord�e ou la d�cision (celles-ci sont supprim�es)
     * 
     * @param demande
     * @return
     * @throws JadePersistenceException
     * @throws DemandeException
     * @throws Exception
     */

    Demande update(Demande demande, Boolean renonciation, Boolean checkEnfant) throws JadePersistenceException,
            DemandeException, SituationFamilleException;

    Demande updateAndClean(Demande demande, Boolean checkEnfant) throws JadePersistenceException, DemandeException,
            SituationFamilleException;

}
