package ch.globaz.vulpecula.process.caissemaladie;

import globaz.globall.db.BSession;
import ch.globaz.vulpecula.documents.DocumentConstants;

public class CaisseMaladieAdmissionExcel extends CaisseMaladieExcel {

    private String datePeriodeDebut = "";
    private String datePeriodeFin = "";

    public CaisseMaladieAdmissionExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_CAISSES_MALADIES_ADMISSION;
    }

    public String getDatePeriodeDebut() {
        return datePeriodeDebut;
    }

    public void setDatePeriodeDebut(String datePeriodeDebut) {
        this.datePeriodeDebut = datePeriodeDebut;
    }

    public String getDatePeriodeFin() {
        return datePeriodeFin;
    }

    public void setDatePeriodeFin(String datePeriodeFin) {
        this.datePeriodeFin = datePeriodeFin;
    }

}