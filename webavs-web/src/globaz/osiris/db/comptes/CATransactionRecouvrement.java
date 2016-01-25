package globaz.osiris.db.comptes;

import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.ordres.CAOrganeExecution;

/**
 * Insérez la description du type ici. Date de création : (14.02.2002 10:35:52)
 * 
 * @author: Administrator
 */

public class CATransactionRecouvrement extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private globaz.osiris.db.ordres.CAOrganeExecution _organeExecution = null;
    private String codeRefus = new String();
    private java.lang.String dateEcheance = new String();

    private java.lang.String genreTransaction = new String();
    private java.lang.String idOperation = new String();
    private java.lang.String idOrganeExecution = new String();
    private java.lang.String monnaieISO = new String();

    private java.lang.String reference = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CATransactionBVR
     */
    public CATransactionRecouvrement() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CAOPRDP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idOperation = statement.dbReadNumeric("IDOPERATION");
        genreTransaction = statement.dbReadString("GENRETRANSACTION");
        reference = statement.dbReadString("REFERENCE");
        dateEcheance = statement.dbReadDateAMJ("DATEECHEANCE");
        monnaieISO = statement.dbReadString("MONNAIEISO");
        idOrganeExecution = statement.dbReadNumeric("IDORGANEEXECUTION");
        codeRefus = statement.dbReadNumeric("CODEREFUS");
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
        statement.writeField("REFERENCE", this._dbWriteString(statement.getTransaction(), getReference(), "reference"));
        statement.writeField("DATEECHEANCE",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateEcheance(), "dateEcheance"));
        statement.writeField("MONNAIEISO",
                this._dbWriteString(statement.getTransaction(), getMonnaieISO(), "monnaieISO"));
        statement.writeField("IDORGANEEXECUTION",
                this._dbWriteNumeric(statement.getTransaction(), getIdOrganeExecution(), "idOrganeExecution"));
        statement
                .writeField("CODEREFUS", this._dbWriteNumeric(statement.getTransaction(), getCodeRefus(), "codeRefus"));
    }

    /**
     * @author: sel Créé le : 28 sept. 06
     * @return
     */
    public String getCodeRefus() {
        return codeRefus;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.12.2002 10:02:35)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateEcheance() {
        return dateEcheance;
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

    public java.lang.String getReference() {
        return reference;
    }

    /**
     * @author: sel Créé le : 28 sept. 06
     * @param string
     */
    public void setCodeRefus(String newCodeRefus) {
        codeRefus = newCodeRefus;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.12.2002 10:02:35)
     * 
     * @param newDateEcheance
     *            java.lang.String
     */
    public void setDateEcheance(java.lang.String newDateEcheance) {
        dateEcheance = newDateEcheance;
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

    public void setReference(java.lang.String newReference) {
        reference = newReference;
    }

}
