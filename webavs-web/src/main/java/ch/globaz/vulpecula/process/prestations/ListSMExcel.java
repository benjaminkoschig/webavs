package ch.globaz.vulpecula.process.prestations;

import globaz.globall.db.BSession;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.models.servicemilitaire.GenreSM;
import ch.globaz.vulpecula.domain.models.servicemilitaire.SMsParType;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServicesMilitaires;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ListSMExcel extends PrestationsListExcel {
    private static final String SHEET_TITLE = "SM";

    private Map<Convention, SMsParType> servicesMilitairesParTypeParConvention;

    private final int LAST_COLUMN_OFFSET = 13;

    private final int COL_ID_TRAVAILLEUR = 0;
    private final int COL_TRAVAILLEUR = 1;
    private final int COL_NO_AFFILIE = 2;
    private final int COL_BENEFICIAIRE = 3;
    private final int COL_PERIODE = 4;
    private final int COL_JOURS = 5;
    private final int COL_SAL_H = 6;
    private final int COL_TOTAL_SAL_COUV = 7;
    private final int COL_UNKNOWN = 8;
    private final int COL_APG = 9;
    private final int COL_BRUT = 10;
    private final int COL_AVS_AC = 11;
    private final int COL_AF = 12;
    private final int COL_TOTAL_VERSE = 13;

    public ListSMExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        HSSFSheet sheet = createSheet(SHEET_TITLE);
        sheet.setColumnWidth((short) COL_ID_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) COL_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) COL_NO_AFFILIE, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_BENEFICIAIRE, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_PERIODE, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_JOURS, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_SAL_H, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_TOTAL_SAL_COUV, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_UNKNOWN, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_APG, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_BRUT, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_AVS_AC, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_AF, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_TOTAL_VERSE, AbstractListExcel.COLUMN_WIDTH_MONTANT);
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
        createCell(getSession().getLabel("LISTE_PERIODE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_JOURS"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_SAL_H"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_TAUX"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_TOTAL_SALAIRE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_APG"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_BRUT"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_AVS_AC"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_AF"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_TOTAL_VERSE"), getStyleListTitleLeft());

        for (Map.Entry<Convention, SMsParType> conventionServicesMilitaire : servicesMilitairesParTypeParConvention
                .entrySet()) {
            Convention convention = conventionServicesMilitaire.getKey();
            SMsParType smsParType = conventionServicesMilitaire.getValue();

            String conventionDescription = convention.getCode() + " " + convention.getDesignation();

            int newRow = createRow();
            createMergedRegion(0, LAST_COLUMN_OFFSET, newRow, newRow, conventionDescription,
                    getStyleListGris25PourcentGras());

            for (Map.Entry<GenreSM, ServicesMilitaires> smParType : smsParType.entrySet()) {
                GenreSM genreSM = smParType.getKey();
                ServicesMilitaires servicesMilitaires = smParType.getValue();
                String genreSMLibelle = getSession().getCodeLibelle(genreSM.getValue());
                int genreSMRow = createRow();
                createMergedRegion(1, LAST_COLUMN_OFFSET, genreSMRow, genreSMRow, genreSMLibelle,
                        getStyleListGris25PourcentGras());

                for (ServiceMilitaire serviceMilitaire : smParType.getValue()) {
                    createRow();
                    createCell(serviceMilitaire.getTravailleurIdTiers(), getStyleListLeft());
                    createCell(serviceMilitaire.getNomPrenomTravailleur(), getStyleListLeft());
                    createCell(serviceMilitaire.getNoAffilie(), getStyleListLeft());
                    createCell(getSession().getCodeLibelle(serviceMilitaire.getBeneficiaire().getValue()),
                            getStyleListLeft());
                    createCell(serviceMilitaire.getPeriodeAsString(), getStyleMontant());
                    createCell(String.valueOf(serviceMilitaire.getNbJours()), getStyleMontant());
                    createCell(serviceMilitaire.getSalaireHoraire().getValue(), getStyleMontant());
                    createCell(serviceMilitaire.getCouvertureAPG().getValue(), getStyleMontant());
                    createEmptyCellForMontant(0);
                    createCell(serviceMilitaire.getTotalSalaire().getValue(), getStyleMontant());
                    createCell(serviceMilitaire.getVersementAPG().getValue(), getStyleMontant());
                    createCell(serviceMilitaire.getMontantBrut().getValue(), getStyleMontant());
                    createCell(serviceMilitaire.getMontantAVS_AC().getValue(), getStyleMontant());
                    createCell(serviceMilitaire.getMontantAF().getValue(), getStyleMontant());
                    createCell(serviceMilitaire.getMontantAVerser().getValue(), getStyleMontant());
                }

                int totalGenreSM = createRow();
                createMergedRegion(1, COL_PERIODE, totalGenreSM, totalGenreSM,
                        getSession().getLabel("LISTE_TOTAL_COLON") + genreSMLibelle, getStyleListGris25PourcentGras());
                createCell(servicesMilitaires.getSommeNbjours(), getStyleListMontantGris25PourcentGras());
                createEmptyCellForMontant(2, getStyleListMontantGris25PourcentGras());
                createCell(servicesMilitaires.getSommeCouvertureAPG().getValue(),
                        getStyleListMontantGris25PourcentGras());
                createCell(servicesMilitaires.getSommeVersementAPG().getValue(),
                        getStyleListMontantGris25PourcentGras());
                createCell(servicesMilitaires.getSommeBruts().getValue(), getStyleListMontantGris25PourcentGras());
                createCell(servicesMilitaires.getSommeMontantAVS_AC().getValue(),
                        getStyleListMontantGris25PourcentGras());
                createCell(servicesMilitaires.getSommeMontantAF().getValue(), getStyleListMontantGris25PourcentGras());
                createCell(servicesMilitaires.getSommeTotalVerse().getValue(), getStyleListMontantGris25PourcentGras());
            }

            int conventionEndRow = createRow();
            createMergedRegion(0, COL_PERIODE, conventionEndRow, conventionEndRow,
                    getSession().getLabel("LISTE_TOTAL_COLON") + conventionDescription,
                    getStyleListGris25PourcentGras());
            createCell(smsParType.getSommeNbJours(), getStyleListMontantGris25PourcentGras());
            createEmptyCellForMontant(2, getStyleListMontantGris25PourcentGras());
            createCell(smsParType.getSommeVersementAPG().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(smsParType.getSommeCouvertureAPG().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(smsParType.getSommeBruts().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(smsParType.getSommeMontantAVS_AC().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(smsParType.getSommeMontantAF().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(smsParType.getSommeTotalVerse().getValue(), getStyleListMontantGris25PourcentGras());
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_SM_TYPE_NUMBER;
    }

    public void setServicesMilitairesParConvention(Map<Convention, SMsParType> servicesMilitairesParTypeParConvention) {
        this.servicesMilitairesParTypeParConvention = servicesMilitairesParTypeParConvention;
    }

    @Override
    public String getListName() {
        return getSession().getLabel("LISTE_VERSEMENT_SERVICES_MILITAIRES");
    }
}
