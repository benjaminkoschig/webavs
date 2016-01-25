package globaz.helios.db.modeles;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.CGLibelleInterface;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class CGModeleEcriture extends BEntity implements Serializable, CGLibelleInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_ESTMODELESYSTEME = "ESTMODELESYSTEME";

    public static final String FIELD_IDMANDAT = "IDMANDAT";
    public static final String FIELD_IDMODELEECRITURE = "IDMODELEECRITURE";
    public static final String FIELD_IDPARAMETREBOUCL = "IDPARAMETREBOUCL";
    public static final String FIELD_LIBELLEDE = "LIBELLEDE";
    public static final String FIELD_LIBELLEFR = "LIBELLEFR";
    public static final String FIELD_LIBELLEIT = "LIBELLEIT";
    public static final String FIELD_PIECE = "PIECE";
    public static final String TABLE_NAME = "CGMODLP";

    private Boolean estModeleSysteme = new Boolean(false);
    private String idMandat = new String();
    private String idModeleEcriture = new String();
    private String idParametreBouclement = new String();
    private String libelleDe = new String();
    private String libelleFr = new String();
    private String libelleIt = new String();
    private String piece = new String();
    private Boolean saisieEcran = new Boolean(false);

    /**
     * Commentaire relatif au constructeur CGModeleEcriture
     */
    public CGModeleEcriture() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente de +1 le numéro
        setIdModeleEcriture(this._incCounter(transaction, "0"));

    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        CGEnteteModeleEcritureManager managerEntete = new CGEnteteModeleEcritureManager();
        managerEntete.setSession(getSession());
        managerEntete.setForIdModeleEcriture(getIdModeleEcriture());

        managerEntete.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < managerEntete.size(); i++) {
            CGEnteteModeleEcriture entete = (CGEnteteModeleEcriture) managerEntete.get(i);

            CGLigneModeleEcritureManager mgr = new CGLigneModeleEcritureManager();
            mgr.setSession(getSession());
            mgr.setForIdEnteteModeleEcriture(entete.getIdEnteteModeleEcriture());

            mgr.find(transaction, BManager.SIZE_NOLIMIT);

            for (int j = 0; j < mgr.size(); j++) {
                CGLigneModeleEcriture ecriture = (CGLigneModeleEcriture) mgr.getEntity(j);
                ecriture.delete(transaction);
            }

            entete.delete(transaction);
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return CGModeleEcriture.TABLE_NAME;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idModeleEcriture = statement.dbReadNumeric(CGModeleEcriture.FIELD_IDMODELEECRITURE);
        idMandat = statement.dbReadNumeric(CGModeleEcriture.FIELD_IDMANDAT);
        idParametreBouclement = statement.dbReadNumeric(CGModeleEcriture.FIELD_IDPARAMETREBOUCL);
        libelleFr = statement.dbReadString(CGModeleEcriture.FIELD_LIBELLEFR);
        libelleDe = statement.dbReadString(CGModeleEcriture.FIELD_LIBELLEDE);
        libelleIt = statement.dbReadString(CGModeleEcriture.FIELD_LIBELLEIT);
        piece = statement.dbReadString(CGModeleEcriture.FIELD_PIECE);
        estModeleSysteme = statement.dbReadBoolean(CGModeleEcriture.FIELD_ESTMODELESYSTEME);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isBlank(getLibelle())) {
            _addError(statement.getTransaction(), getSession().getLabel("MODELE_ECR_LIBELLE_ERROR"));
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(CGModeleEcriture.FIELD_IDMODELEECRITURE,
                this._dbWriteNumeric(statement.getTransaction(), getIdModeleEcriture(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(CGModeleEcriture.FIELD_IDMODELEECRITURE,
                this._dbWriteNumeric(statement.getTransaction(), getIdModeleEcriture(), "idModeleEcriture"));
        statement.writeField(CGModeleEcriture.FIELD_IDMANDAT,
                this._dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
        statement.writeField(CGModeleEcriture.FIELD_IDPARAMETREBOUCL,
                this._dbWriteNumeric(statement.getTransaction(), getIdParametreBouclement(), "idParametreBouclement"));
        statement.writeField(CGModeleEcriture.FIELD_LIBELLEFR,
                this._dbWriteString(statement.getTransaction(), getLibelleFr(), "libelleFr"));
        statement.writeField(CGModeleEcriture.FIELD_LIBELLEDE,
                this._dbWriteString(statement.getTransaction(), getLibelleDe(), "libelleDe"));
        statement.writeField(CGModeleEcriture.FIELD_LIBELLEIT,
                this._dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleIt"));
        statement.writeField(CGModeleEcriture.FIELD_PIECE,
                this._dbWriteString(statement.getTransaction(), getPiece(), "piece"));
        statement.writeField(CGModeleEcriture.FIELD_ESTMODELESYSTEME, this._dbWriteBoolean(statement.getTransaction(),
                isEstModeleSysteme(), BConstants.DB_TYPE_BOOLEAN_CHAR, "estModeleSysteme"));
    }

    /**
     * Returns the idMandat.
     * 
     * @return String
     */
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * Returns the idModeleEcriture.
     * 
     * @return String
     */
    public String getIdModeleEcriture() {
        return idModeleEcriture;
    }

    /**
     * Returns the idParametreBouclement.
     * 
     * @return String
     */
    public String getIdParametreBouclement() {
        return idParametreBouclement;
    }

    @Override
    public String getLibelle() {
        return CGLibelle.getLibelleApp(this);
    }

    /**
     * Returns the libelleDe.
     * 
     * @return String
     */
    @Override
    public String getLibelleDe() {
        return libelleDe;
    }

    /**
     * Returns the libelleFr.
     * 
     * @return String
     */
    @Override
    public String getLibelleFr() {
        return libelleFr;
    }

    /**
     * Returns the libelleIt.
     * 
     * @return String
     */
    @Override
    public String getLibelleIt() {
        return libelleIt;
    }

    /**
     * Returns the piece.
     * 
     * @return String
     */
    public String getPiece() {
        return piece;
    }

    public Boolean getSaisieEcran() {
        return saisieEcran;
    }

    /**
     * Returns the isModeleSysteme.
     * 
     * @return Boolean
     */
    public Boolean isEstModeleSysteme() {
        return estModeleSysteme;
    }

    /**
     * Sets the estModeleSysteme.
     * 
     * @param estModeleSysteme
     *            The estModeleSysteme to set
     */
    public void setEstModeleSysteme(Boolean estModeleSysteme) {
        this.estModeleSysteme = estModeleSysteme;
    }

    /**
     * Sets the idMandat.
     * 
     * @param idMandat
     *            The idMandat to set
     */
    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }

    /**
     * Sets the idModeleEcriture.
     * 
     * @param idModeleEcriture
     *            The idModeleEcriture to set
     */
    public void setIdModeleEcriture(String idEnteteModeleEcriture) {
        idModeleEcriture = idEnteteModeleEcriture;
    }

    /**
     * Sets the idParametreBouclement.
     * 
     * @param idParametreBouclement
     *            The idParametreBouclement to set
     */
    public void setIdParametreBouclement(String idParametreBouclement) {
        this.idParametreBouclement = idParametreBouclement;
    }

    /**
     * Sets the libelleDe.
     * 
     * @param libelleDe
     *            The libelleDe to set
     */
    public void setLibelleDe(String libelleDe) {
        this.libelleDe = libelleDe;
    }

    /**
     * Sets the libelleFr.
     * 
     * @param libelleFr
     *            The libelleFr to set
     */
    public void setLibelleFr(String libelleFr) {
        this.libelleFr = libelleFr;
    }

    /**
     * Sets the libelleIt.
     * 
     * @param libelleIt
     *            The libelleIt to set
     */
    public void setLibelleIt(String libelleIt) {
        this.libelleIt = libelleIt;
    }

    /**
     * Sets the piece.
     * 
     * @param piece
     *            The piece to set
     */
    public void setPiece(String piece) {
        this.piece = piece;
    }

    public void setSaisieEcran(String newSaisieEcran) {
        try {
            saisieEcran = Boolean.valueOf(newSaisieEcran);
        } catch (Exception ex) {
            saisieEcran = new Boolean(false);
        }
    }

}
