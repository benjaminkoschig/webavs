package ch.globaz.vulpecula.domain.models.common;

import java.util.List;

/**
 * Une année comptable commence au 01.02.XXX1 et se termine l'année suivante 31.01.XXX2
 */
public class AnneeComptable {
    private final Annee annee;

    public AnneeComptable(int annee) {
        this.annee = new Annee(annee);
    }

    public AnneeComptable(String annee) {
        this.annee = new Annee(annee);
    }

    public AnneeComptable(Annee annee) {
        this.annee = annee;
    }

    public Date getDateDebut() {
        return annee.getFirstDayOfYear().addMonth(1);
    }

    public String getDateDebutAsSwissValue() {
        return getDateDebut().getSwissValue();
    }

    public Date getDateFin() {
        return annee.getLastDayOfYear().addMonth(1);
    }

    public String getDateFinAsSwissValue() {
        return getDateFin().getSwissValue();
    }

    public List<Date> getMois() {
        return new Periode(getDateDebut(), getDateFin()).getMois();
    }
}
