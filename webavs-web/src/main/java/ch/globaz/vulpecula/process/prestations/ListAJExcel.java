package ch.globaz.vulpecula.process.prestations;

import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AJsParType;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsencesJustifiees;
import ch.globaz.vulpecula.domain.models.absencejustifiee.TypeAbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import globaz.globall.db.BSession;

public class ListAJExcel extends PrestationsListExcel {
    private static final String SHEET_TITLE = "AJ";

    private Map<Convention, AJsParType> ajParConventionParType;

    private final int LAST_COLUMN_OFFSET = 18;
    private final int COL_ID_TRAVAILLEUR = 0;
    private final int COL_TRAVAILLEUR = 1;
    private final int COL_NO_AFFILIE = 2;
    private final int COL_EMPLOYEUR = 3;
    private final int COL_BENEFICIAIRE = 4;
    private final int COL_PERIODE = 5;
    private final int COL_DATE_DEBUT = 6;
    private final int COL_DATE_FIN = 7;
    private final int COL_JOURS = 8;
    private final int COL_SAL_H = 9;
    private final int COL_BRUT_OUVRIERS = 10;
    private final int COL_BRUT_EMPLOYES = 11;
    private final int COL_AVS_S_ABS = 12;
    private final int COL_AC_S_ABS = 13;
    private final int COL_PART_PATRON = 14;
    private final int COL_NET_VERSE = 15;
    private final int COL_AVS_PART_TRAV = 16;
    private final int COL_AC_PART_TRAV = 17;
    private final int COL_TOTAL_PART_TRAV = 18;

    public ListAJExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        HSSFSheet sheet = createSheet(SHEET_TITLE);
        sheet.setAutobreaks(true);
        sheet.setColumnWidth((short) COL_ID_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) COL_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) COL_NO_AFFILIE, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_EMPLOYEUR, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) COL_BENEFICIAIRE, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_PERIODE, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_DATE_DEBUT, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_DATE_FIN, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_JOURS, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_SAL_H, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_BRUT_OUVRIERS, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_BRUT_EMPLOYES, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_AVS_S_ABS, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_AC_S_ABS, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_PART_PATRON, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_NET_VERSE, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_AVS_PART_TRAV, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_AC_PART_TRAV, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_TOTAL_PART_TRAV, AbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    public void setAbsencesJustifiees(Map<Convention, AJsParType> ajParConventionParType) {
        this.ajParConventionParType = ajParConventionParType;
    }

    @Override
    public void createContent() {
        HSSFPrintSetup ps = initPage(true);
        ps.setFitHeight((short) 0);
        ps.setFitWidth((short) 1);
        createRow();
        createCriteres();
        createRow(2);
        createTable();

    }

    private void createTable() {
        createCell(getSession().getLabel("LISTE_ID_TRAVAILLEUR"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_TRAVAILLEUR"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_NO_AFFILIE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_EMPLOYEUR"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_BENEFICIAIRE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_PERIODE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_DEBUT_ABSENCE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_FIN_ABSENCE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_JOURS"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_SAL_HORAIRE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_BRUT_OUVRIER"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_BRUT_EMPLOYE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_AVS_S_ABS"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_AC_S_ABS"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_PART_PATRON"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_NET_VERSE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_AVS_PART_TRAVAILLEUR"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_AC_PART_TRAVAILLEUR"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_TOTAL_PART_TRAVAILLEUR"), getStyleListTitleLeft());

        for (Map.Entry<Convention, AJsParType> conventionTypeAJ : ajParConventionParType.entrySet()) {
            Convention convention = conventionTypeAJ.getKey();
            AJsParType ajsParType = conventionTypeAJ.getValue();

            String conventionDescription = convention.getCode() + " " + convention.getDesignation();

            int newRow = createRow();
            createMergedRegion(COL_ID_TRAVAILLEUR, LAST_COLUMN_OFFSET, newRow, newRow, conventionDescription,
                    getStyleListGris25PourcentGras());

            Montant partPatronalTotal = Montant.ZERO;
            Montant avsPartTravTotal = Montant.ZERO;
            Montant acPartTravTotal = Montant.ZERO;
            Montant totalPartTravTotal = Montant.ZERO;

            for (Map.Entry<TypeAbsenceJustifiee, AbsencesJustifiees> typeAbsences : ajsParType.entrySet()) {
                TypeAbsenceJustifiee typeAbsenceJustifiee = typeAbsences.getKey();
                AbsencesJustifiees absencesJustifiees = typeAbsences.getValue();

                String libelleTypeAbsenceJustifiee = getSession().getCodeLibelle(typeAbsenceJustifiee.getValue());

                int typeRow = createRow();
                createMergedRegion(COL_TRAVAILLEUR, LAST_COLUMN_OFFSET, typeRow, typeRow, libelleTypeAbsenceJustifiee,
                        getStyleListGris25PourcentGras());

                Montant partPatronalSomme = Montant.ZERO;
                Montant avsPartTravSomme = Montant.ZERO;
                Montant acPartTravSomme = Montant.ZERO;
                Montant totalPartTravSomme = Montant.ZERO;

                for (AbsenceJustifiee absenceJustifiee : absencesJustifiees) {
                    createRow();
                    boolean isPartPatronale = absenceJustifiee.getMontantVerse().isPositive()
                            && absenceJustifiee.getMontantVerse().greaterOrEquals(absenceJustifiee
                                    .getMontantBrutOuvrier().add(absenceJustifiee.getMontantBrutEmploye()));
                    createCell(absenceJustifiee.getTravailleurIdTiers(), getStyleListLeft());
                    createCell(absenceJustifiee.getNomPrenomTravailleur(), getStyleListLeft());
                    createCell(absenceJustifiee.getNoAffilie(), getStyleListLeft());
                    if (absenceJustifiee.getEmployeur().getId() != null) {
                        createCell(absenceJustifiee.getEmployeur().getRaisonSociale(), getStyleListLeft());
                    } else {
                        createCell("-", getStyleListLeft());
                    }
                    createCell(getSession().getCodeLibelle(absenceJustifiee.getBeneficiaire().getValue()),
                            getStyleListLeft());
                    createCell(absenceJustifiee.getPeriodeAsString(), getStyleMontant());
                    createCell(String.valueOf(absenceJustifiee.getDateDebutAbsence()), getStyleMontant());
                    createCell(String.valueOf(absenceJustifiee.getDateFinAbsence()), getStyleMontant());
                    createCell(absenceJustifiee.getNombreDeJours(), getStyleListRight());
                    createCell(absenceJustifiee.getSalaireHoraire(), getStyleMontant());
                    createCell(absenceJustifiee.getMontantBrutOuvrier(), getStyleMontant());
                    createCell(absenceJustifiee.getMontantBrutEmploye(), getStyleMontant());
                    createEmptyCellForMontant(2, getStyleMontant());
                    if (isPartPatronale) {
                        createCell(absenceJustifiee.getPartPatronale(), getStyleMontant());
                        partPatronalSomme = partPatronalSomme.add(absenceJustifiee.getPartPatronale());
                    } else {
                        createCell(0, getStyleMontant());
                    }
                    createCell(absenceJustifiee.getMontantVerse(), getStyleMontant());
                    if (isPartPatronale) {
                        createCell(0, getStyleMontant());
                        createCell(0, getStyleMontant());
                        createCell(0, getStyleMontant());
                    } else {
                        avsPartTravSomme = avsPartTravSomme.add(absenceJustifiee.getMontantAVS());
                        acPartTravSomme = acPartTravSomme.add(absenceJustifiee.getMontantAC());
                        totalPartTravSomme = totalPartTravSomme.add(absenceJustifiee.getMontantAVS())
                                .add(absenceJustifiee.getMontantAC());
                        createCell(absenceJustifiee.getMontantAVS(), getStyleMontant());
                        createCell(absenceJustifiee.getMontantAC(), getStyleMontant());
                        createCell(absenceJustifiee.getMontantAVS().add(absenceJustifiee.getMontantAC()),
                                getStyleMontant());
                    }
                }

                int totalRow = createRow();
                setCurrentCell(COL_TRAVAILLEUR);
                createMergedRegion(COL_TRAVAILLEUR, COL_DATE_FIN, totalRow, totalRow,
                        getSession().getLabel("LISTE_TOTAL") + libelleTypeAbsenceJustifiee,
                        getStyleListGris25PourcentGras());
                createCell(absencesJustifiees.sommeNombreJours(), getStyleListNombreD1Gris25PourcentGras());
                createCell(absencesJustifiees.sommeSalairesHoraires().getValue(),
                        getStyleListMontantGris25PourcentGras());
                createCell(absencesJustifiees.sommeMontantsBrutsOuvrier().getValue(),
                        getStyleListMontantGris25PourcentGras());
                createCell(absencesJustifiees.sommeMontantsBrutsEmploye().getValue(),
                        getStyleListMontantGris25PourcentGras());
                createEmptyCell(2);
                setCurrentCell(COL_PART_PATRON);
                createCell(partPatronalSomme, getStyleListMontantGris25PourcentGras());
                createCell(absencesJustifiees.sommeMontantsVerses(), getStyleListMontantGris25PourcentGras());
                createCell(avsPartTravSomme, getStyleListMontantGris25PourcentGras());
                createCell(acPartTravSomme, getStyleListMontantGris25PourcentGras());
                createCell(totalPartTravSomme, getStyleListMontantGris25PourcentGras());

                partPatronalTotal = partPatronalTotal.add(partPatronalSomme);
                avsPartTravTotal = avsPartTravTotal.add(avsPartTravSomme);
                acPartTravTotal = acPartTravTotal.add(acPartTravSomme);
                totalPartTravTotal = totalPartTravTotal.add(totalPartTravSomme);
            }
            int conventionEndRow = createRow();
            createMergedRegion(COL_ID_TRAVAILLEUR, COL_DATE_FIN, conventionEndRow, conventionEndRow,
                    getSession().getLabel("LISTE_TOTAL_COLON") + conventionDescription,
                    getStyleListGris25PourcentGras());
            createCell(ajsParType.sommeNombreJours(), getStyleListMontantGris25PourcentGras());
            createCell(ajsParType.sommeSalairesHoraires().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(ajsParType.sommeMontantsBrutsOuvrier().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(ajsParType.sommeMontantsBrutsEmploye().getValue(), getStyleListMontantGris25PourcentGras());
            createEmptyCellForMontant(2, getStyleListMontantGris25PourcentGras());
            createCell(partPatronalTotal.getValue(), getStyleListMontantGris25PourcentGras());
            createCell(ajsParType.sommeMontantVerses().getValue(), getStyleListMontantGris25PourcentGras());

            createCell(avsPartTravTotal.getValue(), getStyleListMontantGris25PourcentGras());
            createCell(acPartTravTotal.getValue(), getStyleListMontantGris25PourcentGras());
            createCell(totalPartTravTotal.getValue(), getStyleListMontantGris25PourcentGras());
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
