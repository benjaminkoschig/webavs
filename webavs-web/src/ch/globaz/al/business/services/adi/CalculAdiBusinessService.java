package ch.globaz.al.business.services.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.adi.AdiEnfantMoisComplexSearchModel;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;

/**
 * Interface fournissant tous les services m�tier li�s au calcul des ADI
 * 
 * @author GMO
 * 
 */
public interface CalculAdiBusinessService extends JadeApplicationService {

    /**
     * 
     * Calcul et m�morise pour un d�compte donn� le r�sultat dans la table correspondante (ADIEnfantMois)
     * 
     * @param decompte
     *            D�compte ADI
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void calculForDecompte(DecompteAdiModel decompte) throws JadePersistenceException, JadeApplicationException;

    /**
     * Calcul et m�morise pour un d�compte donn� le r�sultat dans la table correspondante (ADIEnfantMois) pour un droit
     * pr�cis
     * 
     * @param decompte
     *            D�compte ADI
     * @param idDroit
     *            id du droit
     * @param prestationsTravail
     *            R�sultat d'une recherche de prestations de travail
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void calculForDroit(DecompteAdiModel decompte, String idDroit,
            DetailPrestationComplexSearchModel prestationsTravail) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * REtourne le total format� du solde � payer pour un d�compte (prend en compte les prestations d�j� pay�es dans un
     * �ventuel pr�c�dent d�compte)
     * 
     * @param idDecompte
     *            l'id du d�compte dont on veut le solde
     * @return le solde � payer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getSoldeAPayer(String idDecompte) throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne la somme des prestations d�j� vers�e pour la p�riode du d�compte pass� en param�tre
     * 
     * @param idDecompte
     *            l'id du d�compte dont il faut v�rifier les prestations d�j� vers�es
     * @return la somme d�j� vers�e
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getSommeDejaVersee(String idDecompte) throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne le taux de change � appliquer selon la monnaie et la p�riode de calcul
     * 
     * @param csMonnaie
     *            - constante d�finissant le taux de la bonne monnaie � r�cup�rer
     * @param periodeCalcul
     *            P�riode � calculer
     * @return le taux appliqu� pour la p�riode
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTauxAFForPeriode(String csMonnaie, String periodeCalcul) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Additionne tous les montants dus par enfant par mois du d�compte
     * 
     * @param searchComplexModel
     *            mod�le de recherche charg� avec les adi par enfant et par mois (calcul)
     * @return le montant total format 0.00 (au 5 centimes
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTotalDecompte(AdiEnfantMoisComplexSearchModel searchComplexModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne le total vers� par droit pour un d�compte, avec les suppl�ments r�partis (MEN, FNB,...)
     * 
     * @param idDecompte
     *            id du d�compte
     * @param idDroit
     *            id de du droit
     * @return le total vers� pour cet enfant
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTotalVerseParDroitAvecSupp(String idDecompte, String idDroit) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne le total vers� par droit pour un d�compte sans les suppl�ments r�partis (que l'allocation ENF,FORM,NAIS)
     * 
     * @param idDecompte
     *            id du d�compte
     * @param idDroit
     *            id du droit
     * @return le total vers� pour cet enfant
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTotalVerseParDroitSansSupp(String idDecompte, String idDroit) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne le total vers� par enfant pour un d�compte, avec les suppl�ments r�partis (MEN, FNB,...)
     * 
     * @param idDecompte
     *            id du d�compte
     * @param idEnfant
     *            id de l'enfant
     * @return le total vers� pour cet enfant
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTotalVerseParEnfantAvecSupp(String idDecompte, String idEnfant) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne le total vers� par enfant pour un d�compte sans les suppl�ments r�partis (que l'allocation
     * ENF,FORM,NAIS)
     * 
     * @param idDecompte
     *            id du d�compte
     * @param idEnfant
     *            id de l'enfant
     * @return le total vers� pour cet enfant
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTotalVerseParEnfantSansSupp(String idDecompte, String idEnfant) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Retourne le total vers� des suppl�ments (MEN,FNB,...) pour un d�compte
     * 
     * @param idDecompte
     *            id du d�compte
     * @return le total vers� des suppl�ments
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public String getTotalVerseSupp(String idDecompte) throws JadeApplicationException, JadePersistenceException;

}
