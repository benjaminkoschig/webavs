package ch.globaz.vulpecula.process.communicationsalaires;

import globaz.globall.db.BSession;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.communicationsalaires.CommunicationSalairesRetaval;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.process.prestations.PrestationsListExcel;

public class ListSalairesRetavalExcel extends PrestationsListExcel {
    private Annee annee;

    private static final String SHEET_TITLE = "Salaires RETAVAL";
    private List<CommunicationSalairesRetaval> listDecompteSalaire;

    private final int COL_CODE_CONVENTION = 0;
    private final int COL_NUM_AFFILIE = 1;
    private final int COL_RAISON_SOCIAL = 2;
    private final int COL_ID_TIERS = 3;
    private final int COL_ID_POSTE_TRAVAIL = 4;
    private final int COL_NOM_TRAVAILLEUR = 5;
    private final int COL_PRENOM_TRAVAILLEUR = 6;
    private final int COL_DATE_NAISSANCE = 7;
    private final int COL_NSS = 8;
    private final int COL_MASSE = 9;

    private int currentElement = 1;

    public ListSalairesRetavalExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        HSSFSheet sheet = createSheet(SHEET_TITLE);
        sheet.setColumnWidth((short) COL_CODE_CONVENTION, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_NUM_AFFILIE, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_RAISON_SOCIAL, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_ID_TIERS, AbstractListExcel.COLUMN_WIDTH_2000);
        sheet.setColumnWidth((short) COL_ID_POSTE_TRAVAIL, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_NOM_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_PRENOM_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_DATE_NAISSANCE, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_NSS, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_MASSE, AbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    @Override
    public void createContent() {
        initPage(true);
        createRow();
        createTitle();
        createRow(2);
        createTable();
    }

    private void createTitle() {
        createMergedRegion(2, getSession().getLabel("LISTE_SALAIRES_RETAVAL_TITRE_LISTE") + " " + annee.toString(),
                getStyleGras());
    }

    private void createTable() {
        createCell(getSession().getLabel("LISTE_SALAIRES_RETAVAL_TITRE_COL_CODE_CONVENTION"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SALAIRES_RETAVAL_TITRE_COL_NUM_AFFILIE"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SALAIRES_RETAVAL_TITRE_COL_RAISON_SOCIAL"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SALAIRES_RETAVAL_TITRE_COL_ID_TIERS"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SALAIRES_RETAVAL_TITRE_COL_ID_POSTE_TRAVAIL"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SALAIRES_RETAVAL_TITRE_COL_NOM"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SALAIRES_RETAVAL_TITRE_COL_PRENOM"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SALAIRES_RETAVAL_TITRE_COL_DATE_NAISSANCE"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SALAIRES_RETAVAL_TITRE_COL_NSS"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_SALAIRES_RETAVAL_TITRE_COL_MASSE"), getStyleListGris25PourcentGras());

        createRows();
    }

    private void createRows() {
        for (CommunicationSalairesRetaval ligneSalaire : listDecompteSalaire) {
            createRow();
            createCell(ligneSalaire.getCodeConvention(), getStyleListLeft());
            createCell(ligneSalaire.getNumAffilie(), getStyleListLeft());
            createCell(ligneSalaire.getRaisonSocial(), getStyleListLeft());
            createCell(Integer.valueOf(ligneSalaire.getIdTiers()), getStyleListLeft());
            createCell(Integer.valueOf(ligneSalaire.getIdPosteTravail()), getStyleListLeft());
            createCell(ligneSalaire.getNom(), getStyleListLeft());
            createCell(ligneSalaire.getPrenom(), getStyleListLeft());
            createCell(new Date(ligneSalaire.getDateNaissance()).getSwissValue(), getStyleListLeft());
            createCell(ligneSalaire.getNss(), getStyleListLeft());
            createCell(new Montant(ligneSalaire.getMasse()), getStyleMontant());
            currentElement++;
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_SALAIRES_RETAVAL_TYPE_NUMBER;
    }

    @Override
    public String getListName() {
        return getSession().getLabel("LISTE_SALAIRES_RETAVAL");
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    public List<CommunicationSalairesRetaval> getListDecompteSalaire() {
        return listDecompteSalaire;
    }

    public void setListDecompteSalaire(List<CommunicationSalairesRetaval> listeSalairesRetaval) {
        listDecompteSalaire = listeSalairesRetaval;
    }

    /**
     * Retourne le numéro de l'élément actuellement en traitement
     * 
     * @return int représentant le numéro du traitement
     */
    public int getCurrentElement() {
        return currentElement;
    }
}