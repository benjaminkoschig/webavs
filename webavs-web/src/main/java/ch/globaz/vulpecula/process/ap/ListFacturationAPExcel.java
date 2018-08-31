package ch.globaz.vulpecula.process.ap;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.process.ap.FacturationAssociationsProfessionnellesProcess.ListePourImpression;

public class ListFacturationAPExcel extends AbstractListExcel {
    private static final String SHEET_TITLE = "Facturation AP";

    private Collection<ListePourImpression> listePourImpression = new ArrayList<ListePourImpression>();

    private final int COL_EMPLOYEUR_NUMERO = 0;
    private final int COL_EMPLOYEUR = 1;
    private final int COL_ANNEE = 2;
    private final int COL_ETAT = 3;
    private final int COL_ASSOCIATION = 4;
    private final int COL_MESSAGE = 5;
    private final int COL_MASSE = 6;
    private final int COL_MONTANT = 7;

    public ListFacturationAPExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        HSSFSheet sheet = createSheet(SHEET_TITLE);
        sheet.setAutobreaks(true);
        HSSFPrintSetup ps = initPage(true);
        ps.setFitHeight((short) 1);
        ps.setFitHeight((short) 0);
        sheet.setColumnWidth((short) COL_EMPLOYEUR_NUMERO, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_EMPLOYEUR, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_ANNEE, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_ETAT, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_ASSOCIATION, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_MESSAGE, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) COL_MASSE, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_MONTANT, AbstractListExcel.COLUMN_WIDTH_MONTANT);

        for (short counter = 1; counter <= ListePourImpression.getCotisationsLabels().size(); counter++) {
            short columnId = (short) (counter + COL_MONTANT);
            sheet.setColumnWidth(columnId, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        }
    }

    @Override
    public void createContent() {
        initPage(true);
        createRow();
        createTable();
    }

    private void createTable() {
        createCell(getSession().getLabel("LISTE_FACTU_AP_NUM_AFFILIE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_FACTU_AP_ENTREPRISE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_FACTU_AP_ANNEE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_FACTU_AP_ETAT"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_FACTU_AP_ASSOCIATION"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_FACTU_AP_MESSAGE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_FACTU_AP_MASSE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_FACTU_AP_MONTANT"), getStyleListGris25PourcentGras());

        for (String cotisationKey : ListePourImpression.getCotisationsLabels().keySet()) {
            createCell(ListePourImpression.getCotisationsLabels(cotisationKey), getStyleListGris25PourcentGras());
        }

        for (ListePourImpression facture : listePourImpression) {
            createRow();
            if (!JadeStringUtil.isEmpty(facture.getIdEmployeur())) {
                Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findById(
                        facture.getIdEmployeur());
                createCell(employeur.getAffilieNumero(), getStyleListLeft());
                createCell(employeur.getRaisonSociale(), getStyleListLeft());
            } else {
                createCell("", getStyleListLeft());
                createCell("", getStyleListLeft());
            }
            createCell(facture.getAnnee().toString(), getStyleListLeft());
            createCell(getSession().getCodeLibelle(facture.getEtat().getValue()), getStyleListLeft());

            if (!JadeStringUtil.isEmpty(facture.getIdAssociation())) {
                Administration association = VulpeculaRepositoryLocator.getAdministrationRepository().findById(
                        facture.getIdAssociation());
                createCell(association.getCodeAdministration(), getStyleListLeft());
            } else {
                createCell("", getStyleListLeft());
            }
            createCell(facture.getMessage(), getStyleListLeft());
            createCell(facture.getMasse(), getStyleMontant());

            if (facture.getMontantTotal() == null) {
                createCell(Montant.ZERO, getStyleMontant());
            } else {
                createCell(facture.getMontantTotal(), getStyleMontant());
            }

            for (String cotisationKey : ListePourImpression.getCotisationsLabels().keySet()) {
                if (facture.getCotisationsMontant().containsKey(cotisationKey)) {
                    createCell(facture.getCotisationsMontant(cotisationKey), getStyleMontant());
                } else {
                    createEmptyCell();
                }
            }
        }
    }

    public Collection<ListePourImpression> getListePourImpression() {
        return listePourImpression;
    }

    public void setListePourImpression(Collection<ListePourImpression> listePourImpression) {
        this.listePourImpression = listePourImpression;
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_FACTURATION_AP_TYPE_NUMBER;
    }
}
