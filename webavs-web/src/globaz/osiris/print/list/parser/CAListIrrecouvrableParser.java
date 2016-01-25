package globaz.osiris.print.list.parser;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CAIrrecouvrableManager;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.db.contentieux.CAMotifContentieuxManager;
import globaz.osiris.print.list.CAAbstractListExcel;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * Crée la liste des rentiers et irrécouvrables
 * 
 * @author SEL
 * 
 */
public class CAListIrrecouvrableParser extends CAAbstractListExcel {

    private static final String NUMERO_REFERENCE_INFOROM = "0123GCA";

    // créé la feuille xls
    public CAListIrrecouvrableParser(BSession session) {
        super(session, "ListIrrecouvrable", "Liste des rentiers et irrécouvrables");
    }

    @Override
    public String getNumeroInforom() {
        return CAListIrrecouvrableParser.NUMERO_REFERENCE_INFOROM;
    }

    /**
     * Création de la page
     * 
     * @return
     */
    private void initListe() {
        // Titres des colonnes
        ArrayList colTitles = new ArrayList();
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTEANNEXE")));
        colTitles.add(new ParamTitle(getSession().getLabel("ROLE")));
        colTitles.add(new ParamTitle(getSession().getLabel("SECTION")));
        colTitles.add(new ParamTitle(getSession().getLabel("MOTIF")));
        if (CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            colTitles.add(new ParamTitle(getSession().getLabel("DATE_DEBUT")));
            colTitles.add(new ParamTitle(getSession().getLabel("DATE_FIN")));
        }
        colTitles.add(new ParamTitle(getSession().getLabel("SOLDE")));

        createSheet(getSession().getLabel("LISTE"));

        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListIrrecouvrableParser.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        int numCol = 0;
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION); // Compte
        // Annexe
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_ROLE); // Role
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_SECTION); // Section
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_REMARQUE); // Motif
        if (CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
            currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DATE); // Date
            // début
            currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_DATE); // Date
            // fin
        }
        currentSheet.setColumnWidth((short) numCol++, CAAbstractListExcel.COLUMN_WIDTH_MONTANT); // Solde
    }

    /**
     * initialisation de la feuille xls
     */
    public HSSFSheet populateSheetListe(CAIrrecouvrableManager manager, BTransaction transaction) throws Exception {
        BStatement statement = manager.cursorOpen(transaction);
        CASection section = null;

        // création de la page
        initListe();

        // parcours du manager et remplissage des cell
        while (((section = (CASection) manager.cursorReadNext(statement)) != null) && (!section.isNew())) {
            if (section != null) {
                // Recherche du compte annexe pour obtenir le motif de blocage
                CACompteAnnexe compte = (CACompteAnnexe) section.getCompteAnnexe();

                createRow();
                // CA
                this.createCell(compte.getIdExterneRole() + " " + compte.getDescription(), getStyleListLeft());
                // Role
                this.createCell(compte.getRole().getDescription(), getStyleListLeft());
                // Section
                this.createCell(section.getIdExterne(), getStyleListLeft());
                // Motif
                this.createCell(compte.getUcMotifContentieuxSuspendu().getCodeUtiLib(), getStyleListLeft());
                // Solde
                this.createCell(JadeStringUtil.parseDouble(section.getSolde(), 0), false);
            }
        }
        return currentSheet;
    }

    /**
     * initialisation de la feuille xls
     */
    public HSSFSheet populateSheetListeAquila(CAIrrecouvrableManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = manager.cursorOpen(transaction);
        CASection section = null;

        // création de la page
        initListe();

        // parcours du manager et remplissage des cell
        while (((section = (CASection) manager.cursorReadNext(statement)) != null) && (!section.isNew())) {
            if (section != null) {
                // Recherche du compte annexe pour obtenir le motif de blocage
                CACompteAnnexe compte = (CACompteAnnexe) section.getCompteAnnexe();

                CAMotifContentieuxManager motifMgr = new CAMotifContentieuxManager();
                motifMgr.setSession(getSession());
                motifMgr.setForIdCompteAnnexe(compte.getIdCompteAnnexe());
                motifMgr.changeManagerSize(0);
                motifMgr.find(transaction);
                CAMotifContentieux motifContentieux = (CAMotifContentieux) motifMgr.getFirstEntity();

                createRow();
                // CA
                this.createCell(compte.getIdExterneRole() + " " + compte.getDescription(), getStyleListLeft());
                // Role
                this.createCell(compte.getRole().getDescription(), getStyleListLeft());
                // Section
                this.createCell(section.getIdExterne(), getStyleListLeft());
                // Motif
                this.createCell(motifContentieux.getUcMotifContentieuxSuspendu().getCodeUtiLib(), getStyleListLeft());
                // Date début
                this.createCell(motifContentieux.getDateDebut(), getStyleListCenter());
                // Date début
                this.createCell(motifContentieux.getDateFin(), getStyleListCenter());
                // Solde
                this.createCell(JadeStringUtil.parseDouble(section.getSolde(), 0), false);
            }
        }
        return currentSheet;
    }
}
