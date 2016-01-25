package globaz.musca.db.gestionJourFerie;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author MMO
 * @since 4 aout 2010
 */
public class FAGestionJourFerieDomaine extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String F_ID_DOMAINE = "IDCOSP";

    public static final String F_ID_FERCOS = "IDFERCOS";
    public static final String F_ID_FERIE = "IDFERIE";
    /**
     * Constantes
     */
    public static final String TABLE_NAME = "FAFERCOS";

    private String idDomaine = "";
    /**
     * Attributs
     */
    private String idFerCos = "";
    private String idFerie = "";

    /**
     * Méthodes
     */

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdFerCos(this._incCounter(transaction, idFerCos));
    }

    @Override
    protected String _getTableName() {
        return FAGestionJourFerieDomaine.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idFerCos = statement.dbReadNumeric(FAGestionJourFerieDomaine.F_ID_FERCOS);
        idFerie = statement.dbReadNumeric(FAGestionJourFerieDomaine.F_ID_FERIE);
        idDomaine = statement.dbReadNumeric(FAGestionJourFerieDomaine.F_ID_DOMAINE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FAGestionJourFerieDomaine.F_ID_FERCOS,
                this._dbWriteNumeric(statement.getTransaction(), getIdFerCos(), "idFerCos"));

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FAGestionJourFerieDomaine.F_ID_FERCOS,
                this._dbWriteNumeric(statement.getTransaction(), getIdFerCos(), "idFerCos"));
        statement.writeField(FAGestionJourFerieDomaine.F_ID_FERIE,
                this._dbWriteNumeric(statement.getTransaction(), getIdFerie(), "idFerie"));
        statement.writeField(FAGestionJourFerieDomaine.F_ID_DOMAINE,
                this._dbWriteNumeric(statement.getTransaction(), getIdDomaine(), "idDomaine"));
    }

    public String getIdDomaine() {
        return idDomaine;
    }

    /**
     * Getter
     */
    public String getIdFerCos() {
        return idFerCos;
    }

    public String getIdFerie() {
        return idFerie;
    }

    public void setIdDomaine(String newIdDomaine) {
        idDomaine = newIdDomaine;
    }

    /**
     * Setter
     */
    public void setIdFerCos(String newIdFerCos) {
        idFerCos = newIdFerCos;
    }

    public void setIdFerie(String newIdFerie) {
        idFerie = newIdFerie;
    }

}
