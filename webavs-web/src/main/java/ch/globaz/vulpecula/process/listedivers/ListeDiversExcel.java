package ch.globaz.vulpecula.process.listedivers;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.io.FileNotFoundException;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.common.domaine.Date;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.listedivers.ListeDivers;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ListeDiversExcel extends AbstractListExcel {

    private final int COL_NUM_AFFILIE = 0;
    private final int COL_RAISON_SOCIAL = 1;
    private final int COL_DATE_DEBUT_AFFILIATION = 2;
    private final int COL_DATE_FIN_AFFILIATION = 3;
    private final int COL_MOTIF_RADIATION = 4;
    private final int COL_DATE_DEBUT_PARTICULARITE = 5;
    private final int COL_DATE_FIN_PARTICULARITE = 6;

    private final int COL_CONVENTION = 0;
    private final int COL_NB_FRANCAIS = 1;
    private final int COL_NB_ALLEMAND = 2;

    private List<ListeDivers> listeAffiliesAnnuels;
    private List<ListeDivers> listePlusieursCP;
    private List<ListeDivers> listeAffiliesAvecPersoSansCP;
    private List<ListeDivers> listeAffiliesSansPerso;

    private List<ListeDivers> listeNbreActifParConventionEtLangue;
    private List<ListeDivers> listeAffiliesRadies;

    private Annee annee;

    public ListeDiversExcel(BSession session, String filenameRoot, String documentTitle) throws FileNotFoundException {
        super(session, filenameRoot, documentTitle);
    }

    @Override
    public void createContent() {
        createPartieAffAnnuels();
        createPartieAffRadies();
        createPartieAffAvecPlusieursCP();
        createPartieAffSansPerso();
        createPartieAffAvecPersoSansCP();
        createPartieAffParConvParLan();
    }

    private void createPartieAffAvecPlusieursCP() {
        createSheetCommun(getSession().getLabel("LISTE_DIVERS_TITRE_FEUILLE_AFF_AVEC_PLUSIEURS_CP"), listePlusieursCP);
        createHeadersCommun(getSession().getLabel("LISTE_DIVERS_TITRE_LISTE_AFFILIES_AVEC_PLUSIEURS_CP"),
                listePlusieursCP);
        createRows(listePlusieursCP);
    }

    private void createPartieAffAvecPersoSansCP() {
        createSheetCommun(getSession().getLabel("LISTE_DIVERS_TITRE_FEUILLE_AFF_AVEC_PERSO_SANS_CP"),
                listeAffiliesAvecPersoSansCP);
        createHeadersCommun(getSession().getLabel("LISTE_DIVERS_TITRE_LISTE_AFFILIES_AVEC_PERSO_SANS_CP"),
                listeAffiliesAvecPersoSansCP);
        createRows(listeAffiliesAvecPersoSansCP);
    }

    private void createPartieAffSansPerso() {
        createSheetCommun(getSession().getLabel("LISTE_DIVERS_TITRE_FEUILLE_AFF_SANS_PERSO"), listeAffiliesSansPerso);
        createHeadersCommun(getSession().getLabel("LISTE_DIVERS_TITRE_LISTE_AFFILIES_SANS_PERSO"),
                listeAffiliesSansPerso);
        createRows(listeAffiliesSansPerso);
    }

    private void createPartieAffAnnuels() {
        createSheetCommun(getSession().getLabel("LISTE_DIVERS_TITRE_FEUILLE_AFF_ANNUELS"), listeAffiliesAnnuels);
        createHeadersCommun(getSession().getLabel("LISTE_DIVERS_TITRE_LISTE_AFFILIES_ANNUELS"), listeAffiliesAnnuels);
        createRows(listeAffiliesAnnuels);
    }

    private void createPartieAffRadies() {
        createSheetCommun(getSession().getLabel("LISTE_DIVERS_TITRE_FEUILLE_AFF_RADIES"), listeAffiliesRadies);
        createHeadersCommun(getSession().getLabel("LISTE_DIVERS_TITRE_LISTE_AFFILIES_RADIES"), listeAffiliesRadies);
        createRows(listeAffiliesRadies);
    }

    private void createSheetCommun(String nomFeuille, List<ListeDivers> liste) {
        HSSFSheet sheet = createSheet(nomFeuille);
        sheet.setColumnWidth((short) COL_NUM_AFFILIE, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_RAISON_SOCIAL, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) COL_DATE_DEBUT_AFFILIATION, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_DATE_FIN_AFFILIATION, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_MOTIF_RADIATION, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        if (!liste.isEmpty()
                && (!JadeStringUtil.isBlankOrZero(liste.get(0).getDateDebutParticularite()) || !JadeStringUtil
                        .isBlankOrZero(liste.get(0).getDateFinParticularite()))) {
            sheet.setColumnWidth((short) COL_DATE_DEBUT_PARTICULARITE, AbstractListExcel.COLUMN_WIDTH_5500);
            sheet.setColumnWidth((short) COL_DATE_FIN_PARTICULARITE, AbstractListExcel.COLUMN_WIDTH_5500);
        }
    }

    private void createHeadersCommun(String titreFeuille, List<ListeDivers> liste) {
        createRow();
        createRow();
        createMergedRegion(3, titreFeuille + " " + annee.toString(), getStyleGras());
        createEmptyCell();
        createCell("Date du document: " + getDateImpression());
        createRow();
        createRow();
        createCell(getSession().getLabel("LISTE_DIVERS_TITRE_COL_NUM_EMPLOYEUR"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_DIVERS_TITRE_COL_RAISON_SOCIAL"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_DIVERS_TITRE_COL_DATE_DEBUT_AFFILIATION"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_DIVERS_TITRE_COL_DATE_FIN_AFFILIATION"),
                getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_DIVERS_TITRE_COL_MOTIF_AFFILIATION"), getStyleListGris25PourcentGras());
        if (!liste.isEmpty()
                && (!JadeStringUtil.isBlankOrZero(liste.get(0).getDateDebutParticularite()) || !JadeStringUtil
                        .isBlankOrZero(liste.get(0).getDateFinParticularite()))) {
            createCell(getSession().getLabel("LISTE_DIVERS_TITRE_COL_DATE_DEBUT_PARTICULARITE"),
                    getStyleListGris25PourcentGras());
            createCell(getSession().getLabel("LISTE_DIVERS_TITRE_COL_DATE_FIN_PARTICULARITE"),
                    getStyleListGris25PourcentGras());
        }
    }

    private void createRows(List<ListeDivers> liste) {
        for (ListeDivers row : liste) {
            createRow();
            createCell(row.getNoAffilie(), getStyleListLeft());
            createCell(row.getRaisonSocial(), getStyleListLeft());
            createCell(formatDate(row.getDateDebutAffiliation()), getStyleListLeft());
            createCell(formatDate(row.getDateFinAffiliation()), getStyleListLeft());
            createCell(row.getMotifRadiation(), getStyleListLeft());
            if (!JadeStringUtil.isBlankOrZero(row.getDateDebutParticularite())
                    || !JadeStringUtil.isBlankOrZero(row.getDateFinParticularite())) {
                createCell(formatDate(row.getDateDebutParticularite()), getStyleListLeft());
                createCell(formatDate(row.getDateFinParticularite()), getStyleListLeft());
            }
        }
    }

    private void createPartieAffParConvParLan() {
        createSheetListAffParConvParLan(getSession().getLabel(
                "LISTE_DIVERS_TITRE_FEUILLE_NBRE_ACTIFS_PAR_CONVENTION_ET_LANGUE"));
        createHeadersAffParConvParLan(getSession().getLabel(
                "LISTE_DIVERS_TITRE_LISTE_AFFILIES_NBRE_ACTIFS_PAR_CONVENTION_ET_LANGUE"));
        createRowsAffParConvParLan(listeNbreActifParConventionEtLangue);
    }

    private void createRowsAffParConvParLan(List<ListeDivers> listeNbreActifParConv) {
        for (ListeDivers listeAffParConvParLan : listeNbreActifParConv) {
            createRow();
            createCell(listeAffParConvParLan.getConvention(), getStyleListLeft());
            Integer nbActif = 0;
            if (!JadeStringUtil.isBlank(listeAffParConvParLan.getNbFrancais())) {
                nbActif = Integer.valueOf(listeAffParConvParLan.getNbFrancais());
            }
            createCell(nbActif, getStyleListLeft());
            nbActif = 0;
            if (!JadeStringUtil.isBlank(listeAffParConvParLan.getNbAllemand())) {
                nbActif = Integer.valueOf(listeAffParConvParLan.getNbAllemand());
            }
            createCell(nbActif, getStyleListLeft());
        }
    }

    private void createSheetListAffParConvParLan(String nomFeuille) {
        HSSFSheet sheet = createSheet(nomFeuille);
        sheet.setColumnWidth((short) COL_CONVENTION, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) COL_NB_FRANCAIS, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_NB_ALLEMAND, AbstractListExcel.COLUMN_WIDTH_4500);
    }

    private void createHeadersAffParConvParLan(String titreFeuille) {
        createRow();
        createRow();
        createMergedRegion(2, titreFeuille + " " + annee.toString(), getStyleGras());
        createCell("Date du document: " + getDateImpression());
        createRow();
        createRow();
        createCell(getSession().getLabel("LISTE_DIVERS_TITRE_COL_CONVENTION"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_DIVERS_TITRE_COL_ACTIFS_FRANCAIS"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_DIVERS_TITRE_COL_ACTIFS_ALLEMAND"), getStyleListGris25PourcentGras());
    }

    private String formatDate(String date) {
        if (!JadeStringUtil.isBlankOrZero(date)) {
            return new Date(date).getSwissValue();
        }
        return "";
    }

    @Override
    public String getNumeroInforom() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<ListeDivers> getListeAffiliesAnnuels() {
        return listeAffiliesAnnuels;
    }

    public List<ListeDivers> getListePlusieursCP() {
        return listePlusieursCP;
    }

    public List<ListeDivers> getListeAffiliesAvecPersoSansCP() {
        return listeAffiliesAvecPersoSansCP;
    }

    public List<ListeDivers> getListeNbreActifParConventionEtLangue() {
        return listeNbreActifParConventionEtLangue;
    }

    public List<ListeDivers> getListeAffiliesRadies() {
        return listeAffiliesRadies;
    }

    public void setListeAffiliesAnnuels(List<ListeDivers> listeAffiliesAnnuels) {
        this.listeAffiliesAnnuels = listeAffiliesAnnuels;
    }

    public void setListePlusieursCP(List<ListeDivers> listePlusieursCP) {
        this.listePlusieursCP = listePlusieursCP;
    }

    public void setListeAffiliesAvecPersoSansCP(List<ListeDivers> listeAffiliesAvecPersoSansCP) {
        this.listeAffiliesAvecPersoSansCP = listeAffiliesAvecPersoSansCP;
    }

    public void setListeNbreActifParConventionEtLangue(List<ListeDivers> listeNbreActifParConventionEtLangue) {
        this.listeNbreActifParConventionEtLangue = listeNbreActifParConventionEtLangue;
    }

    public void setListeAffiliesRadies(List<ListeDivers> listeAffiliesRadies) {
        this.listeAffiliesRadies = listeAffiliesRadies;
    }

    public List<ListeDivers> getListeAffiliesSansPerso() {
        return listeAffiliesSansPerso;
    }

    public void setListeAffiliesSansPerso(List<ListeDivers> listeAffiliesSansPerso) {
        this.listeAffiliesSansPerso = listeAffiliesSansPerso;
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }
}
