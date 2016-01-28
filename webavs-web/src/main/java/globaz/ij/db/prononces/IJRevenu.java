package globaz.ij.db.prononces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJRevenu extends BEntity implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELDNAME_ANNEE = "XCDANN";

    public static final String FIELDNAME_CS_PERIODICITE_REVENU = "XCTPRE";
    public static final String FIELDNAME_ID_REVENU = "XCIREV";
    public static final String FIELDNAME_NB_HEURES_SEMAINE = "XCMNHS";
    public static final String FIELDNAME_REVENU = "XCMREV";
    public static final String TABLE_NAME = "IJREVENU";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String annee = "0";
    private String csPeriodiciteRevenu = "";
    private String idRevenu = "";
    private String nbHeuresSemaine = "0";
    private String revenu = "0";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        idRevenu = _incCounter(transaction, "0");
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        revenu = statement.dbReadNumeric(FIELDNAME_REVENU, 2);
        nbHeuresSemaine = statement.dbReadNumeric(FIELDNAME_NB_HEURES_SEMAINE, 2);
        csPeriodiciteRevenu = statement.dbReadNumeric(FIELDNAME_CS_PERIODICITE_REVENU);
        annee = statement.dbReadNumeric(FIELDNAME_ANNEE);
        idRevenu = statement.dbReadNumeric(FIELDNAME_ID_REVENU);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // l'annee est requise pour le calcul avec ACOR
        if (!JadeStringUtil.isDecimalEmpty(getRevenu())) {
            _propertyMandatory(statement.getTransaction(), annee, getSession().getLabel("ANNEE_REQUISE"));
        }
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_REVENU, _dbWriteNumeric(statement.getTransaction(), idRevenu));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_REVENU, _dbWriteNumeric(statement.getTransaction(), revenu, "revenu"));
        statement.writeField(FIELDNAME_NB_HEURES_SEMAINE,
                _dbWriteNumeric(statement.getTransaction(), nbHeuresSemaine, "nbHeuresSemaine"));
        statement.writeField(FIELDNAME_CS_PERIODICITE_REVENU,
                _dbWriteNumeric(statement.getTransaction(), csPeriodiciteRevenu, "csPeriodiciteRevenu"));
        statement.writeField(FIELDNAME_ANNEE, _dbWriteNumeric(statement.getTransaction(), annee, "annee"));
        statement.writeField(FIELDNAME_ID_REVENU, _dbWriteNumeric(statement.getTransaction(), idRevenu, "idRevenu"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @param action
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        IJRevenu clone = new IJRevenu();

        clone.setAnnee(getAnnee());
        clone.setCsPeriodiciteRevenu(getCsPeriodiciteRevenu());
        clone.setNbHeuresSemaine(getNbHeuresSemaine());
        clone.setRevenu(getRevenu());

        // On ne veut pas de la validation pendant une duplication
        clone.wantCallValidate(false);

        return clone;
    }

    /**
     * getter pour l'attribut annee.
     * 
     * @return la valeur courante de l'attribut annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * getter pour l'attribut cs periodicite revenu.
     * 
     * @return la valeur courante de l'attribut cs periodicite revenu
     */
    public String getCsPeriodiciteRevenu() {
        return csPeriodiciteRevenu;
    }

    /**
     * getter pour l'attribut id revenu.
     * 
     * @return la valeur courante de l'attribut id revenu
     */
    public String getIdRevenu() {
        return idRevenu;
    }

    /**
     * getter pour l'attribut nb heures semaines.
     * 
     * @return la valeur courante de l'attribut nb heures semaines
     */
    public String getNbHeuresSemaine() {
        return nbHeuresSemaine;
    }

    /**
     * getter pour l'attribut revenu.
     * 
     * @return la valeur courante de l'attribut revenu
     */
    public String getRevenu() {
        return revenu;
    }

    /**
     * getter pour l'attribut unique primary key.
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdRevenu();
    }

    /**
     * setter pour l'attribut annee.
     * 
     * @param annee
     *            une nouvelle valeur pour cet attribut
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * setter pour l'attribut cs periodicite revenu.
     * 
     * @param csPeriodiciteRevenu
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsPeriodiciteRevenu(String csPeriodiciteRevenu) {
        this.csPeriodiciteRevenu = csPeriodiciteRevenu;
    }

    /**
     * setter pour l'attribut id revenu.
     * 
     * @param idRevenu
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRevenu(String idRevenu) {
        this.idRevenu = idRevenu;
    }

    /**
     * setter pour l'attribut nb heures semaines.
     * 
     * @param nbHeuresSemaines
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbHeuresSemaine(String nbHeuresSemaines) {
        nbHeuresSemaine = nbHeuresSemaines;
    }

    /**
     * setter pour l'attribut revenu.
     * 
     * @param revenu
     *            une nouvelle valeur pour cet attribut
     */
    public void setRevenu(String revenu) {
        this.revenu = revenu;
    }

    /**
     * setter pour l'attribut unique primary key.
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdRevenu(pk);
    }
}
