package globaz.helios.db.consolidation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BTransaction;
import java.io.Serializable;

public class CGSuccursale extends BEntity implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_IDSUCCURSALE = "IDSUCCURSALE";
    public static final String FIELD_NOM = "NOM";
    public static final String FIELD_NUMEROSUCCURSALE = "NUMSUCCURSALE";

    public static final String TABLE_NAME = "CGSUCCP";

    private String idSuccursale = new String();
    private String nom = new String();
    private String numeroSuccursale = new String();

    /**
     * Commentaire relatif au constructeur CGSuccursale
     */
    public CGSuccursale() {
        super();
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdSuccursale(_incCounter(transaction, "0"));
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
        idSuccursale = statement.dbReadNumeric(FIELD_IDSUCCURSALE);
        nom = statement.dbReadString(FIELD_NOM);
        numeroSuccursale = statement.dbReadString(FIELD_NUMEROSUCCURSALE);
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
        statement.writeKey(FIELD_IDSUCCURSALE, _dbWriteNumeric(statement.getTransaction(), getIdSuccursale(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(FIELD_IDSUCCURSALE,
                _dbWriteNumeric(statement.getTransaction(), getIdSuccursale(), "idSuccursale"));
        statement.writeField(FIELD_NOM, _dbWriteString(statement.getTransaction(), getNom(), "nom"));
        statement.writeField(FIELD_NUMEROSUCCURSALE,
                _dbWriteString(statement.getTransaction(), getNumeroSuccursale(), "numeroSuccursale"));
    }

    /**
     * Getter
     */
    public String getIdSuccursale() {
        return idSuccursale;
    }

    public String getNom() {
        return nom;
    }

    public String getNumeroSuccursale() {
        return numeroSuccursale;
    }

    /**
     * Setter
     */
    public void setIdSuccursale(String newIdSuccursale) {
        idSuccursale = newIdSuccursale;
    }

    public void setNom(String newNom) {
        nom = newNom;
    }

    public void setNumeroSuccursale(String newNumeroSuccursale) {
        numeroSuccursale = newNumeroSuccursale;
    }
}
