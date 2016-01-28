package ch.globaz.vulpecula.process.statistiques;

import globaz.globall.db.BSession;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.domain.models.common.DetailGroupeLocalite;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class SalaireSocioEconomiqueExcel extends AbstractListExcel {
    private static final String SHEET_TITLE = "Stats";

    private List<EntreeSalaireSocioEconomique> entrees;

    private final int COL_ENTREPRISE = 0;
    private final int COL_ACTIVITE_DEB = 1;
    private final int COL_ACTIVITE_FIN = 2;
    private final int COL_REGION = 3;
    private final int COL_DISTRICT = 4;
    private final int COL_CAISSE_METIER = 5;
    private final int COL_CONVENTION = 6;
    private final int COL_TRAVAILLEUR = 7;
    private final int COL_SALAIRE = 8;
    private final int COL_QUALIFICATION = 9;

    public SalaireSocioEconomiqueExcel(List<EntreeSalaireSocioEconomique> entrees, BSession session,
            String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        this.entrees = entrees;

        HSSFSheet sheet = createSheet(SHEET_TITLE);
        sheet.setColumnWidth((short) COL_ENTREPRISE, AbstractListExcel.COLUMN_WIDTH_REMARQUE);
        sheet.setColumnWidth((short) COL_ACTIVITE_DEB, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) COL_ACTIVITE_FIN, AbstractListExcel.COLUMN_WIDTH_3500);
        sheet.setColumnWidth((short) COL_REGION, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_DISTRICT, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_CAISSE_METIER, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_CONVENTION, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_TRAVAILLEUR, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        sheet.setColumnWidth((short) COL_SALAIRE, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_QUALIFICATION, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
    }

    @Override
    public void createContent() {
        initPage(true);

        createRow();
        createCell(getSession().getLabel("LISTE_EMPLOYEUR"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_ACTIVITE_DEBUT"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_ACTIVITE_FIN"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_REGION"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_DISTRICT"), getStyleListTitleLeft());
        createCell(getSession().getLabel("JSP_CAISSE_METIER"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_CONVENTION"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_TRAVAILLEUR"), getStyleListTitleLeft());
        createCell(getSession().getLabel("LISTE_SALAIRES"), getStyleListTitleLeft());
        createCell(getSession().getLabel("JSP_QUALIFICATION"), getStyleListTitleLeft());

        for (EntreeSalaireSocioEconomique entree : entrees) {
            for (Map.Entry<PosteTravail, Collection<DecompteSalaire>> entry : entree.getDecomptesGroupByPoste()
                    .entrySet()) {

                PosteTravail poste = entry.getKey();

                createRow();

                createCell(entree.getEmployeur().getRaisonSociale());
                createCell(entree.getEmployeur().getDateDebut());
                createCell(entree.getEmployeur().getDateFin());

                // Test si region / district / caisseMetier est null, et crétion de cellule en conséquence
                DetailGroupeLocalite region = entree.getDetailGroupeLocalites().getRegion();
                DetailGroupeLocalite district = entree.getDetailGroupeLocalites().getDistrict();

                if (region == null) {
                    createCell("Autre");
                } else {
                    createCell(region.getGroupeLocalite().getNomGroupeFR());
                }

                if (district == null) {
                    createCell("Autre");
                } else {
                    createCell(district.getGroupeLocalite().getNomGroupeFR());
                }

                if (entree.getCaisseMetier() == null) {
                    createCell("");
                } else {
                    createCell(entree.getCaisseMetier().getLibellePlanCaisse());
                }

                createCell(entree.getEmployeur().getNomConvention());

                createCell(poste.getNomPrenomTravailleur());

                Montant salaireAnnee = Montant.ZERO;
                for (DecompteSalaire decompte : entry.getValue()) {
                    salaireAnnee = salaireAnnee.add(decompte.getSalaireTotal());
                }

                createCell(salaireAnnee);
                createCell(getSession().getCodeLibelle(poste.getQualificationAsValue()));
            }
        }
    }

    @Override
    public String getNumeroInforom() {
        return null;
    }

}
