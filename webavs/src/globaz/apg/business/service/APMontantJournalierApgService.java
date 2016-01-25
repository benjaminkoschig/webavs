package globaz.apg.business.service;

import globaz.apg.pojo.NombreEnfants;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.math.BigDecimal;

/**
 * Calculateur de montant journalier APG selon le salaire journalier et (si nécessaire) le nombre d'enfantsl.<br/>
 * <br/>
 * Se référer aux documents de la confédération (Prescriptions de calculs des allocations journalières APG) pour les
 * formules de calculs
 * 
 * @author PBA
 */
public interface APMontantJournalierApgService extends JadeApplicationService {

    /**
     * Calcul et retourne le montant journalier des APG pour un cadres en service long, selon le salaire journalier et
     * le nombre d'enfants passés en paramètres
     * 
     * @param salaireJournalier
     *            le salaire journalier du bénéficiaire de l'APG (calculé par le service
     *            {@link APSalaireJournalierApgService})
     * @param nombreEnfants
     *            le nombre d'enfant du bénéficiaire des APG
     * @return le montant journalier qui sera versé au bénéficiaire pour chaque jour de sa période APG
     */
    public BigDecimal getMontantJournalierApgCadresServiceLong(BigDecimal salaireJournalier, NombreEnfants nombreEnfants);

    /**
     * Calcul et retourne le montant journalier pour une APG maternité, selon le salaire journalier passé en paramètre
     * 
     * @param salaireJournalier
     *            le salaire journalier du bénéficiaire de l'APG (calculé par le service
     *            {@link APSalaireJournalierApgService})
     * @return le montant journalier qui sera versé au bénéficiaire pour chaque jour de sa période APG
     */
    public BigDecimal getMontantJournalierApgMaternite(BigDecimal salaireJournalier);

    /**
     * Calcul et retourne le montant journalier des APG pour une recrue, selon le salaire journalier et le nombre
     * d'enfants passés en paramètres
     * 
     * @param salaireJournalier
     *            le salaire journalier du bénéficiaire de l'APG (calculé par le service
     *            {@link APSalaireJournalierApgService})
     * @param nombreEnfants
     *            le nombre d'enfant du bénéficiaire des APG
     * @return le montant journalier qui sera versé au bénéficiaire pour chaque jour de sa période APG
     */
    public BigDecimal getMontantJournalierApgRecrue(BigDecimal salaireJournalier, NombreEnfants nombreEnfants);

    /**
     * Calcul et retourne le montant journalier des APG pour un service d'avancement (école de sous-officier, école
     * d'officier, paiement de gallons, etc...), selon le salaire journalier et le nombre d'enfants passés en paramètres
     * 
     * @param salaireJournalier
     *            le salaire journalier du bénéficiaire de l'APG (calculé par le service
     *            {@link APSalaireJournalierApgService})
     * @param nombreEnfants
     *            le nombre d'enfant du bénéficiaire des APG
     * @return le montant journalier qui sera versé au bénéficiaire pour chaque jour de sa période APG
     */
    public BigDecimal getMontantJournalierApgServiceAvancement(BigDecimal salaireJournalier, NombreEnfants nombreEnfants);

    /**
     * Calcul et retourne le montant journalier des APG pour un service normal, selon le salaire journalier et le nombre
     * d'enfants passés en paramètres
     * 
     * @param salaireJournalier
     *            le salaire journalier du bénéficiaire de l'APG (calculé par le service
     *            {@link APSalaireJournalierApgService})
     * @param nombreEnfants
     *            le nombre d'enfant du bénéficiaire des APG
     * @return le montant journalier qui sera versé au bénéficiaire pour chaque jour de sa période APG
     */
    public BigDecimal getMontantJournalierApgServiceNormal(BigDecimal salaireJournalier, NombreEnfants nombreEnfants);
}
