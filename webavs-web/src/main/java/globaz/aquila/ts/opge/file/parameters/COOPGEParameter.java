package globaz.aquila.ts.opge.file.parameters;

import globaz.globall.db.BStatement;
import globaz.globall.db.FWFindParameter;

/**
 * Surcharge class FW (FWPARP) dans le but de pouvoir faire les update sans validation pour RDP FER.
 * 
 * @author dda
 */
public class COOPGEParameter extends FWFindParameter {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String PARAM_NUMERO_PLAGE_DEBUT_POURSUITE = "NUMPRSTDEB";
    public static final String PARAM_NUMERO_PLAGE_FIN_POURSUITE = "NUMPRSTFIN";
    public static final String PARAM_NUMERO_PLAGE_LAST_POURSUITE = "NUMPRSTLST";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_autoInherits()
     */
    @Override
    protected boolean _autoInherits() {
        // L'héritage à pour cible la même table que la classe parent.
        return false;
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires).
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _propertyMandatory(statement.getTransaction(), getIdApplParameter(), "L'ID de l'application est obligatoire");
        _propertyMandatory(statement.getTransaction(), getIdCleDiffere(), "La cle differe est obligatoire");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.FWFindParameter#_writePrimaryKey(globaz.globall.db. BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Surcharge writePrimaryKey car la date de validité du mot de passe ne
        // peut être utilisé comme clef.
        statement.writeKey("PPARAP",
                this._dbWriteString(statement.getTransaction(), getIdApplParametre(), "ID Application Parametre"));
        statement.writeKey("PPACDI",
                this._dbWriteString(statement.getTransaction(), getIdCleDiffere(), "ID Cle Differe"));
        statement.writeKey("PCOSID",
                this._dbWriteNumeric(statement.getTransaction(), getIdCodeSysteme(), "ID Code Systeme"));
        statement.writeKey("PPARIA",
                this._dbWriteNumeric(statement.getTransaction(), getIdActeurParametre(), "ID Acteur Parametre"));
        statement.writeKey("PPARPD",
                this._dbWriteNumeric(statement.getTransaction(), getPlageValDeParametre(), "Plage Val de Parametre"));
    }

}
