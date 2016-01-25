/*
 * Créé le 9 janv. 07
 */
package globaz.corvus.db.demandes;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSpy;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.tools.PRHierarchique;

/**
 * @author hpe
 * 
 *         Jointure entre les tables des demandes de rentes, les demandes et les tiers
 * 
 */
public class REDemandeRenteJointDemande extends BEntity implements PRHierarchique {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATE_NAI_FAM = "WGDNAI";
    public static final String FIELDNAME_DATEDECES = "HPDDEC";
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";
    public static final String FIELDNAME_ID_GESTIONNAIRE = "KUSER";
    public static final String FIELDNAME_ID_MBR_FAM = "WGIMEF";
    public static final String FIELDNAME_ID_MBR_REL_FAM = "WFIMEF";
    public static final String FIELDNAME_ID_MBR_REQUERANT = "WDIMEF";
    public static final String FIELDNAME_ID_REQ_REL_FAM = "WFIREQ";
    public static final String FIELDNAME_ID_REQU_REQU = "WDIREQ";
    public static final String FIELDNAME_ID_TIERS_MBR_FAM = "WGITIE";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FAM = "WGLNOU";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FAM = "WGLPRU";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";
    public static final String FIELDNAME_SEXE_FAM = "WGTSEX";
    public static final String FIELDNAME_VISA_GESTIONNAIRE = "FVISA";
    public static final String IS_EN_COURS = "IS_EN_COURS";
    public static final String LABEL_NON_VALIDE = "NON_VALIDE";
    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_GESTIONNAIRES = "FWSUSRP";
    public static final String TABLE_MEMBRE_FAMILLE = "SFMBRFAM";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_RELATION_FAMILLE = "SFREFARE";
    public static final String TABLE_REQUERANT_FAMILLE = "SFREQUER";
    public static final String TABLE_TIERS = "TITIERP";

    public static String createFromClause(String schema) {
        StringBuilder sql = new StringBuilder();
        String innerJoin = " INNER JOIN ";
        String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        sql.append(schema);
        sql.append(PRDemande.TABLE_NAME);

        // jointure entre table des demandes et table des demandes de rentes
        sql.append(innerJoin);
        sql.append(schema);
        sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        sql.append(on);
        sql.append(schema);
        sql.append(PRDemande.TABLE_NAME);
        sql.append(point);
        sql.append(PRDemande.FIELDNAME_IDDEMANDE);
        sql.append(egal);
        sql.append(schema);
        sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        sql.append(point);
        sql.append(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION);

        // jointure entre table des demandes et table des numeros AVS
        sql.append(innerJoin);
        sql.append(schema);
        sql.append(REDemandeRenteJointDemande.TABLE_AVS);
        sql.append(on);
        sql.append(schema);
        sql.append(PRDemande.TABLE_NAME);
        sql.append(point);
        sql.append(PRDemande.FIELDNAME_IDTIERS);
        sql.append(egal);
        sql.append(schema);
        sql.append(REDemandeRenteJointDemande.TABLE_AVS);
        sql.append(point);
        sql.append(REDemandeRenteJointDemande.FIELDNAME_ID_TIERS_TI);

        // jointure entre table table des numeros AVS et table des personnes
        sql.append(innerJoin);
        sql.append(schema);
        sql.append(REDemandeRenteJointDemande.TABLE_PERSONNE);
        sql.append(on);
        sql.append(schema);
        sql.append(REDemandeRenteJointDemande.TABLE_AVS);
        sql.append(point);
        sql.append(REDemandeRenteJointDemande.FIELDNAME_ID_TIERS_TI);
        sql.append(egal);
        sql.append(schema);
        sql.append(REDemandeRenteJointDemande.TABLE_PERSONNE);
        sql.append(point);
        sql.append(REDemandeRenteJointDemande.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des personnes et table des tiers
        sql.append(innerJoin);
        sql.append(schema);
        sql.append(REDemandeRenteJointDemande.TABLE_TIERS);
        sql.append(on);
        sql.append(schema);
        sql.append(REDemandeRenteJointDemande.TABLE_PERSONNE);
        sql.append(point);
        sql.append(REDemandeRenteJointDemande.FIELDNAME_ID_TIERS_TI);
        sql.append(egal);
        sql.append(schema);
        sql.append(REDemandeRenteJointDemande.TABLE_TIERS);
        sql.append(point);
        sql.append(REDemandeRenteJointDemande.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des demandes et table des gestionnaires
        sql.append(leftJoin);
        sql.append(schema);
        sql.append(REDemandeRenteJointDemande.TABLE_GESTIONNAIRES);
        sql.append(on);
        sql.append(schema);
        sql.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        sql.append(point);
        sql.append(REDemandeRente.FIELDNAME_ID_GESTIONNAIRE);
        sql.append(egal);
        sql.append(schema);
        sql.append(REDemandeRenteJointDemande.TABLE_GESTIONNAIRES);
        sql.append(point);
        sql.append(REDemandeRenteJointDemande.FIELDNAME_ID_GESTIONNAIRE);

        // Optimisation pour l'affichage des informations complémentaires (Rente veuve perdure, Décès, etc...)
        sql.append(" LEFT JOIN ");
        sql.append(schema).append(PRInfoCompl.TABLE_NAME);
        sql.append(" ON ");
        sql.append(schema).append(PRInfoCompl.TABLE_NAME).append(".").append(PRInfoCompl.FIELDNAME_ID_INFO_COMPL);
        sql.append("=");
        sql.append(schema).append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE).append(".")
                .append(REDemandeRente.FIELDNAME_ID_INFO_COMPLEMENTAIRE);

        return sql.toString();
    }

    private String csEtatDemande = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String csTypeCalcul = "";
    private String csTypeDemande = "";
    private String csTypeInfoComplementaire = "";
    private String dateDebut = "";
    private String dateDeces = "";
    private String dateDepot = "";
    private String dateFin = "";
    private String dateNaissance = "";
    private String dateReception = "";
    private String dateTraitement = "";
    private transient String fromClause = null;
    private String idDemandePrestation = "";
    private String idDemandeRente = "";
    private String idGestionnaire = "";
    private String idInfoComplementaire = "";
    private String idParent = "";
    private String idRenteCalculee = "";
    private String idTiersRequerant = "";
    private String isEnCours = "";
    private String noAVS = "";
    private String nom = "";
    private String prenom = "";
    private String visaGestionnaire = "";

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
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
     * 
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
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = REDemandeRenteJointDemande.createFromClause(_getCollection());
        }

        return fromClause;
    }

    @Override
    protected String _getTableName() {
        return REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idDemandeRente = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
        idTiersRequerant = statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS);
        idDemandePrestation = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION);
        idParent = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE_PARENT);
        dateTraitement = statement.dbReadDateAMJ(REDemandeRente.FIELDNAME_DATE_TRAITEMENT);
        dateDepot = statement.dbReadDateAMJ(REDemandeRente.FIELDNAME_DATE_DEPOT);
        dateReception = statement.dbReadDateAMJ(REDemandeRente.FIELDNAME_DATE_RECEPTION);
        csEtatDemande = statement.dbReadNumeric(REDemandeRente.FIELDNAME_CS_ETAT);
        idRenteCalculee = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);
        csTypeCalcul = statement.dbReadNumeric(REDemandeRente.FIELDNAME_CS_TYPE_CALCUL);
        csTypeDemande = statement.dbReadNumeric(REDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE);
        dateDebut = statement.dbReadDateAMJ(REDemandeRente.FIELDNAME_DATE_DEBUT);
        dateFin = statement.dbReadDateAMJ(REDemandeRente.FIELDNAME_DATE_FIN);
        noAVS = NSUtil.formatAVSUnknown(statement.dbReadString(REDemandeRenteJointDemande.FIELDNAME_NUM_AVS));
        dateNaissance = statement.dbReadDateAMJ(REDemandeRenteJointDemande.FIELDNAME_DATENAISSANCE);
        dateDeces = statement.dbReadDateAMJ(REDemandeRenteJointDemande.FIELDNAME_DATEDECES);
        csSexe = statement.dbReadNumeric(REDemandeRenteJointDemande.FIELDNAME_SEXE);
        nom = statement.dbReadString(REDemandeRenteJointDemande.FIELDNAME_NOM);
        prenom = statement.dbReadString(REDemandeRenteJointDemande.FIELDNAME_PRENOM);
        idGestionnaire = statement.dbReadString(REDemandeRente.FIELDNAME_ID_GESTIONNAIRE);
        visaGestionnaire = statement.dbReadString(REDemandeRenteJointDemande.FIELDNAME_VISA_GESTIONNAIRE);
        csNationalite = statement.dbReadNumeric(REDemandeRenteJointDemande.FIELDNAME_NATIONALITE);
        isEnCours = statement.dbReadNumeric(REDemandeRenteJointDemande.IS_EN_COURS);
        idInfoComplementaire = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_INFO_COMPLEMENTAIRE);
        csTypeInfoComplementaire = statement.dbReadNumeric(PRInfoCompl.FIELDNAME_TYPE_INFO_COMPL);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // rien du tout... c'est en lecture seule.
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeRente, "idDemandeRente"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    @Override
    public BSpy getCreationSpy() {

        REDemandeRente demandeRente = new REDemandeRente();

        try {
            demandeRente = REDemandeRente.loadDemandeRente(getSession(), getSession().getCurrentThreadTransaction(),
                    getIdDemandeRente(), getCsTypeDemande());
        } catch (Exception e) {
        }

        if (demandeRente instanceof REDemandeRenteAPI) {
            REDemandeRenteAPI rente = (REDemandeRenteAPI) demandeRente;
            return rente.getCreationSpy();
        } else if (demandeRente instanceof REDemandeRenteInvalidite) {
            REDemandeRenteInvalidite rente = (REDemandeRenteInvalidite) demandeRente;
            return rente.getCreationSpy();
        } else if (demandeRente instanceof REDemandeRenteSurvivant) {
            REDemandeRenteSurvivant rente = (REDemandeRenteSurvivant) demandeRente;
            return rente.getCreationSpy();
        } else if (demandeRente instanceof REDemandeRenteVieillesse) {
            REDemandeRenteVieillesse rente = (REDemandeRenteVieillesse) demandeRente;
            return rente.getCreationSpy();
        } else {
            return null;
        }

    }

    public String getCsEtatDemande() {
        return csEtatDemande;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getCsTypeCalcul() {
        return csTypeCalcul;
    }

    public String getCsTypeDemande() {
        return csTypeDemande;
    }

    public String getCsTypeInfoComplementaire() {
        return csTypeInfoComplementaire;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateDepot() {
        return dateDepot;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getDateTraitement() {
        return dateTraitement;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getIdDemandePrestation() {
        return idDemandePrestation;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdInfoComplementaire() {
        return idInfoComplementaire;
    }

    @Override
    public String getIdMajeur() {
        return idDemandeRente;
    }

    @Override
    public String getIdParent() {
        return idParent;
    }

    public String getIdRenteCalculee() {
        return idRenteCalculee;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    public String getIsEnCours() {
        return isEnCours;
    }

    public String getNoAVS() {
        return noAVS;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public BSpy getSpy() {
        return super.getSpy();
    }

    public String getVisaGestionnaire() {
        return visaGestionnaire;
    }

    public void setCsEtatDemande(String string) {
        csEtatDemande = string;
    }

    public void setCsNationalite(String string) {
        csNationalite = string;
    }

    public void setCsSexe(String string) {
        csSexe = string;
    }

    public void setCsTypeCalcul(String string) {
        csTypeCalcul = string;
    }

    public void setCsTypeDemande(String string) {
        csTypeDemande = string;
    }

    public void setCsTypeInfoComplementaire(String csTypeInfoComplementaire) {
        this.csTypeInfoComplementaire = csTypeInfoComplementaire;
    }

    public void setDateDebut(String string) {
        dateDebut = string;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateDepot(String string) {
        dateDepot = string;
    }

    public void setDateFin(String string) {
        dateFin = string;
    }

    public void setDateNaissance(String string) {
        dateNaissance = string;
    }

    public void setDateReception(String string) {
        dateReception = string;
    }

    public void setDateTraitement(String string) {
        dateTraitement = string;
    }

    public void setFromClause(String string) {
        fromClause = string;
    }

    public void setIdDemandePrestation(String string) {
        idDemandePrestation = string;
    }

    public void setIdDemandeRente(String string) {
        idDemandeRente = string;
    }

    public void setIdGestionnaire(String string) {
        idGestionnaire = string;
    }

    public void setIdInfoComplementaire(String string) {
        idInfoComplementaire = string;
    }

    public void setIdParent(String string) {
        idParent = string;
    }

    public void setIdRenteCalculee(String string) {
        idRenteCalculee = string;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

    public void setIsEnCours(String isEnCours) {
        this.isEnCours = isEnCours;
    }

    public void setNoAVS(String string) {
        noAVS = string;
    }

    public void setNom(String string) {
        nom = string;
    }

    public void setPrenom(String string) {
        prenom = string;
    }

    public void setVisaGestionnaire(String string) {
        visaGestionnaire = string;
    }
}
