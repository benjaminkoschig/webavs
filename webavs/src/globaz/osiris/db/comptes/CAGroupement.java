package globaz.osiris.db.comptes;

import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.parameters.FWParametersUserCode;

/**
 * Insérez la description du type ici. Date de création : (11.12.2001 16:09:49)
 * 
 * @author: Administrator
 */
public class CAGroupement extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CUSTOM = "213003";
    public final static String MASTER = "213001";
    public final static String SERIE = "213002";
    private FWParametersSystemCodeManager csTypeGroupements;
    private java.lang.String idGroupement = new String();
    private java.lang.String idOperationMaster = new String();
    private java.lang.String typeGroupement = new String();
    private FWParametersUserCode ucTypeGroupement;

    // code systeme

    /**
     * Commentaire relatif au constructeur CAGroupement
     */
    public CAGroupement() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2002 13:25:58)
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {

        // incrémente le prochain numéro
        setIdGroupement(this._incCounter(transaction, idGroupement));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2002 13:28:06)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Vérifier l'existance de fils dans CAGroupementOperationManager
        if (hasOperations(transaction)) {
            _addError(transaction, getSession().getLabel("7002"));
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CAGROUP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idGroupement = statement.dbReadNumeric("IDGROUPEMENT");
        idOperationMaster = statement.dbReadNumeric("IDOPERATIONMASTER");
        typeGroupement = statement.dbReadNumeric("TYPEGROUPEMENT");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdGroupement(), getSession().getLabel("7320"));
        _propertyMandatory(statement.getTransaction(), getTypeGroupement(), getSession().getLabel("7321"));

        // Vérifier le type de groupement
        if (getCsTypeGroupements().getCodeSysteme(getTypeGroupement()) == null) {
            _addError(statement.getTransaction(), getSession().getLabel("7322"));
        }

        // Id opération obligatoire pour master
        if (getTypeGroupement().equals(CAGroupement.MASTER)) {
            _propertyMandatory(statement.getTransaction(), getIdOperationMaster(), getSession().getLabel("7323"));
        }

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDGROUPEMENT", this._dbWriteNumeric(statement.getTransaction(), getIdGroupement(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDGROUPEMENT",
                this._dbWriteNumeric(statement.getTransaction(), getIdGroupement(), "idGroupement"));
        statement.writeField("IDOPERATIONMASTER",
                this._dbWriteNumeric(statement.getTransaction(), getIdOperationMaster(), "idOperationMaster"));
        statement.writeField("TYPEGROUPEMENT",
                this._dbWriteNumeric(statement.getTransaction(), getTypeGroupement(), "typeGroupement"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2002 16:38:30)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsTypeGroupements() {
        // liste déjà chargée ?
        if (csTypeGroupements == null) {
            // liste pas encore chargée, on la charge
            csTypeGroupements = new FWParametersSystemCodeManager();
            csTypeGroupements.setSession(getSession());
            csTypeGroupements.getListeCodesSup("OSITYPGRP", getSession().getIdLangue());
        }
        return csTypeGroupements;
    }

    /**
     * Getter
     */
    public java.lang.String getIdGroupement() {
        return idGroupement;
    }

    public java.lang.String getIdOperationMaster() {
        return idOperationMaster;
    }

    public java.lang.String getTypeGroupement() {
        return typeGroupement;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.01.2002 16:36:04)
     * 
     * @return globaz.globall.parameters.FWParametersUserCode
     */
    public FWParametersUserCode getUcTypeGroupement() {

        if (ucTypeGroupement == null) {
            // liste pas encore chargee, on la charge
            ucTypeGroupement = new FWParametersUserCode();
            ucTypeGroupement.setSession(getSession());

            // Récupérer le code système dans la langue de l'utilisateur
            ucTypeGroupement.setIdCodeSysteme(getTypeGroupement());
            ucTypeGroupement.setIdLangue(getSession().getIdLangue());
            try {
                ucTypeGroupement.retrieve();
                if (ucTypeGroupement.isNew() || ucTypeGroupement.hasErrors()) {
                    _addError(null, getSession().getLabel("7324"));
                }
            } catch (Exception e) {
                _addError(null, getSession().getLabel("7324"));
            }
        }

        return ucTypeGroupement;
    }

    /**
     * Retourne vrai si le groupement contient des opérations Date de création : (05.12.2002 11:46:57)
     * 
     * @return boolean vrai si le groupement contient des opérations
     * @param transaction
     *            globaz.globall.db.BTransaction la transaction
     */
    public boolean hasOperations(globaz.globall.db.BTransaction transaction) throws Exception {
        CAGroupementOperationManager mgr = new CAGroupementOperationManager();
        mgr.setSession(getSession());
        mgr.setForIdGroupement(getIdGroupement());
        return mgr.getCount(transaction) != 0;

    }

    /**
     * Setter
     */
    public void setIdGroupement(java.lang.String newIdGroupement) {
        idGroupement = newIdGroupement;
    }

    public void setIdOperationMaster(java.lang.String newIdOperationMaster) {
        idOperationMaster = newIdOperationMaster;
    }

    public void setTypeGroupement(java.lang.String newTypeGroupement) {
        typeGroupement = newTypeGroupement;
    }
}
