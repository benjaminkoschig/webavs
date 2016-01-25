package ch.globaz.amal.business.services.sedexRP;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.sedex.message.SedexMessage;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedexSearch;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;

/**
 * Déclaration des services pour les annonces RP AMAL
 * 
 * @author cbu
 * 
 */
public interface AnnoncesRPService extends JadeApplicationService {

    /**
     * Change le traitement de l'annonce en paramètre par le traitement passé en paramètre
     * 
     * @param idAnnonce
     * @param newStatusId
     * @return
     * @throws AnnonceSedexException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DetailFamilleException
     */
    public String changeStatus(String idAnnonce, String newStatusId) throws AnnonceSedexException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DetailFamilleException;

    /**
     * Change le traitement de l'annonce en paramètre par le traitement passé en paramètre
     * 
     * @param idAnnonce
     * @param newTraitementId
     * @return
     * @throws AnnonceSedexException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DetailFamilleException
     */
    public String changeTraitement(String idAnnonce, String newTraitementId) throws AnnonceSedexException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DetailFamilleException;

    /**
     * Clone l'annonce
     * 
     * @param idAnnonce
     * @return
     * @throws AnnonceSedexException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DetailFamilleException
     */
    public String cloneAnnonce(String idAnnonce) throws AnnonceSedexException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DetailFamilleException;

    /**
     * Création et envois des annonces<br>
     * 
     * Utilisé pour la gestion des annonces depuis la page de gestion des assureurs
     * 
     * @param typeMessage
     * 
     * @param anneeHistorique
     * @param selectedIdCaisses
     * 
     * @param idMessageSedex
     * 
     */
    public Map<String, Object> createAndSendAnnonce(String typeMessage, ArrayList<String> selectedIdCaisses,
            String groupe, String anneeHistorique, boolean isSimulation);

    /**
     * Crée l'annonce de l'id passé en paramètre
     * 
     * @param idMessageSedex
     * @param msgSedexRP
     * @return
     */
    public ComplexAnnonceSedex createAnnonce(String idMessageSedex);

    /**
     * Créé les annonces qui sont dans un état initial
     * 
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws AnnonceSedexException
     * @throws DatatypeConfigurationException
     * @throws JadeSedexDirectoryInitializationException
     * @throws JadeApplicationException
     */
    public ComplexAnnonceSedexSearch createAnnonces();

    /**
     * Efface l'annonce dont l'id est passé en paramètre.<br>
     * Possible uniquement si l'annonce est en l'état 'Initial', 'Erreur créé', 'Erreur envoi'
     * 
     * @param idMessageSedex
     * 
     */
    public void deleteAnnonce(String idMessageSedex) throws JadeApplicationException, JadePersistenceException;

    /**
     * Création de la liste des annonces au format CSV
     * 
     * @param idMessageSedex
     * @return L'url du fichier crée
     * 
     */
    public String exportListAnnonces(String filters, String order) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Description
     * 
     * @param idMessageSedex
     * 
     */
    public ArrayList<SimpleAnnonceSedex> getContribuableListSEDEXAnnonces(String idContribuable)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne les infos qui concernent l'annonce dont l'id est passé en paramètre
     * 
     * @param idAnnonceSedex
     * @return
     */
    public StringBuffer getDetailsAnnonce(String idAnnonceSedex);

    /**
     * Retourne la liste des annonces qui correspondent au subside dont l'id est passé en paramètre
     * 
     * @param idMessageSedex
     * 
     */
    public ArrayList<SimpleAnnonceSedex> getListSEDEXAnnonces(String idDetailFamille) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Retourne le xml pour affichage
     * 
     * @param idMessageSedex
     * 
     */
    public String getSedexXMLLines(String idSedexAnnonce, String type) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Importation des annonces RP<br>
     * <br>
     * Appelé par le gestionnaire SEDEX (JadeSedexService.xml)
     * 
     * @param idMessageSedex
     * 
     */
    public void importMessages(SedexMessage message) throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialisation d'une annonce type "Demande de rapport" depuis un subside
     * 
     * @param idMessageSedex
     * 
     */
    public void initAnnonceDemandeRapport(String idDetailFamille, String idCaisses, String anneeHistorique)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialisation d'une annonce 'Interruption'
     * 
     * @param idMessageSedex
     * 
     */
    public SimpleAnnonceSedex initAnnonceInterruption(String idContribuable, String idDetailFamille,
            boolean subsideIsEnabled) throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialisation d'une annonce 'Nouvelle décision'
     * 
     * @param idMessageSedex
     * 
     */
    public SimpleAnnonceSedex initAnnonceNouvelleDecision(String idContribuable, String idDetailFamille)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialise une annonce depuis la JSP
     * 
     * @param idDetailFamille
     * @param idContribuable
     * @param msgType
     * @return
     * @throws AnnonceSedexException
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public SimpleAnnonceSedex initDecreeStopFromJsp(String idDetailFamille, String idContribuable, String msgType,
            String idTiersCaisse) throws AnnonceSedexException, DetailFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Envoi de toute les annonces en cours
     * 
     * @param idMessageSedex
     * @throws DatatypeConfigurationException
     * @throws JadeSedexDirectoryInitializationException
     * 
     */
    public void sendAllAnnonces() throws JadeApplicationException, JadePersistenceException,
            DatatypeConfigurationException, JadeSedexDirectoryInitializationException;

    /**
     * Permet de setter tout les annonces affichés à l'écran en tant que "Traité manuellement"
     * 
     * @param filters
     * @param order
     * @return
     */
    public String setAllAnnoncesManual(String filters, String order);

    /**
     * Simulation d'un envoi d'annonce
     * 
     * @param idMessageSedex
     * 
     */
    public void simulateAnnonce(String idMessageSedex, String subTypeReponse) throws Exception;
}
