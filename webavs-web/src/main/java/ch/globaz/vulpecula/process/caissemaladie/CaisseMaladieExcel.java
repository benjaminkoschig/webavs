package ch.globaz.vulpecula.process.caissemaladie;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Collection;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public abstract class CaisseMaladieExcel extends AbstractListExcel {
    private static final String NON_ANNONCEES = "Non annoncées";

    private Map<Administration, Collection<AffiliationCaisseMaladie>> affiliationsGroupByCaisseMaladie;

    private final int COL_N_AVS = 0;
    private final int COL_NOM = 1;
    private final int COL_PRENOM = 2;
    private final int COL_MOIS_DEBUT = 3;
    private final int COL_MOIS_FIN = 4;
    private final int COL_AMCAB = 5;

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
            createEntetes(caisseMaladie);

            createRows(entry.getValue(), caisseMaladie);

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
        if (NON_ANNONCEES.equals(caisseMaladie.getDesignation1())) {
            sheet.setColumnWidth((short) COL_AMCAB, AbstractListExcel.COLUMN_WIDTH_DATE);
        }
    }

    private void createCriteres(Administration caisseMaladie) {
        createRow();
        createCell(getSession().getLabel("JSP_CAISSE_MALADIE"), getStyleCritereTitle());
        createCell(caisseMaladie.getDesignation1(), getStyleCritere());
        if (this instanceof CaisseMaladieAdmissionExcel && NON_ANNONCEES.equals(caisseMaladie.getDesignation1())) {
            if (!JadeStringUtil.isBlankOrZero(((CaisseMaladieAdmissionExcel) this).getDatePeriodeDebut())) {
                Date dateDebut = new Date(((CaisseMaladieAdmissionExcel) this).getDatePeriodeDebut());
                createCell(getSession().getLabel("PLACEHOLDER_DATE_DEBUT"), getStyleCritereTitle());
                createCell(dateDebut.getJour() + "." + dateDebut.getMois() + "." + dateDebut.getAnnee(),
                        getStyleCritereTitle());
            }
            if (!JadeStringUtil.isBlankOrZero(((CaisseMaladieAdmissionExcel) this).getDatePeriodeFin())) {
                Date dateFin = new Date(((CaisseMaladieAdmissionExcel) this).getDatePeriodeFin());
                createCell(getSession().getLabel("PLACEHOLDER_DATE_FIN"), getStyleCritereTitle());
                createCell(dateFin.getJour() + "." + dateFin.getMois() + "." + dateFin.getAnnee(),
                        getStyleCritereTitle());
            }
        }
    }

    private void createEntetes(Administration caisseMaladie) {
        createCell(getLabel("LISTE_ANNONCE_CAISSES_MALADIES_COL0"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_ANNONCE_CAISSES_MALADIES_COL1"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_ANNONCE_CAISSES_MALADIES_COL2"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_ANNONCE_CAISSES_MALADIES_COL3"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_ANNONCE_CAISSES_MALADIES_COL4"), getStyleListTitleLeft());
        // Pour les cas non annoncés, on ajoute une colonne pour permettre de designer s'il s'agit d'un cas AMCAB ou
        // non.
        if (NON_ANNONCEES.equals(caisseMaladie.getDesignation1())) {

            createCell(getLabel("LISTE_ANNONCE_CAISSES_MALADIES_COL5"), getStyleListTitleLeft());
        }
    }

    private void createRows(Collection<AffiliationCaisseMaladie> affiliationsCaisseMaladie, Administration caisseMaladie) {
        for (AffiliationCaisseMaladie affiliationCaisseMaladie : affiliationsCaisseMaladie) {
            createRow();
            createCell(affiliationCaisseMaladie.getNoAVSTravailleur(), getStyleListLeft());
            createCell(affiliationCaisseMaladie.getNomTravailleur(), getStyleListLeft());
            createCell(affiliationCaisseMaladie.getPrenomTravailleur(), getStyleListLeft());
            if (affiliationCaisseMaladie.getMoisDebut() != null) {
                createCell(affiliationCaisseMaladie.getMoisDebut().getMois() + "."
                        + affiliationCaisseMaladie.getMoisDebut().getAnnee(), getStyleListLeft());
            } else {
                createEmptyCell();
            }
            if (affiliationCaisseMaladie.getMoisFin() != null) {
                createCell(affiliationCaisseMaladie.getMoisFin().getMois() + "."
                        + affiliationCaisseMaladie.getMoisFin().getAnnee(), getStyleListLeft());
            } else {
                createEmptyCell();
            }

            if (NON_ANNONCEES.equals(caisseMaladie.getDesignation1())) {
                createCell(affiliationCaisseMaladie.getHasCotisationAMCAB(), getStyleListCenter());
            }
        }
    }
}
