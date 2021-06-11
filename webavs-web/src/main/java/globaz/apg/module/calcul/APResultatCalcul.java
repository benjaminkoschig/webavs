/*
 * Créé le 11 mai 05
 *
 * Description :
 */
package globaz.apg.module.calcul;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import globaz.apg.enums.APTypeDePrestation;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JADate;
import lombok.Setter;

/**
 * <H1>Description</H1>
 *
 * @author scr
 *
 *         <p>
 *         Descpription : Contient les données du résultat du calcul d'une prestation APG
 *         </p>
 */
public class APResultatCalcul {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private FWCurrency allocationJournaliereExploitation = null;
    private FWCurrency allocationJournaliereMaxFraisGarde = null;
    private FWCurrency basicDailyAmount = null;
    private JADate dateDebut = null;
    private JADate dateFin = null;

    private String idTauxImposition = "";
    private FWCurrency montantJournalier = null;

    private int nombreJoursSoldes = 0;
    private int nombreJoursSupplementaires = 0;
    @Setter
    private List<APResultatCalculSituationProfessionnel> resultatsCalculsSitProfessionnelle = null;
    public FWCurrency revenuDeterminantMoyen = null;
    private FWCurrency revenuJournalierIndependantTotal = new FWCurrency(0);
    private String revision = null;

    private FWCurrency salaireJournalierTotal = new FWCurrency(0);
    private boolean soumisImpotSource;

    private String tauxImposition = "";

    private FWCurrency TL = null;
    private String typeAllocation = null;
    private FWCurrency versementAssure = null;

    private String csGenrePrestion = APTypeDePrestation.STANDARD.getCodesystemString();

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APResultatCalcul.
     */
    public APResultatCalcul() {
        resultatsCalculsSitProfessionnelle = new ArrayList();
    }

    /**
     * DOCUMENT ME!
     *
     * @param resultat
     *                     DOCUMENT ME!
     */
    public void addResultatCalculSitProfessionnelle(APResultatCalculSituationProfessionnel resultat) {
        if (resultat.isIndependant()) {
            if (resultat.getSalaireJournalierNonArrondi() != null) {
                revenuJournalierIndependantTotal.add(resultat.getSalaireJournalierNonArrondi());
            }
        } else {
            if (resultat.getSalaireJournalierNonArrondi() != null) {
                salaireJournalierTotal.add(resultat.getSalaireJournalierNonArrondi());
            }
        }

        resultatsCalculsSitProfessionnelle.add(resultat);
    }

    /**
     * getter pour l'attribut allocation journaliere exploitation
     *
     * @return la valeur courante de l'attribut allocation journaliere exploitation
     */
    public FWCurrency getAllocationJournaliereExploitation() {
        return allocationJournaliereExploitation;
    }

    /**
     * getter pour l'attribut allocation journaliere max frais garde
     *
     * @return la valeur courante de l'attribut allocation journaliere max frais garde
     */
    public FWCurrency getAllocationJournaliereMaxFraisGarde() {
        return new FWCurrency(allocationJournaliereMaxFraisGarde.toString());
    }

    /**
     * @return the basicDailyAmount
     */
    public FWCurrency getBasicDailyAmount() {
        return basicDailyAmount;
    }

    /**
     * getter pour l'attribut date debut
     *
     * @return la valeur courante de l'attribut date debut
     */
    public JADate getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date fin
     *
     * @return la valeur courante de l'attribut date fin
     */
    public JADate getDateFin() {
        return dateFin;
    }

    public String getIdTauxImposition() {
        return idTauxImposition;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public FWCurrency getMontantJournalier() {
        return new FWCurrency(montantJournalier.toString());
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut nombre jours soldes
     *
     * @return la valeur courante de l'attribut nombre jours soldes
     */
    public int getNombreJoursSoldes() {
        return nombreJoursSoldes;
    }

    public int getNombreJoursSupplementaires() {
        return nombreJoursSupplementaires;
    }

    /**
     * getter pour l'attribut resultats calculs sit professionnelle
     *
     * @return la valeur courante de l'attribut resultats calculs sit professionnelle
     */
    public List<APResultatCalculSituationProfessionnel> getResultatsCalculsSitProfessionnelle() {
        return resultatsCalculsSitProfessionnelle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public FWCurrency getRevenuDeterminantMoyen() {
        return new FWCurrency(revenuDeterminantMoyen.toString());
    }

    /**
     * getter pour l'attribut montant total independant
     *
     * @return la valeur courante de l'attribut montant total independant
     */
    public FWCurrency getRevenuJournalierIndependantTotal() {
        return revenuJournalierIndependantTotal;
    }

    /**
     * getter pour l'attribut revision
     *
     * @return la valeur courante de l'attribut revision
     */
    public String getRevision() {
        return revision;
    }

    /**
     * getter pour l'attribut montant total salarie
     *
     * @return la valeur courante de l'attribut montant total salarie
     */
    public FWCurrency getSalaireJournalierTotal() {
        return salaireJournalierTotal;
    }

    public String getTauxImposition() {
        return tauxImposition;
    }

    /**
     * @return
     */
    public FWCurrency getTL() {
        return TL;
    }

    /**
     * getter pour l'attribut type allocation
     *
     * @return la valeur courante de l'attribut type allocation
     */
    public String getTypeAllocation() {
        return typeAllocation;
    }

    /**
     * getter pour l'attribut versement assure
     *
     * @return la valeur courante de l'attribut versement assure
     */
    public FWCurrency getVersementAssure() {
        return versementAssure;
    }

    /**
     * getter pour l'attribut independant et salarie
     *
     * @return la valeur courante de l'attribut independant et salarie
     */
    public boolean isIndependantEtSalarie() {
        return !salaireJournalierTotal.isZero() && !revenuJournalierIndependantTotal.isZero();
    }

    public boolean isSoumisImpotSource() {
        return soumisImpotSource;
    }

    /**
     * retourne vrai si l'assuré est l'unique bénéficiaire des prestations (ses employeurs ne recoivent rien) ou si
     * l'assuré n'a pas d'employeurs.
     *
     * @return vrai si l'assuré recoit toutes les prestations ou si l'assuré n'a pas d'employeurs.
     */
    public boolean isVersementEmployeUniquement() {
        if (resultatsCalculsSitProfessionnelle.isEmpty()) {
            return true;
        }

        boolean retValue = true;

        for (Iterator sitPros = resultatsCalculsSitProfessionnelle.iterator(); retValue && sitPros.hasNext();) {
            APResultatCalculSituationProfessionnel sitPro = (APResultatCalculSituationProfessionnel) sitPros.next();

            retValue = !sitPro.isVersementEmployeur() && retValue;
        }

        return retValue;
    }

    /**
     * setter pour l'attribut allocation journaliere exploitation
     *
     * @param currency
     *                     une nouvelle valeur pour cet attribut
     */
    public void setAllocationJournaliereExploitation(FWCurrency currency) {
        allocationJournaliereExploitation = currency;
    }

    /**
     * setter pour l'attribut allocation journaliere max frais garde
     *
     * @param currency
     *                     une nouvelle valeur pour cet attribut
     */
    public void setAllocationJournaliereMaxFraisGarde(FWCurrency currency) {
        allocationJournaliereMaxFraisGarde = currency;
    }

    /**
     * @param basicDailyAmount
     *                             the basicDailyAmount to set
     */
    public void setBasicDailyAmount(FWCurrency basicDailyAmount) {
        this.basicDailyAmount = basicDailyAmount;
    }

    /**
     * setter pour l'attribut date debut
     *
     * @param date
     *                 une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(JADate date) {
        dateDebut = date;
    }

    /**
     * setter pour l'attribut date fin
     *
     * @param date
     *                 une nouvelle valeur pour cet attribut
     */
    public void setDateFin(JADate date) {
        dateFin = date;
    }

    public void setIdTauxImposition(String idTauxImposition) {
        this.idTauxImposition = idTauxImposition;
    }

    /**
     * DOCUMENT ME!
     *
     * @param currency
     */
    public void setMontantJournalier(FWCurrency currency) {
        montantJournalier = currency;
    }

    /**
     * setter pour l'attribut nombre jours soldes
     *
     * @param i
     *              une nouvelle valeur pour cet attribut
     */
    public void setNombreJoursSoldes(int i) {
        nombreJoursSoldes = i;
    }

    public void setNombreJoursSupplementaires(int nombreJoursSupplementaires) {
        this.nombreJoursSupplementaires = nombreJoursSupplementaires;
    }

    /**
     * DOCUMENT ME!
     *
     * @param currency
     */
    public void setRevenuDeterminantMoyen(FWCurrency currency) {
        revenuDeterminantMoyen = currency;
    }

    /**
     * setter pour l'attribut montant total independant
     *
     * @param montantTotalIndependant
     *                                    une nouvelle valeur pour cet attribut
     */
    public void setRevenuJournalierIndependantTotal(FWCurrency montantTotalIndependant) {
        revenuJournalierIndependantTotal = montantTotalIndependant;
    }

    /**
     * setter pour l'attribut revision
     *
     * @param string
     *                   une nouvelle valeur pour cet attribut
     */
    public void setRevision(String string) {
        revision = string;
    }

    /**
     * setter pour l'attribut montant total salarie
     *
     * @param montantTotalSalarie
     *                                une nouvelle valeur pour cet attribut
     */
    public void setSalaireJournalierTotal(FWCurrency montantTotalSalarie) {
        salaireJournalierTotal = montantTotalSalarie;
    }

    public void setSoumisImpotSource(boolean soumisImpotSource) {
        this.soumisImpotSource = soumisImpotSource;
    }

    public void setTauxImposition(String tauxImposition) {
        this.tauxImposition = tauxImposition;
    }

    /**
     * @param currency
     */
    public void setTL(FWCurrency currency) {
        TL = currency;
    }

    /**
     * setter pour l'attribut type allocation
     *
     * @param string
     *                   une nouvelle valeur pour cet attribut
     */
    public void setTypeAllocation(String string) {
        typeAllocation = string;
    }

    /**
     * setter pour l'attribut versement assure
     *
     * @param versementAssure
     *                            une nouvelle valeur pour cet attribut
     */
    public void setVersementAssure(FWCurrency versementAssure) {
        this.versementAssure = versementAssure;
    }

    public String getCsGenrePrestion() {
        return csGenrePrestion;
    }

    public void setCsGenrePrestion(String csGenrePrestion) {
        this.csGenrePrestion = csGenrePrestion;
    }

}
