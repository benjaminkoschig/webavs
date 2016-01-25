package globaz.osiris.db.comptes;

import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.ordres.CAOrganeExecution;

/**
 * Insérez la description du type ici. Date de création : (14.02.2002 10:35:52)
 * 
 * @author: Administrator
 */

public class CATransactionBVR extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private globaz.osiris.db.ordres.CAOrganeExecution _organeExecution = null;
    private java.lang.String dateDepot = new String();
    private java.lang.String dateInscription = new String();
    private java.lang.String dateTraitement = new String();
    private java.lang.String genreTransaction = new String();
    private java.lang.String idOperation = new String();
    private java.lang.String idOrganeExecution = new String();
    private java.lang.String monnaieISO = new String();
    private java.lang.String referenceBVR = new String();
    private java.lang.String referenceInterne = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CATransactionBVR
     */
    public CATransactionBVR() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CAOPBVP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idOperation = statement.dbReadNumeric("IDOPERATION");
        genreTransaction = statement.dbReadString("GENRETRANSACTION");
        referenceBVR = statement.dbReadString("REFERENCEBVR");
        dateDepot = statement.dbReadDateAMJ("DATEDEPOT");
        dateTraitement = statement.dbReadDateAMJ("DATETRAITEMENT");
        dateInscription = statement.dbReadDateAMJ("DATEINSCRIPTION");
        referenceInterne = statement.dbReadString("REFERENCEINTERNE");
        monnaieISO = statement.dbReadString("MONNAIEISO");
        idOrganeExecution = statement.dbReadNumeric("IDORGANEEXECUTION");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdOperation(), getSession().getLabel("7104"));
        _propertyMandatory(statement.getTransaction(), getIdOrganeExecution(), getSession().getLabel("7205"));

        // Vérifier l'organe d'exécution
        if (getIdOrganeExecution() == null) {
            _addError(
                    statement.getTransaction(),
                    CAApplication
                            .getApplicationOsiris()
                            .getCAParametres()
                            .getMessage(statement.getTransaction().getSession().getApplicationId(), "5159",
                                    statement.getTransaction().getSession().getIdLangueISO()));
        }

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDOPERATION", this._dbWriteNumeric(statement.getTransaction(), getIdOperation(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDOPERATION",
                this._dbWriteNumeric(statement.getTransaction(), getIdOperation(), "idOperation"));
        statement.writeField("GENRETRANSACTION",
                this._dbWriteString(statement.getTransaction(), getGenreTransaction(), "genreTransaction"));
        statement.writeField("REFERENCEBVR",
                this._dbWriteString(statement.getTransaction(), getReferenceBVR(), "referenceBVR"));
        statement
                .writeField("DATEDEPOT", this._dbWriteDateAMJ(statement.getTransaction(), getDateDepot(), "dateDepot"));
        statement.writeField("DATETRAITEMENT",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateTraitement(), "dateTraitement"));
        statement.writeField("DATEINSCRIPTION",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateInscription(), "dateInscription"));
        statement.writeField("REFERENCEINTERNE",
                this._dbWriteString(statement.getTransaction(), getReferenceInterne(), "referenceInterne"));
        statement.writeField("MONNAIEISO",
                this._dbWriteString(statement.getTransaction(), getMonnaieISO(), "monnaieISO"));
        statement.writeField("IDORGANEEXECUTION",
                this._dbWriteNumeric(statement.getTransaction(), getIdOrganeExecution(), "idOrganeExecution"));
    }

    public java.lang.String getDateDepot() {
        return dateDepot;
    }

    public java.lang.String getDateInscription() {
        return dateInscription;
    }

    public java.lang.String getDateTraitement() {
        return dateTraitement;
    }

    public java.lang.String getGenreTransaction() {
        return genreTransaction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 10:38:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdOperation() {
        return idOperation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 11:14:19)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public java.lang.String getMonnaieISO() {
        return monnaieISO;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.02.2002 13:04:26)
     * 
     * @return globaz.osiris.db.ordres.CAOrdreGroupe
     */
    public CAOrganeExecution getOrganeExecution() {

        // Si l'ordre groupé n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(getIdOrganeExecution())) {
            return null;
        }

        // Si pas déjà chargé
        if (_organeExecution == null) {
            // Instancier un nouvel organe d'exécution
            _organeExecution = new CAOrganeExecution();
            _organeExecution.setSession(getSession());

            // Récupérer le log en question
            _organeExecution.setIdOrganeExecution(getIdOrganeExecution());
            try {
                _organeExecution.retrieve();
                if (_organeExecution.isNew() || _organeExecution.hasErrors()) {
                    _organeExecution = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _organeExecution = null;
            }
        }

        return _organeExecution;
    }

    public java.lang.String getReferenceBVR() {
        return referenceBVR;
    }

    public java.lang.String getReferenceInterne() {
        return referenceInterne;
    }

    public void setDateDepot(java.lang.String newDateDepot) {
        dateDepot = newDateDepot;
    }

    public void setDateInscription(java.lang.String newDateInscription) {
        dateInscription = newDateInscription;
    }

    public void setDateTraitement(java.lang.String newDateTraitement) {
        dateTraitement = newDateTraitement;
    }

    public void setGenreTransaction(java.lang.String newGenreTransaction) {
        genreTransaction = newGenreTransaction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 10:38:07)
     * 
     * @param newIdOperation
     *            java.lang.String
     */
    public void setIdOperation(java.lang.String newIdOperation) {
        idOperation = newIdOperation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 11:14:19)
     * 
     * @param newIdOrganeExecution
     *            java.lang.String
     */
    public void setIdOrganeExecution(java.lang.String newIdOrganeExecution) {
        idOrganeExecution = newIdOrganeExecution;
    }

    public void setMonnaieISO(java.lang.String newMonnaieISO) {
        monnaieISO = newMonnaieISO;
    }

    public void setReferenceBVR(java.lang.String newReferenceBVR) {
        referenceBVR = newReferenceBVR;
    }

    public void setReferenceInterne(java.lang.String newReferenceInterne) {
        referenceInterne = newReferenceInterne;
    }
}
