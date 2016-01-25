/*
 * Created on Jun 14, 2005
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.osiris.db.journal.comptecourant;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;

/**
 * @author dda To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public class CAJoinCompteCourantOperationManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String LABEL_SECTION_NON_RENSEIGNEE = "5125";

    private String forIdCompteCourant = null;
    private String forIdSection = null;

    private boolean forSumNegative = false;
    private boolean forSumPositive = false;
    private boolean forVentilationAccepter = false;

    public CAJoinCompteCourantOperationManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_beforeFind(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        super._beforeFind(transaction);

        if (JadeStringUtil.isIntegerEmpty(getForIdSection())) {
            _addError(transaction,
                    getSession().getLabel(CAJoinCompteCourantOperationManager.LABEL_SECTION_NON_RENSEIGNEE));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getSql(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getSql(BStatement statement) {
        String sql = "select a." + CAJoinCompteCourantOperation.IDCOMPTECOURANT_FIELD + ", a."
                + CAJoinCompteCourantOperation.MONTANT_FIELD + ", b." + CAJoinCompteCourantOperation.PRIORITE_FIELD
                + ", b." + CAJoinCompteCourantOperation.ACCEPTER_VENTILATION_FIELD + " from ";
        sql += " ( ";
        sql += " select " + CAJoinCompteCourantOperation.IDCOMPTECOURANT_FIELD + " as "
                + CAJoinCompteCourantOperation.IDCOMPTECOURANT_FIELD + ", sum("
                + CAJoinCompteCourantOperation.MONTANT_FIELD + ") as " + CAJoinCompteCourantOperation.MONTANT_FIELD
                + " from ";
        sql += " " + _getCollection() + CAOperation.TABLE_CAOPERP + " ";
        sql += " where ";
        sql += " (ETAT = " + APIOperation.ETAT_COMPTABILISE + " or ETAT = " + APIOperation.ETAT_PROVISOIRE
                + ") and IDSECTION = " + getForIdSection() + " and IDTYPEOPERATION like 'E%' ";

        if (!JadeStringUtil.isIntegerEmpty(getForIdCompteCourant())) {
            sql += " and " + CAJoinCompteCourantOperation.IDCOMPTECOURANT_FIELD + " = " + getForIdCompteCourant();
        }

        sql += " group by " + CAJoinCompteCourantOperation.IDCOMPTECOURANT_FIELD + " ";
        sql += " ) a, " + _getCollection() + CAJoinCompteCourantOperation.CACPTCP_TABLE_NAME + " b ";
        sql += " where ";
        sql += " a." + CAJoinCompteCourantOperation.IDCOMPTECOURANT_FIELD + " = b."
                + CAJoinCompteCourantOperation.IDCOMPTECOURANT_FIELD + " ";

        if (isForSumNegative()) {
            sql += " and a." + CAJoinCompteCourantOperation.MONTANT_FIELD + " < 0 ";
        }

        if (isForSumPositive()) {
            sql += " and a." + CAJoinCompteCourantOperation.MONTANT_FIELD + " > 0 ";
        }

        if (isForVentilationAccepter()) {
            sql += " and b." + CAJoinCompteCourantOperation.ACCEPTER_VENTILATION_FIELD + " = "
                    + BConstants.DB_BOOLEAN_TRUE_DELIMITED;
        }

        sql += " order by b." + CAJoinCompteCourantOperation.PRIORITE_FIELD;

        return sql;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAJoinCompteCourantOperation();
    }

    /**
     * @return
     */
    public String getForIdCompteCourant() {
        return forIdCompteCourant;
    }

    /**
     * @return
     */
    public String getForIdSection() {
        return forIdSection;
    }

    /**
     * @return
     */
    public boolean isForSumNegative() {
        return forSumNegative;
    }

    /**
     * @return
     */
    public boolean isForSumPositive() {
        return forSumPositive;
    }

    /**
     * @return
     */
    public boolean isForVentilationAccepter() {
        return forVentilationAccepter;
    }

    /**
     * @param string
     */
    public void setForIdCompteCourant(String string) {
        forIdCompteCourant = string;
    }

    /**
     * @param string
     */
    public void setForIdSection(String string) {
        forIdSection = string;
    }

    /**
     * @param b
     */
    public void setForSumNegative(boolean b) {
        forSumNegative = b;
    }

    /**
     * @param b
     */
    public void setForSumPositive(boolean b) {
        forSumPositive = b;
    }

    /**
     * @param b
     */
    public void setForVentilationAccepter(boolean b) {
        forVentilationAccepter = b;
    }

}
