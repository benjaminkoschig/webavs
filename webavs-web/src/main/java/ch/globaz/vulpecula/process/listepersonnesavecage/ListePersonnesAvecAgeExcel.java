package ch.globaz.vulpecula.process.listepersonnesavecage;

import globaz.globall.db.BSession;
import java.io.FileNotFoundException;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.listepersonnesavecage.ListePersonnesAvecAge;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ListePersonnesAvecAgeExcel extends AbstractListExcel {

    private final int COL_ID_TRAVAILLEUR = 0;
    private final int COL_PAVS_NUM_AVS = 1;
    private final int COL_TIERS_NOM = 2;
    private final int COL_TIERS_PRENOM = 3;
    private final int COL_PERS_DATE_NAISS = 4;
    private final int COL_PERS_SEXE = 5;
    private final int COL_NUM_AFF = 6;
    private final int COL_CONVENTION = 7;

    private final int COL_AFF_NUM_AFF = 0;
    private final int COL_AFF_CONVENTION = 6;

    private List<ListePersonnesAvecAge> listeActifs18Ans;
    private List<ListePersonnesAvecAge> listeActifsPlus70Ans;
    private List<ListePersonnesAvecAge> listeActifsH62;
    private List<ListePersonnesAvecAge> listeActifsH65;
    private List<ListePersonnesAvecAge> listeActifsF61;
    private List<ListePersonnesAvecAge> listeActifsF64;
    private List<ListePersonnesAvecAge> listeAffiliesPlus70Ans;

    private Annee annee;

    public ListePersonnesAvecAgeExcel(BSession session, String filenameRoot, String documentTitle)
            throws FileNotFoundException {
        super(session, filenameRoot, documentTitle);
    }

    @Override
    public void createContent() {
        createPartieActifs18Ans();
        createPartieActifsH62();
        createPartieActifsH65();
        createPartieActifsF61();
        createPartieActifsF64();
        createPartieActifsPlus70ans();
        createPartieAffiliesPlus70Ans();
    }

    private void createPartieActifs18Ans() {
        createSheetCommun(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_FEUILLE_ACTIFS_18_ANS"));
        createHeadersCommun(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_LISTE_ACTIFS_18_ANS"));
        createRows(listeActifs18Ans);
    }

    private void createPartieActifsH62() {
        createSheetCommun(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_FEUILLE_ACTIFS_HOMMES_62"));
        createHeadersCommun(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_LISTE_ACTIFS_HOMMES_62"));
        createRows(listeActifsH62);
    }

    private void createPartieActifsH65() {
        createSheetCommun(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_FEUILLE_ACTIFS_HOMMES_65"));
        createHeadersCommun(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_LISTE_ACTIFS_HOMMES_65"));
        createRows(listeActifsH65);
    }

    private void createPartieActifsF61() {
        createSheetCommun(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_FEUILLE_ACTIFS_FEMMES_61"));
        createHeadersCommun(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_LISTE_ACTIFS_FEMMES_61"));
        createRows(listeActifsF61);
    }

    private void createPartieActifsF64() {
        createSheetCommun(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_FEUILLE_ACTIFS_FEMMES_64"));
        createHeadersCommun(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_LISTE_ACTIFS_FEMMES_64"));
        createRows(listeActifsF64);
    }

    private void createPartieActifsPlus70ans() {
        createSheetCommun(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_FEUILLE_ACTIFS_PLUS_70_ANS"));
        createHeadersCommun(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_LISTE_ACTIFS_PLUS_70_ANS"));
        createRows(listeActifsPlus70Ans);
    }

    private void createPartieAffiliesPlus70Ans() {
        createSheetAffilies(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_FEUILLE_AFFILIES_PLUS_70_ANS"));
        createHeadersAffilies(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_LISTE_AFFILIES_PLUS_70_ANS"));
        createRowsAffilies(listeAffiliesPlus70Ans);
    }

    private void createSheetCommun(String nomFeuille) {
        HSSFSheet sheet = createSheet(nomFeuille);
        sheet.setColumnWidth((short) COL_ID_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_PAVS_NUM_AVS, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_TIERS_NOM, AbstractListExcel.COLUMN_WIDTH_NAME);
        sheet.setColumnWidth((short) COL_TIERS_PRENOM, AbstractListExcel.COLUMN_WIDTH_NAME);
        sheet.setColumnWidth((short) COL_PERS_DATE_NAISS, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) COL_PERS_SEXE, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) COL_NUM_AFF, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_CONVENTION, AbstractListExcel.COLUMN_WIDTH_5500);
    }

    private void createHeadersCommun(String titreFeuille) {
        createRow();
        createRow();
        createMergedRegion(3, titreFeuille + " " + annee.toString(), getStyleGras());
        createEmptyCell();
        createCell("Date du document: " + getDateImpression());
        createRow();
        createRow();
        createCell(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_COL_ID_TRAVAILLEUR"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_COL_NUM_AVS"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_COL_NOM_TIERS"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_COL_PRENOM_TIERS"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_COL_DATE_NAISS_TIERS"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_COL_SEXE_TIERS"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_COL_NUM_AFFILIE"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_COL_CONVENTION"),
                getStyleListGris25PourcentGras());
    }

    private void createRows(List<ListePersonnesAvecAge> liste) {
        for (ListePersonnesAvecAge infosPersonne : liste) {
            createRow();
            createCell(infosPersonne.getNumTravailleur(), getStyleListLeft());
            createCell(infosPersonne.getNumAvs(), getStyleListLeft());
            createCell(infosPersonne.getNomTiers(), getStyleListLeft());
            createCell(infosPersonne.getPrenomTiers(), getStyleListLeft());
            createCell(infosPersonne.getDateNaissanceTiers().getSwissValue(), getStyleListLeft());
            createCell(infosPersonne.getSexeTiers(), getStyleListLeft());
            createCell(infosPersonne.getNumAff(), getStyleListLeft());
            createCell(infosPersonne.getConvention(), getStyleListLeft());
        }
    }

    private void createSheetAffilies(String nomFeuille) {
        HSSFSheet sheet = createSheet(nomFeuille);
        sheet.setColumnWidth((short) COL_AFF_NUM_AFF, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_PAVS_NUM_AVS, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_TIERS_NOM, AbstractListExcel.COLUMN_WIDTH_NAME);
        sheet.setColumnWidth((short) COL_TIERS_PRENOM, AbstractListExcel.COLUMN_WIDTH_NAME);
        sheet.setColumnWidth((short) COL_PERS_DATE_NAISS, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_PERS_SEXE, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_AFF_CONVENTION, AbstractListExcel.COLUMN_WIDTH_5500);
    }

    private void createHeadersAffilies(String titreFeuille) {
        createRow();
        createRow();
        createMergedRegion(3, titreFeuille + " " + annee.toString(), getStyleGras());
        createEmptyCell();
        createCell("Date du document: " + getDateImpression());
        createRow();
        createRow();
        createCell(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_COL_AFF_NUM_AFFILIE"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_COL_NUM_AVS"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_COL_NOM_TIERS"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_COL_PRENOM_TIERS"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_COL_DATE_NAISS_TIERS"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_COL_SEXE_TIERS"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_PERSONNES_AVEC_AGE_TITRE_COL_CONVENTION"),
                getStyleListGris25PourcentGras());
    }

    private void createRowsAffilies(List<ListePersonnesAvecAge> liste) {
        for (ListePersonnesAvecAge infosPersonne : liste) {
            createRow();
            createCell(infosPersonne.getNumAff(), getStyleListLeft());
            createCell(infosPersonne.getNumAvs(), getStyleListLeft());
            createCell(infosPersonne.getNomTiers(), getStyleListLeft());
            createCell(infosPersonne.getPrenomTiers(), getStyleListLeft());
            createCell(infosPersonne.getDateNaissanceTiers().getSwissValue(), getStyleListLeft());
            createCell(infosPersonne.getSexeTiers(), getStyleListLeft());
            createCell(infosPersonne.getConvention(), getStyleListLeft());
        }
    }

    @Override
    public String getNumeroInforom() {
        // TODO Auto-generated method stub
        return null;
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    public List<ListePersonnesAvecAge> getListeActifs18Ans() {
        return listeActifs18Ans;
    }

    public List<ListePersonnesAvecAge> getListeActifsPlus70Ans() {
        return listeActifsPlus70Ans;
    }

    public List<ListePersonnesAvecAge> getListeActifsH62() {
        return listeActifsH62;
    }

    public List<ListePersonnesAvecAge> getListeActifsH65() {
        return listeActifsH65;
    }

    public List<ListePersonnesAvecAge> getListeActifsF61() {
        return listeActifsF61;
    }

    public List<ListePersonnesAvecAge> getListeActifsF64() {
        return listeActifsF64;
    }

    public List<ListePersonnesAvecAge> getListeAffiliesPlus70Ans() {
        return listeAffiliesPlus70Ans;
    }

    public void setListeActifs18Ans(List<ListePersonnesAvecAge> listeActifs18Ans) {
        this.listeActifs18Ans = listeActifs18Ans;
    }

    public void setListeActifsPlus70Ans(List<ListePersonnesAvecAge> listeActifsPlus70Ans) {
        this.listeActifsPlus70Ans = listeActifsPlus70Ans;
    }

    public void setListeActifsH62(List<ListePersonnesAvecAge> listeActifsH62) {
        this.listeActifsH62 = listeActifsH62;
    }

    public void setListeActifsH65(List<ListePersonnesAvecAge> listeActifsH65) {
        this.listeActifsH65 = listeActifsH65;
    }

    public void setListeActifsF61(List<ListePersonnesAvecAge> listeActifsF61) {
        this.listeActifsF61 = listeActifsF61;
    }

    public void setListeActifsF64(List<ListePersonnesAvecAge> listeActifsF64) {
        this.listeActifsF64 = listeActifsF64;
    }

    public void setListeAffiliesPlus70Ans(List<ListePersonnesAvecAge> listeAffiliesPlus70Ans) {
        this.listeAffiliesPlus70Ans = listeAffiliesPlus70Ans;
    }
}
