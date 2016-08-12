package globaz.aquila.process.batch.utils;

import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.globall.db.BSession;
import java.util.List;

public class COImprimerJournalContentieuxExcelml extends COAbstractJournalContentieuxExcelml {

    public static final String EXCELML_JOURNAL_CONTENTIEUX_MODEL_NAME = "JournalContentieuxModele.xml";
    public static final String EXCELML_JOURNAL_CONTENTIEUX_OUTPUT_FILE_NAME = "JournalContentieux.xml";

    /**
     * @param theDateReference
     * @param theDateDocument
     * @param theModePrevisionnel
     * @param theListIdRoles
     */
    public COImprimerJournalContentieuxExcelml(String theDateReference, String theDateDocument,
            boolean theModePrevisionnel, List<String> theListIdRoles) {
        super(theDateReference, theDateDocument, theModePrevisionnel, theListIdRoles,
                EXCELML_JOURNAL_CONTENTIEUX_MODEL_NAME, EXCELML_JOURNAL_CONTENTIEUX_OUTPUT_FILE_NAME);
    }

    @Override
    protected void addRowInExcelmlInfoComplementaire(BSession theSession, COContentieux theContentieux, COEtape theEtape) {
        // RIEN A FAIRE CAR TOUT EST DANS L'ABSTRACT POUR CE JOURNAL
    }
}
