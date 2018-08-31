package ch.globaz.vulpecula.process.af;

import globaz.globall.db.BSession;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ListeDecomptesSansAFExcel extends AbstractListExcel {

    private String idPassageFacturation;
    private List<Decompte> decomptes;

    public ListeDecomptesSansAFExcel(BSession session, String filenameRoot, String documentTitle,
            List<Decompte> decomptes, String idPassageFacturation) {
        super(session, filenameRoot, documentTitle);
        this.decomptes = decomptes;
        this.idPassageFacturation = idPassageFacturation;
    }

    @Override
    public void createContent() {
        HSSFSheet sheet = createSheet("Liste des affiliés non générés AF pour le passage " + idPassageFacturation);
        createRow();

        sheet.setColumnWidth((short) 0, (short) 3500);
        sheet.setColumnWidth((short) 1, (short) 3500);
        sheet.setColumnWidth((short) 2, (short) 7500);
        sheet.setColumnWidth((short) 3, (short) 7500);

        createCell("Id décompte", getStyleListTitleCenter());
        createCell("Numéro affilié", getStyleListTitleCenter());
        createCell("Employeur", getStyleListTitleCenter());
        createCell("Période", getStyleListTitleCenter());

        for (Decompte decompte : decomptes) {
            createRow();
            createCell(decompte.getId());
            createCell(decompte.getEmployeurAffilieNumero());
            createCell(decompte.getEmployeur().getRaisonSociale());
            createCell(decompte.getPeriode().toString());
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_DECOMPTES_SANS_AF;
    }

}
