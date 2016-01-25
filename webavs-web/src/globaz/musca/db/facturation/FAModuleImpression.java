package globaz.musca.db.facturation;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class FAModuleImpression extends globaz.globall.db.BEntity implements globaz.musca.api.IFAModuleImpression,
        java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_AUCUN_RECOUVREMENT = "907001"; // mode de recouvrement
    public final static String CS_BVR = "907002"; // mode de recouvrement
    public final static String CS_DECOMPTE_INTERNE = "906001"; // Critère décompte
    public final static String CS_DECOMPTE_POSITIF = "906002"; // Critère décompte
    public final static String CS_DECOMPTE_ZERO = "906004"; // Critère décompte
    public final static String CS_NOTE_CREDIT = "906003"; // Critère décompte
    public final static String CS_RECOUVREMENT_DIRECT = "907004"; // mode de recouvrement
    public final static String CS_REMBOURSEMENT = "907003"; // mode de recouvrement

    public final static java.lang.String TABLE_FIELDS = "FAMOIMP.IDMODULEIMPRESSION, FAMOIMP.LIBELLEFR, FAMOIMP.LIBELLEDE, FAMOIMP.LIBELLEIT, "
            + "FAMOIMP.IDCRITEREDECOMPTE, FAMOIMP.IDMODERECOUVREMENT, FAMOIMP.NOMCLASSE, FAMOIMP.PSPY";
    private java.lang.String idCritereDecompte = new String();
    private java.lang.String idModeRecouvrement = new String();
    private java.lang.String idModuleImpression = new String();
    private java.lang.String libelleDe = new String();
    private java.lang.String libelleFr = new String();
    private java.lang.String libelleIt = new String();
    private java.lang.String libelleMode = new String();
    private java.lang.String libelleType = new String();
    private java.lang.String nomClasse = new String();

    /**
     * Commentaire relatif au constructeur FAModuleImpression
     */
    public FAModuleImpression() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdModuleImpression(this._incCounter(transaction, idModuleImpression));
        // setIdModuleImpression(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FAModuleImpression.TABLE_FIELDS;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "FAMOIMP AS FAMOIMP ";
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "FAMOIMP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idModuleImpression = statement.dbReadNumeric("IDMODULEIMPRESSION");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
        idCritereDecompte = statement.dbReadNumeric("IDCRITEREDECOMPTE");
        idModeRecouvrement = statement.dbReadNumeric("IDMODERECOUVREMENT");
        nomClasse = statement.dbReadString("NOMCLASSE");
        libelleMode = statement.dbReadString("RECOUVREMENT");
        libelleType = statement.dbReadString("LIBELLETYPE");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // Pour les types de décompte INTERNE et DECOMPTE A ZERO, seul le mode
        // AUCUN est possible
        if ((getIdCritereDecompte().equalsIgnoreCase(FAModuleImpression.CS_DECOMPTE_INTERNE))
                || (getIdCritereDecompte().equalsIgnoreCase(FAModuleImpression.CS_DECOMPTE_ZERO))) {
            if (!getIdModeRecouvrement().equalsIgnoreCase(FAEnteteFacture.CS_AUCUN_RECOUVREMENT)) {
                _addError(statement.getTransaction(),
                        "Seul le mode de recouvrement AUCUN est possible pour ce type de décompte. ");
            }
        }
        // Pour le type de décompte NOTES DE CREDIT, seul les modes AUCUN et
        // REMBOURSEMENT sont possible
        if (getIdCritereDecompte().equalsIgnoreCase(FAModuleImpression.CS_NOTE_CREDIT)) {
            if ((!getIdModeRecouvrement().equalsIgnoreCase(FAEnteteFacture.CS_AUCUN_RECOUVREMENT))
                    && (!getIdModeRecouvrement().equalsIgnoreCase(FAEnteteFacture.CS_MODE_REMBOURSEMENT))) {
                _addError(statement.getTransaction(),
                        "Seuls les modes de recouvrement AUCUN et REMBOURSEMENT sont possible pour ce type de décompte. ");
            }
        }
        // Pour le type de décompte POSITIF, seul les modes BVR et RECOUVREMENT
        // sont possible
        if (getIdCritereDecompte().equalsIgnoreCase(FAModuleImpression.CS_DECOMPTE_POSITIF)) {
            if ((!getIdModeRecouvrement().equalsIgnoreCase(FAEnteteFacture.CS_MODE_BVR))
                    && (!getIdModeRecouvrement().equalsIgnoreCase(FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT))) {
                _addError(statement.getTransaction(),
                        "Seuls les modes de recouvrement BVR et RECOUVREMENT sont possible pour ce type de décompte. ");
            }
        }
        // Le libelle de la langue de l'application est obligatoire

        // En attendant les paramêtres pas défaut, on fait le test sur la langue
        // de l'utilisateur
        // !!!!!!!!!!!!!!!! A modifier
        String langue = getSession().getIdLangueISO();

        if ((langue.equals("FR")) && (JadeStringUtil.isBlank(getLibelleFr()))) {
            _addError(statement.getTransaction(), "Le libellé en français doit être renseigné. ");
        }
        if ((langue.equals("AL")) && (JadeStringUtil.isBlank(getLibelleDe()))) {
            _addError(statement.getTransaction(), "Le libellé en allemand doit être renseigné. ");
        }
        if ((langue.equals("IT")) && (JadeStringUtil.isBlank(getLibelleIt()))) {
            _addError(statement.getTransaction(), "Le libellé en italien doit être renseigné. ");
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDMODULEIMPRESSION",
                this._dbWriteNumeric(statement.getTransaction(), getIdModuleImpression(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDMODULEIMPRESSION",
                this._dbWriteNumeric(statement.getTransaction(), getIdModuleImpression(), "idModuleImpression"));
        statement.writeField("LIBELLEFR", this._dbWriteString(statement.getTransaction(), getLibelleFr(), "libelleFr"));
        statement.writeField("LIBELLEDE", this._dbWriteString(statement.getTransaction(), getLibelleDe(), "libelleDe"));
        statement.writeField("LIBELLEIT", this._dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleIt"));
        statement.writeField("IDCRITEREDECOMPTE",
                this._dbWriteNumeric(statement.getTransaction(), getIdCritereDecompte(), "idCritereDecompte"));
        statement.writeField("IDMODERECOUVREMENT",
                this._dbWriteNumeric(statement.getTransaction(), getIdModeRecouvrement(), "idModeRecouvrement"));
        statement.writeField("NOMCLASSE", this._dbWriteString(statement.getTransaction(), getNomClasse(), "nomClasse"));
    }

    @Override
    public java.lang.String getIdCritereDecompte() {
        return idCritereDecompte;
    }

    @Override
    public java.lang.String getIdModeRecouvrement() {
        return idModeRecouvrement;
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdModuleImpression() {
        return idModuleImpression;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:20:10)
     * 
     * @return int
     */
    @Override
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
     * Insérez la description de la méthode ici. Date de création : (11.06.2003 17:29:24)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleMode() {
        return libelleMode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.06.2003 17:30:10)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleType() {
        return libelleType;
    }

    @Override
    public java.lang.String getNomClasse() {
        return nomClasse;
    }

    @Override
    public void setIdCritereDecompte(java.lang.String newIdCritereDecompte) {
        idCritereDecompte = newIdCritereDecompte;
    }

    @Override
    public void setIdModeRecouvrement(java.lang.String newIdModeRecouvrement) {
        idModeRecouvrement = newIdModeRecouvrement;
    }

    /**
     * Setter
     */
    @Override
    public void setIdModuleImpression(java.lang.String newIdModuleImpression) {
        idModuleImpression = newIdModuleImpression;
    }

    @Override
    public void setLibelleDe(java.lang.String newLibelleDe) {
        libelleDe = newLibelleDe;
    }

    @Override
    public void setLibelleFr(java.lang.String newLibelleFr) {
        libelleFr = newLibelleFr;
    }

    @Override
    public void setLibelleIt(java.lang.String newLibelleIt) {
        libelleIt = newLibelleIt;
    }

    @Override
    public void setNomClasse(java.lang.String newNomClasse) {
        nomClasse = newNomClasse;
    }
}
