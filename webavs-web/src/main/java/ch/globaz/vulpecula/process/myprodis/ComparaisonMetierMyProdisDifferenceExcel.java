package ch.globaz.vulpecula.process.myprodis;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.businessimpl.services.myprodis.ComparaisonMyProdisSalaireCP;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;

public class ComparaisonMetierMyProdisDifferenceExcel extends AbstractListExcel {

    private List<ComparaisonMyProdisSalaireCP> liste;
    private Boolean wantCP = false;
    private Boolean wantSalaire = false;
    private String filename;

    public ComparaisonMetierMyProdisDifferenceExcel(BSession session, String filenameRoot, String documentTitle,
            List<ComparaisonMyProdisSalaireCP> liste) {
        super(session, filenameRoot, documentTitle);
        this.liste = liste;
    }

    /**
     * Création d'un fichier Excel permettant de repertorier tous les décomptes étant différents par rapport à MyProdis
     */
    @Override
    public void createContent() {
        HSSFSheet sheet = createSheet(DocumentConstants.LISTES_COMPARAISON_MYPRODIS);

        /*
         * Mise en page de la feuille
         */
        HSSFPrintSetup printSetup;
        printSetup = initPage(true);
        printSetup.setFitWidth((short) 1);

        createRow();
        createCell("Traitée le : " + JACalendar.todayJJsMMsAAAA());
        createRow();
        createCell("Fichier : " + getFilename());

        if (wantCP) {
            createRow();
            createCell(getLabel("JSP_MY_PRODIS_INCLURE_CONGES"));
            createRow();

        }
        if (wantSalaire) {
            createRow();
            createCell(getLabel("JSP_MY_PRODIS_INCLURE_SALAIRES"));
            createRow();
        }

        createRow();

        /*
         * Paramétrage des colonnes
         */
        sheet.setColumnWidth((short) 0, (short) 3500);
        sheet.setColumnWidth((short) 1, (short) 7500);
        sheet.setColumnWidth((short) 2, (short) 3500);
        sheet.setColumnWidth((short) 3, (short) 3500);
        sheet.setColumnWidth((short) 4, (short) 3000);
        sheet.setColumnWidth((short) 5, (short) 5000);

        /*
         * Création des en-têtes de colonne
         */
        createCell(getLabel("LISTE_DIFFERENCES_DECOMPTES_MYPRODIS_AFFILIE"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_DIFFERENCES_DECOMPTES_MYPRODIS_NSS"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_DIFFERENCES_DECOMPTES_MYPRODIS_DATEDEBUT"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_DIFFERENCES_DECOMPTES_MYPRODIS_DATEFIN"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_DIFFERENCES_DECOMPTES_MYPRODIS_SALAIRE"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_DIFFERENCES_DECOMPTES_MYPRODIS_CP"), getStyleListTitleLeft());
        createCell(getLabel("LISTE_DIFFERENCES_DECOMPTES_MYPRODIS_SOURCE"), getStyleListTitleLeft());

        /*
         * Création insertion des lignes dans le fichier Excel
         */
        for (ComparaisonMyProdisSalaireCP decompte : liste) {
            createRow();

            Montant salaire = new Montant(decompte.getSalaire());

            createCell(decompte.getNoAffilie());
            createCell(decompte.getNss());
            createCell(new Date(decompte.getDateDebut()).getSwissValue());
            createCell(new Date(decompte.getDateFin()).getSwissValue());
            createCell(salaire, getStyleMontantNoBorder());
            createCell(decompte.getCp());
            createCell(decompte.getSource());
        }
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_COMPARAISON_MYPRODIS;
    }

    public Boolean getWantCP() {
        return wantCP;
    }

    public void setWantCP(Boolean wantCP) {
        this.wantCP = wantCP;
    }

    public Boolean getWantSalaire() {
        return wantSalaire;
    }

    public void setWantSalaire(Boolean wantSalaire) {
        this.wantSalaire = wantSalaire;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
