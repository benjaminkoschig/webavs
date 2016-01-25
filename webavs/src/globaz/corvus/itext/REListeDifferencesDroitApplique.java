package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.adaptation.REPrestAccJointInfoComptaJointTiers;
import globaz.corvus.db.annonces.REAnnonce51Adaptation;
import globaz.corvus.db.annonces.REAnnonce53Adaptation;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.framework.printing.itext.dynamique.FWIAbstractDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author HPE
 */
public class REListeDifferencesDroitApplique extends FWIAbstractDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static double arrondir(double value, int n) {
        double r = (Math.round(value * Math.pow(10, n))) / (Math.pow(10, n));
        return r;
    }

    private Map<String, ArrayList<Object>> mapDifferencesDroitApplique = new TreeMap<String, ArrayList<Object>>();
    private String moisAnnee = "";

    public REListeDifferencesDroitApplique() throws Exception {
        super(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS), REApplication.APPLICATION_CORVUS_REP, "globaz",
                "Liste des différences entre annonces centrale et rentes accordées",
                REApplication.DEFAULT_APPLICATION_CORVUS);
    }

    private void _addColumnLeft(String columnName, int width) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, FWIAbstractDocumentList.LEFT);
        _setDataTableColumnWidth(_getDataTableColumnCount() - 1, width);
    }

    private void _addColumnRight(String columnName, int width) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, FWIAbstractDocumentList.RIGHT);
        _setDataTableColumnWidth(_getDataTableColumnCount() - 1, width);
    }

    @Override
    public void _beforeExecuteReport() {

        try {
            // on ajoute au doc info le numéro de référence inforom
            getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.ADAPTATION_LISTE_DIFFERENCES);

            // Création du tableau du document
            initializeTable();

            // set des données générales
            _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                    ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
            _setDocumentTitle(getSession().getLabel("PROCESS_LISTE_ERR_5_OBJET_MAIL"));

        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, toString());
            abort();
        }
    }

    /**
     * Crée les lignes du document.
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    protected final void _bindDataTable() throws FWIException {
        try {
            _setDataTableModel();
            populate();
        } catch (Exception e) {
            if (e instanceof FWIException) {
                throw (FWIException) e;
            } else {
                throw new FWIException(e);
            }
        }
    }

    public Map<String, ArrayList<Object>> getMapDifferencesDroitApplique() {
        return mapDifferencesDroitApplique;
    }

    public String getMoisAnnee() {
        return moisAnnee;
    }

    protected String getSchema() {
        return Jade.getInstance().getDefaultJdbcSchema() + ".";
    }

    protected void initializeTable() {
        _addColumnLeft(getSession().getLabel("PROCESS_LISTE_ERR_COL_1"), 2);
        _addColumnLeft(getSession().getLabel("PROCESS_LISTE_ERR_COL_2"), 4);
        _addColumnLeft(getSession().getLabel("PROCESS_LISTE_ERR_COL_3"), 1);
        _addColumnRight(getSession().getLabel("PROCESS_LISTE_ERR_COL_4"), 2);
        _addColumnRight(getSession().getLabel("PROCESS_LISTE_ERR_COL_5"), 2);
        _addColumnRight(getSession().getLabel("PROCESS_LISTE_ERR_COL_6"), 2);
        _addColumnRight("%", 1);
        _addColumnLeft(getSession().getLabel("PROCESS_LISTE_ERR_COL_7"), 4);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private void populate() throws Exception {

        for (String keyMapDiffCentraleCaisse : mapDifferencesDroitApplique.keySet()) {

            // on retourne une arrayList avec l'annonce correspondante et la ra
            ArrayList<Object> listRaAnn = mapDifferencesDroitApplique.get(keyMapDiffCentraleCaisse);

            REPrestAccJointInfoComptaJointTiers ra = (REPrestAccJointInfoComptaJointTiers) listRaAnn.get(1);

            boolean isDixiemeRevisionCentrale = false;

            if (listRaAnn.get(0) instanceof REAnnonce51Adaptation) {
                REAnnonce51Adaptation ann51 = (REAnnonce51Adaptation) listRaAnn.get(0);
                this.remplirLignes(ann51, ra);
            } else {
                REAnnonce53Adaptation ann53 = (REAnnonce53Adaptation) listRaAnn.get(0);
                this.remplirLignes(ann53, ra);
                isDixiemeRevisionCentrale = true;
            }

            Statement statement = null;

            BTransaction transaction = null;
            try {
                transaction = (BTransaction) ((BSession) getISession()).newTransaction();
                transaction.openTransaction();
                statement = transaction.getConnection().createStatement();

                // Retrieve de la base de calcul
                REBasesCalculDixiemeRevision bc10 = new REBasesCalculDixiemeRevision();
                bc10.setSession(getSession());
                bc10.setIdBasesCalcul(ra.getIdBaseCalcul());
                bc10.retrieve();

                // Si 10ème
                if (!bc10.isNew() && !isDixiemeRevisionCentrale) {

                    // FAIRE LE CHANGEMENT DE REVISION !!!!

                    /*
                     * -- Transformer de 10ème en 9ème -- Insertion 9ème 1) INSERT INTO CICIWEB.REBACNR VALUES (idBase,
                     * 0.00, '00', 0, 0, '0', 0, '2', '201012310000globaz', '201012310000globaz'); -- Suppression 10ème
                     * 2) DELETE FROM CICIWEB.REBACDR WHERE YKIBCA = idBase; -- Changement de la révision 3) UPDATE
                     * CICIWEB.REBACAL SET YILDAP = '9' WHERE YIIBCA = idBase;
                     */

                    String sqlQuery1 = "INSERT INTO " + getSchema()
                            + REBasesCalculNeuviemeRevision.TABLE_NAME_BASES_CALCUL_NEUVIEME_REVISION + " VALUES ("
                            + bc10.getIdBasesCalcul()
                            + ", 0.00, '00', 0, 0, '0', 0, '2', '201012310000globaz', '201012310000globaz')";

                    String sqlQuery2 = "DELETE FROM " + getSchema()
                            + REBasesCalculDixiemeRevision.TABLE_NAME_BASES_CALCUL_DIXIEME_REVISION + " WHERE "
                            + REBasesCalculDixiemeRevision.FIELDNAME_ID_BASES_CALCUL_DIXIEME_REVISION + " = "
                            + bc10.getIdBasesCalcul();

                    String sqlQuery3 = "UPDATE " + getSchema() + REBasesCalcul.TABLE_NAME_BASES_CALCUL + " SET "
                            + REBasesCalcul.FIELDNAME_DROIT_APPLIQUE + " = '9' WHERE "
                            + REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL + " = " + bc10.getIdBasesCalcul();

                    statement.execute(sqlQuery1);
                    statement.execute(sqlQuery2);
                    statement.execute(sqlQuery3);

                } else {

                    REBasesCalculNeuviemeRevision bc9 = new REBasesCalculNeuviemeRevision();
                    bc9.setSession(getSession());
                    bc9.setIdBasesCalcul(ra.getIdBaseCalcul());
                    bc9.retrieve();

                    // Si 9ème
                    if (!bc9.isNew() && isDixiemeRevisionCentrale) {

                        // FAIRE LE CHANGEMENT DE REVISION !!!!

                        /*
                         * -- Transformer de 9ème en 10ème -- Insertion 10ème 1) INSERT INTO CICIWEB.REBACDR VALUES
                         * (idBase, '201012310000globaz', '201012310000globaz'); -- Suppression 9ème 2) DELETE FROM
                         * CICIWEB.REBACNR WHERE YJIBCA = idBase; -- Changement de la révision 3) UPDATE CICIWEB.REBACAL
                         * SET YILDAP = '10' WHERE YIIBCA = idBase;
                         */

                        String sqlQuery1 = "INSERT INTO " + getSchema()
                                + REBasesCalculDixiemeRevision.TABLE_NAME_BASES_CALCUL_DIXIEME_REVISION + " VALUES ("
                                + bc10.getIdBasesCalcul() + ", '201012310000globaz', '201012310000globaz')";

                        String sqlQuery2 = "DELETE FROM " + getSchema()
                                + REBasesCalculNeuviemeRevision.TABLE_NAME_BASES_CALCUL_NEUVIEME_REVISION + " WHERE "
                                + REBasesCalculNeuviemeRevision.FIELDNAME_ID_BASES_CALCUL_NEUVIEME_REVISION + " = "
                                + bc10.getIdBasesCalcul();

                        String sqlQuery3 = "UPDATE " + getSchema() + REBasesCalcul.TABLE_NAME_BASES_CALCUL + " SET "
                                + REBasesCalcul.FIELDNAME_DROIT_APPLIQUE + " = '10' WHERE "
                                + REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL + " = " + bc10.getIdBasesCalcul();

                        statement.execute(sqlQuery1);
                        statement.execute(sqlQuery2);
                        statement.execute(sqlQuery3);

                    }
                }

            } catch (Exception e) {
                if (transaction != null) {
                    transaction.setRollbackOnly();
                }
                getAttachedDocuments().clear();
                getMemoryLog().logMessage("Erreur dans le traitement : " + e.toString(), FWMessage.ERREUR, getName());
                transaction.addErrors(e.toString());
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (Exception e) {
                        JadeLogger.warn(this, "Unable to close a statment, reason : " + e.toString());
                    }
                }
                if (transaction != null) {
                    try {
                        if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                            transaction.rollback();
                        } else {
                            transaction.getConnection().commit();
                        }
                    } catch (Exception e) {
                        JadeLogger.warn(this, "Unable to commit or roolback transaction in : Reason = " + e.toString());
                    } finally {
                        transaction.closeTransaction();
                    }
                }
            }

            this._addDataTableRow();
        }

        // Ajouter remarque pour indiquer que les cas ont été corrigés dans la
        // base
        this._addLine(getSession().getLabel("PROCESS_LISTE_DIFF_REV_REMARQUE"), "", "");
    }

    private void remplirLignes(REAnnonce51Adaptation ann51, REPrestAccJointInfoComptaJointTiers ra) {

        _addCell(ra.getNss());
        _addCell(ra.getNom() + " " + ra.getPrenom());
        _addCell(ra.getCodePrestation() + "." + ra.getFractionRenteWithZeroWhenBlank());

        FWCurrency ancienMontant = new FWCurrency(ra.getMontantPrestation());
        _addCell(ancienMontant.toStringFormat());

        FWCurrency nouveauMontant = new FWCurrency(ann51.getMontantPrestation());
        _addCell(nouveauMontant.toString());

        FWCurrency ecart = new FWCurrency(nouveauMontant.toString());
        ecart.sub(ancienMontant.toString());

        _addCell(ecart.toStringFormat());

        double ancien = 0;
        double nouveau = nouveauMontant.doubleValue();
        double result = 100;
        if (!ancienMontant.isZero()) {
            ancien = ancienMontant.doubleValue();
            result = REListeDifferencesDroitApplique.arrondir(((nouveau * 100) / ancien) - 100, 2);
            if (result < 0) {
                FWCurrency res = new FWCurrency(result);
                res.negate();
                result = res.doubleValue();
            }
        }

        _addCell((new FWCurrency(result).toStringFormat()));

        String droitAppliqueRA = ra.getDroitApplique();
        String droitAppliqueAnn = "9";

        _addCell("Droit RA : " + droitAppliqueRA + " / Droit annonce : " + droitAppliqueAnn);

    }

    private void remplirLignes(REAnnonce53Adaptation ann53, REPrestAccJointInfoComptaJointTiers ra) {

        _addCell(ra.getNss());
        _addCell(ra.getNom() + " " + ra.getPrenom());
        _addCell(ra.getCodePrestation() + "." + ra.getFractionRenteWithZeroWhenBlank());

        FWCurrency ancienMontant = new FWCurrency(ra.getMontantPrestation());
        _addCell(ancienMontant.toStringFormat());

        FWCurrency nouveauMontant = new FWCurrency(ann53.getMontantPrestation());
        _addCell(nouveauMontant.toStringFormat());

        FWCurrency ecart = new FWCurrency(nouveauMontant.toString());
        ecart.sub(ancienMontant.toString());

        _addCell(ecart.toStringFormat());

        double ancien = 0;
        double nouveau = nouveauMontant.doubleValue();
        double result = 100;
        if (!ancienMontant.isZero()) {
            ancien = ancienMontant.doubleValue();
            result = REListeDifferencesDroitApplique.arrondir(((nouveau * 100) / ancien) - 100, 2);
            if (result < 0) {
                FWCurrency res = new FWCurrency(result);
                res.negate();
                result = res.doubleValue();
            }
        }

        _addCell((new FWCurrency(result).toStringFormat()));

        String droitAppliqueRA = ra.getDroitApplique();
        String droitAppliqueAnn = "10";

        _addCell("Droit RA : " + droitAppliqueRA + " / Droit annonce : " + droitAppliqueAnn);

    }

    public void setMapDifferencesDroitApplique(Map<String, ArrayList<Object>> mapDifferencesDroitApplique) {
        this.mapDifferencesDroitApplique = mapDifferencesDroitApplique;
    }

    public void setMoisAnnee(String moisAnnee) {
        this.moisAnnee = moisAnnee;
    }

}
