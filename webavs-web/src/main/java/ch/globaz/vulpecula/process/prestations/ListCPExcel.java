package ch.globaz.vulpecula.process.prestations;

import globaz.globall.db.BSession;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.congepaye.CongesPayes;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ListCPExcel extends PrestationsListExcel {
    private static final String SHEET_TITLE = "CP";

    private Map<Convention, CongesPayes> congesPayesParConvention;

    private final int LAST_COLUMN_OFFSET = 18;

    private final int COL_ID_TRAVAILLEUR = 0;
    private final int COL_TRAVAILLEUR = 1;
    private final int COL_DATE_NAISSANCE = 2;
    private final int COL_NO_AFFILIE = 3;
    private final int COL_EMPLOYEUR = 4;
    private final int COL_AJOUT_RETRAIT = 5;
    private final int COL_BENEFICIAIRE = 6;
    private final int COL_PERIODE = 7;
    private final int COL_SALAIRES = 8;
    private final int COL_TAUX = 9;
    private final int COL_BRUT = 10;
    private final int COL_MAL = 11;
    private final int COL_RET = 12;
    private final int COL_LPP = 13;
    private final int COL_AVS = 14;
    private final int COL_AC = 15;
    private final int COL_AF = 16;
    private final int COL_FCFP = 17;
    private final int COL_NET = 18;

    public ListCPExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        HSSFSheet sheet = createSheet(SHEET_TITLE);
        sheet.setColumnWidth((short) COL_ID_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) COL_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) COL_DATE_NAISSANCE, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_NO_AFFILIE, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_EMPLOYEUR, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) COL_AJOUT_RETRAIT, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_BENEFICIAIRE, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_PERIODE, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_SALAIRES, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_TAUX, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_BRUT, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_MAL, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_RET, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_LPP, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_AVS, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_AC, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_AF, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_FCFP, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_NET, AbstractListExcel.COLUMN_WIDTH_MONTANT);
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
        createCell(getSession().getLabel("LISTE_DATE_NAISSANCE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_NO_AFFILIE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_EMPLOYEUR"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_AJOUT_RETRAIT"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_BENEFICIAIRE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_PERIODE"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_SALAIRES"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_TAUX"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_BRUT"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_LPP"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_AVS"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_AC"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_MAL"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_RET"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_AF"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_FCFP"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_NET"), getStyleListTitleLeft());

        for (Map.Entry<Convention, CongesPayes> conventionCongesPayes : congesPayesParConvention.entrySet()) {
            Convention convention = conventionCongesPayes.getKey();
            CongesPayes congesPayes = conventionCongesPayes.getValue();

            String conventionDescription = convention.getCode() + " " + convention.getDesignation();

            int newRow = createRow();
            createMergedRegion(0, LAST_COLUMN_OFFSET, newRow, newRow, conventionDescription,
                    getStyleListGris25PourcentGras());

            for (CongePaye congePaye : congesPayes) {
                createRow();
                createCell(congePaye.getTravailleurIdTiers(), getStyleListLeft());
                createCell(congePaye.getNomPrenomTravailleur(), getStyleListLeft());
                createCell(congePaye.getDateNaissanceTravailleur(), getStyleListLeft());
                createCell(congePaye.getNoAffilie(), getStyleListLeft());
                if (congePaye.getEmployeur().getId() != null) {
                    createCell(congePaye.getEmployeur().getRaisonSociale(), getStyleListLeft());
                } else {
                    createCell("-", getStyleListLeft());
                }
                createEmptyCell(1);
                createCell(getSession().getCodeLibelle(congePaye.getBeneficiaire().getValue()), getStyleListLeft());
                createCell(String.valueOf(congePaye.getPeriodeAsString()), getStyleMontant());
                createCell(congePaye.getSalaires().getValue(), getStyleMontant());
                createCell(congePaye.getTauxCPValueScale2(), getStyleMontant());
                createCell(congePaye.getMontantBrut().getValue(), getStyleMontant());
                createCell(congePaye.getMontantLPP().getValue(), getStyleMontant());
                createCell(congePaye.getMontantAVS().getValue(), getStyleMontant());
                createCell(congePaye.getMontantAC().getValue(), getStyleMontant());
                createCell(congePaye.getMontantMal().getValue(), getStyleMontant());
                createCell(congePaye.getMontantRetaval().getValue(), getStyleMontant());
                createCell(congePaye.getMontantAF().getValue(), getStyleMontant());
                createCell(congePaye.getMontantFCFP().getValue(), getStyleMontant());
                createCell(congePaye.getMontantNet().getValue(), getStyleMontant());
            }

            int conventionEndRow = createRow();
            createMergedRegion(0, COL_PERIODE, conventionEndRow, conventionEndRow,
                    getSession().getLabel("LISTE_TOTAL_COLON") + conventionDescription,
                    getStyleListGris25PourcentGras());
            createCell(congesPayes.getSalaires().getValue(), getStyleListMontantGris25PourcentGras());
            createEmptyCell();
            createCell(congesPayes.getSommeBruts().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(congesPayes.getSommeMal().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(congesPayes.getSommeAVS().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(congesPayes.getSommeAC().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(congesPayes.getSommeRetaval().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(congesPayes.getSommeLPP().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(congesPayes.getSommeAF().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(congesPayes.getSommeFCFP().getValue(), getStyleListMontantGris25PourcentGras());
            createCell(congesPayes.getSommeNets().getValue(), getStyleListMontantGris25PourcentGras());
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_CP_TYPE_NUMBER;
    }

    public void setCongesPayesParConvention(Map<Convention, CongesPayes> congesPayesParConvention) {
        this.congesPayesParConvention = congesPayesParConvention;
    }

    @Override
    public String getListName() {
        return getSession().getLabel("LISTE_VERSEMENT_CONGES_PAYES");
    }
}
