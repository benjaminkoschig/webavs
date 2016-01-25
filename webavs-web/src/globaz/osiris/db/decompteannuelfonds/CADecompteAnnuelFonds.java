package globaz.osiris.db.decompteannuelfonds;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;

/**
 * @author BJO <h1>description</h1> Entity virtuelle uniquement utilisé par un inner join, utilisé pour la liste
 *         décompte annuel autre tache
 * 
 */
public class CADecompteAnnuelFonds extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // De la table rubrique(CARUBRP)
    private String idExterne;

    // De la table operation(CAOPERP)
    private String sommeMontant;

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
        setSommeMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT));
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

    public String getIdExterne() {
        return idExterne;
    }

    public String getSommeMontant() {
        return sommeMontant;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setSommeMontant(String sommeMontant) {
        this.sommeMontant = sommeMontant;
    }

    public String toMyString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("CAListDecompteAnnuelFonds[");
        buffer.append("sommeMontant = ").append(sommeMontant);
        buffer.append("idExterne = ").append(idExterne);
        buffer.append("]");
        return buffer.toString();
    }
}
