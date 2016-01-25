package globaz.helios.db.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.CGLibelleInterface;

/**
 * @author user
 * @version i
 * 
 */
public class CGSimplePlanComptableViewBean extends BEntity implements FWViewBeanInterface, CGLibelleInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.Boolean aReouvrir = new Boolean(false);
    private java.lang.String idCompte = new String();
    private java.lang.String idExerciceComptable = new String();
    private java.lang.String idExterne = new String();
    private java.lang.String libelleDe = new String();
    private java.lang.String libelleFr = new String();
    private java.lang.String libelleIt = new String();

    /**
     * Constructor for CGSimplePlanComptableViewBean.
     */
    public CGSimplePlanComptableViewBean() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "CGPLANP";
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idExerciceComptable = statement.dbReadNumeric("IDEXERCOMPTABLE");
        idCompte = statement.dbReadNumeric("IDCOMPTE");
        idExterne = statement.dbReadString("IDEXTERNE");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
        aReouvrir = statement.dbReadBoolean("AREOUVRIR");
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getCollection() + _getTableName() + "." + "IDEXERCOMPTABLE",
                _dbWriteNumeric(statement.getTransaction(), getIdExerciceComptable(), ""));
        statement.writeKey(_getCollection() + _getTableName() + "." + "IDCOMPTE",
                _dbWriteNumeric(statement.getTransaction(), getIdCompte(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("IDEXERCOMPTABLE",
                _dbWriteNumeric(statement.getTransaction(), getIdExerciceComptable(), "idExerciceComptable"));
        statement.writeField("IDCOMPTE", _dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));
        statement.writeField("IDEXTERNE", _dbWriteString(statement.getTransaction(), getIdExterne(), "idExterne"));
        statement.writeField("LIBELLEFR", _dbWriteString(statement.getTransaction(), getLibelleFr(), "libelleFr"));
        statement.writeField("LIBELLEDE", _dbWriteString(statement.getTransaction(), getLibelleDe(), "libelleDe"));
        statement.writeField("LIBELLEIT", _dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleIt"));
        statement
                .writeField(
                        "AREOUVRIR",
                        _dbWriteBoolean(statement.getTransaction(), isAReouvrir(), BConstants.DB_TYPE_BOOLEAN_CHAR,
                                "aReouvrir"));
    }

    /**
     * Returns the idCompte.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCompte() {
        return idCompte;
    }

    /**
     * Returns the idExerciceComptable.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Returns the idExterne.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdExterne() {
        return idExterne;
    }

    @Override
    public java.lang.String getLibelle() {
        return CGLibelle.getLibelleApp(this);
    }

    /**
     * Returns the libelleDe.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getLibelleDe() {
        return libelleDe;
    }

    /**
     * Returns the libelleFr.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getLibelleFr() {
        return libelleFr;
    }

    /**
     * Returns the libelleIt.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getLibelleIt() {
        return libelleIt;
    }

    /**
     * Returns the aReouvrir.
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean isAReouvrir() {
        return aReouvrir;
    }

    /**
     * Sets the aReouvrir.
     * 
     * @param aReouvrir
     *            The aReouvrir to set
     */
    public void setAReouvrir(java.lang.Boolean aReouvrir) {
        this.aReouvrir = aReouvrir;
    }

    /**
     * Sets the idCompte.
     * 
     * @param idCompte
     *            The idCompte to set
     */
    public void setIdCompte(java.lang.String idCompte) {
        this.idCompte = idCompte;
    }

    /**
     * Sets the idExerciceComptable.
     * 
     * @param idExerciceComptable
     *            The idExerciceComptable to set
     */
    public void setIdExerciceComptable(java.lang.String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

    /**
     * Sets the idExterne.
     * 
     * @param idExterne
     *            The idExterne to set
     */
    public void setIdExterne(java.lang.String idExterne) {
        this.idExterne = idExterne;
    }

    /**
     * Sets the libelleDe.
     * 
     * @param libelleDe
     *            The libelleDe to set
     */
    public void setLibelleDe(java.lang.String libelleDe) {
        this.libelleDe = libelleDe;
    }

    /**
     * Sets the libelleFr.
     * 
     * @param libelleFr
     *            The libelleFr to set
     */
    public void setLibelleFr(java.lang.String libelleFr) {
        this.libelleFr = libelleFr;
    }

    /**
     * Sets the libelleIt.
     * 
     * @param libelleIt
     *            The libelleIt to set
     */
    public void setLibelleIt(java.lang.String libelleIt) {
        this.libelleIt = libelleIt;
    }

}
