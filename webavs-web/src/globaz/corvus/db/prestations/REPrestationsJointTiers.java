/*
 * Créé le 30 fevr. 07
 */
package globaz.corvus.db.prestations;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REValidationDecisions;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author scr Jointure entre les tables des prestations et des tiers
 */
public class REPrestationsJointTiers extends REPrestations {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";

    public static final String FIELDNAME_NATIONALITE = "HNIPAY";

    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";

    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_FIELD_TI_IDTIERS = "HTITIE";
    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_AVS_HISTO_FIELD_NUMERO_AVS = "HTITIE";
    public static final String TABLE_PERSONNE = "TIPERSP";

    public static final String TABLE_TIERS = "TITIERP";

    private String csSexe = "";

    private String csTypeDecision = "";
    private String dateDebutDroit = "";
    private String dateFinDroit = "";
    private String dateFinRetro = "";
    private String dateNaissance = "";
    private String genrePrestation = "";
    private String idNationalite = "";
    // Autres champs nécessaires
    private String idTiersPrestataire = "";
    private String nom = "";
    private String nss = "";
    private int numDateDebutDroit = 0;
    private String prenom = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param schema DOCUMENT ME!
     * @return DOCUMENT ME!
     */

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * Il est interdit d'effacer un objet de ce type.
     * 
     * @return false
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * Il est interdit de mettre un objet de ce type à jour.
     * 
     * @return false
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return faux
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestations.TABLE_NAME_PRESTATION);

        // jointure entre table des prestation et table des décisions
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REDecisionEntity.TABLE_NAME_DECISIONS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestations.TABLE_NAME_PRESTATION);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestations.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REDecisionEntity.TABLE_NAME_DECISIONS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDecisionEntity.FIELDNAME_ID_DECISION);

        // jointure entre table des décisions et validation décisions
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REDecisionEntity.TABLE_NAME_DECISIONS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDecisionEntity.FIELDNAME_ID_DECISION);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REValidationDecisions.FIELDNAME_ID_DECISION);

        // jointure entre table validation décisions et table préstation dues
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REValidationDecisions.FIELDNAME_ID_PRESTATION_DUE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE);

        // jointure entre table préstations dues et table rentes accordées
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationDue.FIELDNAME_ID_RENTE_ACCORDEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        // pour la recherche sur les champs du tiers

        // jointure entre table des décisions et table des numeros AVS
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationsJointTiers.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REDecisionEntity.TABLE_NAME_DECISIONS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDecisionEntity.FIELDNAME_ID_TIERS_BENEF_PRINCIPAL);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationsJointTiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre table table des numeros AVS et table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationsJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationsJointTiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationsJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des personnes et table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationsJointTiers.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationsJointTiers.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsJointTiers.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationsJointTiers.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsJointTiers.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des historiques AVS et table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationsJointTiers.TABLE_AVS_HISTO);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection() + REPrestationsJointTiers.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsJointTiers.TABLE_AVS_FIELD_TI_IDTIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append(REPrestationsJointTiers.TABLE_AVS_HISTO);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsJointTiers.TABLE_AVS_HISTO_FIELD_NUMERO_AVS);

        return fromClauseBuffer.toString();
    }

    /**
     * @param statement DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        idTiersPrestataire = statement.dbReadNumeric(REPrestationsJointTiersManager.FIELDNAME_ID_TIERS_TI);
        nom = statement.dbReadString(REPrestationsJointTiersManager.FIELDNAME_NOM);
        prenom = statement.dbReadString(REPrestationsJointTiersManager.FIELDNAME_PRENOM);
        nss = statement.dbReadString(REPrestationsJointTiersManager.FIELDNAME_NUM_AVS);
        csSexe = statement.dbReadNumeric(REPrestationsJointTiersManager.FIELDNAME_SEXE);
        idNationalite = statement.dbReadNumeric(REPrestationsJointTiersManager.FIELDNAME_NATIONALITE);
        dateNaissance = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadNumeric(REPrestationsJointTiersManager.FIELDNAME_DATENAISSANCE));
        String value = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
        dateDebutDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(value);
        dateFinDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));
        if (!JadeNumericUtil.isEmptyOrZero(value)) {
            numDateDebutDroit = new Integer(value).intValue();
        }
        csTypeDecision = statement.dbReadNumeric(REDecisionEntity.FIELDNAME_TYPE_DECISION);
        dateFinRetro = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REDecisionEntity.FIELDNAME_DATE_FIN_RETRO));
        genrePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     * @param statement DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // rien du tout... c'est en lecture seule.
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     * @param statement DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getCsTypeDecision() {
        return csTypeDecision;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getDateFinRetro() {
        return dateFinRetro;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getGenrePrestation() {
        return genrePrestation;
    }

    public String getIdNationalite() {
        return idNationalite;
    }

    /**
     * @return
     */
    public String getIdTiersPrestataire() {
        return idTiersPrestataire;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public int getNumDateDebutDroit() {
        return numDateDebutDroit;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setCsTypeDecision(String csTypeDecision) {
        this.csTypeDecision = csTypeDecision;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setDateFinRetro(String dateFinRetro) {
        this.dateFinRetro = dateFinRetro;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setGenrePrestation(String genrePrestation) {
        this.genrePrestation = genrePrestation;
    }

    public void setIdNationalite(String s) {
        idNationalite = s;
    }

    /**
     * @param string
     */
    public void setIdTiersPrestataire(String string) {
        idTiersPrestataire = string;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setNumDateDebutDroit(int numDateDebutDroit) {
        this.numDateDebutDroit = numDateDebutDroit;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

}
