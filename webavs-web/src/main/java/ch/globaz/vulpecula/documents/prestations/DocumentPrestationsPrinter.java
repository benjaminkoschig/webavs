package ch.globaz.vulpecula.documents.prestations;

import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.prestations.PrestationParEmployeur;

public abstract class DocumentPrestationsPrinter<T extends PrestationParEmployeur> extends DocumentPrinter<T> {
    protected String idEmployeur;
    protected String idTravailleur;
    protected String idConvention;
    protected String periodeDebut;
    protected String periodeFin;
    protected String idPassageFacturation;

    public final String getIdEmployeur() {
        return idEmployeur;
    }

    public final void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public final String getIdTravailleur() {
        return idTravailleur;
    }

    public final void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public final String getIdConvention() {
        return idConvention;
    }

    public final void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public final String getPeriodeDebut() {
        return periodeDebut;
    }

    public final void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public final String getPeriodeFin() {
        return periodeFin;
    }

    public final void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    public final String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public final void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

}
