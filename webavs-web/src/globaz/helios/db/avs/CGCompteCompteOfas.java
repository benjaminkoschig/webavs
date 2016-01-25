package globaz.helios.db.avs;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.io.Serializable;

public class CGCompteCompteOfas extends BEntity implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Clé alternée sur l'identifiant compte */
    public final static int AK_IDCOMPTE = 1;

    private static final String FIELD_IDCOMPTE = "IDCOMPTE";
    private static final String FIELD_IDCOMPTEOFAS = "IDCOMPTEOFAS";

    private static final String TABLE_NAME = "CGCPCOP";

    private String idCompte = new String();
    private String idCompteOfas = new String();

    /**
     * Commentaire relatif au constructeur CGCompteCompteOfas
     */
    public CGCompteCompteOfas() {
        super();
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
        idCompteOfas = statement.dbReadNumeric(FIELD_IDCOMPTEOFAS);
        idCompte = statement.dbReadNumeric(FIELD_IDCOMPTE);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * @see BEntity#_writeAlternateKey(globaz.globall.db.BStatement, int)
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == AK_IDCOMPTE) {
            statement.writeKey(FIELD_IDCOMPTE,
                    _dbWriteNumeric(statement.getTransaction(), getIdCompte(), FIELD_IDCOMPTE));
        } else {
            throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDCOMPTEOFAS,
                _dbWriteNumeric(statement.getTransaction(), getIdCompteOfas(), FIELD_IDCOMPTEOFAS));
        statement.writeKey(FIELD_IDCOMPTE, _dbWriteNumeric(statement.getTransaction(), getIdCompte(), FIELD_IDCOMPTE));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(FIELD_IDCOMPTEOFAS,
                _dbWriteNumeric(statement.getTransaction(), getIdCompteOfas(), FIELD_IDCOMPTEOFAS));
        statement
                .writeField(FIELD_IDCOMPTE, _dbWriteNumeric(statement.getTransaction(), getIdCompte(), FIELD_IDCOMPTE));
    }

    public String getIdCompte() {
        return idCompte;
    }

    /**
     * Getter
     */
    public String getIdCompteOfas() {
        return idCompteOfas;
    }

    public void setIdCompte(String newIdCompte) {
        idCompte = newIdCompte;
    }

    /**
     * Setter
     */
    public void setIdCompteOfas(String newIdCompteOfas) {
        idCompteOfas = newIdCompteOfas;
    }
}
