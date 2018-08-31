package ch.globaz.vulpecula.documents.ctrlemployeur;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.Region;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.controleemployeur.ControleEmployeur;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ListeControleEmployeurParAnneeExcel extends AbstractListExcel {
    private static final String SHEET_TITLE = "controles";
    protected static final double UNITS_COL_FACTOR = 0.0036;
    private String annee;

    Map<String, List<ControleEmployeur>> datas = null;

    public ListeControleEmployeurParAnneeExcel(BSession session,
            Map<String, List<ControleEmployeur>> mapControlesByConvention, String annee) {
        super(BSessionUtil.getSessionFromThreadContext(),
                DocumentConstants.LISTES_CONTROLES_EMPLOYEURS_PAR_ANNEE_DOC_NAME,
                DocumentConstants.LISTES_CONTROLES_EMPLOYEURS_PAR_ANNEE_NAME);
        datas = mapControlesByConvention;
        this.annee = annee;
    }

    private void createOnglet(String nomOnglet) {
        HSSFSheet sheet = createSheet(nomOnglet);

        // Permet d'ajuster les colonnes sur toute la page lors de l'impression
        sheet.getPrintSetup().setLandscape(true);
        sheet.getPrintSetup().setFitWidth((short) 1);
        sheet.getPrintSetup().setFitHeight((short) 0);
        sheet.setAutobreaks(true);

        // N° affiliation
        sheet.setColumnWidth((short) 0, AbstractListExcel.COLUMN_WIDTH_3500);
        // Employeur
        sheet.setColumnWidth((short) 1, AbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        // N° rapport
        sheet.setColumnWidth((short) 2, AbstractListExcel.COLUMN_WIDTH_3500);
        // Date du contrôle
        sheet.setColumnWidth((short) 3, AbstractListExcel.COLUMN_WIDTH_DATE);
        // Période
        sheet.setColumnWidth((short) 4, AbstractListExcel.COLUMN_WIDTH_DATE);
        // Montant
        sheet.setColumnWidth((short) 5, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        // Type
        sheet.setColumnWidth((short) 6, AbstractListExcel.COLUMN_WIDTH_3500);
        // Autres mesures
        sheet.setColumnWidth((short) 7, AbstractListExcel.COLUMN_WIDTH_3500);
        // Réviseur
        sheet.setColumnWidth((short) 8, AbstractListExcel.COLUMN_WIDTH_3500);

        Region region = new Region(2, (short) 0, 2, (short) 1);
        sheet.addMergedRegion(region);
        Region region2 = new Region(3, (short) 0, 3, (short) 1);
        sheet.addMergedRegion(region2);
    }

    @Override
    public void createContent() {
        createTable();
    }

    private void setTitle(String nomConvention) {
        createRow();
        createRow();
        createRow();
        createCell(getSession().getLabel("JSP_LISTE_CONTROLE_EMPLOYEUR_TITLE") + " " + annee, getStyleGras());
        createRow();
        createCell("Convention " + nomConvention, getStyleGras());
        createRow();
        createRow();
        createCell(getSession().getLabel("JSP_LISTE_CONTROLE_EMPLOYEUR_COLUMN_NO_AFFILIATION"),
                getStyleListTitleCenter());
        createCell(getSession().getLabel("JSP_LISTE_CONTROLE_EMPLOYEUR_COLUMN_EMPLOYEUR"), getStyleListTitleCenter());
        createCell(getSession().getLabel("JSP_LISTE_CONTROLE_EMPLOYEUR_COLUMN_NO_RAPPORT"), getStyleListTitleCenter());
        createCell(getSession().getLabel("JSP_LISTE_CONTROLE_EMPLOYEUR_COLUMN_DATE_CONTROLE"),
                getStyleListTitleCenter());
        createCell(getSession().getLabel("JSP_LISTE_CONTROLE_EMPLOYEUR_COLUMN_PERIODE"), getStyleListTitleCenter());
        createCell(getSession().getLabel("JSP_LISTE_CONTROLE_EMPLOYEUR_COLUMN_MONTANT"), getStyleListTitleCenter());
        createCell(getSession().getLabel("JSP_LISTE_CONTROLE_EMPLOYEUR_COLUMN_TYPE"), getStyleListTitleCenter());
        createCell(getSession().getLabel("JSP_LISTE_CONTROLE_EMPLOYEUR_COLUMN_AUTRES_MESURES"),
                getStyleListTitleCenter());
        createCell(getSession().getLabel("JSP_LISTE_CONTROLE_EMPLOYEUR_COLUMN_REVISEUR"), getStyleListTitleCenter());

    }

    private void createTable() {
        for (Entry<String, List<ControleEmployeur>> entry : datas.entrySet()) {
            createOnglet(entry.getKey());
            setTitle(entry.getKey());

            Montant totalMontants = new Montant(0);
            int nbElements = 0;
            for (ControleEmployeur controleEmployeur : entry.getValue()) {
                createRow();
                createCell(controleEmployeur.getEmployeur().getAffilieNumero());
                createCell(controleEmployeur.getEmployeur().getRaisonSociale());
                createCell(controleEmployeur.getNumeroMeroba());
                createCell(controleEmployeur.getDateControle().getSwissValue());
                createCell(controleEmployeur.getDateAuAsSwissValue());
                createCell(controleEmployeur.getMontant());
                if (controleEmployeur.getType() != null) {
                    createCell(getSession().getCodeLibelle(controleEmployeur.getType().getValue()));
                } else {
                    createCell("Sans type");
                }
                createCell(controleEmployeur.isAutresMesures() ? "Oui" : "Non");
                createCell(controleEmployeur.getIdUtilisateur());
                nbElements++;
                totalMontants = totalMontants.add(controleEmployeur.getMontant());
            }
            createRow();
            createCell(getSession().getLabel("JSP_LISTE_COLUMN_TOTAL_DE_CAS"), getStyleListTitleLeft());
            createCell(nbElements, getStyleListTitleCenter());
            createEmptyCell(3);
            createCell(totalMontants, getStyleMontantBorderMediumTotal());
            createEmptyCell(3);
        }
    }

    @Override
    public String getNumeroInforom() {
        // TODO Auto-generated method stub
        return null;
    }

}
