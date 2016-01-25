package ch.globaz.vulpecula.process.is;

import globaz.globall.db.BSession;
import java.util.Collection;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.businessimpl.services.is.PrestationGroupee;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ListISRetenuesExcel extends AbstractListExcel {
    private final int COL_REF_PERMIS = 0;
    private final int COL_NOM_PRENOM = 1;
    private final int COL_DATE_NAISSANCE = 2;
    private final int COL_NPA = 3;
    private final int COL_LOCALITE = 4;
    private final int COL_PERIODE_AF = 5;
    private final int COL_IMPOTS = 6;
    private final int COL_MONTANT = 7;

    private final String START_IMPOTS_COLUMN = "H8";
    private final String START_MONTANT_COLUMN = "I8";

    // Critères
    private Annee annee;
    private String canton;
    private String idProcessusAF;

    private Map<String, Collection<PrestationGroupee>> prestationsAImprimer;
    private String caisseAF;

    public ListISRetenuesExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        setWantHeader(false);
        setWantFooter(false);
    }

    @Override
    public void createContent() {
        for (Map.Entry<String, Collection<PrestationGroupee>> entry : prestationsAImprimer.entrySet()) {
            createNewSheet(entry.getKey());
            createCriteres(entry.getKey());
            createRow();
            createRow();
            createEntetes();
            for (PrestationGroupee prestationAImprimer : entry.getValue()) {
                createRow();
                createCell(prestationAImprimer.getReferencePermis(), getStyleListLeft());
                createCell(prestationAImprimer.getNom() + " " + prestationAImprimer.getPrenom(), getStyleListLeft());
                createCell(prestationAImprimer.getDateNaissance().getSwissValue(), getStyleListLeft());
                createCell(prestationAImprimer.getNpa(), getStyleListLeft());
                createCell(prestationAImprimer.getLocalite(), getStyleListLeft());
                createCell(prestationAImprimer.getDebutVersement().getSwissValue(), getStyleListLeft());
                createCell(prestationAImprimer.getFinVersement().getSwissValue(), getStyleListLeft());
                createCell(prestationAImprimer.getImpots().doubleValue(), getStyleMontant());
                createCell(prestationAImprimer.getMontantPrestations().doubleValue(), getStyleMontant());
            }
            createTotaux();
        }
    }

    private void createNewSheet(String name) {
        HSSFSheet sheet = createSheet(name);
        sheet.setColumnWidth((short) COL_REF_PERMIS, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_NOM_PRENOM, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_DATE_NAISSANCE, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_NPA, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_LOCALITE, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_PERIODE_AF, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_IMPOTS, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_MONTANT, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        initPage(true);
    }

    private void createCriteres(String nomCaisse) {
        createRow();
        if (annee != null) {
            createCell(getLabel("LISTE_AF_RETENUES_TITRE") + annee.getValue());
            createRow();
            createRow();
            createCell(getLabel("LISTE_AF_CANTON"), getStyleCritereTitle());
            createCell(getCodeLibelle(canton), getStyleCritere());
            createRow();
            createCell(getLabel("LISTE_AF_CAISSE_AF"), getStyleCritereTitle());
            createCell(nomCaisse, getStyleCritere());
        } else {
            createCell(getLabel("LISTE_AF_RETENUES_PROCESSUS_AF") + idProcessusAF);
            createRow();
            createRow();
            createRow();
        }
    }

    private void createEntetes() {
        createRow();
        createCell(getLabel("LISTE_AF_REF_PERMIS"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_NOM_PRENOM"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_DATE_NAISSANCE"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_NPA"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_LOCALITE"), getStyleGris25PourcentGras());
        createMergedRegion(2, getLabel("LISTE_AF_PERIODE_AF"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_IMPOTS"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_MONTANT"), getStyleGris25PourcentGras());
    }

    private void createTotaux() {
        createRow();
        createEmptyCell(7);
        createCellFormula(
                FORMULA_SUM + "(" + getRangeBetween(START_IMPOTS_COLUMN, getReferenceValueFromCurrentCell(1, 0)) + ")",
                getStyleMontant());
        createCellFormula(
                FORMULA_SUM + "(" + getRangeBetween(START_MONTANT_COLUMN, getReferenceValueFromCurrentCell(1, 0)) + ")",
                getStyleMontant());
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_AF_RETENUES;
    }

    public Map<String, Collection<PrestationGroupee>> getPrestationsAImprimer() {
        return prestationsAImprimer;
    }

    public void setPrestationsAImprimer(Map<String, Collection<PrestationGroupee>> prestationsAImprimer) {
        this.prestationsAImprimer = prestationsAImprimer;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setCaisseAF(String libelleCaisseAF) {
        caisseAF = libelleCaisseAF;
    }

    public void setIdProcessusAF(String idProcessusAF) {
        this.idProcessusAF = idProcessusAF;
    }
}
