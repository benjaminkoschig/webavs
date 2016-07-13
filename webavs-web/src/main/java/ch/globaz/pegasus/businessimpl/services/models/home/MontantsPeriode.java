package ch.globaz.pegasus.businessimpl.services.models.home;

import ch.globaz.pegasus.businessimpl.services.models.home.MontantPeriodePrixChambre.TypeMontant;

public class MontantsPeriode {

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    private MontantPeriodePrixChambre prixChambre = null;
    private MontantPeriodePrixChambre montantPlafond = null;
    private String dateDebut = null;
    private String dateFin = null;

    public String getDateFin() {
        return dateFin;
    }

    public MontantsPeriode() {
    }

    public MontantsPeriode(MontantPeriodePrixChambre prixChambre, MontantPeriodePrixChambre montantPlafond,
            String dateDebut, String dateFin) {
        this.prixChambre = prixChambre;
        this.montantPlafond = montantPlafond;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public void addMontant(MontantPeriodePrixChambre montant, String dateDebut, String dateFin) {
        switch (montant.getType()) {
            case MONTANT_PLAFOND_ADMIS:
                montantPlafond = montant;
                break;

            case PRIX_JOURNALIER:
                prixChambre = montant;
        }

        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public void addMontant(MontantPeriodePrixChambre montant) {
        switch (montant.getType()) {
            case MONTANT_PLAFOND_ADMIS:
                montantPlafond = montant;
                break;

            case PRIX_JOURNALIER:
                prixChambre = montant;
        }
    }

    public MontantPeriodePrixChambre getPrixChambre() {
        return prixChambre;
    }

    public MontantPeriodePrixChambre getMontantPlafond() {
        return montantPlafond;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    @Override
    public String toString() {
        return "MontantsPeriode [dateDebutPeriode: " + dateDebut + ", prixChambre=" + prixChambre + ", montantPlafond="
                + montantPlafond + "]";
    }

    public MontantsPeriode(MontantPeriodePrixChambre montant) {
        super();
        switch (montant.getType()) {
            case MONTANT_PLAFOND_ADMIS:
                montantPlafond = montant;
                break;

            case PRIX_JOURNALIER:
                prixChambre = montant;
        }
    }

    public MontantsPeriode(MontantPeriodePrixChambre montant, String dateDebut, String dateFin) {
        switch (montant.getType()) {
            case MONTANT_PLAFOND_ADMIS:
                montantPlafond = montant;
                break;

            case PRIX_JOURNALIER:
                prixChambre = montant;
        }

        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public boolean isEmpty() {
        return null == prixChambre && null == montantPlafond;
    }

    public boolean isFull() {
        return null != prixChambre && null != montantPlafond;
    }

    public TypeMontant whichTypeIsDefined() {
        if (null != prixChambre) {
            return prixChambre.getType();
        } else if (null != montantPlafond) {
            return montantPlafond.getType();
        } else {
            throw new RuntimeException(
                    "Problem during evaluate which type is defined. Assumed only one type musst be defined.");
        }
    }

}
