/*
 * Créé le 16 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.db.BStatement;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.db.demandes.PRDemande;

/**
 * @author SCR
 * 
 *         Jointure entre les tables des bases de calcul et des demandes de rentes
 * 
 */
public class RERenteAccJoinTblTiersJoinDemandeRente extends RERenteAccordee {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDECES = "HPDDEC";
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
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        // String leftJoin = " LEFT JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);

        // jointure entre table des prestations accordées et des rentes
        // accordées
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        // jointure entre table des prestations accordées et informations
        // comptabilités
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REInformationsComptabilite.TABLE_NAME_INFO_COMPTA);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);

        // jointure entre table des prestations accordes et table des numeros
        // AVS
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table table des numeros AVS et table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table des personnes et table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // //INNER JOIN CVCIWEB.TIHAVSP AS TIHAVSP
        // //ON (CVCIWEB.TIPAVSP.HTITIE = TIHAVSP.HTITIE)
        // //Jointure entre table historique et table des tiers
        // fromClauseBuffer.append(innerJoin);
        // fromClauseBuffer.append(schema);
        // fromClauseBuffer.append(TABLE_AVS_HISTO);
        // fromClauseBuffer.append(" AS ").append(TABLE_AVS_HISTO);
        // fromClauseBuffer.append(on);
        // fromClauseBuffer.append(schema);
        // fromClauseBuffer.append(TABLE_PERSONNE);
        // fromClauseBuffer.append(point);
        // fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);
        // fromClauseBuffer.append(egal);
        // fromClauseBuffer.append(schema);
        // fromClauseBuffer.append(TABLE_AVS_HISTO);
        // fromClauseBuffer.append(point);
        // fromClauseBuffer.append(FIELDNAME_ID_TIERS_TI);

        // jointure entre table des bases de calculs et rentes accordee
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);

        // jointure entre table des rentes calculees et table des bases de
        // calculs
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REBasesCalcul.TABLE_NAME_BASES_CALCUL);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE);

        // jointure entre table des demandes de rentes et table des rentes
        // calculees
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RERenteCalculee.TABLE_NAME_RENTE_CALCULEE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);

        // jointure entre table PRDEMAP et table des demandes de rentes
        // INNER JOIN CVCIWEB.PRDEMAP ON
        // CVCIWEB.PRDEMAP.WAIDEM=CVCIWEB.REDEREN.YAIMDO
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(PRDemande.TABLE_NAME);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(PRDemande.FIELDNAME_IDDEMANDE);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION);

        return fromClauseBuffer.toString();
    }

    private String csTypeBasesCalcul = "";
    private String dateDebutDemande = "";
    private String dateDecesBenef = "";
    private String dateNaissanceBenef = "";
    private String dateTraitementDemande = "";
    private String degreInvalidite = "";
    private String idCompteAnnexe = "";

    private String idDomaineAdressePmt = "";
    private String idTierAdressePmt = "";
    private String idTierRequerant = "";
    // informations comptabilités
    private String idTiersAdressePmt = "";
    private String nationaliteBenef = "";
    // Autres champs nécessaires
    private String noDemandeRente = "";
    private String nomBenef = "";

    // champs du tiers beneficiaire
    private String numeroAvsBenef = "";
    private String prenomBenef = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String sexeBenef = "";

    /**
     * Il est interdit d'ajouter un objet de ce type.
     * 
     * @return false
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return true;
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
        return true;
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
        return true;
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

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        noDemandeRente = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
        idTierAdressePmt = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        idDomaineAdressePmt = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_DOMAINE_APPLICATION);
        degreInvalidite = statement.dbReadNumeric(REBasesCalcul.FIELDNAME_DEGRE_INVALIDITE);
        idTierRequerant = statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS);
        dateDebutDemande = statement.dbReadDateAMJ(REDemandeRente.FIELDNAME_DATE_DEBUT);
        dateTraitementDemande = statement.dbReadDateAMJ(REDemandeRente.FIELDNAME_DATE_TRAITEMENT);
        csTypeBasesCalcul = statement.dbReadString(REBasesCalcul.FIELDNAME_DROIT_APPLIQUE);

        // champs du tiers beneficiaire
        numeroAvsBenef = statement.dbReadString(FIELDNAME_NUM_AVS);
        nomBenef = statement.dbReadString(FIELDNAME_NOM);
        prenomBenef = statement.dbReadString(FIELDNAME_PRENOM);
        dateNaissanceBenef = statement.dbReadDateAMJ(FIELDNAME_DATENAISSANCE);
        dateDecesBenef = statement.dbReadDateAMJ(FIELDNAME_DATEDECES);
        nationaliteBenef = statement.dbReadNumeric(FIELDNAME_NATIONALITE);
        sexeBenef = statement.dbReadNumeric(FIELDNAME_SEXE);

        // informations comptabilités
        idTiersAdressePmt = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        idCompteAnnexe = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE);
    }

    public String getCsTypeBasesCalcul() {
        return csTypeBasesCalcul;
    }

    public String getDateDebutDemande() {
        return dateDebutDemande;
    }

    public String getDateDecesBenef() {
        return dateDecesBenef;
    }

    public String getDateNaissanceBenef() {
        return dateNaissanceBenef;
    }

    public String getDateTraitementDemande() {
        return dateTraitementDemande;
    }

    public String getDegreInvalidite() {
        return degreInvalidite;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdDomaineAdressePmt() {
        return idDomaineAdressePmt;
    }

    /**
     * @return
     */
    public String getIdTierAdressePmt() {
        return idTierAdressePmt;
    }

    /**
     * @return
     */
    public String getIdTierRequerant() {
        return idTierRequerant;
    }

    public String getIdTiersAdressePmt() {
        return idTiersAdressePmt;
    }

    public String getNationaliteBenef() {
        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", nationaliteBenef)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", nationaliteBenef));
        }
    }

    /**
     * @return
     */
    public String getNoDemandeRente() {
        return noDemandeRente;
    }

    public String getNomBenef() {
        return nomBenef;
    }

    public String getNumeroAvsBenef() {
        return numeroAvsBenef;
    }

    public String getPrenomBenef() {
        return prenomBenef;
    }

    public String getSexeBenef() {
        if (PRACORConst.CS_HOMME.equals(sexeBenef)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(sexeBenef)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }
    }

    public void setCsTypeBasesCalcul(String csTypeBasesCalcul) {
        this.csTypeBasesCalcul = csTypeBasesCalcul;
    }

    public void setDateDebutDemande(String dateDebutDemande) {
        this.dateDebutDemande = dateDebutDemande;
    }

    public void setDateDecesBenef(String dateDecesBenef) {
        this.dateDecesBenef = dateDecesBenef;
    }

    public void setDateNaissanceBenef(String dateNaissanceBenef) {
        this.dateNaissanceBenef = dateNaissanceBenef;
    }

    public void setDateTraitementDemande(String dateTraitementDemande) {
        this.dateTraitementDemande = dateTraitementDemande;
    }

    public void setDegreInvalidite(String degreInvalidite) {
        this.degreInvalidite = degreInvalidite;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdDomaineAdressePmt(String idDomaineAdressePmt) {
        this.idDomaineAdressePmt = idDomaineAdressePmt;
    }

    /**
     * @param string
     */
    public void setIdTierAdressePmt(String string) {
        idTierAdressePmt = string;
    }

    /**
     * @param string
     */
    public void setIdTierRequerant(String string) {
        idTierRequerant = string;
    }

    public void setIdTiersAdressePmt(String idTiersAdressePmt) {
        this.idTiersAdressePmt = idTiersAdressePmt;
    }

    public void setNationaliteBenef(String nationaliteBenef) {
        this.nationaliteBenef = nationaliteBenef;
    }

    /**
     * @param string
     */
    public void setNoDemandeRente(String string) {
        noDemandeRente = string;
    }

    public void setNomBenef(String nomBenef) {
        this.nomBenef = nomBenef;
    }

    public void setNumeroAvsBenef(String numeroAvsBenef) {
        this.numeroAvsBenef = numeroAvsBenef;
    }

    public void setPrenomBenef(String prenomBenef) {
        this.prenomBenef = prenomBenef;
    }

    public void setSexeBenef(String sexeBenef) {
        this.sexeBenef = sexeBenef;
    }

}
