package ch.globaz.vulpecula.process.entreprises;

import globaz.globall.db.BSession;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.process.prestations.PrestationsListExcel;

public class ListEntreprisesExcel extends PrestationsListExcel {
    private static final String SHEET_TITLE = "EN";
    private List<Employeur> listEntreprises;

    private final int COL_NUM_AFFILIE = 0;
    private final int COL_ENTREPRISE = 1;
    private final int COL_DATE_DEBUT_AFFILIATION = 2;
    private final int COL_DATE_DEBUT_SANS_PERSONNEL = 3;
    private final int COL_TYPE_SOCIETE = 4;
    private final int COL_ADRESSE = 5;

    private static final String EMPTY_CRITERE = "-";

    public ListEntreprisesExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        HSSFSheet sheet = createSheet(SHEET_TITLE);
        sheet.setColumnWidth((short) COL_NUM_AFFILIE, AbstractListExcel.COLUMN_WIDTH_AFILIE);
        sheet.setColumnWidth((short) COL_ENTREPRISE, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) COL_DATE_DEBUT_AFFILIATION, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_DATE_DEBUT_SANS_PERSONNEL, AbstractListExcel.COLUMN_WIDTH_DATE);
        sheet.setColumnWidth((short) COL_TYPE_SOCIETE, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) COL_ADRESSE, AbstractListExcel.COLUMN_WIDTH_ADRESS);
    }

    @Override
    public void createContent() {
        initPage(true);
        createRow();
        createCriteresConvention();
        createRow(2);
        createTable();
    }

    private void createCriteresConvention() {
        createRow();
        createCell(getSession().getLabel("LISTE_CONVENTION"), getStyleCritereTitle());
        if (convention.getId() != null) {
            createCell(convention.getCode() + " " + convention.getDesignation(), getStyleCritere());
        } else {
            createCell(EMPTY_CRITERE);
        }
    }

    private void createTable() {
        createCell(getSession().getLabel("LISTE_NUM_AFFILIE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_ENTREPRISE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_DATE_DEBUT_AFFILIATION"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_DATE_DEBUT_SANS_PERSONNEL"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_TYPE_SOCIETE"), getStyleListGris25PourcentGras());
        createCell(getSession().getLabel("LISTE_ADRESSE"), getStyleListGris25PourcentGras());

        for (Employeur employeur : listEntreprises) {
            createRow();
            createCell(employeur.getAffilieNumero(), getStyleListLeft());
            createCell(employeur.getRaisonSociale(), getStyleListLeft());
            createCell(employeur.getDateDebut(), getStyleListLeft());
            if (employeur.getParticularites().size() > 0) {
                createCell(employeur.getParticularites().get(0).getDateDebut().toString(), getStyleListLeft());
            } else {
                createEmptyCell();
            }
            createCell(getSession().getCodeLibelle(employeur.getPersonnaliteJuridique()), getStyleListLeft());
            createCell(employeur.getAdressePrincipale().toString(), getStyleListLeft());
        }

    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_ENTREPRISES_TYPE_NUMBER;
    }

    @Override
    public String getListName() {
        return getSession().getLabel("LISTE_ENTREPRISES");
    }

    public void setEntreprises(List<Employeur> listEmployeurs) {
        listEntreprises = listEmployeurs;
    }
}
