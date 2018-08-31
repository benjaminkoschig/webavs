package ch.globaz.vulpecula.process.taxationoffice;

import globaz.globall.db.BSession;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ImprimerTaxationOfficeAnnuleExcel extends AbstractListExcel {

    private List<TaxationOffice> tos;

    public ImprimerTaxationOfficeAnnuleExcel(BSession session, String filenameRoot, String documentTitle,
            List<TaxationOffice> tos) {
        super(session, filenameRoot, documentTitle);
        this.tos = tos;
    }

    @Override
    public void createContent() {
        HSSFSheet sheet = createSheet("Liste des TO prévisionnelles");
        createRow();

        sheet.setColumnWidth((short) 0, (short) 4000);
        sheet.setColumnWidth((short) 1, (short) 12000);
        sheet.setColumnWidth((short) 2, (short) 4000);
        sheet.setColumnWidth((short) 3, (short) 4000);
        sheet.setColumnWidth((short) 4, (short) 5000);
        sheet.setColumnWidth((short) 5, (short) 3000);

        createCell("Id", getStyleListTitleCenter());
        createCell("Employeur", getStyleListTitleCenter());
        createCell("Numéro affilié", getStyleListTitleCenter());
        createCell("Etat", getStyleListTitleCenter());
        createCell("Période", getStyleListTitleCenter());
        createCell("Montant", getStyleListTitleCenter());

        for (TaxationOffice to : tos) {
            createRow();
            createCell(to.getIdDecompte());
            createCell(to.getEmployeur().getRaisonSociale());
            createCell(to.getEmployeurAffilieNumero());
            createCell(getCodeLibelle(to.getEtatValue()));
            createCell(to.getPeriodeDebutAsSwissValue() + " - " + to.getPeriodeFinAsSwissValue());
            createCell(new Montant(to.getMontant()).doubleValue(), getStyleMontantNoBorder());
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_TO_ANNULE_TYPE_NUMBER;
    }

}
