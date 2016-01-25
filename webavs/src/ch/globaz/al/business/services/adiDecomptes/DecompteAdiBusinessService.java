package ch.globaz.al.business.services.adiDecomptes;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.models.adi.AdiSaisieComplexSearchModel;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;

/**
 * Services m�tier li�s aux d�comptes ADI
 * 
 * @author GMO
 * 
 */
public interface DecompteAdiBusinessService extends JadeApplicationService {

    /**
     * Mettre le d�compte li� � la prestation ADI en CO
     * 
     * @param idEnteteAdi
     *            id de la prestation ADI
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void comptabiliserDecompteLie(String idEnteteAdi) throws JadeApplicationException, JadePersistenceException;

    /**
     * Contr�le pour un d�compte si toutes les saisies n�cessaires ont bien �t� effectu�es.
     * 
     * @param decompte
     *            le d�compte dont on veut contr�ler la saisie
     * @param prestationTravail
     *            les d�tails de la prestation de travail engendrant une saisie
     * @param saisiesExistantes
     *            les saisies d�j� existantes du d�compte
     * @return hashmap incluantt pour chaque mois du d�compte le statut (-1 : rien � faire, 0 : � saisir, 1: saisi (ok))
     *         + 1 cl� isOk indiquant true / false pour la saisie globale du d�compte
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public HashMap controleSaisieDecompte(DecompteAdiModel decompte,
            DetailPrestationComplexSearchModel prestationTravail, AdiSaisieComplexSearchModel saisiesExistantes)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * G�n�re la prestation ADI du d�compte, extourne �ventuellement d�j� meme p�riode comptabilis� en ADI. et remplit
     * les champs suivants du d�compte existant pass� en param�tre:
     * <ul>
     * <li>DecompteAdiModel.idPrestationAdi</li>
     * <li>DecompteAdiModel.idDecompteRemplace</li>
     * </ul>
     * 
     * @param decompte
     *            le d�compte pour lequel g�n�rer la prestation ADI
     * @param periodeTraitement
     *            la p�riode � utiliser pour d�finir la p�riode de r�cap
     * @param numFacture
     *            Num�ro de facture
     * @param numProcessus
     *            Num�ro du processus m�tier de paiement li�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void genererPrestationAdi(DecompteAdiModel decompte, String periodeTraitement, String numFacture,
            String numProcessus) throws JadePersistenceException, JadeApplicationException;

    /**
     * G�n�re les prestations potentiellement manquantes ou retourne les prestations dans la p�riode donn�e selon la
     * valeur du param�tre ADI_AUTO_PRESTATIONS_CH {@link ALConstParametres}
     * 
     * @param idDossier
     *            id du dossier dont on veut compl�ter les �ventuelles prestations manquantes
     * @param periodeDebut
     *            d�but de la p�riode � v�rifier
     * @param periodeFin
     *            fin de la p�riode � v�rifier
     * @return DetailPrestationComplexSearchModel les d�tails de prestations de travail correspondantes au d�compte
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise
     */
    public DetailPrestationComplexSearchModel getPrestationsTravailDossier(String idDossier, String periodeDebut,
            String periodeFin) throws JadeApplicationException, JadePersistenceException;

    /**
     * Contr�le si toutes les saisies ont �t� effectu�es sur la base d'une liste formatt�e
     * 
     * @param listeSaisie
     *            les saisies
     * @return vrai ou faux
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isSaisieComplete(HashMap<String, HashMap> listeSaisie) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Supprime les prestations de travail (statut CH, �tat TMP) du dossier concern� et qui couvrent des p�riodes de
     * l'intervalle sp�fici�
     * 
     * @param idDossier
     *            id du dossier concern�
     * @param periodeDebut
     *            d�but de l'intervalle
     * @param periodeFin
     *            fin de l'intervalle
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void supprimerPrestationTravailDossier(String idDossier, String periodeDebut, String periodeFin)
            throws JadeApplicationException, JadePersistenceException;

}
