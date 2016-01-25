/*
 * Créé le 29 jui. 05
 */
package globaz.naos.db.releve;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;

/**
 * La classe définissant l'entité Relevé Montant.
 * 
 * Utilisé pour sauvegarder les valeurs de saisie des Relevés avant facturation.
 * 
 * @author sau 29 jui. 05 13:49:52
 */
public class AFApercuReleveMontant extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String assuranceId = new String();
    private String cotisationId = new String();
    private String dateDebut = new String();
    // DB Table AFREVMP
    // Primary Key
    private String idReleve = new String();
    // Fields
    private String masse = new String();
    private String montantCalculer = new String();

    /**
     * Constructeur
     */
    public AFApercuReleveMontant() {
        super();
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFREVMP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idReleve = statement.dbReadNumeric("MMIREL");
        assuranceId = statement.dbReadNumeric("MBIASS");
        dateDebut = statement.dbReadDateAMJ("MRMDEB");
        masse = statement.dbReadNumeric("MRMMSS");
        montantCalculer = statement.dbReadNumeric("MRMMCA");
        cotisationId = statement.dbReadNumeric("MEICOT");
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        boolean validationOK = true;

        // Test que les champs obligatoires soit renseignés
        validationOK &= _propertyMandatory(statement.getTransaction(), getIdReleve(), "IdReleve doit être renseigné");
        validationOK &= _propertyMandatory(statement.getTransaction(), getAssuranceId(),
                "AssuranceId doit être renseigné");
        validationOK &= _propertyMandatory(statement.getTransaction(), getDateDebut(), "DateDebut doit être renseigné");
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MMIREL", this._dbWriteNumeric(statement.getTransaction(), getIdReleve(), ""));
        statement.writeKey("MBIASS", this._dbWriteNumeric(statement.getTransaction(), getAssuranceId(), ""));
        statement.writeKey("MRMDEB", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), ""));
        statement.writeKey("MEICOT", this._dbWriteNumeric(statement.getTransaction(), getCotisationId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MMIREL", this._dbWriteNumeric(statement.getTransaction(), getIdReleve(), ""));
        statement.writeField("MBIASS", this._dbWriteNumeric(statement.getTransaction(), getAssuranceId(), ""));
        statement.writeField("MRMDEB", this._dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), ""));
        statement.writeField("MRMMSS", this._dbWriteNumeric(statement.getTransaction(), getMasse(), ""));
        statement.writeField("MRMMCA", this._dbWriteNumeric(statement.getTransaction(), getMontantCalculer(), ""));
        statement.writeField("MEICOT", this._dbWriteNumeric(statement.getTransaction(), getCotisationId(), ""));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getAssuranceId() {
        return assuranceId;
    }

    public String getCotisationId() {
        return cotisationId;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getIdReleve() {
        return idReleve;
    }

    public String getMasse() {
        return masse;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public String getMontantCalculer() {
        return montantCalculer;
    }

    public void setAssuranceId(String string) {
        assuranceId = string;
    }

    public void setCotisationId(String cotisationId) {
        this.cotisationId = cotisationId;
    }

    public void setDateDebut(String string) {
        dateDebut = string;
    }

    public void setIdReleve(String string) {
        idReleve = string;
    }

    public void setMasse(String string) {
        masse = JANumberFormatter.deQuote(string);
    }

    public void setMontantCalculer(String string) {
        montantCalculer = JANumberFormatter.deQuote(string);
    }
}
