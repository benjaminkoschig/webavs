package ch.globaz.al.business.services.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.businessimpl.rafam.ContextAnnonceRafam;

/**
 * Services m�tier des annonces RAFAM
 * 
 * @author jts
 * 
 */
public interface AnnonceRafamBusinessService extends JadeApplicationService {

    /**
     * V�rifie si une annonce peut �tre supprim�e manuellement par l'utilisateur
     * 
     * @return <code>true</code> si l'annonce peut �tre valid�e manuellement, sinon <code>false</code>
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public boolean canDelete(AnnonceRafamModel annonce) throws JadeApplicationException, JadePersistenceException;

    /**
     * V�rifie si une annonce peut �tre valid�e manuellement par l'utilisateur
     * 
     * @return <code>true</code> si l'annonce peut �tre valid�e manuellement, sinon <code>false</code>
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public boolean canValidate(AnnonceRafamModel annonce) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime les annonces correspondant � un record number et un type d'annonce. Seules les annonces valid�es sont
     * supprim�es
     * 
     * @param recordNumber
     *            record number pour lequel supprimer les annonces
     * @param typeAnnonce
     *            type d'annonce � supprimer
     * @param excludeLast
     *            indique si la derni�re annonce doit �tre ignor�e
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void deleteAnnoncesForRecordNumber(String recordNumber, RafamTypeAnnonce typeAnnonce, boolean excludeLast)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime les annonces pour un droit et un �tat donn�
     * 
     * @param idDroit
     *            id du droit pour lequel supprimer les annonces
     * @param etat
     *            etat dans lesquels les annonces � supprimer doivent �tre (RafamEtatAnnonce.A_TRANSMETTRE ou
     *            RafamEtatAnnonce_ENREGISTRE)
     * @return nombre d'annonces supprim�es
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public int deleteForEtat(String idDroit, RafamEtatAnnonce etat) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime les annonces pour un droit et un �tat donn�
     *
     * @param idDroit
     *            id du droit pour lequel supprimer les annonces
     * @param etat
     *            etat dans lesquels les annonces � supprimer doivent �tre (RafamEtatAnnonce.A_TRANSMETTRE ou
     *            RafamEtatAnnonce_ENREGISTRE)
     *  @param year
     *      Sp�cifie l'ann�e de l'annonce � supprimer : pour les annonces ADI
     * @return nombre d'annonces supprim�es
     *
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public int deleteForEtatYear(String idDroit, RafamEtatAnnonce etat, String years) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime les annonces non transmises pour un droit
     * 
     * @param idDroit
     *            id du droit pour lequel supprimer les annonces
     * @return nombre d'annonces supprim�es
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public int deleteNotSent(String idDroit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime les annonces non transmises pour un record number
     * 
     * @param idDroit
     *            id du droit pour lequel supprimer les annonces
     * @return nombre d'annonces supprim�es
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public int deleteNotSentForRecordNumber(String recordNumber) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime les annonce refus�e correspondant � un record number
     * 
     * @param recordNumber
     *            record number pour lequel supprimer les annonces
     * @return nombre d'annonces supprim�es
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * 
     * @see AnnonceRafamBusinessService#deleteRefuseesForDroit(String)
     */
    public int deleteRefusees(String recordNumber) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime les annonces refus�es pour un droit
     * 
     * @param idDroit
     *            id du droit pour lequel supprimer les annonces
     * @return nombre d'annonces supprim�es
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * 
     * @see AnnonceRafamBusinessService#deleteRefusees(String)
     */
    public int deleteRefuseesForDroit(String idDroit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne le code syst�me correspondant au code sexe provenant de la centrale
     * 
     * @param sexeEnfantRafam
     *            code sexe provenant de la centrale
     * @return le code syst�me correspondant
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public String getSexeCS(String sexeEnfantRafam) throws JadeApplicationException;

    /**
     * V�rifie s'il s'agit d'un cas de naissance horlog�re. Ce type de prestation correspond aux cas pour lesquels
     * aucune prestation de naissance/accueil n'est normalement vers�e mais pour lesquel les caisses horlog�res versent
     * une prestation selon leurs tarifs
     * 
     * @return <code>true</code> s'il s'agit d'un cas de naissance holog�re
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isNaissanceHorlogere(ContextAnnonceRafam context) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Change les NSS stock�s dans une annonces.
     * Si l'un des NSS pass� en param�tre est vide, le NSS correspondant dans l'annonce est laiss� dans sont �tat actuel
     * 
     * 
     * @param annonce
     *            l'annonce � laquelle ajouter le message
     * @param nssAllocataire
     *            le nouveau NSS pour l'allocataire
     * @param nssEnfant
     *            le nouveau NSS pour l'enfant
     * 
     * @return l'annonce mise � jour
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AnnonceRafamModel setNSS(AnnonceRafamModel annonce, String nssAllocataire, String nssEnfant)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Ajoute un message d'erreur interne � une annonce.
     * 
     * En plus de l'ajout du message, le champ <code>internalError</code> passe � <code>true</code>
     * 
     * @param annonce
     *            l'annonce � laquelle ajouter le message
     * @param message
     *            le message � ajouter
     * 
     * @return l'annonce mise � jour
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AnnonceRafamModel setError(AnnonceRafamModel annonce, String message) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Change l'�tat d'une annonce
     * 
     * @param annonce
     *            l'annonce devant �tre modifi�e
     * @param etat
     *            nouvel �tat de l'annonce
     * 
     * @return l'annonce mise � jour
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AnnonceRafamModel setEtat(AnnonceRafamModel annonce, RafamEtatAnnonce etat) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Passer � "ex�cut�" le code de retour des annonces correspondant � un record number. Seules les annonces ayant
     * l'�tat "valid�" et le code "En attente" sont modifi�es
     * 
     * @param recordNumber
     *            le record number pour lequel les annonces doivent �tre modifi�es
     * @param excludeLast
     *            indique si la derni�re annonce doit �tre ignor�e
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void validateEnAttenteForRecordNumber(String recordNumber, boolean excludeLast)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Passer � "valid�" l'�tat des annonces correspondant � un record number. Seules les annonces ayant l'�tat "re�u"
     * sont modifi�es
     * 
     * @param recordNumber
     *            le record number pour lequel les annonces doivent �tre modifi�es
     * @param excludeLast
     *            indique si la derni�re annonce doit �tre ignor�e
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public void validateForRecordNumber(String recordNumber, boolean excludeLast) throws JadeApplicationException,
            JadePersistenceException;
}
