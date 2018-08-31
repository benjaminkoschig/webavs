package ch.globaz.vulpecula.process.listeslpp;

import globaz.globall.db.BSession;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ListeCPsoumisLPPExcel extends AbstractListExcel {

	private Annee annee;
    private List<CongePaye> congesPayes;

    public ListeCPsoumisLPPExcel(BSession session, String filenameRoot, String documentTitle,
            List<CongePaye> congesPayes, Annee annee) {
        super(session, filenameRoot, documentTitle);
        this.congesPayes = congesPayes;
        this.annee = annee;
    }

    @Override
    public void createContent() {
        HSSFSheet sheet = createSheet("CP soumis LPP pour " + annee.getValue());
        createRow();

        sheet.setColumnWidth((short) 0, (short) 8000);
        sheet.setColumnWidth((short) 1, (short) 1500);
        sheet.setColumnWidth((short) 2, (short) 3000);
        sheet.setColumnWidth((short) 3, (short) 3000);
        sheet.setColumnWidth((short) 4, (short) 3500);
        sheet.setColumnWidth((short) 5, (short) 7500);
        sheet.setColumnWidth((short) 6, (short) 3000);

        createCell("Travailleur", getStyleListTitleCenter());
        createCell("Type", getStyleListTitleCenter());
        createCell("Masse", getStyleListTitleCenter());
        createCell("Période", getStyleListTitleCenter());
        createCell("Numéro affilié", getStyleListTitleCenter());
        createCell("Employeur", getStyleListTitleCenter());
        createCell("Versement", getStyleListTitleCenter());

        for (CongePaye congePaye : congesPayes) {
            createRow();
            createCell(congePaye.getNomPrenomTravailleur());
            createCell(congePaye.getAssuranceLPP().getLibelleCourtFr());
            createCell(congePaye.getMontantBrut().getBigDecimalValue().doubleValue(), getStyleMontantNoBorder());
            createCell(congePaye.getAnneeDebutAsValue() + " - " + congePaye.getAnneeFinAsValue());
            createCell(congePaye.getEmployeur().getAffilieNumero());
            createCell(congePaye.getEmployeur().getRaisonSociale());
            createCell(congePaye.getDateVersement().getSwissValue());
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_CP_SOUMIS_LPP_TYPE_NUMBER;
    }

}
