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
 * Service de recherche d'annonces RAFam. Il fournit diverses méthode permettant de rechercher des annonces selon des
 * critères précis (dernière annonce, annonce correspondant à une notice, ...)
 *
 * @author jts
 *
 */
public interface AnnoncesRafamNewXSDVersionSearchService extends JadeApplicationService {

    /**
     * Recherche l'annonce correspondant à l'annonce de type notice passée en paramètre.
     *
     * Les critères suivants sont utilisés :
     *
     * <ul>
     * <li>état : Recu, validé ou suspendu</li>
     * <li>type : 68a, 68b ou 68c</li>
     * <li>record number : celui de la notice</li>
     * <li>genre d'allocation : celui contenu dans la notice</li>
     * </ul>
     *
     * Si plusieurs annonces sont trouvée (ex : cas de mutations multiples), l'annonce la plus récente est retournée
     *
     * Si aucune annonce n'est trouvée, une exception est levée
     *
     * @param message
     *            La notice pour laquelle rechercher l'annonce
     * @return l'annonce trouvée
     *
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AnnonceRafamModel getAnnonceForNotice(NoticeType message)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche l'annonce correspondant à l'annonce de type reçu passée en paramètre.
     *
     * Les critères suivants sont utilisés :
     *
     * <ul>
     * <li>état : transmis</li>
     * <li>type : 68a, 68b ou 68c</li>
     * <li>record number : celui du reçu</li>
     * <li>genre d'allocation : celui contenu dans le reçu</li>
     * </ul>
     *
     * Si aucune annonces ou si plusieurs annonces sont trouvées, une exception est levée
     *
     * @param message
     *            La notice pour laquelle rechercher l'annonce
     * @return l'annonce trouvée
     *
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AnnonceRafamModel getAnnonceForReceipt(ReceiptType receipt)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche la dernière annonce d'état du registre reçue pour un record number.
     *
     * Si plusieurs annonces d'état du registre existent, la plus récente est utilisée. Si aucune annonce n'est trouvée
     * un nouveau modèle est retourné.
     *
     * @param recordNumber
     *            record number pour lequel charger l'annonce
     * @return L'annonce d'étar du registre trouvée
     *
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     *
     * @deprecated A partir de la version 1-11-00 l'état du registre est traité à l'importation, les annonces ne sont
     *             plus enregistrées
     */
    @Deprecated
    public AnnonceRafamModel getAnnonceRegisterStatus(String recordNumber)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche l'annonce active pour un droit et un type de droit.
     *
     * Les critères suivants sont utilisés :
     *
     * <ul>
     * <li>état : reçu, suspendu, transmis ou validé</li>
     * <li>type : 68a, 68b</li>
     * <li>id du droit</li>
     * <li>genre d'allocation</li>
     * </ul>
     *
     * Pour chacune des annonces correspondant à ces critères, l'existance d'une annonce d'annulation est recherchée
     * selon ces critères :
     *
     * <ul>
     * <li>code de retour : différent de refusé</li>
     * <li>type : 68c</li>
     * <li>record number : celui contenu dans l'annonce 68a/b en cours de traitement</li>
     * </ul>
     *
     * Si plusieurs annonces actives sont trouvées, une exception est levée.
     *
     * Si toutes les annonces ont été annulées, la dernière annonce de type 68c est retournée.
     *
     * Si aucune annonce n'existe une nouvelle instance du modèle d'annonce est retourné
     *
     * @param idDroit
     *            Id du droit pour lequel rechercher l'annonce
     * @param type
     *            Type d'annonce à rechercher
     * @return L'annonce trouvée
     *
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AnnonceRafamModel getLastActive(String idDroit, RafamFamilyAllowanceType type)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche l'annonce active pour un droit et un type de droit, dans l'un des états fourni dans la liste
     *
     * Les critères suivants sont utilisés :
     *
     * <ul>
     * <li>état : selon paramètre états</li>
     * <li>type : 68a, 68b</li>
     * <li>id du droit</li>
     * <li>genre d'allocation</li>
     * </ul>
     *
     * Pour chacune des annonces correspondant à ces critères, l'existance d'une annonce d'annulation est recherchée
     * selon ces critères :
     *
     * <ul>
     * <li>code de retour : différent de refusé</li>
     * <li>type : 68c</li>
     * <li>record number : celui contenu dans l'annonce 68a/b en cours de traitement</li>
     * </ul>
     *
     * Si plusieurs annonces actives sont trouvées, une exception est levée.
     *
     * Si toutes les annonces ont été annulées, la dernière annonce de type 68c est retournée.
     *
     * Si aucune annonce n'existe une nouvelle instance du modèle d'annonce est retourné
     *
     * @param idDroit
     *            Id du droit pour lequel rechercher l'annonce
     * @param type
     *            Type d'annonce à rechercher
     * @param etats
     *            liste des états des annonces à prendre en compte
     * @return L'annonce trouvée
     *
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AnnonceRafamModel getLastActive(String idDroit, RafamFamilyAllowanceType type, ArrayList<String> etats)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche la dernière annonce de type 68a, 68b ou 68c pour le record number passé en paramètre
     *
     * @param recordNumber
     *            Record number pour lequel rechercher l'annnonce
     *
     * @return L'annonce trouvée ou un nouveau modèle si aucune annonce ne corrspond
     *
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AnnonceRafamModel getLastAnnonceForRecordNumber(String recordNumber)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * REcherche les annonces de type 68a, 68b ou 68c pour le record number passé en paramètre
     *
     * @param recordNumber
     *            Record number pour lequel rechercher l'annnonce
     *
     * @return L'annonce trouvée ou un nouveau modèle si aucune annonce ne corrspond
     *
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AnnonceRafamSearchModel getAnnoncesForRecordNumber(String recordNumber)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Vérifie si un droit a une annonce d'un type donné
     *
     * @param type
     *            le type d'annonce à rechercher
     * @param idDroit
     *            id du droit pour lequel rechercher l'annonce
     *
     * @return <code>true</code> si au moins une annonce a été trouvée, <code>false</code> sinon
     *
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public boolean hasAnnonceOfType(String idDroit, RafamFamilyAllowanceType type)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Vérifie si un droit a des annonces qui ont été transmises au RAFam
     *
     * @param idDroit
     *            id du droit à vérifier
     * @return <code>true</code> si des annonces ont été transmies, <code>false</code> dans le cas contraire
     *
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public boolean hasSentAnnonces(String idDroit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les annonces à envoyer à la centrale, en excluant les annonces délégués
     *
     * @return résultat de la recherche
     *
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AnnonceRafamComplexSearchModel loadAnnoncesToSend()
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les annonces à envoyer à la centrale pour un droit donné
     *
     * @param idDroit
     *            id du droit pour lequel charger les annonces
     *
     * @return résultat de la recherche
     *
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AnnonceRafamSearchModel loadAnnoncesToSendForDroit(String idDroit)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge les annonces pour un droit donné
     *
     * @param idDroit
     *            id du droit pour lequel charger les annonces
     *
     * @return résultat de la recherche
     *
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AnnonceRafamSearchModel loadAnnoncesForDroit(String idDroit)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge la dernière annonce UPI non validée pour un record number
     *
     * @param recordNumber
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public AnnonceRafamModel getLastAnnonceUPIForRecordNumber(String recordNumber)
            throws JadeApplicationException, JadePersistenceException;
}
