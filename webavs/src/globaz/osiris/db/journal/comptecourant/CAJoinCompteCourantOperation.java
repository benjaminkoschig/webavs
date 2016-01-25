/*
 * Created on Jun 14, 2005
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.osiris.db.journal.comptecourant;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;

/**
 * @author dda To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public class CAJoinCompteCourantOperation extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ACCEPTER_VENTILATION_FIELD = "ACCEPTERVEN";
    public static final String CACPTCP_TABLE_NAME = "CACPTCP";
    public static final String IDCOMPTECOURANT_FIELD = "IDCOMPTECOURANT";
    public static final String MONTANT_FIELD = "MONTANT";

    public static final String PRIORITE_FIELD = "PRIORITE";

    private String idCompteCourant;
    private String montant;
    private String priorite;
    private Boolean ventilationAccepter;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CAOperation.TABLE_CAOPERP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdCompteCourant(statement.dbReadNumeric(CAJoinCompteCourantOperation.IDCOMPTECOURANT_FIELD));
        setMontant(statement.dbReadNumeric(CAJoinCompteCourantOperation.MONTANT_FIELD));
        setPriorite(statement.dbReadNumeric(CAJoinCompteCourantOperation.PRIORITE_FIELD));
        setVentilationAccepter(statement.dbReadBoolean(CAJoinCompteCourantOperation.ACCEPTER_VENTILATION_FIELD));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * @return
     */
    public String getIdCompteCourant() {
        return idCompteCourant;
    }

    /**
     * @return
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return
     */
    public String getPriorite() {
        return priorite;
    }

    /**
     * @return
     */
    public Boolean getVentilationAccepter() {
        return ventilationAccepter;
    }

    /**
     * @param string
     */
    public void setIdCompteCourant(String string) {
        idCompteCourant = string;
    }

    /**
     * @param string
     */
    public void setMontant(String string) {
        montant = string;
    }

    /**
     * @param string
     */
    public void setPriorite(String string) {
        priorite = string;
    }

    /**
     * @param string
     */
    public void setVentilationAccepter(Boolean b) {
        ventilationAccepter = b;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#toString()
     */
    public String toMyString() {
        return "IdCompteCourant : " + getIdCompteCourant() + "\nMontant : " + getMontant() + "Priorite : "
                + getPriorite() + "\nVentilationAccepter" + getVentilationAccepter();
    }

}
