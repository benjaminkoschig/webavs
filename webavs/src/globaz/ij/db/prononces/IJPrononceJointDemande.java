package globaz.ij.db.prononces;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRHierarchique;

public class IJPrononceJointDemande extends BEntity implements PRHierarchique, Comparable<IJPrononceJointDemande> {

    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_NAISSANCE = "HPDNAI";
    public static final String FIELDNAME_DATEECHEANCE_FOR_SEARCH = "XBDECH";
    private static final String FIELDNAME_ID_GESTIONNAIRE = "KUSER";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    private static final String FIELDNAME_NOM_GESTIONNAIRE = "FLASTNAME";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    private static final String FIELDNAME_PRENOM_GESTIONNAIRE = "FFIRSTNAME";
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
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononce.FIELDNAME_ID_DEMANDE);

        // jointure entre table des demandes et table des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononceJointDemande.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononceJointDemande.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononceJointDemande.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des tiers et table détail des tiers
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononceJointDemande.TABLE_TIERS_DETAIL);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononceJointDemande.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononceJointDemande.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononceJointDemande.TABLE_TIERS_DETAIL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononceJointDemande.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des demandes et table des numeros AVS
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononceJointDemande.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononceJointDemande.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononceJointDemande.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des demandes et table des gestionnaires
        fromClauseBuffer.append(leftJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononceJointDemande.TABLE_GESTIONNAIRES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononce.TABLE_NAME_PRONONCE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononce.FIELDNAME_ID_GESTIONNAIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(IJPrononceJointDemande.TABLE_GESTIONNAIRES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(IJPrononceJointDemande.FIELDNAME_ID_GESTIONNAIRE);

        return fromClauseBuffer.toString();
    }

    private String csEtatDemande = "";

    private String csEtatPrononce = "";
    private String csMotifEcheance = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String csTypeDemande = "";
    private String csTypeIJ = "";
    private String dateDebutPrononce = "";
    private String dateEcheance = "";
    private String dateFinPrononce = "";
    private String dateNaissance = "";
    private transient String fromClause = null;
    private String idDecision = "";
    private String idDemande = "";
    private String idGestionnaire = "";
    private String idParent = "";
    private String idParentCorrigeDepuis = "";
    private String idPrononce = "";
    private String idTiers = "";
    private String noAVS = "";
    private String nom = "";
    private String nomGestionnaire = "";
    private String prenom = "";
    private String prenomGestionnaire = "";

    private Boolean soumisImpotSource = Boolean.FALSE;
    private String communePolitique = "";

    @Override
    protected boolean _allowAdd() {
        return false;
    }

    @Override
    protected boolean _allowDelete() {
        return false;
    }

    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    @Override
    protected String _getTableName() {
        return PRDemande.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDemande = statement.dbReadNumeric(PRDemande.FIELDNAME_IDDEMANDE);
        csTypeDemande = statement.dbReadNumeric(PRDemande.FIELDNAME_TYPE_DEMANDE);
        csEtatDemande = statement.dbReadNumeric(PRDemande.FIELDNAME_ETAT);
        idGestionnaire = statement.dbReadString(IJPrononce.FIELDNAME_ID_GESTIONNAIRE);
        idPrononce = statement.dbReadNumeric(IJPrononce.FIELDNAME_ID_PRONONCE);
        csEtatPrononce = statement.dbReadNumeric(IJPrononce.FIELDNAME_CS_ETAT);
        idParentCorrigeDepuis = statement.dbReadNumeric(IJPrononce.FIELDNAME_PARENT_CORRIGE_DEPUIS);
        dateDebutPrononce = statement.dbReadDateAMJ(IJPrononce.FIELDNAME_DATE_DEBUT_PRONONCE);
        dateFinPrononce = statement.dbReadDateAMJ(IJPrononce.FIELDNAME_DATE_FIN_PRONONCE);
        idDecision = statement.dbReadNumeric(IJPrononce.FIELDNAME_ID_DECISION);
        noAVS = NSUtil.formatAVSUnknown(statement.dbReadString(IJPrononceJointDemande.FIELDNAME_NUM_AVS));
        nom = statement.dbReadString(IJPrononceJointDemande.FIELDNAME_NOM);
        prenom = statement.dbReadString(IJPrononceJointDemande.FIELDNAME_PRENOM);
        nomGestionnaire = statement.dbReadString(IJPrononceJointDemande.FIELDNAME_NOM_GESTIONNAIRE);
        prenomGestionnaire = statement.dbReadString(IJPrononceJointDemande.FIELDNAME_PRENOM_GESTIONNAIRE);
        idParent = statement.dbReadString(IJPrononce.FIELDNAME_ID_PARENT);
        csTypeIJ = statement.dbReadNumeric(IJPrononce.FIELDNAME_CS_TYPE_IJ);
        idTiers = statement.dbReadNumeric(IJPrononceJointDemande.FIELDNAME_ID_TIERS_TI);
        dateNaissance = statement.dbReadDateAMJ(IJPrononceJointDemande.FIELDNAME_DATE_NAISSANCE);
        csSexe = statement.dbReadNumeric(IJPrononceJointDemande.FIELDNAME_SEXE);
        csNationalite = statement.dbReadNumeric(IJPrononceJointDemande.FIELDNAME_NATIONALITE);
        dateEcheance = statement.dbReadDateAMJ(IJPrononce.FIELDNAME_DATE_ECHEANCE);
        csMotifEcheance = statement.dbReadNumeric(IJPrononce.FIELDNAME_MOTIF_ECHEANCE);
        soumisImpotSource = statement.dbReadBoolean(IJPrononce.FIELDNAME_SOUMIS_IMPOT_SOURCE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // rien du tout... c'est en lecture seule.
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(PRDemande.FIELDNAME_IDDEMANDE,
                this._dbWriteNumeric(statement.getTransaction(), idDemande, "idDemande"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Raccord de méthode auto-généré
    }

    /**
     * getter pour l'attribut cs etat demande
     * 
     * @return la valeur courante de l'attribut cs etat demande
     */
    public String getCsEtatDemande() {
        return csEtatDemande;
    }

    /**
     * getter pour l'attribut cs etat prononce
     * 
     * @return la valeur courante de l'attribut cs etat prononce
     */
    public String getCsEtatPrononce() {
        return csEtatPrononce;
    }

    public String getCsMotifEcheance() {
        return csMotifEcheance;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    /**
     * getter pour l'attribut cs type demande
     * 
     * @return la valeur courante de l'attribut cs type demande
     */
    public String getCsTypeDemande() {
        return csTypeDemande;
    }

    /**
     * getter pour l'attribut cs type IJ
     * 
     * @return la valeur courante de l'attribut cs type IJ
     */
    public String getCsTypeIJ() {
        return csTypeIJ;
    }

    /**
     * getter pour l'attribut date debut prononce
     * 
     * @return la valeur courante de l'attribut date debut prononce
     */
    public String getDateDebutPrononce() {
        return dateDebutPrononce;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public String getDateFinPrononce() {
        return dateFinPrononce;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * getter pour l'attribut from clause
     * 
     * @return la valeur courante de l'attribut from clause
     */
    public String getFromClause() {
        return fromClause;
    }

    public String getIdDecision() {
        return idDecision;
    }

    /**
     * getter pour l'attribut id demande
     * 
     * @return la valeur courante de l'attribut id demande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * getter pour l'attribut id gestionnaire
     * 
     * @return la valeur courante de l'attribut id gestionnaire
     */
    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    @Override
    public String getIdMajeur() {
        return idPrononce;
    }

    @Override
    public String getIdParent() {
        return idParent;
    }

    public String getIdParentCorrigeDepuis() {
        return idParentCorrigeDepuis;
    }

    /**
     * getter pour l'attribut id prononce
     * 
     * @return la valeur courante de l'attribut id prononce
     */
    public String getIdPrononce() {
        return idPrononce;
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * getter pour l'attribut no AVS
     * 
     * @return la valeur courante de l'attribut no AVS
     */
    public String getNoAVS() {
        return noAVS;
    }

    /**
     * getter pour l'attribut nom
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * getter pour l'attribut nom gestionnaire
     * 
     * @return la valeur courante de l'attribut nom gestionnaire
     */
    public String getNomGestionnaire() {
        return nomGestionnaire;
    }

    /**
     * getter pour l'attribut prenom
     * 
     * @return la valeur courante de l'attribut prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * getter pour l'attribut prenom gestionnaire
     * 
     * @return la valeur courante de l'attribut prenom gestionnaire
     */
    public String getPrenomGestionnaire() {
        return prenomGestionnaire;
    }

    public Boolean getSoumisImpotSource() {
        return soumisImpotSource;
    }

    public boolean hasNotIdParentCorrigeDepuis() {
        return JadeStringUtil.isBlankOrZero(idParentCorrigeDepuis);
    }

    /**
     * setter pour l'attribut cs etat demande
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsEtatDemande(String string) {
        csEtatDemande = string;
    }

    /**
     * setter pour l'attribut cs etat prononce
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsEtatPrononce(String string) {
        csEtatPrononce = string;
    }

    public void setCsMotifEcheance(String csMotifEcheance) {
        this.csMotifEcheance = csMotifEcheance;
    }

    public void setCsNationalite(String string) {
        csNationalite = string;
    }

    public void setCsSexe(String string) {
        csSexe = string;
    }

    /**
     * setter pour l'attribut cs type demande
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeDemande(String string) {
        csTypeDemande = string;
    }

    /**
     * setter pour l'attribut cs type IJ
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeIJ(String string) {
        csTypeIJ = string;
    }

    /**
     * setter pour l'attribut date debut prononce
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutPrononce(String string) {
        dateDebutPrononce = string;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setDateFinPrononce(String dateFinPrononce) {
        this.dateFinPrononce = dateFinPrononce;
    }

    public void setDateNaissance(String string) {
        dateNaissance = string;
    }

    /**
     * setter pour l'attribut from clause
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromClause(String string) {
        fromClause = string;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    /**
     * setter pour l'attribut id demande
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDemande(String string) {
        idDemande = string;
    }

    /**
     * setter pour l'attribut id gestionnaire
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdGestionnaire(String string) {
        idGestionnaire = string;
    }

    /**
     * setter pour l'attribut id parent
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdParent(String string) {
        idParent = string;
    }

    public void setIdParentCorrigeDepuis(String idParentCorrigeDepuis) {
        this.idParentCorrigeDepuis = idParentCorrigeDepuis;
    }

    /**
     * setter pour l'attribut id prononce
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrononce(String string) {
        idPrononce = string;
    }

    public void setIdTiers(String string) {
        idTiers = string;
    }

    /**
     * setter pour l'attribut no AVS
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoAVS(String string) {
        noAVS = string;
    }

    /**
     * setter pour l'attribut nom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNom(String string) {
        nom = string;
    }

    /**
     * setter pour l'attribut nom gestionnaire
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNomGestionnaire(String string) {
        nomGestionnaire = string;
    }

    /**
     * setter pour l'attribut prenom
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPrenom(String string) {
        prenom = string;
    }

    /**
     * setter pour l'attribut prenom gestionnaire
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPrenomGestionnaire(String string) {
        prenomGestionnaire = string;
    }

    public void setSoumisImpotSource(Boolean soumisImpotSource) {
        this.soumisImpotSource = soumisImpotSource;
    }

    public String getCommunePolitique() {
        return communePolitique;
    }

    public void setCommunePolitique(String communePolitique) {
        this.communePolitique = communePolitique;
    }

    @Override
    public int compareTo(IJPrononceJointDemande o) {
        return getCommunePolitique().compareToIgnoreCase(o.getCommunePolitique());
    }
}
