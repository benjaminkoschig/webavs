package ch.globaz.vulpecula.process.is;

import globaz.globall.db.BSession;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.businessimpl.services.is.PrestationGroupee;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ListISParCAFExcel extends AbstractListExcel {
    private final int COL_CAISSE_AF = 0;
    private final int COL_MONTANT_AF = 1;
    private final int COL_RETENUE = 2;
    private final int COL_FRAIS = 3;
    private final int COL_MONTANT_NET = 4;

    private Map<String, PrestationGroupee> prestationsAImprimer;

    private Annee annee;

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
            Montant montantRetenue = prestation.getImpots();
            Montant montantFrais = prestation.getFrais();
            Montant montantNet = montantRetenue.substract(montantFrais);
            createRow();
            createCell(prestation.getLibelleCaisseAF(), getStyleListLeft());
            createCell(prestation.getMontantPrestations().getValue(), getStyleMontant());
            createCell(prestation.getImpots().getValue(), getStyleMontant());
            createCell(prestation.getFrais().getValue(), getStyleMontant());
            createCell(montantNet.getValue(), getStyleMontant());
        }
    }

    private void createSheet() {
        HSSFSheet sheet = createSheet("Liste");
        sheet.setColumnWidth((short) COL_CAISSE_AF, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_MONTANT_AF, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_RETENUE, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_FRAIS, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_MONTANT_NET, AbstractListExcel.COLUMN_WIDTH_5500);
        initPage(false);
    }

    private void createTitle() {
        createRow();
        createCell(getLabel("LISTE_AF_RETENUES_PAR_CAF").replace(":annee", String.valueOf(annee.getValue())));
    }

    private void createEntetes() {
        createRow();
        createRow();
        createRow();
        createCell(getLabel("LISTE_AF_CAISSE_AF"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_MONTANT_AF"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_RETENUE"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_FRAIS_%"), getStyleGris25PourcentGras());
        createCell(getLabel("LISTE_AF_MONTANT_NET"), getStyleGris25PourcentGras());
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_AF_RETENUES;
    }

    public void setPrestationsAImprimer(Map<String, PrestationGroupee> prestationsAImprimer) {
        this.prestationsAImprimer = prestationsAImprimer;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }
}
