/*
 * 
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.batch.manager;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author spa Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CAMiseAJourEcritureFacturationExterne extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Private members
    private String idExterneRole;
    private String idRole;
    private String idSection;
    private String numPeriode;

    /**
	 *
	 */
    public CAMiseAJourEcritureFacturationExterne() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
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

        idExterneRole = statement.dbReadString("FANAFF");
        idRole = statement.dbReadNumeric("FATROL");
        numPeriode = statement.dbReadString("FANPER");
        idSection = statement.dbReadNumeric("IDSECTION");
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
        // throw new
        // Exception("Cannot use method 'writePrimaryKey' on this Entity");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        throw new Exception("Cannot use method 'writeProperty' on this Entity");
    }

    /**
     * @return the idExterneRole
     */
    public String getIdExterneRole() {
        return idExterneRole;
    }

    /**
     * @return the idRole
     */
    public String getIdRole() {
        return idRole;
    }

    /**
     * @return
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * @return the numPeriode
     */
    public String getNumPeriode() {
        return numPeriode;
    }

    /**
     * @param idExterneRole
     *            the idExterneRole to set
     */
    public void setIdExterneRole(String idExterneRole) {
        this.idExterneRole = idExterneRole;
    }

    /**
     * @param numPeriode
     *            the numPeriode to set
     */
    public void setIdFacturationExt(String numPeriode) {
        this.numPeriode = numPeriode;
    }

    /**
     * @param idRole
     *            the idRole to set
     */
    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    /**
     * @param string
     */
    public void setIdSection(String string) {
        idSection = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#toString()
     */
    public String toMyString() {
        try {
            return "Compte annexe : " + getIdExterneRole() + " Role : " + getIdRole() + " N° section : "
                    + getNumPeriode();
        } catch (Exception e) {
            return super.toString();
        }
    }
}
