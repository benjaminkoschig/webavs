package ch.globaz.vulpecula.process.syndicats;

import globaz.globall.db.BSession;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ListeTravailleursSansSyndicatExcel extends AbstractListExcel {
    private List<Travailleur> travailleurs;

    private final int COL_N_AVS = 0;
    private final int COL_NOM = 1;
    private final int COL_PRENOM = 2;
    private final int COL_DATE_NAISSANCE = 3;

    private Annee annee;

    public ListeTravailleursSansSyndicatExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        HSSFSheet sheet = createSheet(DocumentConstants.LISTES_TRAVAILLEURS_SANS_SYNDICAT_NAME);
        sheet.setColumnWidth((short) COL_N_AVS, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_NOM, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_PRENOM, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_DATE_NAISSANCE, AbstractListExcel.COLUMN_WIDTH_4500);
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    @Override
    public void createContent() {
        initPage(true);
        createCriteres();
        createEntetes();
        createRows();
    }

    private void createCriteres() {
        createRow();
        createCell(getLabel("LISTE_SYNDICATS_ANNEE"), getStyleCritereTitle());
        createCell(annee.getValue());
    }

    private void createEntetes() {
        createRow();
        createCell(getLabel("LISTE_SYNDICATS_NO_AVS"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SYNDICATS_NOM"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SYNDICATS_PRENOM"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SYNDICATS_DATE_DE_NAISSANCE"), getStyleListTitleLeft());
    }

    private void createRows() {
        for (Travailleur travailleur : travailleurs) {
            createRow();
            createCell(travailleur.getNumAvsActuel(), getStyleListLeft());
            createCell(travailleur.getDesignation1(), getStyleListLeft());
            createCell(travailleur.getDesignation2(), getStyleListLeft());
            createCell(travailleur.getDateNaissance(), getStyleListLeft());
        }
    }

    public void setTravailleurs(List<Travailleur> travailleurs) {
        this.travailleurs = travailleurs;
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_TRAVAILLEURS_SANS_SYNDICAT;
    }
}
