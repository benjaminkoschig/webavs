package globaz.aquila.db.amende;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;

/**
 * @author dda CAOPERP table entity. Use for the "Extrait de Compte" function.
 */
public class COAmende extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String SECTIONDATE_FIELD = "SECTIONDATE";

    private String idExterne;
    private String idRubrique;
    // CAOPERP variable
    private String idSection;
    private String somme;

    /**
     * Return le nom de la table (CAOPERP).
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
        setIdRubrique(statement.dbReadNumeric(CARubrique.FIELD_IDRUBRIQUE));
        setIdExterne(statement.dbReadString(CARubrique.FIELD_IDEXTERNE));
        setSomme(statement.dbReadNumeric("somme", 2));
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
        // Not needed here
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

    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * @return l'idSection
     */
    public String getIdSection() {
        return idSection;
    }

    public String getSomme() {
        return somme;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    /**
     * @param id
     *            string
     */
    public void setIdSection(String id) {
        idSection = id;
    }

    public void setSomme(String somme) {
        this.somme = somme;
    }

}
