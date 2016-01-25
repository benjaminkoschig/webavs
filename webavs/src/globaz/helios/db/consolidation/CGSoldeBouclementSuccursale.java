package globaz.helios.db.consolidation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BTransaction;
import globaz.helios.api.consolidation.ICGConsolidationSolde;
import java.io.Serializable;

public class CGSoldeBouclementSuccursale extends BEntity implements Serializable, ICGConsolidationSolde {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_AVOIR = "AVOIR";
    public static final String FIELD_DOIT = "DOIT";
    public static final String FIELD_ESTPERIODE = "ESTPERIODE";
    public static final String FIELD_IDCOMPTE = "IDCOMPTE";
    public static final String FIELD_IDEXERCICECOMPTABLE = "IDEXERCOMPTABLE";
    public static final String FIELD_IDMANDAT = "IDMANDAT";
    public static final String FIELD_IDPERIODECOMPTABLE = "IDPERIODECOMPTABLE";
    public static final String FIELD_IDSOLDEBOUCLEMENTSUCCURSALE = "IDSOLDEBOUCLSUCCUR";
    public static final String FIELD_IDSUCCURSALE = "IDSUCCURSALE";
    public static final String FIELD_SOLDE = "SOLDE";

    public static final String TABLE_NAME = "CGSOBSP";

    private String avoir = new String();
    private String doit = new String();
    private Boolean estPeriode = new Boolean(false);
    private String idCompte = new String();
    private String idExerComptable = new String();
    private String idMandat = new String();
    private String idPeriodeComptable = new String();
    private String idSoldeBouclementSuccursale = new String();
    private String idSuccursale = new String();
    private String solde = new String();

    /**
     * Commentaire relatif au constructeur CGSoldeBouclementSuccursale
     */
    public CGSoldeBouclementSuccursale() {
        super();
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdSoldeBouclementSuccursale(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idSoldeBouclementSuccursale = statement.dbReadNumeric(FIELD_IDSOLDEBOUCLEMENTSUCCURSALE, 0);
        idSuccursale = statement.dbReadNumeric(FIELD_IDSUCCURSALE, 0);
        idMandat = statement.dbReadNumeric(FIELD_IDMANDAT, 0);
        idExerComptable = statement.dbReadNumeric(FIELD_IDEXERCICECOMPTABLE, 0);
        idPeriodeComptable = statement.dbReadNumeric(FIELD_IDPERIODECOMPTABLE, 0);
        idCompte = statement.dbReadNumeric(FIELD_IDCOMPTE, 0);
        estPeriode = statement.dbReadBoolean(FIELD_ESTPERIODE);
        doit = statement.dbReadNumeric(FIELD_DOIT, 2);
        avoir = statement.dbReadNumeric(FIELD_AVOIR, 2);
        solde = statement.dbReadNumeric(FIELD_SOLDE, 2);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDSOLDEBOUCLEMENTSUCCURSALE,
                _dbWriteNumeric(statement.getTransaction(), getIdSoldeBouclementSuccursale(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(
                FIELD_IDSOLDEBOUCLEMENTSUCCURSALE,
                _dbWriteNumeric(statement.getTransaction(), getIdSoldeBouclementSuccursale(),
                        "idSoldeBouclementSuccursale"));
        statement.writeField(FIELD_IDSUCCURSALE,
                _dbWriteNumeric(statement.getTransaction(), getIdSuccursale(), "idSuccursale"));
        statement.writeField(FIELD_IDMANDAT, _dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
        statement.writeField(FIELD_IDEXERCICECOMPTABLE,
                _dbWriteNumeric(statement.getTransaction(), getIdExerComptable(), "idExerciceComptable"));
        statement.writeField(FIELD_IDPERIODECOMPTABLE,
                _dbWriteNumeric(statement.getTransaction(), getIdPeriodeComptable(), "idPeriodeComptable"));
        statement.writeField(FIELD_IDCOMPTE, _dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));
        statement.writeField(
                FIELD_ESTPERIODE,
                _dbWriteBoolean(statement.getTransaction(), isEstPeriode(),
                        globaz.globall.db.BConstants.DB_TYPE_BOOLEAN_CHAR, "estPeriode"));
        statement.writeField(FIELD_DOIT, _dbWriteNumeric(statement.getTransaction(), getDoit(), "doit"));
        statement.writeField(FIELD_AVOIR, _dbWriteNumeric(statement.getTransaction(), getAvoir(), "avoir"));
        statement.writeField(FIELD_SOLDE, _dbWriteNumeric(statement.getTransaction(), getSolde(), "solde"));
    }

    @Override
    public String getAvoir() {
        return avoir;
    }

    @Override
    public String getAvoirProvisoire() {
        // Only implemented by CGSolde
        return null;
    }

    @Override
    public String getDoit() {
        return doit;
    }

    @Override
    public String getDoitProvisoire() {
        // Only implemented by CGSolde
        return null;
    }

    public Boolean getEstPeriode() {
        return estPeriode;
    }

    @Override
    public String getIdCompte() {
        return idCompte;
    }

    @Override
    public String getIdExerComptable() {
        return idExerComptable;
    }

    @Override
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * Getter
     */
    @Override
    public String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    public String getIdSoldeBouclementSuccursale() {
        return idSoldeBouclementSuccursale;
    }

    public String getIdSuccursale() {
        return idSuccursale;
    }

    @Override
    public String getSolde() {
        return solde;
    }

    @Override
    public String getSoldeProvisoire() {
        // Only implemented by CGSolde
        return null;
    }

    @Override
    public Boolean isEstPeriode() {
        return getEstPeriode();
    }

    @Override
    public void setAvoir(String newAvoir) {
        avoir = newAvoir;
    }

    @Override
    public void setAvoirProvisoire(String newAvoirProvisoire) {
        // Do nothing. Only implemented by CGSolde
    }

    @Override
    public void setDoit(String newDoit) {
        doit = newDoit;
    }

    @Override
    public void setDoitProvisoire(String newDoitProvisoire) {
        // Do nothing. Only implemented by CGSolde
    }

    @Override
    public void setEstPeriode(Boolean estPeriode) {
        this.estPeriode = estPeriode;
    }

    @Override
    public void setIdCompte(String newIdCompte) {
        idCompte = newIdCompte;
    }

    @Override
    public void setIdExerComptable(String idExerciceComptable) {
        idExerComptable = idExerciceComptable;
    }

    @Override
    public void setIdMandat(String newIdMandat) {
        idMandat = newIdMandat;
    }

    /**
     * Setter
     */
    @Override
    public void setIdPeriodeComptable(String newIdPeriodeComptable) {
        idPeriodeComptable = newIdPeriodeComptable;
    }

    public void setIdSoldeBouclementSuccursale(String idSoldeBouclementSuccursale) {
        this.idSoldeBouclementSuccursale = idSoldeBouclementSuccursale;
    }

    @Override
    public void setIdSuccursale(String newIdSuccursale) {
        idSuccursale = newIdSuccursale;
    }

    @Override
    public void setSolde(String newSolde) {
        solde = newSolde;
    }

    @Override
    public void setSoldeProvisoire(String newSoldeProvisoire) {
        // Do nothing. Only implemented by CGSolde
    }

}
