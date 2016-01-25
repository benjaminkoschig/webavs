package ch.globaz.vulpecula.process.prestations;

import globaz.globall.db.BSession;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AJsParType;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsencesJustifiees;
import ch.globaz.vulpecula.domain.models.absencejustifiee.TypeAbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ListAJExcel extends PrestationsListExcel {
    private static final String SHEET_TITLE = "AJ";

    private Map<Convention, AJsParType> ajParConventionParType;

    private final int LAST_COLUMN_OFFSET = 11;
    private final int COL_ID_TRAVAILLEUR = 0;
    private final int COL_TRAVAILLEUR = 1;
    private final int COL_NO_AFFILIE = 2;
    private final int COL_BENEFICIAIRE = 3;
    private final int COL_JOURS = 4;
    private final int COL_SAL_H = 5;
    private final int COL_BRUT_OUVRIERS = 6;
    private final int COL_BRUT_EMPLOYES = 7;
    private final int COL_AVS_S_ABS = 8;
    private final int COL_AC_S_ABS = 9;
    private final int COL_PART_PATRON = 10;
    private final int COL_NET_VERSE = 11;

    public ListAJExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        HSSFSheet sheet = createSheet(SHEET_TITLE);
        sheet.setColumnWidth((short) COL_ID_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) COL_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) COL_NO_AFFILIE, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_BENEFICIAIRE, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_JOURS, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_SAL_H, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_BRUT_OUVRIERS, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_BRUT_EMPLOYES, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_AVS_S_ABS, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_AC_S_ABS, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_PART_PATRON, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_NET_VERSE, AbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    public void setAbsencesJustifiees(Map<Convention, AJsParType> ajParConventionParType) {
        this.ajParConventionParType = ajParConventionParType;
    }

    @Override
    public void createContent() {
        initPage(true);
        createRow();
        createCriteres();
        createRow(2);
        createTable();
    }

    private void createTable() {
        createCell(getSession().getLabel("LISTE_ID_TRAVAILLEUR"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_TRAVAILLEUR"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_NO_AFFILIE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_BENEFICIAIRE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_JOURS"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_SAL_HORAIRE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_BRUT_OUVRIER"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_BRUT_EMPLOYE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_AVS_S_ABS"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_AC_S_ABS"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_PART_PATRON"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_NET_VERSE"), getStyleListTitleLeft());

        for (Map.Entry<Convention, AJsParType> conventionTypeAJ : ajParConventionParType.entrySet()) {
            Convention convention = conventionTypeAJ.getKey();
            AJsParType ajsParType = conventionTypeAJ.getValue();

            String conventionDescription = convention.getCode() + " " + convention.getDesignation();

            int newRow = createRow();
            createMergedRegion(0, LAST_COLUMN_OFFSET, newRow, newRow, conventionDescription,
                    getStyleListGris25PourcentGras());

            for (Map.Entry<TypeAbsenceJustifiee, AbsencesJustifiees> typeAbsences : ajsParType.entrySet()) {
                TypeAbsenceJustifiee typeAbsenceJustifiee = typeAbsences.getKey();
                AbsencesJustifiees absencesJustifiees = typeAbsences.getValue();

                String libelleTypeAbsenceJustifiee = getSession().getCodeLibelle(typeAbsenceJustifiee.getValue());

                int typeRow = createRow();
                createMergedRegion(1, LAST_COLUMN_OFFSET, typeRow, typeRow, libelleTypeAbsenceJustifiee,
                        getStyleListGris25PourcentGras());

                for (AbsenceJustifiee absenceJustifiee : absencesJustifiees) {
                    createRow();
                    createCell(absenceJustifiee.getTravailleurIdTiers(), getStyleListLeft());
                    createCell(absenceJustifiee.getNomPrenomTravailleur(), getStyleListLeft());
                    createCell(absenceJustifiee.getNoAffilie(), getStyleListLeft());
                    createCell(getSession().getCodeLibelle(absenceJustifiee.getBeneficiaire().getValue()),
                            getStyleListLeft());
                    createCell(String.valueOf(absenceJustifiee.getNombreDeJours()), getStyleMontant());
                    createCell(absenceJustifiee.getSalaireHoraire().getValue(), getStyleMontant());
                    createCell(absenceJustifiee.getMontantBrutOuvrier().getValue(), getStyleMontant());
                    createCell(absenceJustifiee.getMontantBrutEmploye().getValue(), getStyleMontant());
                    createEmptyCellForMontant(2);
                    createCell(absenceJustifiee.getPartPatronale().getValue(), getStyleMontant());
                    createCell(absenceJustifiee.getMontantVerse().getValue(), getStyleMontant());
                }

                int totalRow = createRow();
                setCurrentCell(COL_TRAVAILLEUR);
                createMergedRegion(1, 3, totalRow, totalRow, getSession().getLabel("LISTE_TOTAL")
                        + libelleTypeAbsenceJustifiee, getStyleListGris25PourcentGras());
                createCell(absencesJustifiees.sommeNombreJours(), getStyleListNombreD1Gris25PourcentGras());
                createCell(absencesJustifiees.sommeSalairesHoraires().getValue(),
                        getStyleListMontantGris25PourcentGras());
                createCell(absencesJustifiees.sommeMontantsBrutsOuvrier().getValue(),
                        getStyleListMontantGris25PourcentGras());
                createCell(absencesJustifiees.sommeMontantsBrutsEmploye().getValue(),
                        getStyleListMontantGris25PourcentGras());
                createEmptyCell(2);
                setCurrentCell(COL_PART_PATRON);
                createCell(absencesJustifiees.sommePartsPatronales().getValue(),
                        getStyleListMontantGris25PourcentGras());
                createCell(absencesJustifiees.sommeMontantsVerses().getValue(), getStyleListMontantGris25PourcentGras());
            }
            int conventionEndRow = createRow();
            createMergedRegion(0, COL_BENEFICIAIRE, conventionEndRow, conventionEndRow,
                    getSession().getLabel("LISTE_TOTAL_COLON") + conventionDescription,
                    getStyleListGris25PourcentGras());
            createCell(ajsParType.sommeNombreJours(), getStyleListMontantGris25PourcentGras());
            createCell(ajsParType.sommeSalairesHoraires().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(ajsParType.sommeMontantsBrutsOuvrier().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(ajsParType.sommeMontantsBrutsEmploye().getValue(), getStyleListMontantGris25PourcentGras());
            createEmptyCellForMontant(2, getStyleListMontantGris25PourcentGras());
            createCell(ajsParType.sommePartsPatronales().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(ajsParType.sommeMontantVerses().getValue(), getStyleListMontantGris25PourcentGras());
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_AJ_TYPE_NUMBER;
    }

    @Override
    public String getListName() {
        return getSession().getLabel("LISTE_VERSEMENT_ABSENCES_JUSTIFIEES");
    }
}
