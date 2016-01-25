package ch.globaz.vulpecula.process.caissemaladie;

import globaz.globall.db.BSession;
import java.util.Collection;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.caissemaladie.SuiviCaisseMaladie;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class SuiviCaisseMaladieExcel extends AbstractListExcel {

    private static final int COL_NO_TRAVAILLEUR = 0;
    private static final int COL_NOM_PRENOM = 1;
    private static final int COL_NSS = 2;
    private static final int COL_FEUILLE = 3;

    private Map<Administration, Collection<SuiviCaisseMaladie>> suivis;

    public SuiviCaisseMaladieExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        setWantHeader(false);
        setWantFooter(false);
    }

    public void setSuivis(Map<Administration, Collection<SuiviCaisseMaladie>> suivis) {
        this.suivis = suivis;
    }

    @Override
    public void createContent() {
        for (Map.Entry<Administration, Collection<SuiviCaisseMaladie>> entry : suivis.entrySet()) {
            createSheet(entry.getKey());
            createCriteres(entry.getKey());
            createHeaders();
            for (SuiviCaisseMaladie suivi : entry.getValue()) {
                createRow();
                createCell(suivi.getTravailleur().getId());
                createCell(suivi.getNomTravailleur() + " " + suivi.getPrenomTravailleur());
                createCell(suivi.getNoAVSTravailleur());
                createCell(getSession().getCodeLibelle(suivi.getTypeDocument().getValue()));
            }
        }
    }

    private void createSheet(Administration administration) {
        HSSFSheet sheet = createSheet(administration.getDesignation1() + " " + administration.getDesignation2());
        sheet.setColumnWidth((short) COL_NO_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) COL_NOM_PRENOM, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) COL_NSS, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_FEUILLE, AbstractListExcel.COLUMN_WIDTH_4500);
    }

    private void createCriteres(Administration administration) {
        createRow();
        createCell(administration.getDesignation1() + " " + administration.getDesignation2());
    }

    private void createHeaders() {
        createRow(3);
        createCell(getLabel("LISTE_SUIVI_CAISSES_NO_TRAVAILLEUR"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SUIVI_CAISSES_NOM_PRENOM"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SUIVI_CAISSES_NO_NSS"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_SUIVI_CAISSES_FEUILLE_NO"), getStyleListTitleLeft());
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.SUIVI_CAISSE_STANDARD_TYPE_NUMBER;
    }

}
