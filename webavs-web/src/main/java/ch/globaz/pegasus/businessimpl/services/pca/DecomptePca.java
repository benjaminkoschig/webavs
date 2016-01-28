package ch.globaz.pegasus.businessimpl.services.pca;

import java.math.BigDecimal;
import java.util.List;

public class DecomptePca {
    private String dateDebut;
    private String dateFin;
    private BigDecimal montant;
    private List<PeriodePca> periodesPca;

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public List<PeriodePca> getPeriodesPca() {
        return periodesPca;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public void setPeriodesPca(List<PeriodePca> periodesPca) {
        this.periodesPca = periodesPca;
    }
}
