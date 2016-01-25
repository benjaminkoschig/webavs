/*
 * Créé le 24 mars 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.print.list;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.interets.CAApercuInteretMoratoire;
import globaz.osiris.db.interets.CAApercuInteretMoratoireManager;
import globaz.osiris.translation.CACodeSystem;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author jts 24 mars 05 12:34:07
 */
public class CAListInteretMoratoire extends CAAbstractListExcel {
    private static final String NUMERO_REFERENCE_INFOROM = "0150GCA";

    private String dateCalculDebut = new String();

    private String dateCalculFin = new String();
    private String idGenreInteret = new String();
    private String idJournalCalcul = new String();
    private String idJournalFacturation = new String();
    private String idMotifCalcul = new String();
    private BProcess processAppelant = null;

    // créé la feuille xls
    public CAListInteretMoratoire(BSession session) {
        super(session, session.getLabel("IM_LISTE"), session.getLabel("IM_LISTE"));
    }

    /**
     * @return
     */
    public String getDateCalculDebut() {
        return dateCalculDebut;
    }

    /**
     * @return
     */
    public String getDateCalculFin() {
        return dateCalculFin;
    }

    /**
     * @return
     */
    public String getIdGenreInteret() {
        return idGenreInteret;
    }

    /**
     * @return
     */
    public String getIdJournalCalcul() {
        return idJournalCalcul;
    }

    /**
     * @return
     */
    public String getIdJournalFacturation() {
        return idJournalFacturation;
    }

    /**
     * @return
     */
    public String getIdMotifCalcul() {
        return idMotifCalcul;
    }

    @Override
    public String getNumeroInforom() {
        return CAListInteretMoratoire.NUMERO_REFERENCE_INFOROM;
    }

    public BProcess getProcessAppelant() {
        return processAppelant;
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
        // Genre d'intérêt
        if (!JadeStringUtil.isBlank(getIdGenreInteret())) {
            String value = "";
            try {
                value = CACodeSystem.getLibelle(getSession(), getIdGenreInteret());
            } catch (Exception e) {
                e.printStackTrace();
            }
            createRow();
            this.createCell(getSession().getLabel("IM_GENRE"), styleTitreCritere);
            this.createCell(value, getStyleCritere());
        }

        // Date
        if (!JadeStringUtil.isBlank(getDateCalculDebut()) || !JadeStringUtil.isBlank(getDateCalculFin())) {
            String value = "";

            if (!JadeStringUtil.isBlank(getDateCalculDebut()) && JadeStringUtil.isBlank(getDateCalculFin())) {
                // si la date de début est indiquée
                value = getSession().getLabel("IM_DEPUIS") + " " + getDateCalculDebut();
            } else if (JadeStringUtil.isBlank(getDateCalculDebut()) && !JadeStringUtil.isBlank(getDateCalculFin())) {
                // si la date de fin est indiquée
                value = getSession().getLabel("IM_JUSQUAU") + " " + getDateCalculFin();
            } else {
                // si les deux date sont indiquée
                value = getSession().getLabel("IM_DU") + " " + getDateCalculDebut() + " "
                        + getSession().getLabel("IM_AU") + " " + getDateCalculFin();
            }
            createRow();
            this.createCell(getSession().getLabel("IM_DATE_CALCUL"), styleTitreCritere);
            this.createCell(value, getStyleCritere());
        }

        // No de Journal de calcul
        if (!JadeStringUtil.isBlank(getIdJournalCalcul())) {
            createRow();
            this.createCell(getSession().getLabel("IM_JOUCAL"), styleTitreCritere);
            this.createCell(getIdJournalCalcul(), getStyleCritere());
        }

        // Motif
        if (!JadeStringUtil.isBlank(getIdMotifCalcul())) {
            String value = "";
            try {
                value = globaz.osiris.translation.CACodeSystem.getLibelle(getSession(), getIdMotifCalcul());
            } catch (Exception e) {
                e.printStackTrace();
            }
            createRow();
            this.createCell(getSession().getLabel("IM_MOTIF"), styleTitreCritere);
            this.createCell(value, getStyleCritere());
        }

        // Suspens
        if (getIdJournalFacturation().equals("0")) {
            createRow();
            this.createCell(getSession().getLabel("IM_SUSPENS"), styleTitreCritere);
            this.createCell(getSession().getLabel("OUI"), getStyleCritere());
        } else if (!JadeStringUtil.isBlank(getIdJournalFacturation())) {
            createRow();
            this.createCell(getSession().getLabel("IM_SUSPENS"), styleTitreCritere);
            this.createCell(getSession().getLabel("NON"), getStyleCritere());
        }

        // No de Journal de facturation
        if (!JadeStringUtil.isBlank(getIdJournalFacturation()) && !getIdJournalFacturation().equals("0")) {
            createRow();
            this.createCell(getSession().getLabel("IM_JOUFAC"), styleTitreCritere);
            this.createCell(getIdJournalFacturation(), getStyleCritere());
        }
    }

    /**
     * Création de la page ("Liste des décisions d'intérêts")
     * 
     * @return
     */
    private void initListe() {
        ArrayList colTitles = new ArrayList();
        colTitles.add(new ParamTitle(getSession().getLabel("SECTION")));
        colTitles.add(new ParamTitle(getSession().getLabel("COMPTEANNEXE")));
        colTitles.add(new ParamTitle(getSession().getLabel("IM_GENRE")));
        colTitles.add(new ParamTitle(getSession().getLabel("MONTANT")));
        colTitles.add(new ParamTitle(getSession().getLabel("IM_MOTIF")));
        colTitles.add(new ParamTitle(getSession().getLabel("IM_DATE_FACTURATION")));

        createSheet(getSession().getLabel("LISTE"));
        initCritere();
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListInteretMoratoire.NUMERO_REFERENCE_INFOROM);

        // définition de la taille des cell
        currentSheet.setColumnWidth((short) 0, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        currentSheet.setColumnWidth((short) 1, CAAbstractListExcel.COLUMN_WIDTH_DESCRIPTION);
        currentSheet.setColumnWidth((short) 2, CAAbstractListExcel.COLUMN_WIDTH_5500);
        currentSheet.setColumnWidth((short) 3, CAAbstractListExcel.COLUMN_WIDTH_MONTANT);
        currentSheet.setColumnWidth((short) 4, CAAbstractListExcel.COLUMN_WIDTH_3500);
        currentSheet.setColumnWidth((short) 5, CAAbstractListExcel.COLUMN_WIDTH_DATE);
    }

    /**
     * Création de la page ("Stats")
     * 
     * @return
     */
    private void initStats() {
        ArrayList colTitles = new ArrayList();
        colTitles.add(new ParamTitle(getSession().getLabel("IM_GENRE")));
        colTitles.add(new ParamTitle(getSession().getLabel("NOMBRE")));
        colTitles.add(new ParamTitle(getSession().getLabel("IM_MOTIF")));
        colTitles.add(new ParamTitle(getSession().getLabel("MONTANT")));

        createSheet("Stats");
        initTitleRow(colTitles);
        initPage(true);
        createHeader();
        createFooter(CAListInteretMoratoire.NUMERO_REFERENCE_INFOROM);

        currentSheet.setColumnWidth((short) 0, CAAbstractListExcel.COLUMN_WIDTH_5500);
        currentSheet.setColumnWidth((short) 1, CAAbstractListExcel.COLUMN_WIDTH_3500);
    }

    /**
     * initialisation de la feuille xls (onglet "Liste des décisions d'intérêts");
     */
    public HSSFSheet populateSheetListe(CAApercuInteretMoratoireManager manager, BTransaction transaction)
            throws Exception {
        BStatement statement = null;
        CAApercuInteretMoratoire decisionsMec = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());

        // style
        HSSFCellStyle styleAlignLeftWrap = getStyleListLeft();
        HSSFCellStyle styleAlignCenter = getStyleListCenter();

        // création de la page
        initListe();
        statement = manager.cursorOpen(transaction);

        // parcours du manager et remplissage des cell
        while (((decisionsMec = (CAApercuInteretMoratoire) manager.cursorReadNext(statement)) != null)
                && (!decisionsMec.isNew())) {
            if (decisionsMec != null) {
                createRow();
                // Compte annexe
                this.createCell(
                        decisionsMec.getCompteAnnexe().getCARole().getDescription() + " "
                                + decisionsMec.getIdExterneRole() + "\n" + decisionsMec.getDescription(),
                        styleAlignLeftWrap);
                // Section
                this.createCell(decisionsMec.getIdExterne() + "\n" + decisionsMec.getLibelleDescription(),
                        styleAlignLeftWrap);
                // Genre d'intérêt
                this.createCell(CACodeSystem.getLibelle(getSession(), decisionsMec.getIdGenreInteret()),
                        styleAlignLeftWrap);
                // Montant de l'intérêt
                this.createCell(JadeStringUtil.parseDouble(decisionsMec.getTotalMontantInteret(), 0), false);
                // Motif
                this.createCell(CACodeSystem.getLibelle(getSession(), decisionsMec.getMotifCalcul()),
                        styleAlignLeftWrap);
                // Date de facturation
                String value = "";
                if (decisionsMec.getIdJournalFacturation().equalsIgnoreCase("0")) {
                    value = "-";
                } else {
                    value = decisionsMec.getDateFacturation();
                }
                this.createCell(value, styleAlignCenter);

                processAppelant.incProgressCounter();
            }
        }
        return currentSheet;
    }

    /**
     * initialisation de la feuille xls (onglet "Stats");
     */
    public HSSFSheet populateSheetStats(CAApercuInteretMoratoireManager manager, BTransaction transaction)
            throws Exception {
        FWCurrency montantTotal = new FWCurrency();
        int nombreTotal = 0;
        BStatement statement = null;
        CAApercuInteretMoratoire decisionsMec = null;
        // Pour information: indique le nombre d'annonces à charger
        processAppelant.setProgressScaleValue(manager.getCount());

        // création de la page
        initStats();
        statement = manager.cursorOpen(transaction);

        // parcours du manager et remplissage des cell
        while (((decisionsMec = (CAApercuInteretMoratoire) manager.cursorReadNext(statement)) != null)
                && (!decisionsMec.isNew())) {
            if (decisionsMec != null) {
                createRow();
                // Genre d'intérêt
                this.createCell(CACodeSystem.getLibelle(getSession(), decisionsMec.getIdGenreInteret()),
                        getStyleListLeft());
                // Motif
                this.createCell(CACodeSystem.getLibelle(getSession(), decisionsMec.getMotifCalcul()),
                        getStyleListLeft());
                // Nombre
                this.createCell(JadeStringUtil.parseInt(decisionsMec.getNombre(), 0), getStyleListRight());
                // Montant de l'intérêt
                this.createCell(JadeStringUtil.parseDouble(decisionsMec.getTotalMontantInteret(), 0), false);

                processAppelant.incProgressCounter();
                montantTotal.add(decisionsMec.getTotalMontantInteret());
                nombreTotal += JadeStringUtil.parseInt(decisionsMec.getNombre(), 0);
            }
        }

        createRow();
        this.createCell(getSession().getLabel("TOTAL"), getStyleListTitleLeft());
        this.createCell("", getStyleListTitleLeft());
        // nombre total
        createCellFormula("SUM(C2:C" + (currentSheet.getPhysicalNumberOfRows() - 1) + ")", getStyleListTitleRight());
        // montant total
        createCellFormula("SUM(D2:D" + (currentSheet.getPhysicalNumberOfRows() - 1) + ")", getStyleMontantTotal());

        return currentSheet;
    }

    /**
     * @param string
     */
    public void setDateCalculDebut(String string) {
        dateCalculDebut = string;
    }

    /**
     * @param string
     */
    public void setDateCalculFin(String string) {
        dateCalculFin = string;
    }

    /**
     * @param string
     */
    public void setIdGenreInteret(String string) {
        idGenreInteret = string;
    }

    /**
     * @param string
     */
    public void setIdJournalCalcul(String string) {
        idJournalCalcul = string;
    }

    /**
     * @param string
     */
    public void setIdJournalFacturation(String string) {
        idJournalFacturation = string;
    }

    /**
     * @param string
     */
    public void setIdMotifCalcul(String string) {
        idMotifCalcul = string;
    }

    public void setProcessAppelant(BProcess process) {
        processAppelant = process;
    }
}
