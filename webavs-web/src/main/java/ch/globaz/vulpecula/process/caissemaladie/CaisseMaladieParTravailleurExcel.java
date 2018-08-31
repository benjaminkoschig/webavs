package ch.globaz.vulpecula.process.caissemaladie;

import globaz.globall.db.BSession;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class CaisseMaladieParTravailleurExcel extends AbstractListExcel {

    private Map<String, LigneDecompteCMDTO> map;

    public CaisseMaladieParTravailleurExcel(BSession session, String filenameRoot, String documentTitle,
            Map<String, LigneDecompteCMDTO> map) {
        super(session, filenameRoot, documentTitle);
        this.map = map;
    }

    @Override
    public void createContent() {
        HSSFSheet sheet = createSheet(DocumentConstants.LISTES_AMCAB);

        /*
         * Mise en page de la feuille
         */
        HSSFPrintSetup printSetup = sheet.getPrintSetup();
        printSetup = initPage(true);
        printSetup.setFitWidth((short) 1);

        createRow();

        /*
         * Paramétrage des colonnes
         */
        sheet.setColumnWidth((short) 0, (short) 3500);
        sheet.setColumnWidth((short) 1, (short) 7500);
        sheet.setColumnWidth((short) 2, (short) 3500);
        sheet.setColumnWidth((short) 3, (short) 7500);
        sheet.setColumnWidth((short) 4, (short) 3000);
        sheet.setColumnWidth((short) 5, (short) 5000);
        sheet.setColumnWidth((short) 6, (short) 3500);
        sheet.setColumnWidth((short) 7, (short) 3500);
        sheet.setColumnWidth((short) 8, (short) 3000);
        sheet.setColumnWidth((short) 9, (short) 3000);
        sheet.setColumnWidth((short) 10, (short) 3000);
        sheet.setColumnWidth((short) 11, (short) 3000);
        sheet.setColumnWidth((short) 12, (short) 3000);
        sheet.setColumnWidth((short) 13, (short) 3000);
        sheet.setColumnWidth((short) 14, (short) 7500);

        /*
         * Création des en-têtes de colonne
         */
        createCell(getLabel("LISTE_AMCAB_COL_IDEXTERNE"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_AMCAB_COL_CONVENTION"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_AMCAB_COL_AFFILIE"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_AMCAB_COL_EMPLOYEUR"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_AMCAB_COL_IDPOSTE"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_AMCAB_COL_NSS"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_AMCAB_COL_NOM"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_AMCAB_COL_PRENOM"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_AMCAB_COL_SEXE"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_AMCAB_COL_TAUX"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_AMCAB_COL_SOMME"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_AMCAB_COL_TOTAL"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_AMCAB_COL_PERIODEDEBUT"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_AMCAB_COL_PERIODEFIN"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_AMCAB_COL_ASSURANCE"), getStyleListTitleLeft());

        /*
         * Pour chaque ligne de caisse maladie pour travailleur, une ligne sera ajoutée dans la liste Excel.
         * Les colonnes sont paramétrée en fonction de type de données (Texte, numérique, etc...)
         */
        for (Entry entry : map.entrySet()) {
            LigneDecompteCMDTO l = (LigneDecompteCMDTO) entry.getValue();

            Montant TauxxSomme = new Montant(l.getTotal()).multiply(new Taux(l.getTaux()));
            Montant total = new Montant(l.getTotal());
            Taux taux = new Taux(l.getTaux());

            String dateDebut = l.getPeriodeDebut();
            String dateFin = l.getPeriodeFin();

            dateDebut = controlAndFormatDate(dateDebut);
            dateFin = controlAndFormatDate(dateFin);

            createRow();
            createCell(l.getIdExterne());
            createCell(l.getConvention());
            createCell(l.getMalnaf());
            createCell(l.getDescription());
            createCell(l.getIdPoste());
            createCell(l.getNss());
            createCell(l.getNom());
            createCell(l.getPrenom());
            createCell(l.getSexe());
            createCell(taux, getStyleMontantNoBorder());
            createCell(total, getStyleMontantNoBorder());
            createCell(TauxxSomme, getStyleMontantNoBorder());
            createCell(dateDebut);
            createCell(dateFin);
            createCell(l.getCaisseMaladie());
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_AMCAB;
    }

    /**
     * Contrôle que la date ne soit ni vide ni null.
     * 
     * @param pDate La date à contrôler
     * @return Renvoie 0 si null ou vide, sinon renvoie la date (mois.année) formatée correctement.
     */
    private String controlAndFormatDate(String pDate) {
        if (pDate == "N/A" || pDate == null) {
            return "0";
        } else {
            return new Date(pDate).getMoisAnneeFormatte();
        }
    }

}
