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
 * Services m�tiers li�s aux r�caps
 * 
 * @author GMO
 */
public interface RecapitulatifEntrepriseBusinessService extends JadeApplicationService {

    /**
     * Calcule le montant total d'une recapitulation entreprise
     * 
     * @param listeRecap
     *            listes des r�capitulatifs entreprises
     * @param idRecap
     *            identifiant de la recap pour laquelle if faut trouver le montant total
     * @return montantTotal
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String calculMontantPourUneRecapEntreprise(String idRecap, ArrayList listeRecap)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Retourne la p�riode de d�but de la r�cap selon le dossier et la p�riode en cours Exemple : p�riode en cours
     * <code>periode</code> 05.2011, dossier indirect avec affili� trimestriel <br>
     * retourne p�riode d�but = 04.2011
     * 
     * @param dossier
     *            le mod�le du dossier pour lequel on veut g�n�rer
     * @param periode
     *            la p�riode en cours <b>mm.YYYY</b>
     * @return la p�riode de d�but de r�cap
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getDebutRecap(DossierModel dossier, String periode) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne la p�riode de fin de la r�cap selon le dossier et la p�riode en cours Exemple : p�riode en cours
     * <code>periode</code> 05.2011, dossier indirect avec affili� trimestriel <br>
     * retourne p�riode d�but = 06.2011
     * 
     * @param dossier
     *            le mod�le du dossier pour lequel on veut g�n�rer
     * @param periode
     *            la p�riode en cours <b>mm.YYYY</b>
     * @return la p�riode de fin de r�cap
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getFinRecap(DossierModel dossier, String periode) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne le nombre de prestations restantes � compl�ter (� l'heure/jour) dans la r�cap sp�cifi�e
     * 
     * @param numRecap
     *            - La r�cap � parcourir
     * @return le nombre de prestations restantes
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int getNbPrestationsASaisir(String numRecap) throws JadePersistenceException, JadeApplicationException;

    /**
     * Retourne la prochaine prestation � compl�ter (� l'heure/jour) dans la r�cap sp�cifi�e
     * 
     * @param numRecap
     *            - La r�cap � parcourir
     * @return EntetePrestationModel � compl�ter
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public EntetePrestationModel getNextPrestationASaisir(String numRecap) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * r�cup�re le montant total d'une r�cap
     * 
     * @param idRecap
     *            l'id de la r�cap dont on veut le montant
     * @return montantTotal
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTotalRecap(String idRecap) throws JadePersistenceException, JadeApplicationException;

    /**
     * Initialise une r�cap ouverte pour une p�riode donn�e et un dossier
     * 
     * @param dossier
     * @param periode
     * @param bonification
     * @return la r�cap cr��e ou trouv�e <RecapitulatifEntrepriseModel>
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RecapitulatifEntrepriseModel initRecap(DossierModel dossier, String periode, String bonification)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Indique si la r�cap pass�e en param�tre est verouill�e ou non selon le contexte de la caisse
     * 
     * @param recap
     * @return vrai ou faux
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isRecapVerouillee(RecapitulatifEntrepriseModel recap) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * m�thode qui retourne une liste de r�sultats
     * 
     * @param recap
     *            liste des r�cap recherch�es
     * @param periodeDe
     *            p�riode de la r�cap
     * @param etatRecap
     *            �tat de la r�cap
     * @param typeRecap
     *            type de la r�capitulation
     * 
     * @return ArrayList liste des r�capitulatifs
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList resultSearchRecap(RecapitulatifEntrepriseImpressionComplexSearchModel recap, String typeRecap,
            String periodeDe, String etatRecap) throws JadePersistenceException, JadeApplicationException;

    /**
     * Retourne liste r�capitulatif selon num�ro de lot
     * 
     * @param noLot
     *            num�ro du lot
     * @return ArrayList liste des r�capitulatifs
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList resultSearchRecap(String noLot) throws JadePersistenceException, JadeApplicationException;

    /**
     * M�thode qui retournes une liste de r�sultats des r�caps li�e � un processus donn�
     * 
     * @param idProcessus
     *            le processus li�
     * @return la liste des r�caps li�es aux r�caps
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList<RecapitulatifEntrepriseImpressionComplexSearchModel> resultSearchRecapNumProcessus(
            String idProcessus) throws JadePersistenceException, JadeApplicationException;

    /**
     * 
     * @param numRecap
     *            identifiant de la r�cap
     * @return ArrayList
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList resultSearchRecapNumRecap(String numRecap) throws JadePersistenceException,
            JadeApplicationException;
}
