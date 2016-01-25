package ch.globaz.vulpecula.process.caissemaladie;

import globaz.globall.db.BSession;
import java.util.Collection;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public abstract class CaisseMaladieExcel extends AbstractListExcel {
    private Map<Administration, Collection<AffiliationCaisseMaladie>> affiliationsGroupByCaisseMaladie;

    private final int COL_N_AVS = 0;
    private final int COL_NOM = 1;
    private final int COL_PRENOM = 2;
    private final int COL_MOIS_DEBUT = 3;
    private final int COL_MOIS_FIN = 4;

    public CaisseMaladieExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        setWantHeader(false);
        setWantFooter(false);
    }

    public void setAffiliationsCaisseMaladie(
            Map<Administration, Collection<AffiliationCaisseMaladie>> affiliationsGroupByCaisseMaladie) {
        this.affiliationsGroupByCaisseMaladie = affiliationsGroupByCaisseMaladie;
    }

    @Override
    public void createContent() {
        for (Map.Entry<Administration, Collection<AffiliationCaisseMaladie>> entry : affiliationsGroupByCaisseMaladie
                .entrySet()) {
            Administration caisseMaladie = entry.getKey();
            createSheet(caisseMaladie);

            createHeader();
            createRow(2);

            createCriteres(caisseMaladie);
            createRow(2);
            createEntetes();

            createRows(entry.getValue());

            createFooter(getNumeroInforom());
        }
    }

    private void createSheet(Administration caisseMaladie) {
        HSSFSheet sheet = createSheet(caisseMaladie.getDesignation1());
        sheet.setColumnWidth((short) COL_N_AVS, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_NOM, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_PRENOM, AbstractListExcel.COLUMN_WIDTH_4500);
        sheet.setColumnWidth((short) COL_MOIS_DEBUT, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_MOIS_FIN, AbstractListExcel.COLUMN_WIDTH_DATE);
    }

    private void createCriteres(Administration caisseMaladie) {
        createRow();
        createCell("Caisse maladie", getStyleCritereTitle());
        createCell(caisseMaladie.getDesignation1(), getStyleCritere());
    }

    private void createEntetes() {
        createCell(getLabel("LISTE_ANNONCE_CAISSES_MALADIES_COL0"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_ANNONCE_CAISSES_MALADIES_COL1"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_ANNONCE_CAISSES_MALADIES_COL2"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_ANNONCE_CAISSES_MALADIES_COL3"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_ANNONCE_CAISSES_MALADIES_COL4"), getStyleListTitleLeft());
    }

    private void createRows(Collection<AffiliationCaisseMaladie> affiliationsCaisseMaladie) {
        for (AffiliationCaisseMaladie affiliationCaisseMaladie : affiliationsCaisseMaladie) {
            createRow();
            createCell(affiliationCaisseMaladie.getNoAVSTravailleur(), getStyleListLeft());
            createCell(affiliationCaisseMaladie.getNomTravailleur(), getStyleListLeft());
            createCell(affiliationCaisseMaladie.getPrenomTravailleur(), getStyleListLeft());
            createCell(affiliationCaisseMaladie.getMoisDebut().getMois() + "."
                    + affiliationCaisseMaladie.getMoisDebut().getAnnee(), getStyleListLeft());
            if (affiliationCaisseMaladie.getMoisFin() != null) {
                createCell(affiliationCaisseMaladie.getMoisFin().getMois() + "."
                        + affiliationCaisseMaladie.getMoisDebut().getAnnee(), getStyleListLeft());
            } else {
                createEmptyCell();
            }
        }
    }
}
