package globaz.apg.db.droits;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRHierarchique;

/**
 * @author VRE
 */
public class APDroitLAPGJointDemande extends BEntity implements PRHierarchique {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_NAISSANCE = "HPDNAI";
    public static final String FIELDNAME_ID_GESTIONNAIRE = "KUSER";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_NOM_GESTIONNAIRE = "FLASTNAME";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_PRENOM_GESTIONNAIRE = "FFIRSTNAME";
    public static final String FIELDNAME_SEXE = "HPTSEX";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_GESTIONNAIRES = "FWSUSRP";
    public static final String TABLE_TIERS = "TITIERP";
    public static final String TABLE_TIERS_DETAIL = "TIPERSP";

    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);

        // jointure entre table des demandes et table des droits
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPG.FIELDNAME_IDDEMANDE);

        // jointure entre table des demandes et table des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPGJointDemande.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPGJointDemande.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPGJointDemande.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des tiers et table détail des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPGJointDemande.TABLE_TIERS_DETAIL);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPGJointDemande.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPGJointDemande.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPGJointDemande.TABLE_TIERS_DETAIL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPGJointDemande.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des demandes et table des numeros AVS
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPGJointDemande.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPGJointDemande.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPGJointDemande.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des demandes et table des gestionnaires
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPGJointDemande.TABLE_GESTIONNAIRES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPG.TABLE_NAME_LAPG);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPG.FIELDNAME_IDGESTIONNAIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(APDroitLAPGJointDemande.TABLE_GESTIONNAIRES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(APDroitLAPGJointDemande.FIELDNAME_ID_GESTIONNAIRE);

        return fromClauseBuffer.toString();
    }

    private String csNationalite = "";
    private String csSexe = "";
    private String dateDebutDroit = "";
    private String dateFinDroit = "";
    private String dateNaissance = "";
    private String etatDemande = "";
    private String etatDroit = "";
    private transient String fromClause = null;
    private String genreService = "";
    private String idDemande = "";
    private String idDroit = "";
    private String idGestionnaire = "";
    private String idParent = "";
    private String idTiers = "";
    private String noAVS = "";
    private String nom = "";
    private String nomGestionnaire = "";
    private String prenom = "";
    private String prenomGestionnaire = "";

    private String typeDemande = "";

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = APDroitLAPGJointDemande.createFromClause(_getCollection());
        }

        return fromClause;
    }

    @Override
    protected String _getTableName() {
        return PRDemande.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDemande = statement.dbReadNumeric(PRDemande.FIELDNAME_IDDEMANDE);
        typeDemande = statement.dbReadNumeric(PRDemande.FIELDNAME_TYPE_DEMANDE);
        etatDemande = statement.dbReadNumeric(PRDemande.FIELDNAME_ETAT);
        idGestionnaire = statement.dbReadString(APDroitLAPG.FIELDNAME_IDGESTIONNAIRE);
        idDroit = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_IDDROIT_LAPG);
        genreService = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_GENRESERVICE);
        etatDroit = statement.dbReadNumeric(APDroitLAPG.FIELDNAME_ETAT);
        dateDebutDroit = statement.dbReadDateAMJ(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT);
        dateFinDroit = statement.dbReadDateAMJ(APDroitLAPG.FIELDNAME_DATEFINDROIT);
        noAVS = NSUtil.formatAVSUnknown(statement.dbReadString(APDroitLAPGJointDemande.FIELDNAME_NUM_AVS));
        nom = statement.dbReadString(APDroitLAPGJointDemande.FIELDNAME_NOM);
        prenom = statement.dbReadString(APDroitLAPGJointDemande.FIELDNAME_PRENOM);
        nomGestionnaire = statement.dbReadString(APDroitLAPGJointDemande.FIELDNAME_NOM_GESTIONNAIRE);
        prenomGestionnaire = statement.dbReadString(APDroitLAPGJointDemande.FIELDNAME_PRENOM_GESTIONNAIRE);
        idParent = statement.dbReadString(APDroitLAPG.FIELDNAME_IDDROIT_LAPG_PARENT);
        dateNaissance = statement.dbReadDateAMJ(APDroitLAPGJointDemande.FIELDNAME_DATE_NAISSANCE);
        csSexe = statement.dbReadNumeric(APDroitLAPGJointDemande.FIELDNAME_SEXE);
        csNationalite = statement.dbReadNumeric(APDroitLAPGJointDemande.FIELDNAME_NATIONALITE);
        idTiers = statement.dbReadNumeric(APDroitLAPGJointDemande.FIELDNAME_ID_TIERS_TI);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // nope
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(PRDemande.FIELDNAME_IDDEMANDE,
                this._dbWriteNumeric(statement.getTransaction(), idDemande, "idDemande"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // on arrive ici que si l'action est ACTION_COPY (du moins
        // theoriquement)
        statement.writeField(PRDemande.FIELDNAME_IDDEMANDE,
                this._dbWriteNumeric(statement.getTransaction(), idDemande, "idDemande"));
        statement.writeField(PRDemande.FIELDNAME_TYPE_DEMANDE,
                this._dbWriteNumeric(statement.getTransaction(), typeDemande, "typeDemande"));
        statement.writeField(PRDemande.FIELDNAME_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), etatDemande, "etatDemande"));
        statement.writeField(APDroitLAPG.FIELDNAME_IDGESTIONNAIRE,
                this._dbWriteString(statement.getTransaction(), idGestionnaire, "idGestionnaire"));
        statement.writeField(APDroitLAPG.FIELDNAME_GENRESERVICE,
                this._dbWriteNumeric(statement.getTransaction(), genreService, "genreService"));
        statement.writeField(APDroitLAPG.FIELDNAME_ETAT,
                this._dbWriteNumeric(statement.getTransaction(), etatDroit, "etatDroit"));
        statement.writeField(APDroitLAPG.FIELDNAME_DATEFINDROIT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFinDroit, "dateFinDroit"));
        statement.writeField(APDroitLAPGJointDemande.FIELDNAME_NUM_AVS,
                this._dbWriteAVS(statement.getTransaction(), noAVS, "noAVS"));
        statement.writeField(APDroitLAPGJointDemande.FIELDNAME_NOM,
                this._dbWriteString(statement.getTransaction(), nom, "nom"));
        statement.writeField(APDroitLAPGJointDemande.FIELDNAME_PRENOM,
                this._dbWriteString(statement.getTransaction(), prenom, "prenom"));
        statement.writeField(APDroitLAPGJointDemande.FIELDNAME_PRENOM_GESTIONNAIRE,
                this._dbWriteString(statement.getTransaction(), prenomGestionnaire, "prenomGestionnaire"));
        statement.writeField(APDroitLAPGJointDemande.FIELDNAME_NOM_GESTIONNAIRE,
                this._dbWriteString(statement.getTransaction(), nomGestionnaire, "nomGestionnaire"));
        statement.writeField(APDroitLAPGJointDemande.FIELDNAME_NATIONALITE,
                this._dbWriteNumeric(statement.getTransaction(), csNationalite, "csNationalite"));
        statement.writeField(APDroitLAPGJointDemande.FIELDNAME_SEXE,
                this._dbWriteNumeric(statement.getTransaction(), csSexe, "csSexe"));
        statement.writeField(APDroitLAPGJointDemande.FIELDNAME_DATE_NAISSANCE,
                this._dbWriteDateAMJ(statement.getTransaction(), dateNaissance, "dateNaissance"));
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getEtatDemande() {
        return etatDemande;
    }

    public String getEtatDroit() {
        return etatDroit;
    }

    public String getGenreService() {
        return genreService;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    @Override
    public String getIdMajeur() {
        return idDroit;
    }

    @Override
    public String getIdParent() {
        return idParent;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNoAVS() {
        return noAVS;
    }

    public String getNom() {
        return nom;
    }

    public String getNomGestionnaire() {
        return nomGestionnaire;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPrenomGestionnaire() {
        return prenomGestionnaire;
    }

    public String getTypeDemande() {
        return typeDemande;
    }

    /**
     * Le pspy est desactive pour ce BEntity car il n'y a pas de champ pspy dans la table des droits lapg.
     * 
     * <p>
     * De toutes facons, il est interdit de faire des modifications en utilisant ce BEntity donc le pspy ne sert a rien.
     * </p>
     */
    @Override
    public boolean hasSpy() {
        return false;
    }

    public void setCsNationalite(String string) {
        csNationalite = string;
    }

    public void setCsSexe(String string) {
        csSexe = string;
    }

    public void setDateDebutDroit(String string) {
        dateDebutDroit = string;
    }

    public void setDateFinDroit(String string) {
        dateFinDroit = string;
    }

    public void setDateNaissance(String string) {
        dateNaissance = string;
    }

    public void setEtatDemande(String string) {
        etatDemande = string;
    }

    public void setEtatDroit(String string) {
        etatDroit = string;
    }

    public void setGenreService(String string) {
        genreService = string;
    }

    public void setIdDemande(String string) {
        idDemande = string;
    }

    public void setIdDroit(String string) {
        idDroit = string;
    }

    public void setIdGestionnaire(String string) {
        idGestionnaire = string;
    }

    public void setIdParent(String string) {
        idParent = string;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNoAVS(String string) {
        noAVS = string;
    }

    public void setNom(String string) {
        nom = string;
    }

    public void setNomGestionnaire(String string) {
        nomGestionnaire = string;
    }

    public void setPrenom(String string) {
        prenom = string;
    }

    public void setPrenomGestionnaire(String string) {
        prenomGestionnaire = string;
    }

    public void setTypeDemande(String string) {
        typeDemande = string;
    }
}
