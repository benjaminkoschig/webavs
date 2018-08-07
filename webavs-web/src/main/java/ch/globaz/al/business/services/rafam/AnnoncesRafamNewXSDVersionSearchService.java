package ch.globaz.al.business.services.rafam;

import java.util.ArrayList;
import al.ch.ech.xmlns.ech_0104_69._4.NoticeType;
import al.ch.ech.xmlns.ech_0104_69._4.ReceiptType;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.models.rafam.AnnonceRafamComplexSearchModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamSearchModel;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service de recherche d'annonces RAFam. Il fournit diverses m�thode permettant de rechercher des annonces selon des
 * crit�res pr�cis (derni�re annonce, annonce correspondant � une notice, ...)
 *
 * @author jts
 *
 */
public interface AnnoncesRafamNewXSDVersionSearchService extends JadeApplicationService {

    /**
     * Recherche l'annonce correspondant � l'annonce de type notice pass�e en param�tre.
     *
     * Les crit�res suivants sont utilis�s :
     *
     * <ul>
     * <li>�tat : Recu, valid� ou suspendu</li>
     * <li>type : 68a, 68b ou 68c</li>
     * <li>record number : celui de la notice</li>
     * <li>genre d'allocation : celui contenu dans la notice</li>
     * </ul>
     *
     * Si plusieurs annonces sont trouv�e (ex : cas de mutations multiples), l'annonce la plus r�cente est retourn�e
     *
     * Si aucune annonce n'est trouv�e, une exception est lev�e
     *
     * @param message
     *            La notice pour laquelle rechercher l'annonce
     * @return l'annonce trouv�e
     *
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AnnonceRafamModel getAnnonceForNotice(NoticeType message)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche l'annonce correspondant � l'annonce de type re�u pass�e en param�tre.
     *
     * Les crit�res suivants sont utilis�s :
     *
     * <ul>
     * <li>�tat : transmis</li>
     * <li>type : 68a, 68b ou 68c</li>
     * <li>record number : celui du re�u</li>
     * <li>genre d'allocation : celui contenu dans le re�u</li>
     * </ul>
     *
     * Si aucune annonces ou si plusieurs annonces sont trouv�es, une exception est lev�e
     *
     * @param message
     *            La notice pour laquelle rechercher l'annonce
     * @return l'annonce trouv�e
     *
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AnnonceRafamModel getAnnonceForReceipt(ReceiptType receipt)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche la derni�re annonce d'�tat du registre re�ue pour un record number.
     *
     * Si plusieurs annonces d'�tat du registre existent, la plus r�cente est utilis�e. Si aucune annonce n'est trouv�e
     * un nouveau mod�le est retourn�.
     *
     * @param recordNumber
     *            record number pour lequel charger l'annonce
     * @return L'annonce d'�tar du registre trouv�e
     *
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     *
     * @deprecated A partir de la version 1-11-00 l'�tat du registre est trait� � l'importation, les annonces ne sont
     *             plus enregistr�es
     */
    @Deprecated
    public AnnonceRafamModel getAnnonceRegisterStatus(String recordNumber)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche l'annonce active pour un droit et un type de droit.
     *
     * Les crit�res suivants sont utilis�s :
     *
     * <ul>
     * <li>�tat : re�u, suspendu, transmis ou valid�</li>
     * <li>type : 68a, 68b</li>
     * <li>id du droit</li>
     * <li>genre d'allocation</li>
     * </ul>
     *
     * Pour chacune des annonces correspondant � ces crit�res, l'existance d'une annonce d'annulation est recherch�e
     * selon ces crit�res :
     *
     * <ul>
     * <li>code de retour : diff�rent de refus�</li>
     * <li>type : 68c</li>
     * <li>record number : celui contenu dans l'annonce 68a/b en cours de traitement</li>
     * </ul>
     *
     * Si plusieurs annonces actives sont trouv�es, une exception est lev�e.
     *
     * Si toutes les annonces ont �t� annul�es, la derni�re annonce de type 68c est retourn�e.
     *
     * Si aucune annonce n'existe une nouvelle instance du mod�le d'annonce est retourn�
     *
     * @param idDroit
     *            Id du droit pour lequel rechercher l'annonce
     * @param type
     *            Type d'annonce � rechercher
     * @return L'annonce trouv�e
     *
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AnnonceRafamModel getLastActive(String idDroit, RafamFamilyAllowanceType type)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche l'annonce active pour un droit et un type de droit, dans l'un des �tats fourni dans la liste
     *
     * Les crit�res suivants sont utilis�s :
     *
     * <ul>
     * <li>�tat : selon param�tre �tats</li>
     * <li>type : 68a, 68b</li>
     * <li>id du droit</li>
     * <li>genre d'allocation</li>
     * </ul>
     *
     * Pour chacune des annonces correspondant � ces crit�res, l'existance d'une annonce d'annulation est recherch�e
     * selon ces crit�res :
     *
     * <ul>
     * <li>code de retour : diff�rent de refus�</li>
     * <li>type : 68c</li>
     * <li>record number : celui contenu dans l'annonce 68a/b en cours de traitement</li>
     * </ul>
     *
     * Si plusieurs annonces actives sont trouv�es, une exception est lev�e.
     *
     * Si toutes les annonces ont �t� annul�es, la derni�re annonce de type 68c est retourn�e.
     *
     * Si aucune annonce n'existe une nouvelle instance du mod�le d'annonce est retourn�
     *
     * @param idDroit
     *            Id du droit pour lequel rechercher l'annonce
     * @param type
     *            Type d'annonce � rechercher
     * @param etats
     *            liste des �tats des annonces � prendre en compte
     * @return L'annonce trouv�e
     *
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AnnonceRafamModel getLastActive(String idDroit, RafamFamilyAllowanceType type, ArrayList<String> etats)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche la derni�re annonce de type 68a, 68b ou 68c pour le record number pass� en param�tre
     *
     * @param recordNumber
     *            Record number pour lequel rechercher l'annnonce
     *
     * @return L'annonce trouv�e ou un nouveau mod�le si aucune annonce ne corrspond
     *
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AnnonceRafamModel getLastAnnonceForRecordNumber(String recordNumber)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * REcherche les annonces de type 68a, 68b ou 68c pour le record number pass� en param�tre
     *
     * @param recordNumber
     *            Record number pour lequel rechercher l'annnonce
     *
     * @return L'annonce trouv�e ou un nouveau mod�le si aucune annonce ne corrspond
     *
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AnnonceRafamSearchModel getAnnoncesForRecordNumber(String recordNumber)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * V�rifie si un droit a une annonce d'un type donn�
     *
     * @param type
     *            le type d'annonce � rechercher
     * @param idDroit
     *            id du droit pour lequel rechercher l'annonce
     *
     * @return <code>true</code> si au moins une annonce a �t� trouv�e, <code>false</code> sinon
     *
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public boolean hasAnnonceOfType(String idDroit, RafamFamilyAllowanceType type)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * V�rifie si un droit a des annonces qui ont �t� transmises au RAFam
     *
     * @param idDroit
     *            id du droit � v�rifier
     * @return <code>true</code> si des annonces ont �t� transmies, <code>false</code> dans le cas contraire
     *
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public boolean hasSentAnnonces(String idDroit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les annonces � envoyer � la centrale, en excluant les annonces d�l�gu�s
     *
     * @return r�sultat de la recherche
     *
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AnnonceRafamComplexSearchModel loadAnnoncesToSend()
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les annonces � envoyer � la centrale pour un droit donn�
     *
     * @param idDroit
     *            id du droit pour lequel charger les annonces
     *
     * @return r�sultat de la recherche
     *
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AnnonceRafamSearchModel loadAnnoncesToSendForDroit(String idDroit)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les annonces pour un droit donn�
     *
     * @param idDroit
     *            id du droit pour lequel charger les annonces
     *
     * @return r�sultat de la recherche
     *
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AnnonceRafamSearchModel loadAnnoncesForDroit(String idDroit)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge la derni�re annonce UPI non valid�e pour un record number
     *
     * @param recordNumber
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public AnnonceRafamModel getLastAnnonceUPIForRecordNumber(String recordNumber)
            throws JadeApplicationException, JadePersistenceException;
}
