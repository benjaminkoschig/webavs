/*
 * Créé le 7 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.prestation.db.employeurs;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Entity pour les noms des départements.
 * </p>
 * 
 * @author vre
 */
public class PRDepartement extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 
     */
    public static final String FIELDNAME_DEPARTEMENT = "MFLALP";

    /** 
     */
    public static final String FIELDNAME_ID_AFFILIE = "MAIAFF";

    /** 
     */
    public static final String FIELDNAME_ID_PART = "MFIPAR";

    /** 
     */
    public static final String FIELDNAME_TYPE_PART = "MFTPAR";

    // Particularité Affiliation
    public final static String PARTIC_AFFILIE_DEPARTEMENT = "818009";

    /** 
     */
    public static final String TABLE_NAME = "AFPARTP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String departement = "";
    private String idAffilie = "";
    private String idParticularite = "";
    private String typeParticularite = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * peut pas ajouter.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * peut pas effacer.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * peut pas modifier.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idParticularite = statement.dbReadNumeric(FIELDNAME_ID_PART);
        idAffilie = statement.dbReadNumeric(FIELDNAME_ID_AFFILIE);
        departement = statement.dbReadString(FIELDNAME_DEPARTEMENT);
        typeParticularite = statement.dbReadNumeric(FIELDNAME_TYPE_PART);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_PART,
                _dbWriteNumeric(statement.getTransaction(), idParticularite, "idParticularite"));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * getter pour l'attribut departement.
     * 
     * @return la valeur courante de l'attribut departement
     */
    public String getDepartement() {
        return departement;
    }

    /**
     * getter pour l'attribut id affilie.
     * 
     * @return la valeur courante de l'attribut id affilie
     */
    public String getIdAffilie() {
        return idAffilie;
    }

    /**
     * getter pour l'attribut id particulier.
     * 
     * @return la valeur courante de l'attribut id particulier
     */
    public String getIdParticularite() {
        return idParticularite;
    }

    public String getTypeParticularite() {
        return typeParticularite;
    }

    /**
     * setter pour l'attribut departement.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDepartement(String string) {
        departement = string;
    }

    /**
     * setter pour l'attribut id affilie.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAffilie(String string) {
        idAffilie = string;
    }

    /**
     * setter pour l'attribut id particulier.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdParticularite(String string) {
        idParticularite = string;
    }

    public void setTypeParticularite(String typeParticularite) {
        this.typeParticularite = typeParticularite;
    }
}
