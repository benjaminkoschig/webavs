package ch.globaz.al.business.services.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;

/**
 * Services métier des annonces RAFAM
 * 
 * @author jts
 * 
 */
public interface AnnonceRafamBusinessService extends JadeApplicationService {

    /**
     * Vérifie si une annonce peut être supprimée manuellement par l'utilisateur
     * 
     * @return <code>true</code> si l'annonce peut être validée manuellement, sinon <code>false</code>
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public boolean canDelete(AnnonceRafamModel annonce) throws JadeApplicationException, JadePersistenceException;

    /**
     * Vérifie si une annonce peut être validée manuellement par l'utilisateur
     * 
     * @return <code>true</code> si l'annonce peut être validée manuellement, sinon <code>false</code>
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public boolean canValidate(AnnonceRafamModel annonce) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime les annonces correspondant à un record number et un type d'annonce. Seules les annonces validées sont
     * supprimées
     * 
     * @param recordNumber
     *            record number pour lequel supprimer les annonces
     * @param typeAnnonce
     *            type d'annonce à supprimer
     * @param excludeLast
     *            indique si la dernière annonce doit être ignorée
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void deleteAnnoncesForRecordNumber(String recordNumber, RafamTypeAnnonce typeAnnonce, boolean excludeLast)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime les annonces pour un droit et un état donné
     * 
     * @param idDroit
     *            id du droit pour lequel supprimer les annonces
     * @param etat
     *            etat dans lesquels les annonces à supprimer doivent être (RafamEtatAnnonce.A_TRANSMETTRE ou
     *            RafamEtatAnnonce_ENREGISTRE)
     * @return nombre d'annonces supprimées
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public int deleteForEtat(String idDroit, RafamEtatAnnonce etat) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime les annonces pour un droit et un état donné
     *
     * @param idDroit
     *            id du droit pour lequel supprimer les annonces
     * @param etat
     *            etat dans lesquels les annonces à supprimer doivent être (RafamEtatAnnonce.A_TRANSMETTRE ou
     *            RafamEtatAnnonce_ENREGISTRE)
     *  @param year
     *      Spécifie l'année de l'annonce à supprimer : pour les annonces ADI
     * @return nombre d'annonces supprimées
     *
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public int deleteForEtatYear(String idDroit, RafamEtatAnnonce etat, String years) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime les annonces non transmises pour un droit
     * 
     * @param idDroit
     *            id du droit pour lequel supprimer les annonces
     * @return nombre d'annonces supprimées
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public int deleteNotSent(String idDroit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime les annonces non transmises pour un record number
     * 
     * @param idDroit
     *            id du droit pour lequel supprimer les annonces
     * @return nombre d'annonces supprimées
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public int deleteNotSentForRecordNumber(String recordNumber) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime les annonce refusée correspondant à un record number
     * 
     * @param recordNumber
     *            record number pour lequel supprimer les annonces
     * @return nombre d'annonces supprimées
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * 
     * @see AnnonceRafamBusinessService#deleteRefuseesForDroit(String)
     */
    public int deleteRefusees(String recordNumber) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime les annonces refusées pour un droit
     * 
     * @param idDroit
     *            id du droit pour lequel supprimer les annonces
     * @return nombre d'annonces supprimées
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * 
     * @see AnnonceRafamBusinessService#deleteRefusees(String)
     */
    public int deleteRefuseesForDroit(String idDroit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne le code système correspondant au code sexe provenant de la centrale
     * 
     * @param sexeEnfantRafam
     *            code sexe provenant de la centrale
     * @return le code système correspondant
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public String getSexeCS(String sexeEnfantRafam) throws JadeApplicationException;

    /**
     * Vérifie s'il s'agit d'un cas de naissance horlogère. Ce type de prestation correspond aux cas pour lesquels
     * aucune prestation de naissance/accueil n'est normalement versée mais pour lesquel les caisses horlogères versent
     * une prestation selon leurs tarifs
     * 
     * @return <code>true</code> s'il s'agit d'un cas de naissance hologère
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isNaissanceHorlogere(ContextAnnonceRafam context) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Change les NSS stockés dans une annonces.
     * Si l'un des NSS passé en paramètre est vide, le NSS correspondant dans l'annonce est laissé dans sont état actuel
     * 
     * 
     * @param annonce
     *            l'annonce à laquelle ajouter le message
     * @param nssAllocataire
     *            le nouveau NSS pour l'allocataire
     * @param nssEnfant
     *            le nouveau NSS pour l'enfant
     * 
     * @return l'annonce mise à jour
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AnnonceRafamModel setNSS(AnnonceRafamModel annonce, String nssAllocataire, String nssEnfant)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Ajoute un message d'erreur interne à une annonce.
     * 
     * En plus de l'ajout du message, le champ <code>internalError</code> passe à <code>true</code>
     * 
     * @param annonce
     *            l'annonce à laquelle ajouter le message
     * @param message
     *            le message à ajouter
     * 
     * @return l'annonce mise à jour
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AnnonceRafamModel setError(AnnonceRafamModel annonce, String message) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Change l'état d'une annonce
     * 
     * @param annonce
     *            l'annonce devant être modifiée
     * @param etat
     *            nouvel état de l'annonce
     * 
     * @return l'annonce mise à jour
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public AnnonceRafamModel setEtat(AnnonceRafamModel annonce, RafamEtatAnnonce etat) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Passer à "exécuté" le code de retour des annonces correspondant à un record number. Seules les annonces ayant
     * l'état "validé" et le code "En attente" sont modifiées
     * 
     * @param recordNumber
     *            le record number pour lequel les annonces doivent être modifiées
     * @param excludeLast
     *            indique si la dernière annonce doit être ignorée
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void validateEnAttenteForRecordNumber(String recordNumber, boolean excludeLast)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Passer à "validé" l'état des annonces correspondant à un record number. Seules les annonces ayant l'état "reçu"
     * sont modifiées
     * 
     * @param recordNumber
     *            le record number pour lequel les annonces doivent être modifiées
     * @param excludeLast
     *            indique si la dernière annonce doit être ignorée
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public void validateForRecordNumber(String recordNumber, boolean excludeLast) throws JadeApplicationException,
            JadePersistenceException;
}
