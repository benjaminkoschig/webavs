package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;

/**
 * Services métiers liés aux récaps
 * 
 * @author GMO
 */
public interface RecapitulatifEntrepriseBusinessService extends JadeApplicationService {

    /**
     * Calcule le montant total d'une recapitulation entreprise
     * 
     * @param listeRecap
     *            listes des récapitulatifs entreprises
     * @param idRecap
     *            identifiant de la recap pour laquelle if faut trouver le montant total
     * @return montantTotal
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String calculMontantPourUneRecapEntreprise(String idRecap, ArrayList listeRecap)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Retourne la période de début de la récap selon le dossier et la période en cours Exemple : période en cours
     * <code>periode</code> 05.2011, dossier indirect avec affilié trimestriel <br>
     * retourne période début = 04.2011
     * 
     * @param dossier
     *            le modèle du dossier pour lequel on veut générer
     * @param periode
     *            la période en cours <b>mm.YYYY</b>
     * @return la période de début de récap
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getDebutRecap(DossierModel dossier, String periode) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne la période de fin de la récap selon le dossier et la période en cours Exemple : période en cours
     * <code>periode</code> 05.2011, dossier indirect avec affilié trimestriel <br>
     * retourne période début = 06.2011
     * 
     * @param dossier
     *            le modèle du dossier pour lequel on veut générer
     * @param periode
     *            la période en cours <b>mm.YYYY</b>
     * @return la période de fin de récap
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getFinRecap(DossierModel dossier, String periode) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne le nombre de prestations restantes à compléter (à l'heure/jour) dans la récap spécifiée
     * 
     * @param numRecap
     *            - La récap à parcourir
     * @return le nombre de prestations restantes
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int getNbPrestationsASaisir(String numRecap) throws JadePersistenceException, JadeApplicationException;

    /**
     * Retourne la prochaine prestation à compléter (à l'heure/jour) dans la récap spécifiée
     * 
     * @param numRecap
     *            - La récap à parcourir
     * @return EntetePrestationModel à compléter
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EntetePrestationModel getNextPrestationASaisir(String numRecap) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * récupère le montant total d'une récap
     * 
     * @param idRecap
     *            l'id de la récap dont on veut le montant
     * @return montantTotal
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTotalRecap(String idRecap) throws JadePersistenceException, JadeApplicationException;

    /**
     * Initialise une récap ouverte pour une période donnée et un dossier
     * 
     * @param dossier
     * @param periode
     * @param bonification
     * @return la récap créée ou trouvée <RecapitulatifEntrepriseModel>
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseModel initRecap(DossierModel dossier, String periode, String bonification)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Indique si la récap passée en paramètre est verouillée ou non selon le contexte de la caisse
     * 
     * @param recap
     * @return vrai ou faux
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isRecapVerouillee(RecapitulatifEntrepriseModel recap) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * méthode qui retourne une liste de résultats
     * 
     * @param recap
     *            liste des récap recherchées
     * @param periodeDe
     *            période de la récap
     * @param etatRecap
     *            état de la récap
     * @param typeRecap
     *            type de la récapitulation
     * 
     * @return ArrayList liste des récapitulatifs
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList resultSearchRecap(RecapitulatifEntrepriseImpressionComplexSearchModel recap, String typeRecap,
            String periodeDe, String etatRecap) throws JadePersistenceException, JadeApplicationException;

    /**
     * Retourne liste récapitulatif selon numéro de lot
     * 
     * @param noLot
     *            numéro du lot
     * @return ArrayList liste des récapitulatifs
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList resultSearchRecap(String noLot) throws JadePersistenceException, JadeApplicationException;

    /**
     * Méthode qui retournes une liste de résultats des récaps liée à un processus donné
     * 
     * @param idProcessus
     *            le processus lié
     * @return la liste des récaps liées aux récaps
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList<RecapitulatifEntrepriseImpressionComplexSearchModel> resultSearchRecapNumProcessus(
            String idProcessus) throws JadePersistenceException, JadeApplicationException;

    /**
     * 
     * @param numRecap
     *            identifiant de la récap
     * @return ArrayList
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList resultSearchRecapNumRecap(String numRecap) throws JadePersistenceException,
            JadeApplicationException;
}
