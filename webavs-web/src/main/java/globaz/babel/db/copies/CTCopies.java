/*
 * Créé le 2 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.db.copies;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

public class CTCopies extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_DEBUT_COPIES = "CGDDEB";
    public static final String FIELDNAME_DATE_FIN_COPIES = "CGDFIN";
    public static final String FIELDNAME_DOMAINE_APP_ADR_COPIE = "CGTDOA";
    public static final String FIELDNAME_ID_COPIE = "CGIDEC";
    public static final String FIELDNAME_ID_TIERS_COPIE_A = "CGITIC";
    public static final String FIELDNAME_ID_TIERS_REQUERANT = "CGITIR";
    // BZ 5364
    public static final String FIELDNAME_REFERENCE = "CGLREF";
    // TODO Ajouter type de copie lorsqu'il y aura autre chose que des copies
    // pour les décisions !!!!!
    public static final String TABLE_COPIES = "CTCOPIES";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDebutCopie;
    private String dateFinCopie;
    private String domaineAppAdrCopie;
    private String idCopie;
    private String idTiersCopieA;
    private String idTiersRequerant;
    private String reference;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdCopie(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return CTCopies.TABLE_COPIES;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idCopie = statement.dbReadNumeric(CTCopies.FIELDNAME_ID_COPIE);
        idTiersCopieA = statement.dbReadNumeric(CTCopies.FIELDNAME_ID_TIERS_COPIE_A);
        dateDebutCopie = statement.dbReadDateAMJ(CTCopies.FIELDNAME_DATE_DEBUT_COPIES);
        dateFinCopie = statement.dbReadDateAMJ(CTCopies.FIELDNAME_DATE_FIN_COPIES);
        domaineAppAdrCopie = statement.dbReadNumeric(CTCopies.FIELDNAME_DOMAINE_APP_ADR_COPIE);
        idTiersRequerant = statement.dbReadNumeric(CTCopies.FIELDNAME_ID_TIERS_REQUERANT);
        // BZ 5364
        reference = statement.dbReadString(CTCopies.FIELDNAME_REFERENCE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        ;
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CTCopies.FIELDNAME_ID_COPIE,
                this._dbWriteNumeric(statement.getTransaction(), idCopie, "idCopie"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CTCopies.FIELDNAME_ID_COPIE,
                this._dbWriteNumeric(statement.getTransaction(), idCopie, "idCopie"));
        statement.writeField(CTCopies.FIELDNAME_ID_TIERS_COPIE_A,
                this._dbWriteNumeric(statement.getTransaction(), idTiersCopieA, "idTiersCopieA"));
        statement.writeField(CTCopies.FIELDNAME_DATE_DEBUT_COPIES,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebutCopie, "dateDebutCopie"));
        statement.writeField(CTCopies.FIELDNAME_DATE_FIN_COPIES,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFinCopie, "dateFinCopie"));
        statement.writeField(CTCopies.FIELDNAME_DOMAINE_APP_ADR_COPIE,
                this._dbWriteNumeric(statement.getTransaction(), domaineAppAdrCopie, "domaineAppAdrCopie"));
        statement.writeField(CTCopies.FIELDNAME_ID_TIERS_REQUERANT,
                this._dbWriteNumeric(statement.getTransaction(), idTiersRequerant, "idTiersRequerant"));
        // BZ 5364
        statement.writeField(CTCopies.FIELDNAME_REFERENCE,
                this._dbWriteString(statement.getTransaction(), reference, "reference"));
    }

    public String getDateDebutCopie() {
        return dateDebutCopie;
    }

    public String getDateFinCopie() {
        return dateFinCopie;
    }

    public String getDomaineAppAdrCopie() {
        return domaineAppAdrCopie;
    }

    public String getIdCopie() {
        return idCopie;
    }

    public String getIdTiersCopieA() {
        return idTiersCopieA;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    // BZ 5364
    public String getReference() {
        return reference;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    public void setDateDebutCopie(String dateDebutCopie) {
        this.dateDebutCopie = dateDebutCopie;
    }

    public void setDateFinCopie(String dateFinCopie) {
        this.dateFinCopie = dateFinCopie;
    }

    public void setDomaineAppAdrCopie(String domaineAppAdrCopie) {
        this.domaineAppAdrCopie = domaineAppAdrCopie;
    }

    public void setIdCopie(String idCopie) {
        this.idCopie = idCopie;
    }

    public void setIdTiersCopieA(String idTiersCopieA) {
        this.idTiersCopieA = idTiersCopieA;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

    // BZ 5364
    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean validate() {
        return false;
    }
}