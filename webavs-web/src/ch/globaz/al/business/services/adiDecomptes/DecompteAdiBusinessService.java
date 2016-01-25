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
 * Services métier liés aux décomptes ADI
 * 
 * @author GMO
 * 
 */
public interface DecompteAdiBusinessService extends JadeApplicationService {

    /**
     * Mettre le décompte lié à la prestation ADI en CO
     * 
     * @param idEnteteAdi
     *            id de la prestation ADI
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void comptabiliserDecompteLie(String idEnteteAdi) throws JadeApplicationException, JadePersistenceException;

    /**
     * Contrôle pour un décompte si toutes les saisies nécessaires ont bien été effectuées.
     * 
     * @param decompte
     *            le décompte dont on veut contrôler la saisie
     * @param prestationTravail
     *            les détails de la prestation de travail engendrant une saisie
     * @param saisiesExistantes
     *            les saisies déjà existantes du décompte
     * @return hashmap incluantt pour chaque mois du décompte le statut (-1 : rien à faire, 0 : à saisir, 1: saisi (ok))
     *         + 1 clé isOk indiquant true / false pour la saisie globale du décompte
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public HashMap controleSaisieDecompte(DecompteAdiModel decompte,
            DetailPrestationComplexSearchModel prestationTravail, AdiSaisieComplexSearchModel saisiesExistantes)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Génère la prestation ADI du décompte, extourne éventuellement déjà meme période comptabilisé en ADI. et remplit
     * les champs suivants du décompte existant passé en paramètre:
     * <ul>
     * <li>DecompteAdiModel.idPrestationAdi</li>
     * <li>DecompteAdiModel.idDecompteRemplace</li>
     * </ul>
     * 
     * @param decompte
     *            le décompte pour lequel générer la prestation ADI
     * @param periodeTraitement
     *            la période à utiliser pour définir la période de récap
     * @param numFacture
     *            Numéro de facture
     * @param numProcessus
     *            Numéro du processus métier de paiement lié
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void genererPrestationAdi(DecompteAdiModel decompte, String periodeTraitement, String numFacture,
            String numProcessus) throws JadePersistenceException, JadeApplicationException;

    /**
     * Génère les prestations potentiellement manquantes ou retourne les prestations dans la période donnée selon la
     * valeur du paramètre ADI_AUTO_PRESTATIONS_CH {@link ALConstParametres}
     * 
     * @param idDossier
     *            id du dossier dont on veut compléter les éventuelles prestations manquantes
     * @param periodeDebut
     *            début de la période à vérifier
     * @param periodeFin
     *            fin de la période à vérifier
     * @return DetailPrestationComplexSearchModel les détails de prestations de travail correspondantes au décompte
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise
     */
    public DetailPrestationComplexSearchModel getPrestationsTravailDossier(String idDossier, String periodeDebut,
            String periodeFin) throws JadeApplicationException, JadePersistenceException;

    /**
     * Contrôle si toutes les saisies ont été effectuées sur la base d'une liste formattée
     * 
     * @param listeSaisie
     *            les saisies
     * @return vrai ou faux
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isSaisieComplete(HashMap<String, HashMap> listeSaisie) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Supprime les prestations de travail (statut CH, état TMP) du dossier concerné et qui couvrent des périodes de
     * l'intervalle spéficié
     * 
     * @param idDossier
     *            id du dossier concerné
     * @param periodeDebut
     *            début de l'intervalle
     * @param periodeFin
     *            fin de l'intervalle
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void supprimerPrestationTravailDossier(String idDossier, String periodeDebut, String periodeFin)
            throws JadeApplicationException, JadePersistenceException;

}
