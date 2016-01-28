/*
 * Créé le 29-08-2013
 */
package statofas;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author RCO
 */
public class REStatOFASDemandeACalculer extends BEntity {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String avs = "";
    private String messageOO = "";
    private String nom = "";
    private String prenom = "";

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return null; // PAS DE TABLES !!!
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        nom = statement.dbReadString("Nom");
        avs = statement.dbReadString("Avs");
        prenom = statement.dbReadString("Prenom");
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _addError(statement.getTransaction(), "interdit d'ajouter");
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    // private String libelleEcriture = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getAvs() {
        return avs;
    }

    public String getMessageOO() {
        return messageOO;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setAvs(String avs) {
        this.avs = avs;
    }

    public void setMessageOO(String message) {
        messageOO = message;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

}
