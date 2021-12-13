package globaz.ij.db.prononces;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.ij.api.prononces.IIJMotifFpi;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.IPRCloneable;

import java.util.Optional;

/**
 * <H1>Description</H1>
 * 
 * @author ebko
 */
public class IJFpi extends IJPrononce implements IPRCloneable {

    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ID_PRONONCE_FPI = "ID_PRONONCE_FPI";
    public static final String FIELDNAME_CS_SITUATION_ASSURE = "SITUATION_ASSURE";

    public static final String FIELDNAME_ID_DERNIER_REVENU_OU_MANQUE_A_GAGNER = "ID_DERNIER_REVENU";
    public static final String FIELDNAME_DATE_FORMATION = "DATE_FORMATION";

    public static final String TABLE_NAME_FPI = "IJFPI";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     *
     * @param schema
     *
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append(TABLE_NAME_FPI);

        // jointure avec la table des prononces
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(TABLE_NAME_PRONONCE);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_ID_PRONONCE_FPI);
        fromClause.append("=");
        fromClause.append(FIELDNAME_ID_PRONONCE);

        return fromClause.toString();
    }

    private String csSituationAssure = "";
    private transient IJRevenu revenu;
    private String idDernierRevenuOuManqueAGagner = "";
    private String dateFormation = "";

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(BTransaction)
     * 
     * @param transaction
     *
     * @throws Exception
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
        setCsTypeIJ(IIJPrononce.CS_FPI);
    }

    /**
     *
     * @param statement
     *
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_FPI;
    }

    /**
     *
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idPrononce = statement.dbReadNumeric(FIELDNAME_ID_PRONONCE_FPI);
        csSituationAssure = statement.dbReadNumeric(FIELDNAME_CS_SITUATION_ASSURE);
        idDernierRevenuOuManqueAGagner = statement.dbReadNumeric(FIELDNAME_ID_DERNIER_REVENU_OU_MANQUE_A_GAGNER);
        dateFormation = statement.dbReadDateAMJ(FIELDNAME_DATE_FORMATION);
    }

    /**
     * @param statement
     *
     * @throws Exception
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * @param statement
     *
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_PRONONCE_FPI,
                _dbWriteNumeric(statement.getTransaction(), getIdPrononce()));
    }

    /**
     * @param statement
     *
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        if (_getAction() == ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(FIELDNAME_ID_PRONONCE_FPI,
                    _dbWriteNumeric(statement.getTransaction(), getIdPrononce(), "idPrononce"));
        }

        statement.writeField(FIELDNAME_CS_SITUATION_ASSURE,
                _dbWriteNumeric(statement.getTransaction(), csSituationAssure, "csSituationAssure"));
        statement.writeField(
                FIELDNAME_ID_DERNIER_REVENU_OU_MANQUE_A_GAGNER,
                _dbWriteNumeric(statement.getTransaction(), idDernierRevenuOuManqueAGagner,
                        "idDernierRevenuOuManqueAGagner"));
        statement.writeField(FIELDNAME_DATE_FORMATION, _dbWriteDateAMJ(statement.getTransaction(), dateFormation, "dateFormation"));
    }

    /**
     *
     * @param action
     *
     * @throws Exception
     */
    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        IJFpi clone = new IJFpi();

        duplicatePrononce(clone, action);

        clone.setCsSituationAssure(getCsSituationAssure());
        clone.setIdDernierRevenuOuManqueAGagner(getIdDernierRevenuOuManqueAGagner());
        clone.setDateFormation(getDateFormation());
        return clone;
    }

    /**
     * getter pour l'attribut cs situation assure
     * 
     * @return la valeur courante de l'attribut cs situation assure
     */
    public String getCsSituationAssure() {
        return csSituationAssure;
    }

    /**
     * getter pour l'attribut dernier revenu ou manque AGagner
     * 
     * @return la valeur courante de l'attribut dernier revenu ou manque AGagner
     */
    public String getIdDernierRevenuOuManqueAGagner() {
        return idDernierRevenuOuManqueAGagner;
    }


    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return true;
    }

    /**
     * charge le precedent revenu (avant l'ij) pour cette petite ij.
     * 
     * @return un revenu ou null is id dernier revenu est null.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJRevenu loadRevenu() throws Exception {
        if ((revenu == null) && !JadeStringUtil.isIntegerEmpty(idDernierRevenuOuManqueAGagner)) {
            revenu = new IJRevenu();
            revenu.setIdRevenu(idDernierRevenuOuManqueAGagner);
            revenu.setSession(getSession());
            revenu.retrieve();
        }

        return revenu;
    }

    /**
     * setter pour l'attribut cs situation assure
     * 
     * @param csSituationAssure
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsSituationAssure(String csSituationAssure) {
        this.csSituationAssure = csSituationAssure;
    }

    /**
     * setter pour l'attribut dernier revenu ou manque AGagner
     * 
     * @param dernierRevenuOuManqueAGagner
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDernierRevenuOuManqueAGagner(String dernierRevenuOuManqueAGagner) {
        idDernierRevenuOuManqueAGagner = dernierRevenuOuManqueAGagner;
    }

    public String getDateFormation() {
        return dateFormation;
    }

    public void setDateFormation(String dateFormation) {
        this.dateFormation = dateFormation;
    }
}
