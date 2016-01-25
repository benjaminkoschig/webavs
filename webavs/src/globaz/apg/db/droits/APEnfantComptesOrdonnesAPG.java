/*
 * Créé le 30 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author vre
 */
public class APEnfantComptesOrdonnesAPG extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** DOCUMENT ME! */
    public static final String FIELDNAME_NB_ENFANTS = "VENENF";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFields(String schema) {
        return "COUNT(" + APEnfantAPG.FIELDNAME_DATEDEBUTDROIT + ") AS "
                + APEnfantComptesOrdonnesAPG.FIELDNAME_NB_ENFANTS + ", " + APEnfantAPG.FIELDNAME_DATEDEBUTDROIT;
    }

    private String date = "";
    private transient String fields = null;

    private String idSituationFam = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String nbEnfants = "";

    /**
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (fields == null) {
            fields = createFields(_getCollection());
        }

        return fields;
    }

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + APEnfantAPG.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return APEnfantAPG.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idSituationFam = statement.dbReadNumeric(APEnfantAPG.FIELDNAME_IDSITUATIONFAM);
        date = statement.dbReadDateAMJ(APEnfantAPG.FIELDNAME_DATEDEBUTDROIT);
        nbEnfants = statement.dbReadNumeric(FIELDNAME_NB_ENFANTS);
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
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    /**
     * getter pour l'attribut date
     * 
     * @return la valeur courante de l'attribut date
     */
    public String getDate() {
        return date;
    }

    /**
     * getter pour l'attribut id situation fam
     * 
     * @return la valeur courante de l'attribut id situation fam
     */
    public String getIdSituationFam() {
        return idSituationFam;
    }

    /**
     * getter pour l'attribut nb enfants
     * 
     * @return la valeur courante de l'attribut nb enfants
     */
    public String getNbEnfants() {
        return nbEnfants;
    }

    /**
     * setter pour l'attribut date
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDate(String string) {
        date = string;
    }

    /**
     * setter pour l'attribut id situation fam
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSituationFam(String string) {
        idSituationFam = string;
    }

    /**
     * setter pour l'attribut nb enfants
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbEnfants(String string) {
        nbEnfants = string;
    }
}
