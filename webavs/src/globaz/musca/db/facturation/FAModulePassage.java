package globaz.musca.db.facturation;

import globaz.globall.db.BConstants;

public class FAModulePassage extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_ACTION_COMPTABILISE = "903004";
    public final static String CS_ACTION_ERREUR_COMPTA = "903007";
    public final static String CS_ACTION_ERREUR_GEN = "903006";
    public final static String CS_ACTION_GENERE = "903002";
    public final static String CS_ACTION_IMPRIMER = "903003";
    /*
     * IMPORTANT Ces codes systèmes sont en fait des ETATS du Module de passage et plus des actions Changement avril
     * 2005 RRI
     */
    public final static String CS_ACTION_SUPPRIMER = "903001";
    public final static String CS_ACTION_VIDE = "903005";

    public final static java.lang.String TABLE_FIELDS = "FAMOPAP.IDPASSAGE, FAMOPAP.IDMODFAC, FAMOPAP.ESTGENERE, FAMOPAP.IDACTION, FAMOPAP.PSPY, FAMOPAP.IDPLAN";
    private java.lang.String dateFacturation;
    private java.lang.Boolean estGenere = new Boolean(false);
    private java.lang.String idAction = new String();
    private java.lang.String idModuleFacturation = new String();
    private java.lang.String idPassage = new String();
    private java.lang.String idPlan = new String();
    private java.lang.String idTypeModule = new String();
    private java.lang.String libelleDe = new String();
    private java.lang.String libelleFr = new String();
    private java.lang.String libelleIt = new String();
    private java.lang.String niveauAppel = new String();
    private java.lang.String nomClasse = new String();

    /**
     * Commentaire relatif au constructeur FAModulePassage
     */
    public FAModulePassage() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Test si module déjà présent dans le plan
        FAModulePassage modPassage = new FAModulePassage();
        modPassage.setSession(getSession());
        modPassage.setIdModuleFacturation(getIdModuleFacturation());
        modPassage.setIdPassage(getIdPassage());
        modPassage.retrieve();
        if (modPassage.isNew() == false) {
            _addError(transaction, getSession().getLabel("EXISTE_MODULE_PASSAGE"));
        }
    }

    /*
     * Traitement avant suppression
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Suppression seulement si le module n'existe pas dans les afacts
        FAAfactManager afactManager = new FAAfactManager();
        afactManager.setForIdModuleFacturation(getIdModuleFacturation());
        afactManager.setForIdPassage(getIdPassage());
        afactManager.setSession(transaction.getSession());
        try {
            afactManager.find(transaction);
            if (afactManager.size() > 0) {
                FAModuleFacturation moduleFacturation = new FAModuleFacturation();
                moduleFacturation.setIdModuleFacturation(getIdModuleFacturation());
                moduleFacturation.setISession(transaction.getSession());
                moduleFacturation.retrieve();
                // BTC:15.11.2005: temporaire: Il est posssible de supprimer le
                // module cot. per.
                // car on peut le changer et en mettre un autre
                if (((moduleFacturation != null) && (!moduleFacturation.isNew()))) {
                    if (!FAModuleFacturation.CS_MODULE_COT_PERS.equals(moduleFacturation.getIdTypeModule())
                            && !FAModuleFacturation.CS_MODULE_COT_PERS_NAC.equals(moduleFacturation.getIdTypeModule())
                            && !FAModuleFacturation.CS_MODULE_COT_PERS_IND.equals(moduleFacturation.getIdTypeModule())) {
                        _addError(transaction,
                                "Il existe des afacts pour ce module dans ce passage, suppression impossible. ");
                    }
                }

            }
        } catch (Exception e) {
            _addError(transaction, "Erreur lors du contrôle de la suppression. ");
        }
        // Suppression si estGenere est false
        if (isEstGenere().booleanValue()) {
            _addError(transaction, "Le module a été généré, suppression impossible. ");
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "FAMOPAP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idPassage = statement.dbReadNumeric("IDPASSAGE");
        idModuleFacturation = statement.dbReadNumeric("IDMODFAC");
        estGenere = statement.dbReadBoolean("ESTGENERE");
        idAction = statement.dbReadNumeric("IDACTION");
        idPlan = statement.dbReadNumeric("IDPLAN");
        // Champs appartenant - FAMODUP (manger join)
        nomClasse = statement.dbReadString("NOMCLASSE");
        niveauAppel = statement.dbReadNumeric("NIVEAUAPPEL");
        idTypeModule = statement.dbReadString("IDTYPEMODULE");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // Voir aussi validate() du viewBean pour le contrôle de l'action en cas
        // de mutation

        // Action Supprimer non admise pour type de module STANDART et LISTE.
        // Récupération du type du module
        if (!globaz.jade.client.util.JadeStringUtil.isBlank(getIdModuleFacturation())) {
            FAModuleFacturation modFac = new FAModuleFacturation();
            modFac.setSession(statement.getTransaction().getSession());
            modFac.setIdModuleFacturation(getIdModuleFacturation());
            try {
                modFac.retrieve(statement.getTransaction());

                if ((modFac.getIdTypeModule().equalsIgnoreCase(FAModuleFacturation.CS_MODULE_STANDARD))
                        || (modFac.getIdTypeModule().equalsIgnoreCase(FAModuleFacturation.CS_MODULE_LISTE))) {
                    if (getIdAction().equalsIgnoreCase(FAModulePassage.CS_ACTION_SUPPRIMER)) {
                        _addError(statement.getTransaction(),
                                "L'action SUPPRIMER est impossible pour les modules de facturation de type STANDARD ou LISTE. ");
                    }
                }
            } catch (Exception e) {
                _addError(statement.getTransaction(),
                        "Erreur lors de l'extraction des données du module de facturation. ");
            }
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDPASSAGE", this._dbWriteNumeric(statement.getTransaction(), getIdPassage(), ""));
        statement.writeKey("IDMODFAC", this._dbWriteNumeric(statement.getTransaction(), getIdModuleFacturation(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement
                .writeField("IDPASSAGE", this._dbWriteNumeric(statement.getTransaction(), getIdPassage(), "idPassage"));
        statement.writeField("IDMODFAC",
                this._dbWriteNumeric(statement.getTransaction(), getIdModuleFacturation(), "idModuleFacturation"));
        statement.writeField("ESTGENERE", this._dbWriteBoolean(statement.getTransaction(), isEstGenere(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "estGenere"));
        statement.writeField("IDACTION", this._dbWriteNumeric(statement.getTransaction(), getIdAction(), "idAction"));
        statement.writeField("IDPLAN", this._dbWriteNumeric(statement.getTransaction(), getIdPlan(), "idPlan"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 08:23:35)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateFacturation() {
        return dateFacturation;
    }

    public java.lang.String getIdAction() {
        return idAction;
    }

    public java.lang.String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    /**
     * Getter
     */
    public java.lang.String getIdPassage() {
        return idPassage;
    }

    public java.lang.String getIdPlan() {
        return idPlan;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.04.2003 15:13:58)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTypeModule() {
        return idTypeModule;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2003 17:12:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelle() {
        String langue = new String();

        langue = getSession().getIdLangueISO();

        if (langue.equalsIgnoreCase("fr")) {

            return libelleFr;
        }
        if (langue.equalsIgnoreCase("de")) {

            return libelleDe;
        } else {

            return libelleIt;
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2003 17:12:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleAction() {
        return getSession().getCodeLibelle(idAction);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2003 17:12:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleDe() {
        return libelleDe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2003 17:12:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleFr() {
        return libelleFr;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2003 17:12:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleIt() {
        return libelleIt;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:13:30)
     * 
     * @return java.lang.String
     */
    public String getNiveauAppel() {
        return niveauAppel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:14:03)
     * 
     * @return java.lang.String
     */
    public String getNomClasse() {
        return nomClasse;
    }

    public java.lang.Boolean isEstGenere() {
        return estGenere;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 08:23:35)
     * 
     * @param newDateFacturation
     *            java.lang.String
     */
    public void setDateFacturation(java.lang.String newDateFacturation) {
        dateFacturation = newDateFacturation;
    }

    public void setEstGenere(java.lang.Boolean newEstGenere) {
        estGenere = newEstGenere;
    }

    public void setIdAction(java.lang.String newIdAction) {
        idAction = newIdAction;
    }

    public void setIdModuleFacturation(java.lang.String newIdModuleFacturation) {
        idModuleFacturation = newIdModuleFacturation;
    }

    /**
     * Setter
     */
    public void setIdPassage(java.lang.String newIdPassage) {
        idPassage = newIdPassage;
    }

    public void setIdPlan(java.lang.String idPlan) {
        this.idPlan = idPlan;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.04.2003 15:13:58)
     * 
     * @param newIdTypeModule
     *            java.lang.String
     */
    public void setIdTypeModule(java.lang.String newIdTypeModule) {
        idTypeModule = newIdTypeModule;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:13:30)
     * 
     * @return java.lang.String
     */
    public void setNiveauAppel(String niveauAppel) {
        this.niveauAppel = niveauAppel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.03.2003 15:14:03)
     * 
     * @return java.lang.String
     */
    public void setNomClasse(String nomClasse) {
        this.nomClasse = nomClasse;
    }
}
