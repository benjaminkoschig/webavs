package globaz.aquila.process.batch.utils;

import globaz.aquila.db.access.batch.COEtape;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.globall.db.BSession;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import java.util.List;

public class COImprimerJournalContentieuxInfoComplementaireExcelml extends COAbstractJournalContentieuxExcelml {

    public static final String EXCELML_JOURNAL_CONTENTIEUX_MODEL_NAME = "JournalContentieuxInfoComplModele.xml";
    public static final String EXCELML_JOURNAL_CONTENTIEUX_OUTPUT_FILE_NAME = "JournalContentieux.xml";

    public static final String EXCELML_MODEL_COL_DECOMPTE_FINAL = "COL_DECOMPTE_FINAL";
    public static final String EXCELML_MODEL_COL_NBRE_PO_ENCOURS = "COL_NBRE_PO_ENCOURS";
    public static final String EXCELML_MODEL_COL_SECT_RECENTE_PAYE = "COL_SECT_RECENTE_PAYE";
    public static final String EXCELML_MODEL_COL_CREDIT_EXISTANT = "COL_CREDIT_EXISTANT";
    public static final String EXCELML_MODEL_COL_DATE_RADIATION = "COL_DATE_RADIATION";
    public static final String EXCELML_MODEL_COL_POSTIT_CA = "COL_POSTIT_CA";

    /**
     * @param theDateReference
     * @param theDateDocument
     * @param theModePrevisionnel
     * @param theListIdRoles
     */
    public COImprimerJournalContentieuxInfoComplementaireExcelml(String theDateReference, String theDateDocument,
            boolean theModePrevisionnel, List<String> theListIdRoles) {
        super(theDateReference, theDateDocument, theModePrevisionnel, theListIdRoles,
                EXCELML_JOURNAL_CONTENTIEUX_MODEL_NAME, EXCELML_JOURNAL_CONTENTIEUX_OUTPUT_FILE_NAME);
    }

    @Override
    protected void addRowInExcelmlInfoComplementaire(BSession theSession, COContentieux theContentieux, COEtape theEtape) {
        CASection theSection = theContentieux.getSection();
        CACompteAnnexe compteAnnexe = (CACompteAnnexe) theSection.getCompteAnnexe();

        // Le décompte final (paritaires) a été établi pour l'année concernée par la future section en PO ?
        getContainerJournalContentieuxExcelml().put(EXCELML_MODEL_COL_DECOMPTE_FINAL, "");

        // Le nombre de poursuite en cours
        getContainerJournalContentieuxExcelml().put(EXCELML_MODEL_COL_NBRE_PO_ENCOURS, "");

        // La section la plus récente est-elle payé ?
        getContainerJournalContentieuxExcelml().put(EXCELML_MODEL_COL_SECT_RECENTE_PAYE, "");

        // Oui si un crédit est existant autrement vide.
        getContainerJournalContentieuxExcelml().put(EXCELML_MODEL_COL_CREDIT_EXISTANT, "");

        // La date de radiation de l'affiliation
        getContainerJournalContentieuxExcelml().put(EXCELML_MODEL_COL_DATE_RADIATION, "");

        // Le texte du post-it sur le compte annexe
        getContainerJournalContentieuxExcelml().put(EXCELML_MODEL_COL_POSTIT_CA, compteAnnexe.getRemarque());

    }
}
