package globaz.helios.db.classifications;

import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.CGLibelleInterface;
import globaz.helios.db.interfaces.ITreeListable;

public class CGClassification extends globaz.globall.db.BEntity implements ITreeListable, CGLibelleInterface,
        java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Classification
    public final static String CS_TYPE_AVS_COMPTE = "714003"; // Type
    public final static String CS_TYPE_AVS_SECTEUR = "714002"; // Type
    // Classification
    public final static String CS_TYPE_DOMAINE = "714004"; // Type
    public final static String CS_TYPE_MANUEL = "714001"; // Type Classification
    // Classification
    public final static String CS_TYPE_USAM = "714005"; // Type Classification
    private java.lang.String idClassification = new String();

    private java.lang.String idMandat = new String();
    private java.lang.String idTypeClassification = "";
    private Boolean isCreateDefaultDefinitionListe = new Boolean(true);
    private java.lang.String libelleDe = new String();
    private java.lang.String libelleFr = new String();

    private java.lang.String libelleIt = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CGClassification
     */
    public CGClassification() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_afterAdd(BTransaction)
     */
    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        super._afterAdd(transaction);

        // Si aucune définition de liste associé à cette classification, on en
        // créé une
        // par défaut
        if (isCreateDefaultDefinitionListe()) {
            CGDefinitionListeManager mgr = new CGDefinitionListeManager();
            mgr.setSession(getSession());
            mgr.setForIdClassification(getIdClassification());
            mgr.find(transaction, 2);
            if (mgr.size() == 0) {
                CGDefinitionListe defListe = new CGDefinitionListe();
                defListe.setSession(getSession());
                defListe.setIdClassification(getIdClassification());
                defListe.setLibelleDe(getLibelleDe());
                defListe.setLibelleFr(getLibelleFr());
                defListe.setLibelleIt(getLibelleIt());
                defListe.add(transaction);
            }
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_afterDelete(BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        super._afterDelete(transaction);

        // On supprime les définitions des listes liées à la classification
        CGDefinitionListeManager defListe = new CGDefinitionListeManager();
        defListe.setSession(getSession());
        defListe.setForIdClassification(getIdClassification());
        defListe.find(transaction);
        for (int i = 0; i < defListe.size(); i++) {
            CGDefinitionListe entity = (CGDefinitionListe) defListe.getEntity(i);
            entity.delete(transaction);
        }
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdClassification(_incCounter(transaction, "0"));
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        super._beforeDelete(transaction);

        CGClasseCompteManager mgr = new CGClasseCompteManager();
        mgr.setSession(getSession());
        mgr.setForIdClassification(getIdClassification());
        mgr.find(transaction, 2);
        if (mgr.size() > 0) {
            _addError(transaction, getSession().getLabel("CLASSIFICATION_CLASSE_CPT_NOT_EMPTY"));
        }

        // if
        // (!CGClassification.CS_TYPE_MANUEL.equals(getIdTypeClassification()))
        // {
        // _addError(transaction,
        // getSession().getLabel("CLASSIFICATION_TYPE_CLASSE_READ_ONLY"));
        // return;
        // }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGCLASP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idClassification = statement.dbReadNumeric("IDCLASSIFICATION");
        idMandat = statement.dbReadNumeric("IDMANDAT");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
        idTypeClassification = statement.dbReadNumeric("IDTYPECLASSIFICA");

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
        statement.writeKey("IDCLASSIFICATION", _dbWriteNumeric(statement.getTransaction(), getIdClassification(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDCLASSIFICATION",
                _dbWriteNumeric(statement.getTransaction(), getIdClassification(), "idClassification"));
        statement.writeField("IDMANDAT", _dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
        statement.writeField("LIBELLEFR", _dbWriteString(statement.getTransaction(), getLibelleFr(), "libelleFr"));
        statement.writeField("LIBELLEDE", _dbWriteString(statement.getTransaction(), getLibelleDe(), "libelleDe"));
        statement.writeField("LIBELLEIT", _dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleIt"));
        statement.writeField("IDTYPECLASSIFICA",
                _dbWriteNumeric(statement.getTransaction(), getIdTypeClassification(), "idTypeClassification"));
    }

    @Override
    public BManager[] getChilds() {

        CGDefinitionListeManager dlManager = new CGDefinitionListeManager();
        dlManager.setForIdClassification(getIdClassification());

        CGClasseCompteManager classeCompteManager = new CGClasseCompteManager();
        classeCompteManager.setForIdClassification(getIdClassification());
        classeCompteManager.setForIdSuperClasse("0");

        return new BManager[] { dlManager, classeCompteManager };

    }

    /**
     * Getter
     */
    public java.lang.String getIdClassification() {
        return idClassification;
    }

    public java.lang.String getIdMandat() {
        return idMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.10.2002 11:14:37)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTypeClassification() {
        return idTypeClassification;
    }

    @Override
    public java.lang.String getLibelle() {
        return CGLibelle.getLibelleUser(this);
    }

    @Override
    public java.lang.String getLibelleDe() {
        return libelleDe;
    }

    @Override
    public java.lang.String getLibelleFr() {
        return libelleFr;
    }

    @Override
    public java.lang.String getLibelleIt() {
        return libelleIt;
    }

    public String getMandatLibelle() {
        return CGMandat.getLibelle(getSession(), getIdMandat());
    }

    /**
     * Returns the isCreateDefaultDefinitionListe.
     * 
     * @return boolean
     */
    public boolean isCreateDefaultDefinitionListe() {
        return isCreateDefaultDefinitionListe.booleanValue();
    }

    /**
     * Setter
     */
    public void setIdClassification(java.lang.String newIdClassification) {
        idClassification = newIdClassification;
    }

    public void setIdMandat(java.lang.String newIdMandat) {
        idMandat = newIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.10.2002 11:14:37)
     * 
     * @param newIdTypeClassification
     *            java.lang.String
     */
    public void setIdTypeClassification(java.lang.String newIdTypeClassification) {
        idTypeClassification = newIdTypeClassification;
    }

    /**
     * Sets the isCreateDefaultDefinitionListe.
     * 
     * @param isCreateDefaultDefinitionListe
     *            The isCreateDefaultDefinitionListe to set
     */
    public void setIsCreateDefaultDefinitionListe(Boolean isCreateDefaultDefinitionListe) {
        this.isCreateDefaultDefinitionListe = isCreateDefaultDefinitionListe;
    }

    public void setLibelleDe(java.lang.String newLibelleDe) {
        libelleDe = newLibelleDe;
    }

    public void setLibelleFr(java.lang.String newLibelleFr) {
        libelleFr = newLibelleFr;
    }

    public void setLibelleIt(java.lang.String newLibelleIt) {
        libelleIt = newLibelleIt;
    }

}
