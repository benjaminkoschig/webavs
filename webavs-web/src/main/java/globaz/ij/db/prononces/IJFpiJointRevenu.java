package globaz.ij.db.prononces;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * <H1>Description</H1>
 * 
 * @author ebko
 */
public class IJFpiJointRevenu extends IJFpi {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = "";
    private String csPeriodiciteRevenu = "";
    private String fromClause = null;
    private String idRevenu = "";
    private String nbHeuresSemaine = "";

    private String revenu = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc).
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * (non-Javadoc).
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * (non-Javadoc).
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_beforeUpdate(BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        IJRevenu revenu = new IJRevenu();

        revenu.setSession(getSession());
        revenu.setIdRevenu(idRevenu);
        revenu.retrieve(transaction);

        revenu.setRevenu(this.revenu);
        revenu.setNbHeuresSemaine(nbHeuresSemaine);
        revenu.setCsPeriodiciteRevenu(csPeriodiciteRevenu);
        revenu.setAnnee(annee);

        revenu.save(transaction);
        setIdDernierRevenuOuManqueAGagner(revenu.getIdRevenu());
    }

    /**
     * (non-Javadoc).
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_getFrom(BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            StringBuffer fromClauseBuffer = new StringBuffer(createFromClause(_getCollection()));
            String leftJoin = " LEFT JOIN ";
            String on = " ON ";
            String point = ".";
            String egal = "=";

            // jointure entre table des FPI et des revenus
            fromClauseBuffer.append(" ");
            fromClauseBuffer.append(leftJoin);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(IJRevenu.TABLE_NAME);
            fromClauseBuffer.append(on);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(IJFpi.TABLE_NAME_FPI);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(IJFpi.FIELDNAME_ID_DERNIER_REVENU_OU_MANQUE_A_GAGNER);
            fromClauseBuffer.append(egal);
            fromClauseBuffer.append(_getCollection());
            fromClauseBuffer.append(IJRevenu.TABLE_NAME);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(IJRevenu.FIELDNAME_ID_REVENU);

            fromClause = fromClauseBuffer.toString();
        }

        return fromClause;
    }

    /**
     * (non-Javadoc).
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return IJFpi.TABLE_NAME_FPI;
    }

    /**
     * (non-Javadoc).
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_readProperties(BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        revenu = statement.dbReadNumeric(IJRevenu.FIELDNAME_REVENU, 2);
        nbHeuresSemaine = statement.dbReadNumeric(IJRevenu.FIELDNAME_NB_HEURES_SEMAINE, 2);
        csPeriodiciteRevenu = statement.dbReadNumeric(IJRevenu.FIELDNAME_CS_PERIODICITE_REVENU);
        annee = statement.dbReadNumeric(IJRevenu.FIELDNAME_ANNEE);
        idRevenu = statement.dbReadNumeric(IJRevenu.FIELDNAME_ID_REVENU);
    }

    /**
     * (non-Javadoc).
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Raccord de méthode auto-généré
    }

    /**
     * (non-Javadoc).
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(IJFpi.FIELDNAME_ID_PRONONCE_FPI,
                _dbWriteNumeric(statement.getTransaction(), getIdPrononce(), "idFpi"));
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
     * getter pour l'attribut from clause.
     * 
     * @return la valeur courante de l'attribut from clause
     */
    public String getFromClause() {
        return fromClause;
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
     * getter pour l'attribut nb heures semaine.
     * 
     * @return la valeur courante de l'attribut nb heures semaine
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
     * setter pour l'attribut annee.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * setter pour l'attribut cs periodicite revenu.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsPeriodiciteRevenu(String string) {
        csPeriodiciteRevenu = string;
    }

    /**
     * setter pour l'attribut from clause.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromClause(String string) {
        fromClause = string;
    }

    /**
     * setter pour l'attribut id revenu.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRevenu(String string) {
        idRevenu = string;
    }

    /**
     * setter pour l'attribut nb heures semaine.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbHeuresSemaine(String string) {
        nbHeuresSemaine = string;
    }

    /**
     * setter pour l'attribut revenu.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setRevenu(String string) {
        revenu = string;
    }
}
