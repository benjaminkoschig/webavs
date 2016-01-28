package globaz.apg.business.service;

import globaz.apg.pojo.NombreEnfants;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.math.BigDecimal;

/**
 * Calculateur de montant journalier APG selon le salaire journalier et (si n�cessaire) le nombre d'enfantsl.<br/>
 * <br/>
 * Se r�f�rer aux documents de la conf�d�ration (Prescriptions de calculs des allocations journali�res APG) pour les
 * formules de calculs
 * 
 * @author PBA
 */
public interface APMontantJournalierApgService extends JadeApplicationService {

    /**
     * Calcul et retourne le montant journalier des APG pour un cadres en service long, selon le salaire journalier et
     * le nombre d'enfants pass�s en param�tres
     * 
     * @param salaireJournalier
     *            le salaire journalier du b�n�ficiaire de l'APG (calcul� par le service
     *            {@link APSalaireJournalierApgService})
     * @param nombreEnfants
     *            le nombre d'enfant du b�n�ficiaire des APG
     * @return le montant journalier qui sera vers� au b�n�ficiaire pour chaque jour de sa p�riode APG
     */
    public BigDecimal getMontantJournalierApgCadresServiceLong(BigDecimal salaireJournalier, NombreEnfants nombreEnfants);

    /**
     * Calcul et retourne le montant journalier pour une APG maternit�, selon le salaire journalier pass� en param�tre
     * 
     * @param salaireJournalier
     *            le salaire journalier du b�n�ficiaire de l'APG (calcul� par le service
     *            {@link APSalaireJournalierApgService})
     * @return le montant journalier qui sera vers� au b�n�ficiaire pour chaque jour de sa p�riode APG
     */
    public BigDecimal getMontantJournalierApgMaternite(BigDecimal salaireJournalier);

    /**
     * Calcul et retourne le montant journalier des APG pour une recrue, selon le salaire journalier et le nombre
     * d'enfants pass�s en param�tres
     * 
     * @param salaireJournalier
     *            le salaire journalier du b�n�ficiaire de l'APG (calcul� par le service
     *            {@link APSalaireJournalierApgService})
     * @param nombreEnfants
     *            le nombre d'enfant du b�n�ficiaire des APG
     * @return le montant journalier qui sera vers� au b�n�ficiaire pour chaque jour de sa p�riode APG
     */
    public BigDecimal getMontantJournalierApgRecrue(BigDecimal salaireJournalier, NombreEnfants nombreEnfants);

    /**
     * Calcul et retourne le montant journalier des APG pour un service d'avancement (�cole de sous-officier, �cole
     * d'officier, paiement de gallons, etc...), selon le salaire journalier et le nombre d'enfants pass�s en param�tres
     * 
     * @param salaireJournalier
     *            le salaire journalier du b�n�ficiaire de l'APG (calcul� par le service
     *            {@link APSalaireJournalierApgService})
     * @param nombreEnfants
     *            le nombre d'enfant du b�n�ficiaire des APG
     * @return le montant journalier qui sera vers� au b�n�ficiaire pour chaque jour de sa p�riode APG
     */
    public BigDecimal getMontantJournalierApgServiceAvancement(BigDecimal salaireJournalier, NombreEnfants nombreEnfants);

    /**
     * Calcul et retourne le montant journalier des APG pour un service normal, selon le salaire journalier et le nombre
     * d'enfants pass�s en param�tres
     * 
     * @param salaireJournalier
     *            le salaire journalier du b�n�ficiaire de l'APG (calcul� par le service
     *            {@link APSalaireJournalierApgService})
     * @param nombreEnfants
     *            le nombre d'enfant du b�n�ficiaire des APG
     * @return le montant journalier qui sera vers� au b�n�ficiaire pour chaque jour de sa p�riode APG
     */
    public BigDecimal getMontantJournalierApgServiceNormal(BigDecimal salaireJournalier, NombreEnfants nombreEnfants);
}
