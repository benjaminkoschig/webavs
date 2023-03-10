package ch.globaz.vulpecula.process.statistiques;

import globaz.globall.db.BSession;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class SalaireQualificationExcel extends AbstractListExcel {
    private static final String SHEET_TITLE = "Stats";

    private Map<String, List<EntreeSalaireQualification>> entrees;
    private String convention;
    private Date dateDebut;
    private Date dateFin;

    private final int COL_REGION = 0;
    private final int COL_QUALIFICATION = 1;
    private final int COL_NOMBRE_POSTE = 2;
    private final int COL_MOYENNE_SALAIRE_HORAIRE = 3;

    public SalaireQualificationExcel(Map<String, List<EntreeSalaireQualification>> entrees, Date dateDebut,
            Date dateFin, String convention, BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);

        this.entrees = entrees;
        this.convention = convention;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;

    }

    @Override
    public void createContent() {

        boolean firstSheet = true;

        for (Map.Entry<String, List<EntreeSalaireQualification>> entree : entrees.entrySet()) {

            convention = entree.getKey();

            if (firstSheet) {
                HSSFSheet sheet = createSheet(convention);
                sheet.setColumnWidth((short) COL_REGION, AbstractListExcel.COLUMN_WIDTH_5500);
                sheet.setColumnWidth((short) COL_QUALIFICATION, AbstractListExcel.COLUMN_WIDTH_5500);
                sheet.setColumnWidth((short) COL_NOMBRE_POSTE, AbstractListExcel.COLUMN_WIDTH_3500);
                sheet.setColumnWidth((short) COL_MOYENNE_SALAIRE_HORAIRE, AbstractListExcel.COLUMN_WIDTH_3500);
                initPage(true);
                firstSheet = false;
            } else {
                HSSFSheet sheet = createSheet(convention);
                sheet.setColumnWidth((short) COL_REGION, AbstractListExcel.COLUMN_WIDTH_5500);
                sheet.setColumnWidth((short) COL_QUALIFICATION, AbstractListExcel.COLUMN_WIDTH_5500);
                sheet.setColumnWidth((short) COL_NOMBRE_POSTE, AbstractListExcel.COLUMN_WIDTH_3500);
                sheet.setColumnWidth((short) COL_MOYENNE_SALAIRE_HORAIRE, AbstractListExcel.COLUMN_WIDTH_3500);

            }

            createRow();
            createCell("Statistique des salaires du " + dateDebut + " au " + dateFin + " pour " + convention);

            createRow();

            createRow();
            createCell(getSession().getLabel("LISTE_REGION"), getStyleListTitleLeft());
            createCell(getSession().getLabel("JSP_QUALIFICATION"), getStyleListTitleLeft());
            createCell(getSession().getLabel("LISTE_NOMBRE_POSTE"), getStyleListTitleLeft());
            createCell(getSession().getLabel("LISTE_MOYENNE_SALAIRE_HORAIRE"), getStyleListTitleLeft());

            for (EntreeSalaireQualification entreeSalaire : entree.getValue()) {
                createRow();
                Pair<String, Qualification> pairRegionQualif = entreeSalaire.getPairRegionQualification();

                String region = pairRegionQualif.getLeft();
                if (region == null) {
                    createCell("Autre");
                } else {
                    createCell(region);
                }

                createCell(getSession().getCodeLibelle(pairRegionQualif.getRight().getValue()));

                createCell(entreeSalaire.getPostes().size());

                createCell(entreeSalaire.getMoyenneSalaireHoraire().normalize(), getStyleMontantNoBorder());
            }
        }
    }

    @Override
    public String getNumeroInforom() {
        return null;
    }

}
