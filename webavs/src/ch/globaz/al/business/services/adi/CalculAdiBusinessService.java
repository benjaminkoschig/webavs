package ch.globaz.al.business.services.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.adi.AdiEnfantMoisComplexSearchModel;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;

/**
 * Interface fournissant tous les services métier liés au calcul des ADI
 * 
 * @author GMO
 * 
 */
public interface CalculAdiBusinessService extends JadeApplicationService {

    /**
     * 
     * Calcul et mémorise pour un décompte donné le résultat dans la table correspondante (ADIEnfantMois)
     * 
     * @param decompte
     *            Décompte ADI
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void calculForDecompte(DecompteAdiModel decompte) throws JadePersistenceException, JadeApplicationException;

    /**
     * Calcul et mémorise pour un décompte donné le résultat dans la table correspondante (ADIEnfantMois) pour un droit
     * précis
     * 
     * @param decompte
     *            Décompte ADI
     * @param idDroit
     *            id du droit
     * @param prestationsTravail
     *            Résultat d'une recherche de prestations de travail
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void calculForDroit(DecompteAdiModel decompte, String idDroit,
            DetailPrestationComplexSearchModel prestationsTravail) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * REtourne le total formaté du solde à payer pour un décompte (prend en compte les prestations déjà payées dans un
     * éventuel précédent décompte)
     * 
     * @param idDecompte
     *            l'id du décompte dont on veut le solde
     * @return le solde à payer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getSoldeAPayer(String idDecompte) throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne la somme des prestations déjà versée pour la période du décompte passé en paramètre
     * 
     * @param idDecompte
     *            l'id du décompte dont il faut vérifier les prestations déjà versées
     * @return la somme déjà versée
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getSommeDejaVersee(String idDecompte) throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne le taux de change à appliquer selon la monnaie et la période de calcul
     * 
     * @param csMonnaie
     *            - constante définissant le taux de la bonne monnaie à récupérer
     * @param periodeCalcul
     *            Période à calculer
     * @return le taux appliqué pour la période
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTauxAFForPeriode(String csMonnaie, String periodeCalcul) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Additionne tous les montants dus par enfant par mois du décompte
     * 
     * @param searchComplexModel
     *            modèle de recherche chargé avec les adi par enfant et par mois (calcul)
     * @return le montant total format 0.00 (au 5 centimes
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTotalDecompte(AdiEnfantMoisComplexSearchModel searchComplexModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne le total versé par droit pour un décompte, avec les suppléments répartis (MEN, FNB,...)
     * 
     * @param idDecompte
     *            id du décompte
     * @param idDroit
     *            id de du droit
     * @return le total versé pour cet enfant
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTotalVerseParDroitAvecSupp(String idDecompte, String idDroit) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne le total versé par droit pour un décompte sans les suppléments répartis (que l'allocation ENF,FORM,NAIS)
     * 
     * @param idDecompte
     *            id du décompte
     * @param idDroit
     *            id du droit
     * @return le total versé pour cet enfant
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTotalVerseParDroitSansSupp(String idDecompte, String idDroit) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne le total versé par enfant pour un décompte, avec les suppléments répartis (MEN, FNB,...)
     * 
     * @param idDecompte
     *            id du décompte
     * @param idEnfant
     *            id de l'enfant
     * @return le total versé pour cet enfant
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTotalVerseParEnfantAvecSupp(String idDecompte, String idEnfant) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne le total versé par enfant pour un décompte sans les suppléments répartis (que l'allocation
     * ENF,FORM,NAIS)
     * 
     * @param idDecompte
     *            id du décompte
     * @param idEnfant
     *            id de l'enfant
     * @return le total versé pour cet enfant
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTotalVerseParEnfantSansSupp(String idDecompte, String idEnfant) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne le total versé des suppléments (MEN,FNB,...) pour un décompte
     * 
     * @param idDecompte
     *            id du décompte
     * @return le total versé des suppléments
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public String getTotalVerseSupp(String idDecompte) throws JadeApplicationException, JadePersistenceException;

}
