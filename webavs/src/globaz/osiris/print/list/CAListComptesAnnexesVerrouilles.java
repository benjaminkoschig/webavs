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
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.translation.CACodeSystem;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author jts 24 mars 05 12:34:07
 */
public class CAListComptesAnnexesVerrouilles extends CAAbstractListExcel {
    private static final String NUMERO_REFERENCE_INFOROM = "0147GCA";
    private String forIdGenreCompte = new String();
    private String forSelectionRole = new String();

    private String forSelectionTri = new String();
    private BProcess processAppelant = null;
    private String user = new String();

    // créé la feuille xls
    public CAListComptesAnnexesVerrouilles(BSession session) {
        super(session, session.getLabel("CAV_TITRE"), session.getLabel("CAV_TITRE"));
    }

    /**
     * @return
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * @return
     */
    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * @return
     */
    public String getForSelectionTri() {
        return forSelectionTri;
    }

    @Override
    public String getNumeroInforom() {
        return CAListComptesAnnexesVerrouilles.NUMERO_REFERENCE_INFOROM;
    }

    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    public String getUser() {
        return user;
    }

    /**
     * méthode pour la création du style de la feuille pour l'onglet "Liste des décisions d'intérêts" (entêtes, des
     * bordures...)
     */
    private void initCritere() {
        // styles
        HSSFCellStyle styleTitreCritere = getStyleCritereTitle();

        /*
         * Critères
         */
        // Genre
        if (!JadeStringUtil.isBlank(getForIdGenreCompte()) && !getForIdGenreCompte().equals("0")) {
            String value = "";
            createRow();
            this.createCell(getSession().getLabel("GENRE"), styleTitreCritere);
            try {
                value = CACodeSystem.getLibelle(getSession(), getForIdGenreCompte());
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.createCell(value, getStyleCritere());
        }

        // Rôle
        if (!JadeStringUtil.isBlank(getForSelectionRole())) {
            StringBuffer ssTitle = new StringBuffer("");
            createRow();
            this.createCell(getSession().getLabel("ROLE"), styleTitreCritere);
            try {
                if (getForSelectionRole().indexOf(',') != -1) {
                    String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);
                    for (int id = 0; id < roles.length; ++id) {
                        if (id > 0) {
                            ssTitle.append(',');
                        }
                        ssTitle.append(CACodeSystem.getLibelle(getSession(), roles[id]));
                    }
                } else {
                    ssTitle.append(CACodeSystem.getLibelle(getSession(), getForSelectionRole()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.createCell(ssTitle.toString(), getStyleCritere());
            ;
        }
    }

    /**
     * Création de la page ("Liste des décisions d'intérêts")
     * 
     * @return
     */
    private void initListe() {
        ArrayList colTitles = new ArrayList();
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTEANNEXE")));
        colTitles.add(new ParamTitle(getSession().getLabel("SOLDE")));

        createSheet(getSession().getLabel("LISTE"));
        initCritere();
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListComptesAnnexesVerrouilles.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        currentSheet.setColumnWidth((short) 0, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        currentSheet.setColumnWidth((short) 1, CAAbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    /**
     * initialisation de la feuille xls
     */
    public HSSFSheet populateSheetListe(CACompteAnnexeManager manager, BTransaction transaction) throws Exception {
        BStatement statement = null;
        CACompteAnnexe compteAnnexe = null;
        int numPremiereLigne = 0;

        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());
        // création de la page
        initListe();
        statement = manager.cursorOpen(transaction);
        // récupération du numéro de la première ligne
        numPremiereLigne = currentSheet.getPhysicalNumberOfRows() + 1;

        // parcours du manager et remplissage des cell
        while (((compteAnnexe = (CACompteAnnexe) manager.cursorReadNext(statement)) != null) && (!compteAnnexe.isNew())) {
            if (compteAnnexe != null) {
                createRow();
                // Compte annexe
                this.createCell(compteAnnexe.getCARole().getDescription() + " " + compteAnnexe.getIdExterneRole() + " "
                        + compteAnnexe.getDescription(), getStyleListLeft());
                // Solde
                this.createCell(JadeStringUtil.parseDouble(compteAnnexe.getSolde(), 0), false);
                processAppelant.incProgressCounter();
            }
        }
        createRow();
        this.createCell(getSession().getLabel("TOTAL"), getStyleListTitleLeft());
        // solde total
        createCellFormula("SUM(B" + numPremiereLigne + ":B" + (currentSheet.getPhysicalNumberOfRows() - 1) + ")",
                getStyleMontantTotal());

        return currentSheet;
    }

    /**
     * @param string
     */
    public void setForIdGenreCompte(String string) {
        forIdGenreCompte = string;
    }

    /**
     * @param string
     */
    public void setForSelectionRole(String string) {
        forSelectionRole = string;
    }

    /**
     * @param string
     */
    public void setForSelectionTri(String string) {
        forSelectionTri = string;
    }

    public void setProcessAppelant(BProcess process) {
        processAppelant = process;
    }

    public void setUser(String string) {
        user = string;
    }
}
