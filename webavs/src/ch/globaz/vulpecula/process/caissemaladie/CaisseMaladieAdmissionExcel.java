package ch.globaz.vulpecula.process.caissemaladie;

import globaz.globall.db.BSession;
import ch.globaz.vulpecula.documents.DocumentConstants;

public class CaisseMaladieAdmissionExcel extends CaisseMaladieExcel {
    public CaisseMaladieAdmissionExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_CAISSES_MALADIES_ADMISSION;
    }
}