package globaz.helios.db.solde;

import globaz.globall.db.BConstants;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGSolde;
import globaz.helios.db.comptes.CGSoldeManager;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;

public class CGSoldeUpdater {

    private String forIdCentreCharge;
    private String forIdCompte;
    private String forIdExerciceComptable;
    private String forIdMandat;
    private String forIdPeriodeComptable;

    private boolean updateOnlyProvisoire = true;

    /**
     * Constructor. Aucune initialisation ici, ne pas oublié de setter par la suite.
     */
    public CGSoldeUpdater() {
        // Do nothing. No special initialisation here.
    }

    /**
     * Constructor avec initialisation.
     * 
     * @param forIdMandat
     * @param forIdExerciceComptable
     * @param forIdPeriodeComptable
     * @param forIdCompte
     * @param forIdCentreCharge
     */
    public CGSoldeUpdater(String forIdMandat, String forIdExerciceComptable, String forIdPeriodeComptable,
            String forIdCompte, String forIdCentreCharge, boolean updateOnlyProvisoire) {
        this.forIdMandat = forIdMandat;
        this.forIdExerciceComptable = forIdExerciceComptable;
        this.forIdPeriodeComptable = forIdPeriodeComptable;
        this.forIdCompte = forIdCompte;
        this.forIdCentreCharge = forIdCentreCharge;
        this.updateOnlyProvisoire = updateOnlyProvisoire;
    }

    public String getForIdCentreCharge() {
        return forIdCentreCharge;
    }

    public String getForIdCompte() {
        return forIdCompte;
    }

    public String getForIdExerciceComptable() {
        return forIdExerciceComptable;
    }

    public String getForIdMandat() {
        return forIdMandat;
    }

    public String getForIdPeriodeComptable() {
        return forIdPeriodeComptable;
    }

    /**
     * Return la mise à jour du montant depuis la table des écritures.
     * 
     * @param sumColumn
     * @param isProvisoire
     * @param isDebit
     * @param isCredit
     * @return
     * @throws Exception
     */
    private String getUpdateMontantInnerSql(String sumColumn, boolean isProvisoire, boolean isDebit, boolean isCredit) {
        String sql = "SELECT SUM(a." + sumColumn + ") from ";
        sql += new CGSoldeManager().getCollection() + CGEcritureViewBean.TABLE_CGECRIP + " a, "
                + new CGSoldeManager().getCollection() + CGJournal.TABLE_NAME + " b ";
        sql += " WHERE ";

        sql += " a." + CGEcritureViewBean.FIELD_IDJOURNAL + " = b." + CGJournal.FIELD_IDJOURNAL + " AND ";
        sql += " a." + CGEcritureViewBean.FIELD_IDCOMPTE + " = " + getForIdCompte() + " AND ";

        if (!JadeStringUtil.isIntegerEmpty(getForIdCentreCharge())) {
            sql += " a." + CGEcritureViewBean.FIELD_IDCENTRECHARGE + " = " + getForIdCentreCharge() + " AND ";
        }

        sql += " a." + CGEcritureViewBean.FIELD_IDEXERCOMPTABLE + " = " + getForIdExerciceComptable() + " AND ";
        sql += " b." + CGJournal.FIELD_IDEXERCOMPTABLE + " = " + getForIdExerciceComptable() + " AND ";

        if (!JadeStringUtil.isIntegerEmpty(getForIdPeriodeComptable())) {
            sql += " b." + CGJournal.FIELD_IDPERIODECOMPTABLE + " = " + getForIdPeriodeComptable() + " AND ";
        }

        if (isDebit) {
            sql += " (a." + CGEcritureViewBean.FIELD_CODEDEBITCREDIT + " = " + CodeSystem.CS_DEBIT + " OR ";
            sql += " a." + CGEcritureViewBean.FIELD_CODEDEBITCREDIT + " = " + CodeSystem.CS_EXTOURNE_DEBIT + ") AND ";
        }

        if (isCredit) {
            sql += " (a." + CGEcritureViewBean.FIELD_CODEDEBITCREDIT + " = " + CodeSystem.CS_CREDIT + " OR ";
            sql += " a." + CGEcritureViewBean.FIELD_CODEDEBITCREDIT + " = " + CodeSystem.CS_EXTOURNE_CREDIT + ") AND ";
        }

        if (!isProvisoire) {
            sql += " a." + CGEcritureViewBean.FIELD_ESTPROVISOIRE + " = " + BConstants.DB_BOOLEAN_FALSE_DELIMITED
                    + " AND ";
        }

        sql += " a." + CGEcritureViewBean.FIELD_ESTACTIVE + " = " + BConstants.DB_BOOLEAN_TRUE_DELIMITED + " AND ";
        sql += " a." + CGEcritureViewBean.FIELD_ESTERREUR + " = " + BConstants.DB_BOOLEAN_FALSE_DELIMITED;

        return sql;
    }

    /**
     * Return la query a éxécuter.
     * 
     * @param session
     * @return
     */
    private String getUpdateQuery(BSession session) {
        String updateQuery = "UPDATE " + new CGSoldeManager().getCollection() + CGSolde.TABLE_NAME;
        updateQuery += " SET ";

        String updateSet = "";
        updateSet += CGSolde.FIELD_SOLDEPROVISOIRE + " = ("
                + getUpdateMontantInnerSql(CGEcritureViewBean.FIELD_MONTANT, true, false, false) + "),";
        updateSet += CGSolde.FIELD_DOITPROVISOIRE + " = ("
                + getUpdateMontantInnerSql(CGEcritureViewBean.FIELD_MONTANT, true, true, false) + "),";
        updateSet += CGSolde.FIELD_AVOIRPROVISOIRE + " = ("
                + getUpdateMontantInnerSql(CGEcritureViewBean.FIELD_MONTANT, true, false, true) + "),";

        updateSet += CGSolde.FIELD_SOLDEPROVIMONNAIE + " = ("
                + getUpdateMontantInnerSql(CGEcritureViewBean.FIELD_MONTANTMONNAIE, true, false, false) + "),";
        updateSet += CGSolde.FIELD_DOITPROVIMONNAIE + " = ("
                + getUpdateMontantInnerSql(CGEcritureViewBean.FIELD_MONTANTMONNAIE, true, true, false) + "),";
        updateSet += CGSolde.FIELD_AVOIRPROVIMONNAIE + " = ("
                + getUpdateMontantInnerSql(CGEcritureViewBean.FIELD_MONTANTMONNAIE, true, false, true) + ")";

        if (!isUpdateOnlyProvisoire()) {
            updateSet += ", ";
            updateSet += CGSolde.FIELD_SOLDE + " = ("
                    + getUpdateMontantInnerSql(CGEcritureViewBean.FIELD_MONTANT, false, false, false) + "),";
            updateSet += CGSolde.FIELD_DOIT + " = ("
                    + getUpdateMontantInnerSql(CGEcritureViewBean.FIELD_MONTANT, false, true, false) + "),";
            updateSet += CGSolde.FIELD_AVOIR + " = ("
                    + getUpdateMontantInnerSql(CGEcritureViewBean.FIELD_MONTANT, false, false, true) + "),";

            updateSet += CGSolde.FIELD_SOLDEMONNAIE + " = ("
                    + getUpdateMontantInnerSql(CGEcritureViewBean.FIELD_MONTANTMONNAIE, false, false, false) + "),";
            updateSet += CGSolde.FIELD_DOITMONNAIE + " = ("
                    + getUpdateMontantInnerSql(CGEcritureViewBean.FIELD_MONTANTMONNAIE, false, true, false) + "),";
            updateSet += CGSolde.FIELD_AVOIRMONNAIE + " = ("
                    + getUpdateMontantInnerSql(CGEcritureViewBean.FIELD_MONTANTMONNAIE, false, false, true) + ")";
        }

        updateQuery += updateSet;

        updateQuery += updateSpyQuery(session, updateSet.length() > 0);

        updateQuery += " WHERE " + CGSolde.FIELD_IDPERIODECOMPTABLE + " = " + getForIdPeriodeComptable();
        updateQuery += " AND " + CGSolde.FIELD_IDCOMPTE + " = " + getForIdCompte();
        updateQuery += " AND " + CGSolde.FIELD_IDEXERCOMPTABLE + " = " + getForIdExerciceComptable();
        updateQuery += " AND " + CGSolde.FIELD_IDMANDAT + " = " + getForIdMandat();
        updateQuery += " AND " + CGSolde.FIELD_IDCENTRECHARGE + " = " + getForIdCentreCharge();

        return updateQuery;
    }

    public boolean isUpdateOnlyProvisoire() {
        return updateOnlyProvisoire;
    }

    public void setForIdCentreCharge(String forIdCentreCharge) {
        this.forIdCentreCharge = forIdCentreCharge;
    }

    public void setForIdCompte(String forIdCompte) {
        this.forIdCompte = forIdCompte;
    }

    public void setForIdExerciceComptable(String forIdExerciceComptable) {
        this.forIdExerciceComptable = forIdExerciceComptable;
    }

    public void setForIdMandat(String forIdMandat) {
        this.forIdMandat = forIdMandat;
    }

    public void setForIdPeriodeComptable(String forIdPeriodeComptable) {
        this.forIdPeriodeComptable = forIdPeriodeComptable;
    }

    public void setUpdateOnlyProvisoire(boolean updateOnlyProvisoire) {
        this.updateOnlyProvisoire = updateOnlyProvisoire;
    }

    /**
     * Effectue la mise à jour de la table des soldes. Call validate first.
     * 
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    public boolean update(BSession session, BTransaction transaction) throws Exception {
        if (!validate(session, transaction)) {
            return false;
        }

        BStatement s = new BStatement(transaction);
        s.createStatement();

        s.execute(getUpdateQuery(session));

        s.closeStatement();

        return true;
    }

    /**
     * Return la mise à jour du spy.
     * 
     * @param session
     * @param needSeparator
     * @return
     */
    private String updateSpyQuery(BSession session, boolean needSeparator) {
        String result = " ";
        if (needSeparator) {
            result += ", ";
        }

        BSpy spy = new BSpy(session);
        result += BSpy.FIELDNAME + " = '" + spy.toString() + "' ";
        return result;
    }

    /**
     * Validation. Les paramètres sont-ils spécifiés ?
     * 
     * @param session
     * @param transaction
     * @return True si tout est bien spécifié.
     */
    private boolean validate(BSession session, BTransaction transaction) {
        if (transaction == null) {
            return false;
        }

        if (JadeStringUtil.isIntegerEmpty(getForIdCompte())) {
            transaction.addErrors(session.getLabel("AUCUN_COMPTE_RESOLU"));
            return false;
        }

        if (JadeStringUtil.isIntegerEmpty(getForIdExerciceComptable())) {
            transaction.addErrors(session.getLabel("NEED_EXERCICE_COMPTABLE_ERREUR"));
            return false;
        }

        if (JadeStringUtil.isIntegerEmpty(getForIdMandat())) {
            transaction.addErrors(session.getLabel("GLOBAL_MANDAT_INEXISTANT"));
            return false;
        }

        // Centre de charge non null. 0 autorisé.
        if (JadeStringUtil.isBlank(getForIdPeriodeComptable())) {
            transaction.addErrors(session.getLabel("GLOBAL_PERIODE_INEXISTANT"));
            return false;
        }

        // Centre de charge non null. 0 autorisé.
        if (JadeStringUtil.isBlank(getForIdCentreCharge())) {
            transaction.addErrors(session.getLabel("AUCUN_CENTRE_CHARGE_ASSOCIE_ECRITURE"));
            return false;
        }

        return true;
    }
}
