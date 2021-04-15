package globaz.musca.db.facturation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class FAModuleFacturation extends BEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_MODULE_AFACT = "905003";
    public final static String CS_MODULE_AMENDE_DEC_SAL = "905039";
    public final static String CS_MODULE_AMENDES_DEC_STRUCT = "905034";
    public final static String CS_MODULE_BENEFICIAIRE_PC = "905023";

    public final static String CS_MODULE_BULLETIN_NEUTRE = "905028";
    public final static String CS_MODULE_BULLETINS_SOLDES = "905014";
    public final static String CS_MODULE_BULLETINS_SOLDES_EBILL = "905019";
    public final static String CS_MODULE_COMPENSATION = "905004";
    public final static String CS_MODULE_COMPENSATION_APG = "905020";
    public final static String CS_MODULE_COMPENSATION_NEW = "905026";
    public final static String CS_MODULE_COMPTABILISATION = "905035";
    public final static String CS_MODULE_COT_PERS = "905006";
    public final static String CS_MODULE_COT_PERS_IND = "905041";
    public final static String CS_MODULE_COT_PERS_NAC = "905042";
    public final static String CS_MODULE_COT_PERS_PORTAIL = "905045";

    public final static String CS_MODULE_DECISION_CAP_CGAS = "905047";
    public final static String CS_MODULE_DECLARATION_SALAIRE = "905012";
    public final static String CS_MODULE_ETUDIANT = "905032";
    public final static String CS_MODULE_IM_COTARR = "905005";
    public final static String CS_MODULE_IM_TARDIF = "905009";
    public final static String CS_MODULE_IMPRESSION = "905027";
    public final static String CS_MODULE_IMPRESSION_DECISION_CAP_CGAS = "905048";
    public final static String CS_MODULE_LETTRE_RENTIER_NA = "905021";
    public final static String CS_MODULE_LETTRE_TAXE_CO2 = "905036";
    public final static String CS_MODULE_LISTE = "905002";
    public final static String CS_MODULE_PERIODIQUE_30JUIN = "905013";

    // Type de module
    public final static String CS_MODULE_ASSOCIATIONS_PROF = "905051";
    public final static String CS_MODULE_DECOMPTES_SALAIRE = "905050";
    public final static String CS_MODULE_TAXATION_OFFICE = "905060";
    public final static String CS_MODULE_ABSENCES_JUSTIFIEES = "905070";
    public final static String CS_MODULE_CONGE_PAYE = "905080";
    public final static String CS_MODULE_SERVICE_MILITAIRE = "905090";

    public final static String CS_MODULE_PERIODIQUE_CAP_CGAS = "905055";
    public final static String CS_MODULE_PERIODIQUE_LAE = "905040";
    public final static String CS_MODULE_PERIODIQUE_PARITAIRE = "905007";
    public final static String CS_MODULE_PERIODIQUE_PERS = "905008";
    public final static String CS_MODULE_PERIODIQUE_PERS_IND = "905043";
    public final static String CS_MODULE_PERIODIQUE_PERS_NAC = "905044";
    public final static String CS_MODULE_PERIODIQUE_RI_PC = "905031";
    public final static String CS_MODULE_PRESTATIONSAF = "905018";
    public final static String CS_MODULE_PRESTATIONSAF_PAR = "905024";
    public final static String CS_MODULE_PRESTATIONSAF_PERS = "905025";
    public final static String CS_MODULE_PRINT_DECISIONMORATOIRE = "905010";
    public final static String CS_MODULE_RELEVE = "905015";
    public final static String CS_MODULE_REMBOURSER = "905017";
    public final static String CS_MODULE_REPORTER = "905016";
    public final static String CS_MODULE_RESTITUTION_RENTE = "905022";
    public final static String CS_MODULE_STANDARD = "905001";
    public final static String CS_MODULE_SYSTEM = "905011";
    public final static String CS_MODULE_TAXE_CO2_ENTETE_EXISTE = "905033";
    public final static String CS_MODULE_TAXE_CO2_TOUT = "905030";
    public final static String CS_MODULE_TAXE_SOMMATION = "905029";
    public final static String CS_MODULE_TAXE_SOMMATION_DS_LTN = "905038";
    public final static String CS_MODULE_TO_DEC_SAL = "905037";
    public static final String FIELD_IDMODFAC = "IDMODFAC";
    public static final String FIELD_IDTYPEMODULE = "IDTYPEMODULE";
    public static final String FIELD_LIBELLEDE = "LIBELLEDE";
    public static final String FIELD_LIBELLEFR = "LIBELLEFR";
    public static final String FIELD_LIBELLEIT = "LIBELLEIT";
    public static final String FIELD_MODIFIERAFACT = "MODIFIERAFACT";
    public static final String FIELD_NIVEAUAPPEL = "NIVEAUAPPEL";
    public static final String FIELD_NOMCLASSE = "NOMCLASSE";
    public static final String TABLE_FAMODUP = "FAMODUP";

    private String idModuleFacturation = new String();
    private String idTypeModule = new String(FAModuleFacturation.CS_MODULE_STANDARD);
    private String libelleDe = new String();
    private String libelleFr = new String();
    private String libelleIt = new String();
    private Boolean modifierAfact = new Boolean(false);
    private String niveauAppel = new String();

    private String nomClasse = new String();

    /**
     * Commentaire relatif au constructeur FAModuleFacturation
     */
    public FAModuleFacturation() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdModuleFacturation(this._incCounter(transaction, idModuleFacturation));
        // setIdModuleFacturation(_incCounter(transaction, "0"));
    }

    /*
     * Traitement avant suppression
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {
        // Suppression seulement si le module n'existe pas dans les afacts
        FAAfactManager afactManager = new FAAfactManager();
        afactManager.setSession(transaction.getSession());
        afactManager.setForIdModuleFacturation(getIdModuleFacturation());
        try {
            afactManager.find(transaction);
            if (afactManager.size() > 0) {
                _addError(transaction, "Il existe des afacts pour ce module, suppression impossible. ");
            }
        } catch (Exception e) {
            _addError(transaction, "Erreur lors du contrôle de la suppression. ");
        }
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + FAModuleFacturation.TABLE_FAMODUP + " AS " + FAModuleFacturation.TABLE_FAMODUP;
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return FAModuleFacturation.TABLE_FAMODUP;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idModuleFacturation = statement.dbReadNumeric(FAModuleFacturation.FIELD_IDMODFAC);
        libelleFr = statement.dbReadString(FAModuleFacturation.FIELD_LIBELLEFR);
        libelleDe = statement.dbReadString(FAModuleFacturation.FIELD_LIBELLEDE);
        libelleIt = statement.dbReadString(FAModuleFacturation.FIELD_LIBELLEIT);
        nomClasse = statement.dbReadString(FAModuleFacturation.FIELD_NOMCLASSE);
        idTypeModule = statement.dbReadNumeric(FAModuleFacturation.FIELD_IDTYPEMODULE);
        niveauAppel = statement.dbReadNumeric(FAModuleFacturation.FIELD_NIVEAUAPPEL);
        modifierAfact = statement.dbReadBoolean(FAModuleFacturation.FIELD_MODIFIERAFACT);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Le libelle de la langue de l'application est obligatoire
        // En attendant les paramêtres pas défaut, on fait le test sur la langue
        // de l'utilisateur
        // !!!!!!!!!!!!!!!! A modifier
        String langue = new String();
        langue = getSession().getIdLangueISO();
        if ((langue.equals("FR")) && (JadeStringUtil.isBlank(getLibelleFr()))) {
            _addError(statement.getTransaction(), "Le libellé en français doit être renseigné. ");
        }
        if ((langue.equals("DE")) && (JadeStringUtil.isBlank(getLibelleDe()))) {
            _addError(statement.getTransaction(), "Le libellé en allemand doit être renseigné. ");
        }
        if ((langue.equals("IT")) && (JadeStringUtil.isBlank(getLibelleIt()))) {
            _addError(statement.getTransaction(), "Le libellé en italien doit être renseigné. ");
        }
        // Le niveau d'appel doit être compris entre 1 et 999
        if (!globaz.jade.client.util.JadeStringUtil.isBlank(getNiveauAppel())) {
            int niveau = Integer.parseInt(getNiveauAppel());
            if ((niveau < 1) || (niveau > 999)) {
                _addError(statement.getTransaction(), "Le niveau d'appel doit être compris entre 1 et 999. ");
            }
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FAModuleFacturation.FIELD_IDMODFAC,
                this._dbWriteNumeric(statement.getTransaction(), getIdModuleFacturation(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FAModuleFacturation.FIELD_IDMODFAC,
                this._dbWriteNumeric(statement.getTransaction(), getIdModuleFacturation(), "idModuleFacturation"));
        statement.writeField(FAModuleFacturation.FIELD_LIBELLEFR,
                this._dbWriteString(statement.getTransaction(), getLibelleFr(), "libelleFr"));
        statement.writeField(FAModuleFacturation.FIELD_LIBELLEDE,
                this._dbWriteString(statement.getTransaction(), getLibelleDe(), "libelleDe"));
        statement.writeField(FAModuleFacturation.FIELD_LIBELLEIT,
                this._dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleIt"));
        statement.writeField(FAModuleFacturation.FIELD_NOMCLASSE,
                this._dbWriteString(statement.getTransaction(), getNomClasse(), "nomClasse"));
        statement.writeField(FAModuleFacturation.FIELD_IDTYPEMODULE,
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeModule(), "idTypeModule"));
        statement.writeField(FAModuleFacturation.FIELD_NIVEAUAPPEL,
                this._dbWriteNumeric(statement.getTransaction(), getNiveauAppel(), "niveauAppel"));
        statement.writeField(FAModuleFacturation.FIELD_MODIFIERAFACT, this._dbWriteBoolean(statement.getTransaction(),
                isModifierAfact(), BConstants.DB_TYPE_BOOLEAN_CHAR, "modifierAfact"));
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    public String getIdTypeModule() {
        return idTypeModule;
    }

    public String getLibelle() {
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

    public String getLibelleDe() {
        return libelleDe;
    }

    public String getLibelleFr() {
        return libelleFr;
    }

    public String getLibelleIt() {
        return libelleIt;
    }

    public String getLibelleType() {
        return getSession().getCodeLibelle(idTypeModule);
    }

    public String getNiveauAppel() {
        return niveauAppel;
    }

    public String getNomClasse() {
        return nomClasse;
    }

    public Boolean isModifierAfact() {
        return modifierAfact;
    }

    public void setIdModuleFacturation(String newIdModuleFacturation) {
        idModuleFacturation = newIdModuleFacturation;
    }

    public void setIdTypeModule(String newIdTypeModule) {
        idTypeModule = newIdTypeModule;
    }

    public void setLibelleDe(String newLibelleDe) {
        libelleDe = newLibelleDe;
    }

    public void setLibelleFr(String newLibelleFr) {
        libelleFr = newLibelleFr;
    }

    public void setLibelleIt(String newLibelleIt) {
        libelleIt = newLibelleIt;
    }

    public void setModifierAfact(Boolean newModifierAfact) {
        modifierAfact = newModifierAfact;
    }

    public void setNiveauAppel(String newNiveauAppel) {
        niveauAppel = newNiveauAppel;
    }

    public void setNomClasse(String newNomClasse) {
        nomClasse = newNomClasse;
    }
}
