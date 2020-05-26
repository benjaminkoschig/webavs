package ch.globaz.al.impotsource.process;

import globaz.globall.db.BSession;

import java.math.BigDecimal;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.businessimpl.services.is.PrestationGroupee;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ListISParCAFExcel extends AbstractListExcel {
    private static final int COL_CAISSE_AF = 0;
    private static final int COL_MONTANT_AF = 1;
    private static final int COL_RETENUE = 2;
    private static final int COL_FRAIS = 3;
    private static final int COL_MONTANT_NET = 4;
    private static final int COL_MONTANT_IS_COMPTA = 5;
    private static final int COL_MONTANT_DIFF_IS = 6;

    private static final String LISTES_AF_RETENUES = "0019PPT";

    private Map<String, PrestationGroupee> prestationsAImprimer;
    private Map<String, BigDecimal> listeComptaAux;

    private String dateDebut;
    private String dateFin;
    private String canton;

    public ListISParCAFExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
    }

    @Override
    public void createContent() {
        createSheet();
        createTitle();
        createEntetes();
        for (Map.Entry<String, PrestationGroupee> entry : prestationsAImprimer.entrySet()) {
            PrestationGroupee prestation = entry.getValue();
            Montant montantPrestation = prestation.getMontantPrestations();
            Montant montantRetenue = prestation.getImpots();
            Montant montantFrais = prestation.getFrais();
            Montant montantNet = montantRetenue.substract(montantFrais);
            Montant montantComptaAux = new Montant(listeComptaAux.get(prestation.getCodeCaisseAF()));
            Montant montantDiff = montantComptaAux.substract(montantRetenue);
            createRow();
            createCell(prestation.getLibelleCaisseAF(), getStyleListLeft());
            createCell(Double.parseDouble(montantPrestation.getValueNormalisee()), getStyleMontant());
            createCell(Double.parseDouble(montantRetenue.getValueNormalisee()), getStyleMontant());
            createCell(Double.parseDouble(montantFrais.getValueNormalisee()), getStyleMontant());
            createCell(Double.parseDouble(montantNet.getValueNormalisee()), getStyleMontant());
            createCell(Double.parseDouble(montantComptaAux.getValueNormalisee()), getStyleMontant());
            createCell(Double.parseDouble(montantDiff.getValueNormalisee()), getStyleMontant());
        }
    }

    private void createSheet() {
        HSSFSheet sheet = createSheet("Liste");
        sheet.setColumnWidth((short) COL_CAISSE_AF, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_MONTANT_AF, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_RETENUE, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_FRAIS, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_MONTANT_NET, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_MONTANT_IS_COMPTA, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_MONTANT_DIFF_IS, AbstractListExcel.COLUMN_WIDTH_5500);
        initPage(false);
    }

    private void createTitle() {
        createRow();
        createCell(getLabel("LISTE_AF_RETENUES_PAR_CAF").replace("{0}", dateDebut).replace("{1}",dateFin));
    }

    private void createEntetes() {
        createRow();
        createRow();
        String libelleCanton = canton.isEmpty() || "0".equals(canton) ? getLabel("JSP_TOUS") : getCodeLibelle(canton);

        createCell(getLabel("LISTE_AF_CANTON"), getStyleCritereTitle());
        createCell(libelleCanton, getStyleCritere());
        createRow();
        createRow();
        createCell(getLabel("LISTE_AF_CAISSE_AF"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_MONTANT_AF"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_RETENUE"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_FRAIS_%"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_MONTANT_NET"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_MONTANT_IS_COMPTA"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_DIFF_IS"), getStyleGris25PourcentGras());
    }

    @Override
    public String getNumeroInforom() {
        return LISTES_AF_RETENUES;
    }

    public void setPrestationsAImprimer(Map<String, PrestationGroupee> prestationsAImprimer) {
        this.prestationsAImprimer = prestationsAImprimer;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public Map<String, BigDecimal> getListeComptaAux() {
        return listeComptaAux;
    }

    public void setListeComptaAux(Map<String, BigDecimal> listeComptaAux) {
        this.listeComptaAux = listeComptaAux;
    }
}
