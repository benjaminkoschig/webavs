/*
 * Créé le 14 avril 2010
 */
package globaz.cygnus.db.attestations;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * author fha
 */
public class RFAttestation extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_CREATION = "EYDCRE";
    public static final String FIELDNAME_ID_ATTESTATION = "EYIATT";
    public static final String FIELDNAME_ID_GESTIONNAIRE = "EYIGES";
    public static final String FIELDNAME_NIVEAU_AVERTISSEMENT = "EYTNIV";
    public static final String FIELDNAME_TYPE_DOCUMENT = "EYTDOC";

    public static final String TABLE_NAME = "RFATTES";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public static final RFAttestation loadAttestation(BSession session, BITransaction transaction, String idAttestation)
            throws Exception {
        RFAttestation retValue;

        retValue = new RFAttestation();
        retValue.setIdAttestation(idAttestation);
        retValue.setSession(session);

        if (transaction == null) {
            retValue.retrieve();
        } else {
            retValue.retrieve(transaction);
        }

        return retValue;
    }

    private String dateCreation = "";
    private String idAttestation = "";
    private String idGestionnaire = "";
    private String niveauAvertissement = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private String typeDocument = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RFDossiers.
     */
    public RFAttestation() {
        super();
    }

    /**
     * Méthode avant l'ajout l'incrémentation de la clé primaire
     * 
     * @param transaction
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdAttestation(this._incCounter(transaction, "0"));
    }

    /**
     * getter pour le nom de la table des dossiers
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return RFAttestation.TABLE_NAME;
    }

    /**
     * Lecture des propriétés dans les champs de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAttestation = statement.dbReadNumeric(RFAttestation.FIELDNAME_ID_ATTESTATION);
        niveauAvertissement = statement.dbReadNumeric(RFAttestation.FIELDNAME_NIVEAU_AVERTISSEMENT);
        idGestionnaire = statement.dbReadString(RFAttestation.FIELDNAME_ID_GESTIONNAIRE);
        dateCreation = statement.dbReadDateAMJ(RFAttestation.FIELDNAME_DATE_CREATION);
        typeDocument = statement.dbReadNumeric(RFAttestation.FIELDNAME_TYPE_DOCUMENT);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * Définition de la clé primaire de la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFAttestation.FIELDNAME_ID_ATTESTATION,
                this._dbWriteNumeric(statement.getTransaction(), idAttestation, "idAttestation"));

    }

    /**
     * Méthode d'écriture des champs dans la table des dossiers
     * 
     * @param statement
     * @throws Exception
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RFAttestation.FIELDNAME_ID_ATTESTATION,
                this._dbWriteNumeric(statement.getTransaction(), idAttestation, "idAttestation"));
        statement.writeField(RFAttestation.FIELDNAME_NIVEAU_AVERTISSEMENT,
                this._dbWriteNumeric(statement.getTransaction(), niveauAvertissement, "niveauAvertissement"));
        statement.writeField(RFAttestation.FIELDNAME_ID_GESTIONNAIRE,
                this._dbWriteString(statement.getTransaction(), idGestionnaire, "idGestionnaire"));
        statement.writeField(RFAttestation.FIELDNAME_DATE_CREATION,
                this._dbWriteDateAMJ(statement.getTransaction(), dateCreation, "dateCreation"));
        statement.writeField(RFAttestation.FIELDNAME_TYPE_DOCUMENT,
                this._dbWriteNumeric(statement.getTransaction(), typeDocument, "typeDocument"));
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getIdAttestation() {
        return idAttestation;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getNiveauAvertissement() {
        return niveauAvertissement;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setIdAttestation(String idAttestation) {
        this.idAttestation = idAttestation;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setNiveauAvertissement(String niveauAvertissement) {
        this.niveauAvertissement = niveauAvertissement;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

}