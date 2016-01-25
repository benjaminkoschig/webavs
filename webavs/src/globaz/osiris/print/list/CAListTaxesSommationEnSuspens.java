/*
 * Créé le 24 mars 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.print.list;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author jts 24 mars 05 12:34:07
 */
public class CAListTaxesSommationEnSuspens extends CAAbstractListExcel {
    private static final String NUMERO_REFERENCE_INFOROM = "0046GCA";
    private BProcess processAppelant = null;
    private String user = new String();

    // créé la feuille xls
    public CAListTaxesSommationEnSuspens(BSession session) {
        super(session, session.getLabel("TAXES_SOMMATION_SUSPENS_TITRE"), session
                .getLabel("TAXES_SOMMATION_SUSPENS_TITRE"));
    }

    @Override
    public String getNumeroInforom() {
        return CAListTaxesSommationEnSuspens.NUMERO_REFERENCE_INFOROM;
    }

    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    public String getUser() {
        return user;
    }

    /**
     * Création de la page ("Liste des décisions d'intérêts")
     * 
     * @return
     */
    private void initListe() {
        ArrayList colTitles = new ArrayList();
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTEANNEXE")));
        colTitles.add(new ParamTitle(getSession().getLabel("SECTION")));
        colTitles.add(new ParamTitle(getSession().getLabel("SOLDE")));

        createSheet(getSession().getLabel("LISTE"));
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListTaxesSommationEnSuspens.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        currentSheet.setColumnWidth((short) 0, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        currentSheet.setColumnWidth((short) 1, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        currentSheet.setColumnWidth((short) 2, CAAbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    /**
     * initialisation de la feuille xls (onglet "Liste des décisions d'intérêts");
     */
    public HSSFSheet populateSheetListe(CASectionManager manager, BTransaction transaction) throws Exception {
        BStatement statement = null;
        CASection section = null;
        int numPremiereLigne = 0;

        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());

        // création de la page
        initListe();
        // récupération du numéro de la première ligne
        numPremiereLigne = currentSheet.getPhysicalNumberOfRows() + 1;

        statement = manager.cursorOpen(transaction);
        // parcours du manager et remplissage des cell
        while (((section = (CASection) manager.cursorReadNext(statement)) != null) && (!section.isNew())) {
            if (section != null) {
                createRow();
                // Compte annexe
                this.createCell(section.getCompteAnnexe().getIdExterneRole() + " "
                        + section.getCompteAnnexe().getDescription(), getStyleListLeft());
                // Compte annexe
                this.createCell(section.getIdExterne() + " " + section.getDescription(), getStyleListLeft());
                // Solde
                this.createCell(JadeStringUtil.parseDouble(section.getSolde(), 0), false);

                processAppelant.incProgressCounter();
            }
        }
        createRow();
        this.createCell(getSession().getLabel("TOTAL"), getStyleListTitleLeft());
        this.createCell("", getStyleListTitleLeft());
        // solde total
        createCellFormula("SUM(C" + numPremiereLigne + ":C" + (currentSheet.getPhysicalNumberOfRows() - 1) + ")",
                getStyleMontantTotal());

        return currentSheet;
    }

    public void setProcessAppelant(BProcess process) {
        processAppelant = process;
    }

    public void setUser(String string) {
        user = string;
    }
}
