package globaz.helios.db.modeles;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGMandat;
import globaz.jade.client.util.JadeStringUtil;

public class CGEnteteModeleEcriture extends BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idEnteteModeleEcriture = new String();
    private String idMandat = new String();
    private String idModeleEcriture = new String();
    private String idTypeEcriture = new String();
    private String libelle = new String();
    private String piece = new String();

    /**
     * Commentaire relatif au constructeur CGModeleEcriture
     */
    public CGEnteteModeleEcriture() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente de +1 le numéro
        setIdEnteteModeleEcriture(_incCounter(transaction, "0"));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGEMODP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idEnteteModeleEcriture = statement.dbReadNumeric("IDENTETEMODECRIT");
        idModeleEcriture = statement.dbReadString("IDMODELEECRITURE");
        idTypeEcriture = statement.dbReadNumeric("IDTYPEECRITURE");
        libelle = statement.dbReadString("LIBELLE");
        piece = statement.dbReadString("PIECE");
        idMandat = statement.dbReadNumeric("IDMANDAT");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getIdMandat())) {
            _addError(statement.getTransaction(), getSession().getLabel("ENTETE_MODELE_ECR_MANDAT_ERROR"));
        }

        if (JadeStringUtil.isBlank(getIdModeleEcriture())) {
            _addError(statement.getTransaction(), getSession().getLabel("MODELE_INEXISTANT"));
        }

        if (JadeStringUtil.isIntegerEmpty(getIdTypeEcriture())) {
            _addError(statement.getTransaction(), getSession().getLabel("ENTETE_MODELE_ECR_TYPE_ECRITURE_ERROR"));
        }

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDENTETEMODECRIT",
                _dbWriteNumeric(statement.getTransaction(), getIdEnteteModeleEcriture(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDENTETEMODECRIT",
                _dbWriteNumeric(statement.getTransaction(), getIdEnteteModeleEcriture(), "idEnteteModeleEcriture"));
        statement.writeField("IDMODELEECRITURE",
                _dbWriteString(statement.getTransaction(), getIdModeleEcriture(), "idModeleEcriture"));
        statement.writeField("IDTYPEECRITURE",
                _dbWriteNumeric(statement.getTransaction(), getIdTypeEcriture(), "idTypeEcriture"));
        statement.writeField("LIBELLE", _dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField("PIECE", _dbWriteString(statement.getTransaction(), getPiece(), "piece"));
        statement.writeField("IDMANDAT", _dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
    }

    /**
     * Returns the idEnteteModeleEcriture.
     * 
     * @return String
     */
    public String getIdEnteteModeleEcriture() {
        return idEnteteModeleEcriture;
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
     * Returns the idTypeEcriture.
     * 
     * @return String
     */
    public String getIdTypeEcriture() {
        return idTypeEcriture;
    }

    /**
     * Returns the libelle.
     * 
     * @return String
     */
    public String getLibelle() {
        return libelle;
    }

    public CGMandat getMandat() {
        CGMandat mandat = new CGMandat();
        mandat.setSession(getSession());
        mandat.setIdMandat(getIdMandat());

        try {
            mandat.retrieve();

            if (mandat.isNew()) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        return mandat;
    }

    /**
     * Returns the piece.
     * 
     * @return String
     * @deprecated Utiliser la pièce du modèle.
     */
    @Deprecated
    public String getPiece() {
        return piece;
    }

    /**
     * Sets the idEnteteModeleEcriture.
     * 
     * @param idEnteteModeleEcriture
     *            The idEnteteModeleEcriture to set
     */
    public void setIdEnteteModeleEcriture(String idEnteteModeleEcriture) {
        this.idEnteteModeleEcriture = idEnteteModeleEcriture;
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
    public void setIdModeleEcriture(String idModeleEcriture) {
        this.idModeleEcriture = idModeleEcriture;
    }

    /**
     * Sets the idTypeEcriture.
     * 
     * @param idTypeEcriture
     *            The idTypeEcriture to set
     */
    public void setIdTypeEcriture(String idTypeEcriture) {
        this.idTypeEcriture = idTypeEcriture;
    }

    /**
     * Sets the libelle.
     * 
     * @param libelle
     *            The libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Sets the piece.
     * 
     * @param piece
     *            The piece to set
     * @deprecated Utiliser la pièce du modèle.
     */
    @Deprecated
    public void setPiece(String piece) {
        this.piece = piece;
    }
}
