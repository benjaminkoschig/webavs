package globaz.helios.db.classifications;

import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.CGLibelleInterface;
import globaz.helios.db.interfaces.ITreeListable;
import globaz.jade.client.util.JadeStringUtil;

public class CGClasseCompte extends globaz.globall.db.BEntity implements ITreeListable, CGLibelleInterface,
        java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idClasseCompte = new String();
    private java.lang.String idClassification = new String();
    private String idDefinitionListe = new String();
    private java.lang.String idSuperClasse = new String();
    private java.lang.Boolean imprimerResultat = new Boolean(false);
    private java.lang.Boolean imprimerTitre = new Boolean(false);
    private java.lang.Boolean imprimerTotal = new Boolean(false);
    private java.lang.String libelleDe = new String();
    private java.lang.String libelleFr = new String();;
    private java.lang.String libelleIt = new String();;
    private java.lang.String noClasse = new String();

    private java.lang.String numeroOrdre = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CGClasseCompte
     */
    public CGClasseCompte() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdClasseCompte(this._incCounter(transaction, "0"));

    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        super._beforeDelete(transaction);

        CGClassification classification = new CGClassification();
        classification.setSession(getSession());
        classification.setIdClassification(getIdClassification());
        classification.retrieve(transaction);
        if (!CGClassification.CS_TYPE_MANUEL.equals(classification.getIdTypeClassification())) {

            _addError(transaction, getSession().getLabel("CLASSE_COMPTE_TYPE_CLASSE_READ_ONLY"));
            return;
        }

        // tous les fils doivent avoir été préalablement supprimé
        if (!"0".equals(getIdSuperClasse())) {
            CGClasseCompteManager mgr = new CGClasseCompteManager();
            mgr.setSession(getSession());
            mgr.setForIdSuperClasse(getIdClasseCompte());
            mgr.setForIdClassification(getIdClassification());
            mgr.find(transaction, 2);
            if (mgr.size() > 0) {
                _addError(transaction, getSession().getLabel("CLASSE_COMPTE_ENFANTS_EXISTENT"));
                return;
            }
        }

        // On supprime les liens entre comptes et classes de comptes
        CGLiaisonCompteClasseManager mgr2 = new CGLiaisonCompteClasseManager();
        mgr2.setSession(getSession());
        mgr2.setForIdClasseCompte(getIdClasseCompte());
        mgr2.find(transaction, BManager.SIZE_NOLIMIT);
        for (int i = 0; i < mgr2.size(); i++) {
            CGLiaisonCompteClasse entity = (CGLiaisonCompteClasse) mgr2.getEntity(i);
            entity.delete(transaction);
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGCLCOP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idClasseCompte = statement.dbReadNumeric("IDCLASSECOMPTE");
        idClassification = statement.dbReadNumeric("IDCLASSIFICATION");
        idSuperClasse = statement.dbReadNumeric("IDSUPERCLASSE");
        // idSousClasse = statement.dbReadString("IDSOUSCLASSE");
        noClasse = statement.dbReadString("NOCLASSE");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
        numeroOrdre = statement.dbReadNumeric("NUMEROORDRE");
        imprimerTitre = statement.dbReadBoolean("IMPRIMERTITRE");
        imprimerTotal = statement.dbReadBoolean("IMPRIMERTOTAL");
        imprimerResultat = statement.dbReadBoolean("IMPRIMERRESULTAT");

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {

        // L'id de la super classe est obligatoire
        if (JadeStringUtil.isBlank(getIdSuperClasse())) {
            _addError(statement.getTransaction(), getSession().getLabel("CLASSE_COMPTE_ID_PARENT_ERROR"));
            return;
        }

        // Si pas de racine...
        if (!"0".equals(getIdSuperClasse())) {
            CGClasseCompteManager mgr = new CGClasseCompteManager();
            mgr.setSession(getSession());
            mgr.setForIdClasseCompte(getIdSuperClasse());
            mgr.setForIdClassification(getIdClassification());

            mgr.find(statement.getTransaction(), 2);
            if (mgr.size() == 0) {
                _addError(statement.getTransaction(), getSession().getLabel("CLASSE_COMPTE_PARENT_NOT_FOUND"));
                return;
            }
        }

        // Si pas de racine...
        if (!"0".equals(getIdSuperClasse())) {
            CGClasseCompte cc = new CGClasseCompte();
            cc.setSession(getSession());
            cc.setIdClasseCompte(getIdSuperClasse());

            cc.retrieve(statement.getTransaction());
            if ((cc == null) || cc.isNew()) {
                _addError(statement.getTransaction(), getSession().getLabel("CLASSE_COMPTE_PARENT_NOT_FOUND"));
                return;
            }
        }

        if (JadeStringUtil.isBlank(getLibelle())) {
            _addError(statement.getTransaction(), getSession().getLabel("CLASSE_COMPTE_LIBELLE_EMPTY"));
            return;
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDCLASSECOMPTE", this._dbWriteNumeric(statement.getTransaction(), getIdClasseCompte(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDCLASSECOMPTE",
                this._dbWriteNumeric(statement.getTransaction(), getIdClasseCompte(), "idClasseCompte"));
        statement.writeField("IDCLASSIFICATION",
                this._dbWriteNumeric(statement.getTransaction(), getIdClassification(), "idClassification"));
        // statement.writeField("IDSOUSCLASSE",_dbWriteString(statement.getTransaction(),
        // getIdSousClasse(),"idSousClasse"));
        statement.writeField("IDSUPERCLASSE",
                this._dbWriteNumeric(statement.getTransaction(), getIdSuperClasse(), "idSuperClasse"));
        statement.writeField("LIBELLEFR", this._dbWriteString(statement.getTransaction(), getLibelleFr(), "libelleFr"));
        statement.writeField("LIBELLEDE", this._dbWriteString(statement.getTransaction(), getLibelleDe(), "libelleDe"));
        statement.writeField("LIBELLEIT", this._dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleIt"));
        statement.writeField("NOCLASSE", this._dbWriteString(statement.getTransaction(), getNoClasse(), "noClasse"));
        statement.writeField("NUMEROORDRE",
                this._dbWriteNumeric(statement.getTransaction(), getNumeroOrdre(), "numeroOrdre"));
        statement.writeField("IMPRIMERTITRE", this._dbWriteBoolean(statement.getTransaction(), isImprimerTitre(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "imprimerTitre"));
        statement.writeField("IMPRIMERTOTAL", this._dbWriteBoolean(statement.getTransaction(), isImprimerTotal(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "imprimerTotal"));
        statement.writeField("IMPRIMERRESULTAT", this._dbWriteBoolean(statement.getTransaction(), isImprimerTotal(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "imprimerResultat"));
    }

    @Override
    public BManager[] getChilds() {
        CGClasseCompteManager classeCompteManager = new CGClasseCompteManager();
        classeCompteManager.setForIdSuperClasse(getIdClasseCompte());
        CGLiaisonCompteClasse_PlanComptableManager liaisonManager = new CGLiaisonCompteClasse_PlanComptableManager();
        liaisonManager.setForIdClasseCompte(getIdClasseCompte());
        return new BManager[] { classeCompteManager, liaisonManager };
    }

    /**
     * Getter
     */
    public java.lang.String getIdClasseCompte() {
        return idClasseCompte;
    }

    public java.lang.String getIdClassification() {
        return idClassification;
    }

    /**
     * Returns the idDefinitionListe.
     * 
     * @return String
     */
    public String getIdDefinitionListe() {
        return idDefinitionListe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 17:21:56)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdSuperClasse() {
        return idSuperClasse;
    }

    @Override
    public String getLibelle() {
        return CGLibelle.getLibelleApp(this);
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

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 15:45:51)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNoClasse() {
        return noClasse;
    }

    public java.lang.String getNumeroOrdre() {
        return numeroOrdre;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 15:43:56)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean isImprimerResultat() {
        return imprimerResultat;
    }

    public java.lang.Boolean isImprimerTitre() {
        return imprimerTitre;
    }

    public java.lang.Boolean isImprimerTotal() {
        return imprimerTotal;
    }

    public boolean isLeaf() throws Exception {

        CGClasseCompteManager classeCompteManager = new CGClasseCompteManager();
        classeCompteManager.setSession(getSession());
        classeCompteManager.setForIdSuperClasse(getIdClasseCompte());
        classeCompteManager.find(null, 1);
        if (classeCompteManager.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Setter
     */
    public void setIdClasseCompte(java.lang.String newIdClasseCompte) {
        idClasseCompte = newIdClasseCompte;
    }

    public void setIdClassification(java.lang.String newIdClassification) {
        idClassification = newIdClassification;
    }

    /**
     * Sets the idDefinitionListe.
     * 
     * @param idDefinitionListe
     *            The idDefinitionListe to set
     */
    public void setIdDefinitionListe(String idDefinitionListe) {
        this.idDefinitionListe = idDefinitionListe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 17:21:56)
     * 
     * @param newIdSuperClasse
     *            java.lang.String
     */
    public void setIdSuperClasse(java.lang.String newIdSuperClasse) {
        idSuperClasse = newIdSuperClasse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 15:43:56)
     * 
     * @param newImprimerResultat
     *            java.lang.Boolean
     */
    public void setImprimerResultat(java.lang.Boolean newImprimerResultat) {
        imprimerResultat = newImprimerResultat;
    }

    public void setImprimerTitre(java.lang.Boolean newImprimerTitre) {
        imprimerTitre = newImprimerTitre;
    }

    public void setImprimerTotal(java.lang.Boolean newImprimerTotal) {
        imprimerTotal = newImprimerTotal;
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

    /**
     * Insérez la description de la méthode ici. Date de création : (16.09.2002 15:45:51)
     * 
     * @param newNoClasse
     *            java.lang.String
     */
    public void setNoClasse(java.lang.String newNoClasse) {
        noClasse = newNoClasse;
    }

    public void setNumeroOrdre(java.lang.String newNumeroOrdre) {
        numeroOrdre = newNumeroOrdre;
    }
}
