package globaz.osiris.db.suivipaiements;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;

/**
 * @author dda CAOPERP table entity. Use for the "Extrait de Compte" function.
 */
public class CASuiviPaiementsAutresTaches extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String idExterne;

    // CARUBRP variable
    private String idRubrique;
    // CAOPERP variable
    private String sumMontant;

    /**
     * Return nothing. Entity est utilisé uniquement par un Inner-Join.
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setSumMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT));

        setIdRubrique(statement.dbReadNumeric(CARubrique.FIELD_IDRUBRIQUE));
        setIdExterne(statement.dbReadString(CARubrique.FIELD_IDEXTERNE));
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
        // Not needed here
    }

    /**
     * @return Returns the idExterne.
     */
    public String getIdExterne() {
        return idExterne;
    }

    /**
     * @return Returns the idRubrique.
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    public String getRubriqueDescription() {
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(getSession());

        rubrique.setIdRubrique(getIdRubrique());

        try {
            rubrique.retrieve();

            return getIdExterne() + " " + rubrique.getDescription();
        } catch (Exception e) {
            return getIdExterne();
        }
    }

    /**
     * @return
     */
    public String getSumMontant() {
        return sumMontant;
    }

    /**
     * @param idExterne
     *            The idExterne to set.
     */
    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    /**
     * @param idRubrique
     *            The idRubrique to set.
     */
    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    /**
     * @param string
     */
    public void setSumMontant(String string) {
        sumMontant = string;
    }

    /**
     * @return
     * @author
     */
    public String toMyString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("CASuiviPaiementsAutresTaches[");
        buffer.append("idExterne = ").append(idExterne);
        buffer.append(" idRubrique = ").append(idRubrique);
        buffer.append(" sumMontant = ").append(sumMontant);
        buffer.append("]");
        return buffer.toString();
    }

}
