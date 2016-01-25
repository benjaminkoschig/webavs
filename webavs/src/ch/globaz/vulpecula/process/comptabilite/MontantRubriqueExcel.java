package ch.globaz.vulpecula.process.comptabilite;

import globaz.globall.db.BSession;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class MontantRubriqueExcel extends AbstractListExcel {

    private static final String LISTE = "liste";
    private List<MontantRubriqueEntity> entities;
    private Date date;

    public MontantRubriqueExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
    }

    @Override
    public void createContent() {
        createSheet();
        createCriteres();
        createRow(2);
        createEntetes();

        for (MontantRubriqueEntity montantRubrique : getEntities()) {
            createRow();
            createCell(montantRubrique.getNumRubrique(), getStyleListLeft());
            createCell(montantRubrique.getLibelle(), getStyleListLeft());
            createCell(montantRubrique.getCodeAdministration(), getStyleListLeft());
            createCell(Montant.valueOf(montantRubrique.getMasse()), getStyleMontant());
            createCell(Montant.valueOf(montantRubrique.getMontant()), getStyleMontant());
        }
    }

    private void createSheet() {
        HSSFSheet sheet = createSheet(MontantRubriqueExcel.LISTE);
        sheet.setColumnWidth((short) 0, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) 1, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) 2, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) 3, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) 4, AbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    private void createEntetes() {
        createCell("Compte", getStyleListTitleLeft());
        createCell("Libellé", getStyleListTitleLeft());
        createCell("Caisse métier", getStyleListTitleLeft());
        createCell("Masse", getStyleListTitleLeft());
        createCell("Salaire", getStyleListTitleLeft());
    }

    private void createCriteres() {
        createRow();
        createCell(getSession().getLabel("JSP_DATE"), getStyleCritereTitle());
        createCell(date.getMoisAnneeFormatte(), getStyleCritere());
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_MONTANT_RUBRIQUE;
    }

    public List<MontantRubriqueEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<MontantRubriqueEntity> entities) {
        this.entities = entities;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
